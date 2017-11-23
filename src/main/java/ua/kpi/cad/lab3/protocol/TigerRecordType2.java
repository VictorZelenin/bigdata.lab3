package ua.kpi.cad.lab3.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * The writable class for TIGER/Line selected record type 2.
 * For more information please see the documentation here:
 * http://www.census.gov/geo/www/tiger/tiger2006se/TGR06SE.pdf
 */
public class TigerRecordType2 implements WritableComparable<TigerRecordType2> {
    public int compareTo(TigerRecordType2 o) {
        throw new RuntimeException("Not Implemented");
    }

    /**
     * The line id that links this polygon to a type 1 record
     */
    public int lineId;
    /**
     * The feature type. This field must be populated by a join
     */
    public String featureType;
    /**
     * The list of latitudes and longitudes
     */
    public double listLat[];
    public double listLong[];

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }
}
