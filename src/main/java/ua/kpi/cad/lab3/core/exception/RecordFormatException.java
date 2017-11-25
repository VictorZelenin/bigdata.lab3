package ua.kpi.cad.lab3.core.exception;

/**
 * Exception to be thrown when the record attempted to be parsed
 * is malformed
 */
public class RecordFormatException extends Exception {
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
