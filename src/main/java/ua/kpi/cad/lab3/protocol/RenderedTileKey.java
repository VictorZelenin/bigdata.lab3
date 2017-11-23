package ua.kpi.cad.lab3.protocol;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * The key to the rendered tile data for writing out to the HDFS
 */
public class RenderedTileKey implements WritableComparable<RenderedTileKey> {
    public int compareTo(RenderedTileKey o) {
        throw new RuntimeException("Not Implemented");
    }

    /** The zoom level for the rendered tile */
    public int zoomLevel;
    /** The tile ID x component */
    public int tileIdX;
    /** THe tile ID y component */
    public int tileIdY;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException  {
    }
}
