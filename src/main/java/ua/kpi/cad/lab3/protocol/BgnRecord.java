package ua.kpi.cad.lab3.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * This class is the writable object for BGN records. BGN records are
 * place name and type information with coordinates form the USGS.
 * You can get more information on them here:
 * http://geonames.usgs.gov/domestic/download_data.htm
 *
 * The BGN records are delimited by the '|' character.
 * The complete documentation on the file format for BGN is found here
 * http://geonames.usgs.gov/domestic/gaz_fileformat.htm
 */
public class BgnRecord implements WritableComparable<BgnRecord> {
    public int compareTo(BgnRecord o) {
        throw new RuntimeException("not implemented");
    }

    /**
     * The name of the feature. This is the 2nd field in the record
     */
    public String name;
    /**
     * The class of the feature. 3rd field. Refer to the documentation here:
     * http://geonames.usgs.gov/domestic/feature_class.htm
     */
    public String featureClass;
    /**
     * the state to which the feature belongs. 4th field
     */
    public String state;
    /**
     * The county
     */
    public String county;
    /**
     * The primary latitude of the feature
     */
    public double primaryLat;
    /**
     * The primary longitude of the feature
     */
    public double primaryLong;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }
}
