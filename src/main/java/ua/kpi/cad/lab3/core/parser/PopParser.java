package ua.kpi.cad.lab3.core.parser;

import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.PopRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

/**
 * This class parses data out of the population data record into
 * a GeoRecord. You can use this class as an example of how to
 * implement parsers for other data types.
 *
 * Population data comes in a CSV file format, an entry would look
 * something like this:
 *
 * 53,Seattle City,"543,238",   ...
 *
 * This would refer to Washington state (53 is the code for Washington
 * - see the GeoConstants class), the city of Seattle, and the population
 * value of 543238 people.
 */
public class PopParser extends GeoRecordParser {

    /**
     * This method takes an entry from the population dataset and
     * emits a GeoRecord filled with the appropriate PopRecord
     */
    public GeoRecord parse(String line) throws RecordFormatException {
        // Pre-process the string for consumption:
        //  - get rid of the commas in quoted integer literals and
        //    remove the quotes around them.
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length() - 1; i++) {
            if (line.charAt(i) == '"') {
                inQuotes ^= true;
                continue;
            }
            if (inQuotes && line.charAt(i) == ',') {
                continue;
            }
            sb.append(line.charAt(i)).append("");
        }

        String[] entries = sb.toString().trim().split(",");
        try {

            // Here we construct our writable that will contain the values
            // from this record.
            PopRecord rp = new PopRecord();
            // The state is the first field.
            rp.state = GeoConstants.FIPS_STATE_CODE_MAP[tryParseInt(entries[0], 0)];
            // The city or town name is the second field.
            rp.name = entries[1];
            // The population is the third field.
            rp.population = Integer.parseInt(entries[2]);

            // We wrap this object in a GeoRecord so that all extracted
            // data looks homogeneous.
            GeoRecord record = new GeoRecord();
            record.rp = rp;
            // we set the record type as the Population record type
            // so we can identify what sort of record is wrapped by
            // the GeoRecord
            record.recordType = GeoConstants.RECORD_TYPE_POP;
            return record;

        } catch (Exception exn) {
            // This record did not seem to be formatted properly. We want to note this
            // in the mapper, but the mapper should handle this exception so that one
            // bad record does not abort the whole pass.
            throw new RecordFormatException(line, GeoConstants.RECORD_TYPE_POP, exn);
        }
    }
}
