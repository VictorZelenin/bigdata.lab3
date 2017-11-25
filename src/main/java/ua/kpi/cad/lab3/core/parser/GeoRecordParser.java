package ua.kpi.cad.lab3.core.parser;


import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

/**
 * The purpose of this abstract class is to specify how a parser
 * should behave in obtaining a GeoRecord from any of the available
 * data sources. For an example implementation see the PopParser class
 *
 * @author Slava Chernyak
 */
public abstract class GeoRecordParser {

    private static final int QUANTITY_OF_DIGITS_IN_COORDS = 6;

    /**
     * Construct the associated key to a given record
     *
     * @param record
     * @return
     */
    public GeoRecordKey getKey(GeoRecord record) {
        GeoRecordKey key = new GeoRecordKey();
        key.recordType = record.getRecordType();
        return key;
    }

    /**
     * Construct a record from an entry. Implementations are
     * data-source specific.
     *
     * @param entry
     * @return
     */
    public abstract GeoRecord parse(String entry) throws RecordFormatException;

    protected static long tryParseLong(String toParse, long defaultValue) {
        try {
            return Long.parseLong(toParse);
        } catch (NumberFormatException exn) {
            return defaultValue;
        }
    }

    protected static int tryParseInt(String toParse, int defaultValue) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException exn) {
            return defaultValue;
        }
    }

    /**
     * All coordinates are expressed as a signed integer with six
     * decimal places of precision implied (see the section, Positional Accuracy,
     * in Chapter 5).
     */
    protected double parseGeoCoordinateField(String rawValue) {
        try {
            String mainPart = rawValue.substring(0, rawValue.length() - QUANTITY_OF_DIGITS_IN_COORDS);
            String digits = rawValue.substring(rawValue.length() - QUANTITY_OF_DIGITS_IN_COORDS);

            return Double.parseDouble(String.format("%s.%s", mainPart, digits));
        } catch (NumberFormatException exn) {
            throw new IllegalArgumentException("Wrong coordinate.");
        }
    }
}
