package ua.kpi.cad.lab3.core.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.hadoop.io.WritableComparable;

/**
 * The writable class for TIGER/Line selected record type 2.
 * For more information please see the documentation here:
 * http://www.census.gov/geo/www/tiger/tiger2006se/TGR06SE.pdf
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TigerRecordType2 implements WritableComparable<TigerRecordType2>, Cloneable, Serializable {
    public int compareTo(TigerRecordType2 o) {
        throw new RuntimeException("Not Implemented");
    }

    /**
     * The line id that links this polygon to a type 1 record
     */
    private int lineId;
    /**
     * Record of type 2 contains field RTSQ which is a sequence number, i.e. if
     * there are more than one record of type 2 related to single lineId,
     * this field represents position of current record in sequence of other type-2
     * records
     */
    private int sequenceNum;

    /**
     * The feature type. This field must be populated by a join
     */
    private String featureType = "";
    /**
     * The list of latitudes and longitudes
     */
    private double listLat[];
    private double listLong[];

    public void readFields(DataInput d) throws IOException {
        lineId = d.readInt();

        sequenceNum = d.readInt();
        featureType = d.readUTF();

        int listLatLenght = d.readInt();
        double[] listLat = new double[listLatLenght];
        for (int i = 0; i < listLatLenght; i++) {
            listLat[i] = d.readDouble();
        }
        this.listLat = listLat;

        int listLongLength = d.readInt();
        double[] listLong = new double[listLongLength];
        for (int i = 0; i < listLongLength; i++) {
            listLong[i] = d.readDouble();
        }
        this.listLong = listLong;
    }

    public void write(DataOutput d) throws IOException {
        d.writeInt(lineId);
        d.writeInt(sequenceNum);
        d.writeUTF(featureType);

        d.writeInt(listLat.length);
        for (double lat : listLat) {
            d.writeDouble(lat);
        }

        d.writeInt(listLong.length);
        for (double lon : listLong) {
            d.writeDouble(lon);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
