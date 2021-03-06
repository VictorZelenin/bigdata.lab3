package ua.kpi.cad.lab3.core.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ua.kpi.cad.lab3.core.exception.WrongRecordTypeException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType2;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static ua.kpi.cad.lab3.core.GeoConstants.RECORD_TYPE_2;

/**
 * parsers chooses needed fields from second type of record
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TigerRecordType2Parser extends GeoRecordParser {

    private static final int RT_START_POSITION = 0;
    private static final int RT_LENGTH = 1;
    private static final int TLID_START_POSITION = 5;
    private static final int TLID_LENGTH = 10;
    private static final int RTSQ_START_POSITION = 15;
    private static final int RTSQ_LENGTH = 3;
    private static final int LONG1_START_POSITION = 18;
    private static final int LONG_LENGTH = 10;
    private static final int LAT1_START_POSITION = 28;
    private static final int LAT_LENGTH = 9;

    private static final String EMPTY_LONG_VALUE = "+000.000000";
    private static final String EMPTY_LAT_VALUE = "+00.000000";

    @Override
    public GeoRecord parse(String entry) {
        checkRecordType(entry);
        TigerRecordType2 tigerRecordType2 = parseAndCreateRecord(entry);
        return wrapInGeoRecord(tigerRecordType2);
    }

    private void checkRecordType(String entry) {
        val currentRecordType = entry.substring(RT_START_POSITION,
                RT_START_POSITION + RT_LENGTH);
        if (!RECORD_TYPE_2.equals(currentRecordType)) {
            throw new WrongRecordTypeException("Passed record of type: " + currentRecordType +
                    ". must be " + RECORD_TYPE_2);
        }
    }

    private TigerRecordType2 parseAndCreateRecord(String entry) {
        TigerRecordType2 record = new TigerRecordType2();

        record.setLineId(parseLineId(entry));
        record.setSequenceNum(parseSequenceNum(entry));
        record.setListLong(parseLongitudes(entry));
        record.setListLat(parseLatitudes(entry));

        return record;
    }

    private int parseLineId(String entry) {
        return parseInt(entry
                .substring(TLID_START_POSITION, TLID_START_POSITION + TLID_LENGTH)
                .trim()
        );
    }

    private int parseSequenceNum(String entry) {
        return parseInt(
                entry.substring(RTSQ_START_POSITION, RTSQ_START_POSITION + RTSQ_LENGTH)
                .trim()
        );
    }

    private double[] parseLongitudes(String entry) {
        List<Double> longitudes = parseDecimalFields(entry, LONG1_START_POSITION, LONG_LENGTH);
        return toPrimitivesArray(longitudes);
    }

    private double[] parseLatitudes(String entry) {
        List<Double> latitudes = parseDecimalFields(entry, LAT1_START_POSITION, LAT_LENGTH);
        return toPrimitivesArray(latitudes);
    }

    private List<Double> parseDecimalFields(String entry, int startPosition, int fieldLength) {
        List<Double> decimals = new LinkedList<>();

        int currentLinePosition = startPosition;
        String currentDecimal;
        currentDecimal = parseDecimalString(currentLinePosition, fieldLength, entry);
        do {
            decimals.add(Double.valueOf(currentDecimal));
            currentLinePosition += LONG_LENGTH + LAT_LENGTH; // jump over to next position of longitude/latitude
            if (currentLinePosition >= entry.length()) {
                break;
            }
            currentDecimal = parseDecimalString(currentLinePosition, fieldLength, entry);
        } while (isNotEmpty(currentDecimal) && currentLinePosition < entry.length());

        return decimals;
    }

    private String parseDecimalString(int position, int fieldLength, String entry) {
        String rawValue = entry.substring(position, position + fieldLength);
        StringBuilder sb = new StringBuilder(rawValue);
        sb.reverse().insert(6, ".").reverse();
        return sb.toString();
    }

    private boolean isNotEmpty(String decimalField) {
        return !(
                decimalField.equals(EMPTY_LONG_VALUE) || decimalField.equals(EMPTY_LAT_VALUE)
        );
    }

    private double[] toPrimitivesArray(List<Double> decimals) {
        double[] output = new double[decimals.size()];
        int index = 0;
        for (Double decimal : decimals) {
            output[index++] = decimal;
        }

        return output;
    }

    private GeoRecord wrapInGeoRecord(TigerRecordType2 tigerRecordType2) {
        GeoRecord geoRecord = new GeoRecord();
        geoRecord.setRecordType(RECORD_TYPE_2);
        geoRecord.setRecordType2(tigerRecordType2);
        return geoRecord;
    }
}
