package ua.kpi.cad.lab3.core.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * Class for wrapping the results of tile rendering.
 */
public class RenderedTile implements Writable {
    /**
     * The byte array for the rendered tile
     */
    public byte tileData[] = null;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }
}
