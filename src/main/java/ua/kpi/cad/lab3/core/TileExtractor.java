package ua.kpi.cad.lab3.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileRecordReader;
import org.apache.log4j.Logger;
import ua.kpi.cad.lab3.core.divider.TileSetDivider;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;

/**
 * This is a tool to extract the rendered tiles written to the DFS
 * in SequenceFile format onto the local disk.
 *
 * @author Slava Chernyak
 */
public class TileExtractor {
    private final Logger logger = Logger.getLogger(this.getClass().toString());

    /**
     * Extracts the tiles from a result of a render job.
     *
     * @param path     The path to which the result of the mapreduce
     *                 was stored
     * @param conf     The job conf associated with the render
     *                 mapreduce
     * @param numTasks The number of render tasks (number of reducers)
     * @throws IOException
     */
    @SuppressWarnings(value = "deprecation")
    public void ExtractTiles(String path, JobConf conf, int numTasks) throws IOException {
        // create the tile directory structure
        for (int j = TileSetDivider.HIGHEST_ZOOMLEVEL; j <= TileSetDivider.LOWEST_ZOOMLEVEL; j++) {
            File tileDir = new File("tiles/" + j + "/");
            if (!tileDir.exists()) {
                tileDir.mkdirs();
            }
        }

        // get the filesystem object from the configuration
        // this can be local or the DFS
        FileSystem fileSystem = FileSystem.get(conf);

        for (int i = 0; i < numTasks; i++) {
            // build the filename string
            String filename = ("0000" + i);
            filename = filename.substring(filename.length() - 5);
            filename = "part-" + filename;
            filename = (path.endsWith("/")) ? path + filename : path + "/" + filename;
            logger.info("Starting inflate from result file " + filename);

            Path cPath = new Path(filename);
            if (!fileSystem.exists(cPath)) {
                logger.warn(cPath.toString() + " does not exit!");
                continue;
            }

            long len = fileSystem.getFileStatus(cPath).getLen();

            FileSplit fileSplit = new FileSplit(cPath, 0, len, conf);
            SequenceFileRecordReader<RenderedTileKey, RenderedTile> reader = new SequenceFileRecordReader<RenderedTileKey, RenderedTile>(conf, fileSplit);

            RenderedTileKey k = new RenderedTileKey();
            RenderedTile tile = new RenderedTile();

            while (reader.getPos() < len) {
                reader.next(k, tile);
                logger.info("Inflating tile (" + k.tileIdX + "," + k.tileIdY + ") z:" + k.zoomLevel);
                File f = new File("tiles/" + k.zoomLevel + "/" + k.tileIdX
                        + "_" + k.tileIdY + "_" + k.zoomLevel + ".png");
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(tile.tileData);
                fos.close();
            }
        }
        logger.info("Finished inflating tiles.");
    }
}
