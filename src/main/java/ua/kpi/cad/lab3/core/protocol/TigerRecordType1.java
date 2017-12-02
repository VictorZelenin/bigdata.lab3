package ua.kpi.cad.lab3.core.protocol;

import java.io.*;

import lombok.*;
import org.apache.hadoop.io.WritableComparable;
import ua.kpi.cad.lab3.core.exception.NotImplementedException;

/**
 * The writable class for TIGER/Line selected record type 1 data.
 * For more information please see the documentation here:
 * http://www.census.gov/geo/www/tiger/tiger2006se/TGR06SE.pdf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TigerRecordType1 implements WritableComparable<TigerRecordType1>, Cloneable, Serializable {
    // The fields below are all from the TIGER/Line record type 1 with the same names as below
    private int lineId; // TLID (6,15)
    private String prefix; // FEDIRP (18, 19)
    private String name; // FENAME (20, 49)
    private String type; // RT (1, 1)
    private String directionSuffix; // FEDIRS (54, 55)
    private String featureType; // FETYPE (50, 53)
    private long startAddressLeft; // FRADDL (59, 69)
    private long endAddressLeft; // TOADDL (70, 80)
    private long startAddressRight; // FRADDR (81, 91)
    private long endAddressRight; // TOADDR (92, 102)
    private int zipLeft; // ZIPL (107, 111)
    private int zipRight; // ZIPR (117, 121)
    private int stateCode; // STATEL (131, 132)
    private double startLat; // FRLAT (201, 209)
    private double startLong; // FRLONG (191, 200)
    private double endLat; // ENDLAT (220, 228)
    private double endLong; // ENDLONG (210, 219)

    @Override
    public void readFields(DataInput d) throws IOException {
        this.lineId = d.readInt();
        this.prefix = d.readUTF();
        this.name = d.readUTF();
        this.type = d.readUTF();
        this.directionSuffix = d.readUTF();
        this.featureType = d.readUTF();
        this.startAddressLeft = d.readLong();
        this.startAddressRight = d.readLong();
        this.endAddressLeft = d.readLong();
        this.endAddressRight = d.readLong();
        this.zipLeft = d.readInt();
        this.zipRight = d.readInt();
        this.stateCode = d.readInt();
        this.startLat = d.readDouble();
        this.startLong = d.readDouble();
        this.endLat = d.readDouble();
        this.endLong = d.readDouble();
    }

    @Override
    public void write(DataOutput d) throws IOException {
        d.writeInt(lineId);
        d.writeUTF(prefix);
        d.writeUTF(name);
        d.writeUTF(type);
        d.writeUTF(directionSuffix);
        d.writeUTF(featureType);
        d.writeLong(startAddressLeft);
        d.writeLong(startAddressRight);
        d.writeLong(endAddressLeft);
        d.writeLong(endAddressRight);
        d.writeInt(zipLeft);
        d.writeInt(zipRight);
        d.writeInt(stateCode);
        d.writeDouble(startLat);
        d.writeDouble(startLong);
        d.writeDouble(endLat);
        d.writeDouble(endLong);
    }

    @Override
    public int compareTo(TigerRecordType1 o) {
        throw new NotImplementedException("This feature is not implemented yet.");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
