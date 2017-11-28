package ua.kpi.cad.lab3.core;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;
import ua.kpi.cad.lab3.core.divider.TileSetDivider;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

/**
 * This class sets up the mapreduce pipeline that we use to
 * filter (pre-process) the geographic data, perform inner joins,
 * compute the geocode index, and finally, render the map tiles.
 * <p>
 * You should not need to modify the flow or parameters in this
 * file significantly (if at all). You will, however, need to plug in
 * your own mappers, reducers, combiners, partitioners, and writable
 * comparable types wherever applicable. There are TODO's throughout
 * this file that will indicate the places where you need to plug in
 * your own code.
 *
 * @author Slava Chernyak
 * @author dev.syor
 */
public class GeoDriver {
    private static final Logger logger = Logger.getLogger(GeoDriver.class.toString());

    // cmdline arguments
    private static final String ARG_INPUT_PATH_TIGER = "-t";
    private static final String ARG_INPUT_PATH_BGN = "-b";
    private static final String ARG_INPUT_PATH_POP = "-p";
    private static final String ARG_ZOOMLEVEL = "-z";
    private static final String ARG_EXTRACT_TO_LOCAL_LONG = "--extract_to_local";

    // mapreduce config constants
    public static final String MAXIMUM_SIMOULTANEOUS_REDUCES_PER_HOST_KEY = "mapred.tasktracker.reduce.tasks.maximum";
    private static final String TILE_SET_DIVIDER_TYPE_KEY = "edu.washington.tigermap.divider";
    private static final String TILE_RENDERER_TYPE_KEY = "edu.washington.tigermap.renderer";
    private static final String TILE_SET_MIN_LAT_KEY = "edu.washington.tigermap.lat.min";
    private static final String TILE_SET_MAX_LAT_KEY = "edu.washington.tigermap.lat.max";
    private static final String TILE_SET_MIN_LONG_KEY = "edu.washington.tigermap.long.min";
    private static final String TILE_SET_MAX_LONG_KEY = "edu.washington.tigermap.long.max";
    private static final String TILE_SET_ZOOMLEVEL_KEY = "edu.washington.tigermap.zoomlevel";
    private static final String NUM_RENDER_TASKS_KEY = "edu.washington.cse490h.geo.numrendertasks";


    private static final String TILE_SET_DIVIDER_SIMPLE = "SimpleDivider.class";
    private static final String TILE_RENDERER_FAKE = "FakeRenderer.class";

    /**
     * Here is where we run all of the mapreduce passes that get us from
     * geographic data to map tiles.
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // configure the input directories for the raw data

        String inputTiger = parseStringArg(args, ARG_INPUT_PATH_TIGER, null);
        if (inputTiger == null) {
            logger.error("Tiger data input path not specified!");
            printUsage();
            System.exit(1);
        }

        String inputBgn = parseStringArg(args, ARG_INPUT_PATH_BGN, null);
        if (inputBgn == null) {
            logger.error("BGN data input path not specified!");
            printUsage();
            System.exit(1);
        }

        String inputPop = parseStringArg(args, ARG_INPUT_PATH_POP, null);
        if (inputPop == null) {
            logger.error("Pop data input path not specified!");
            printUsage();
            System.exit(1);
        }

        int zoomLevel = Integer.parseInt(parseStringArg(args, ARG_ZOOMLEVEL, null));
        if (zoomLevel > TileSetDivider.LOWEST_ZOOMLEVEL
                || zoomLevel < TileSetDivider.HIGHEST_ZOOMLEVEL) {
            logger.error("Zoom level is out of range! Must be from " +
                    TileSetDivider.HIGHEST_ZOOMLEVEL + " to " +
                    TileSetDivider.LOWEST_ZOOMLEVEL);
            printUsage();
            System.exit(1);
        }

        boolean extract = parseBooleanArg(args, null, ARG_EXTRACT_TO_LOCAL_LONG);

        // filter data from all sources
        filterTigerData(inputTiger);    // TIGER/Line
        filterBgnData(inputBgn);        // BGN
        filterPopData(inputPop);        // Population records

        // Perform joins on data types
        joinBgnPopData();                // BGN into Population
        joinTigerLinePoly();            // Line (Type 1) into Polygonal (Type 2)

        // build geocode index
        buildGeocodeIndex();

        // run the renderer for the specified parameters
        // TODO: the Lat-Long parameters hardcoded here specify which area of the map
        // will get rendered. The current ones select an area around western Washington
        // (including Seattle). These are ok for testing, but for the passes on the cluster
        // with full data, expand these to a much larger area (find out a
        doRender(47.084457, -122.541068, 47.780328, -121.065709, zoomLevel, extract);
    }

    private static String parseStringArg(String[] args, String argName, String argLongName) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(argName) || args[i].equals(argLongName)) return args[i + 1];
        }
        return null;
    }

    private static boolean parseBooleanArg(String[] args, String argName, String argLongName) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argName) || args[i].equals(argLongName)) return true;
        }
        return false;
    }

    private static void printUsage() {
        System.out.println("Usage: GeoDriver -t <tiger input path> -b <bgn input path> -p <pop input path> -z <zoom level>");
    }

    /**
     * This pass should read in the BGN data records and
     * filter out the fields that we want.
     * <p>
     * You will have to implement a new parser that subclasses
     * the GeoRecordParser to parse BGN records.
     * <p>
     * See protocol.BgnRecord for a list of the fields that
     * should be populated.
     */
    public static void filterBgnData(String inputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "BGN DATA filter pass");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(GeoRecord.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //TODO: Don't forget to set your mapper here

        // We set the reduce tasks to 0 so that the output of
        // your mapper will get written straight to the DFS
        job.setNumReduceTasks(0);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path("output_bgn_filtered"));

        job.waitForCompletion(true);
    }

    /**
     * This pass should read in the TIGER/Line data records and
     * filter out the fields that we want.
     * <p>
     * You will have to implement a new parser that subclasses
     * the GeoRecordParser to parse TIGER/Line records.
     * <p>
     * See protocol.TigerRecordType1 and protocol.TigerRecordType2
     * for a list of the fields that should be populated.
     */
    public static void filterTigerData(String inputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "TIGER DATA filter pass");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(GeoRecord.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //TODO: Don't forget to set your mapper here

        // We set the reduce tasks to 0 so that the output of
        // your mapper will get written straight to the DFS
        job.setNumReduceTasks(0);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path("output_tiger_filtered"));

        job.waitForCompletion(true);
    }

    /**
     * This pass should read in the city and town population data
     * records and filter out the fields that we want.
     * <p>
     * A parser (PopParser) is already implemented for this data
     * as an example.
     * <p>
     * See protocol.PopRecord for a list of fields that should be
     * populated by this pass.
     */
    public static void filterPopData(String inputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "POP DATA filter pass");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(GeoRecord.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //TODO: Don't forget to set your mapper here

        // We set the reduce tasks to 0 so that the output of
        // your mapper will get written straight to the DFS
        job.setNumReduceTasks(0);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path("output_pop_filtered"));

        job.waitForCompletion(true);
    }

    /**
     * This pass should join the BGN into the Population data records
     * for the purpose of giving the Population records a latitude
     * and longitude that can be used to render them.
     */
    public static void joinBgnPopData() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "BGN POP join pass");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        //TODO: You may want to set a map output key class here
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //TODO: Don't forget to set your mapper and reducer classes

        // We pull data from the outputs of the filter pass for both the
        // BGN record type and the Population record type
        FileInputFormat.addInputPath(job, new Path("output_pop_filtered"));
        FileInputFormat.addInputPath(job, new Path("output_bgn_filtered"));
        FileOutputFormat.setOutputPath(job, new Path("output_pop_joined"));

        job.waitForCompletion(true);
    }

    /**
     * This pass should build the geocode index for the TIGER/Line data
     * for address resolution. This is a little bit like an inverted index.
     * See the assignment for more detailed info.
     */
    public static void buildGeocodeIndex() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "GEOCODE INDEX pass");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        //TODO: you may want to set your map output key value
        // types here as well as the output types for the job

        job.setOutputFormatClass(TextOutputFormat.class);

        //TODO: Set your mapper here
        // There is no need to set the number of map tasks for the job.
        // The number of map tasks equals the number of blocks in the input file.
        // conf.setNumMapTasks(52);

        //TODO: Set your partitioner and reducer here
        job.setNumReduceTasks(52);

        FileInputFormat.addInputPath(job, new Path("output_tiger_joined"));
        FileOutputFormat.setOutputPath(job, new Path("output_geocode"));

        job.waitForCompletion(true);
    }

    /**
     * This pass should perform a join on line (type 1) and polygon (type 2)
     * records from the filtered TIGER/Line data. The purpose of this is
     * to assign polygons a feature type.
     */
    public static void joinTigerLinePoly() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "TIGER DATA extract poly features");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        //TODO: you may want to set your map output key type here
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //TODO: don't forget to set your mapper and reducer classes
        // There is no need to set the number of map tasks for the job.
        // The number of map tasks equals the number of blocks in the input file.
        // conf.setNumMapTasks(52);
        job.setNumReduceTasks(100);

        FileInputFormat.addInputPath(job, new Path("output_tiger_filtered"));
        FileOutputFormat.setOutputPath(job, new Path("output_tiger_joined"));

        job.waitForCompletion(true);
    }

    /**
     * This is the render pass - here we go from geographic feature data
     * to images. We pull data from the directories in the DFS where we
     * put data after pre-processing and joining, and output rendered tiles
     * back to the DFS into a SequenceFile.
     * <p>
     * This method also calls the  extractor class which opens the generated
     * SequenceFiles on the DFS,  extracts the images, and creates them as PNG
     * files on the machine on which this program is being run.
     */
    public static void doRender(
            double minLat, double minLong, double maxLat,
            double maxLong, int zoomLevel, boolean extract) throws IOException, ClassNotFoundException, InterruptedException {
        // we scale the number of rendering tasks depending on the zoomlevel and
        // the range we are mapping over. The estimate is based around wanting to
        // have ~300 render tasks for the northwestern quadrant of the US at zoomlevel 6
        double delta = (maxLat - minLat) > (maxLong - minLong) ? maxLat - minLat : maxLong - minLong;
        int areaScaleFactor = (int) (delta * delta);
        int constantScaleFactor = 225;
        int numRenderTasks = (int) Math.ceil(((double) areaScaleFactor / (double) constantScaleFactor)
                * Math.pow(4, 10 - zoomLevel));

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "TIGER DATA render pass");
        job.setJarByClass(GeoDriver.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(RenderedTileKey.class);
        job.setOutputValueClass(RenderedTile.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //TODO: Set your render mapper and reducer here.
        job.setNumReduceTasks(numRenderTasks);
        job.setSpeculativeExecution(true);

        // set up the render parameters
        conf.set(TILE_RENDERER_TYPE_KEY, TILE_RENDERER_FAKE);
        conf.set(TILE_SET_DIVIDER_TYPE_KEY, TILE_SET_DIVIDER_SIMPLE);
        conf.set(TILE_SET_MIN_LAT_KEY, String.valueOf(minLat));
        conf.set(TILE_SET_MIN_LONG_KEY, String.valueOf(minLong));
        conf.set(TILE_SET_MAX_LAT_KEY, String.valueOf(maxLat));
        conf.set(TILE_SET_MAX_LONG_KEY, String.valueOf(maxLong));
        conf.set(TILE_SET_ZOOMLEVEL_KEY, String.valueOf(zoomLevel));
        conf.set(NUM_RENDER_TASKS_KEY, String.valueOf(numRenderTasks));

        // We add all the paths from which we want to pull features to render
        String outputPathName = "output_render_z" + zoomLevel;
        FileInputFormat.addInputPath(job, new Path("output_bgn_filtered"));
        FileInputFormat.addInputPath(job, new Path("output_pop_joined"));
        FileInputFormat.addInputPath(job, new Path("output_tiger_joined"));
        FileOutputFormat.setOutputPath(job, new Path(outputPathName));

        job.waitForCompletion(true);

        if (extract) {
            TileExtractor extractor = new TileExtractor();
            extractor.ExtractTiles(outputPathName, job, 1);
        }
    }
}
