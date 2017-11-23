package ua.kpi.cad.lab3.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * The writable class for city and town population records from the US
 * census. More info here:
 * http://www.census.gov/popest/cities/cities.html
 */
public class PopRecord implements WritableComparable<PopRecord> {
    public int compareTo(PopRecord o) {
        throw new RuntimeException("Not Implemented");
    }

    /**
     * The state for this population record
     */
    public String state;
    /**
     * The name for this population record, IE: "Seattle City"
     */
    public String name;
    /**
     * The population
     */
    public int population;
    /**
     * The primary latitude - this gets populated in the join phase
     */
    public double primaryLat;
    /**
     * The primary longitude - this gets populated in the join phase
     */
    public double primaryLong;

    public void readFields(DataInput d) throws IOException {
    }

    public void write(DataOutput d) throws IOException {
    }
}
