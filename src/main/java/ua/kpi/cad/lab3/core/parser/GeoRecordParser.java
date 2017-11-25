package ua.kpi.cad.lab3.core.parser;


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

    /**
     * Construct the associated key to a given record
     *
     * @param record
     * @return
     */
    public GeoRecordKey getKey(GeoRecord record) {
        GeoRecordKey key = new GeoRecordKey();
        key.recordType = record.recordType;
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

    /**
     * Exception to be thrown when the record attempted to be parsed
     * is malformed
     */
    @SuppressWarnings("serial")
    public static class RecordFormatException extends Exception {
        private String entry;
        private String recordType;

        public RecordFormatException(String entry, String recordType) {
            this.entry = entry;
            this.recordType = recordType;
        }

        public RecordFormatException(String entry, String recordType, Exception innerException) {
            super(innerException);
            this.entry = entry;
            this.recordType = recordType;
        }

        public String getEntry() {
            return entry;
        }

        public String getRecordType() {
            return recordType;
        }
    }

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

}
