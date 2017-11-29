package ua.kpi.cad.lab3.mapred.reducers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import ua.kpi.cad.lab3.core.GeoRecordCloner;
import ua.kpi.cad.lab3.core.TileExtractor;
import ua.kpi.cad.lab3.core.divider.SimpleDivider;
import ua.kpi.cad.lab3.core.divider.TileSetDivider;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;
import ua.kpi.cad.lab3.core.renderer.TIleRendererImpl;
import ua.kpi.cad.lab3.core.renderer.TileRenderer;
import ua.kpi.cad.lab3.mapred.mappers.RenderingMapper;

import java.io.IOException;

public class RenderingReducer extends Reducer<IntWritable, GeoRecord, RenderedTileKey, RenderedTile> {

    private TileRenderer renderer = new TIleRendererImpl();
    private TileSetDivider divider;

    @Override
    protected void reduce(IntWritable key, Iterable<GeoRecord> values, Context context) throws IOException, InterruptedException {
        createDivider(context);

        for (GeoRecord record : values) {
            renderer.addRecord(GeoRecordCloner.cloneGeoRecord(record, record.getRecordType()));
        }

        divider.renderTileSet(renderer, key.get(), context);
    }

    // testing unit
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "TIGER DATA render pass");
        job.setJarByClass(RenderingReducer.class);
        job.setMapperClass(RenderingMapper.class);
        job.setReducerClass(RenderingReducer.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(RenderedTileKey.class);
        job.setOutputValueClass(RenderedTile.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setNumReduceTasks(5);

        FileInputFormat.addInputPath(job, new Path("joined"));
        FileOutputFormat.setOutputPath(job, new Path("rendered"));

        job.waitForCompletion(true);

        TileExtractor extractor = new TileExtractor();
        extractor.ExtractTiles("rendered", job, 1);
    }

    private void createDivider(Context context) {
        divider = new SimpleDivider(47.084457, -122.541068, 47.780328, -121.065709, 5);
        divider.assignTileSetIds(5);
    }
}
