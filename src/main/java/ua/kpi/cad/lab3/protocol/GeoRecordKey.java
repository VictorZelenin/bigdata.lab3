package ua.kpi.cad.lab3.protocol;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * We use this key to identify our GeoRecords when writing them out to
 * the HDFS, or if we need to group records by type.
 */
public class GeoRecordKey implements WritableComparable<GeoRecordKey> {
    public int compareTo(GeoRecordKey o) {
        throw new RuntimeException("Not Implemented");
    }

    /** The record type of the associated GeoRecord */
    public String recordType = null;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException  {
    }
}
