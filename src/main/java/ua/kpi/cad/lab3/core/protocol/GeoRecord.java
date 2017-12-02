package ua.kpi.cad.lab3.core.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

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
public class GeoRecord implements WritableComparable<GeoRecord>, Cloneable, Serializable {

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
        this.recordType = d.readUTF();
        if (Objects.equals(recordType, "1")) {
            recordType1 = new TigerRecordType1();
            this.recordType1.readFields(d);
        } else {
            recordType2 = new TigerRecordType2();
            this.recordType2.readFields(d);
        }
    }

    public void write(DataOutput d) throws IOException {
        d.writeUTF(recordType);
        if (recordType1 != null) {
            recordType1.write(d);
        }

        if (recordType2 != null) {
            recordType2.write(d);
        }
    }

    public int compareTo(GeoRecord o) {
        throw new NotImplementedException("This feature is not implemented yet.");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
