package ua.kpi.cad.lab3.protocol;

import java.io.*;

import org.apache.hadoop.io.WritableComparable;

/**
 * The writable class for TIGER/Line selected record type 1 data.
 * For more information please see the documentation here:
 * http://www.census.gov/geo/www/tiger/tiger2006se/TGR06SE.pdf
 */
public class TigerRecordType1 implements WritableComparable<TigerRecordType1> {
    public int compareTo(TigerRecordType1 o) {
        throw new RuntimeException("Not Implemented");
    }


    // The fields below are all from the TIGER/Line record type 1 with
    // the same names as below

    public int lineId;
    public String prefix;
    public String name;
    public String type;
    public String directionSuffix;
    public String featureType;
    public long startAddrLeft;
    public long endAddrLeft;
    public long startAddrRight;
    public long endAddrRight;
    public int zipLeft;
    public int zipRight;
    public int stateCode;
    public double startLat;
    public double startLong;
    public double endLat;
    public double endLong;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }
}
