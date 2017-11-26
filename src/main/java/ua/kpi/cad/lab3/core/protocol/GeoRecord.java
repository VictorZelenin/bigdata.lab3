package ua.kpi.cad.lab3.core.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.*;
import org.apache.hadoop.io.WritableComparable;
import ua.kpi.cad.lab3.core.exception.NotImplementedException;

/**
 * This is a class that serves as a wrapper for the possible geographic
 * records that were parsed from the source data. We use it so that we
 * can pass records of different types within one mapreduce pass.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GeoRecord implements WritableComparable<GeoRecord> {

    /**
     * The marker field that determines
     * what record type is wrapped by this GeoRecord.
     */
    private String recordType;

    // the possible record types that can be wrapped by this
    // GeoRecord
    private TigerRecordType1 recordType1;
    private TigerRecordType2 recordType2;
    private BgnRecord bgnRecord;
    private PopRecord popRecord;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }

    public int compareTo(GeoRecord o) {
        throw new NotImplementedException("This feature is not implemented yet.");
    }
}
