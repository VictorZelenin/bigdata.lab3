package ua.kpi.cad.lab3.core.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * This is a class that serves as a wrapper for the possible geographic
 * records that were parsed from the source data. We use it so that we
 * can pass records of different types within one mapreduce pass.
 */
public class GeoRecord implements WritableComparable<GeoRecord> {
    public int compareTo(GeoRecord o) {
        throw new RuntimeException("Not Implemented");
    }

    /**
     * The marker field that determines
     * what record type is wrapped by this GeoRecord.
     */
    public String recordType = null;

    // the possible record types that can be wrapped by this
    // GeoRecord
    public TigerRecordType1 rt1 = null;
    public TigerRecordType2 rt2 = null;
    public BgnRecord rbgn = null;
    public PopRecord rp = null;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }
}
