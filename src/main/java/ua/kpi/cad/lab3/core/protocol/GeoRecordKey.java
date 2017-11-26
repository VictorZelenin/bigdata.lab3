package ua.kpi.cad.lab3.core.protocol;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * We use this key to identify our GeoRecords when writing them out to
 * the HDFS, or if we need to group records by type.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GeoRecordKey implements WritableComparable<GeoRecordKey> {
    /** The record type of the associated GeoRecord */
    private String recordType;

    public GeoRecordKey(int recordType) {
        this.recordType = String.valueOf(recordType);
    }

    @Override
    public int compareTo(GeoRecordKey o) {
        return Integer.parseInt(recordType) - Integer.parseInt(o.recordType);
    }

    @Override
    public void readFields(DataInput d) throws IOException {
        this.recordType = d.readUTF();
    }

    @Override
    public void write(DataOutput d) throws IOException  {
        d.writeUTF(recordType);
    }
}
