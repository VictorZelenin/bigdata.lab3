package ua.kpi.cad.lab3.core.protocol;

import java.io.*;

import lombok.*;
import org.apache.hadoop.io.WritableComparable;

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
public class TigerRecordType1 implements WritableComparable<TigerRecordType1> {
    // The fields below are all from the TIGER/Line record type 1 with the same names as below
    private int lineId;
    private String prefix;
    private String name;
    private String type;
    private String directionSuffix;
    private String featureType;
    private long startAddressLeft;
    private long endAddressLeft;
    private long startAddressRight;
    private long endAddressRight;
    private int zipLeft;
    private int zipRight;
    private int stateCode;
    private double startLat;
    private double startLong;
    private double endLat;
    private double endLong;

    @Override
    public void readFields(DataInput d) throws IOException {
    }

    @Override
    public void write(DataOutput d) throws IOException {
    }

    @Override
    public int compareTo(TigerRecordType1 o) {
        throw new RuntimeException("Not Implemented");
    }
}
