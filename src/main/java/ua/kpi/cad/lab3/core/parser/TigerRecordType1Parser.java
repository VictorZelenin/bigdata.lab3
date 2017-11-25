package ua.kpi.cad.lab3.core.parser;

import org.apache.log4j.Logger;
import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType1;

/**
 * Parser chooses needed fields from first type of record
 *
 * It accepts single line from tiger RT1 file, and convert
 * it to GeoRecord.
 */
public class TigerRecordType1Parser extends GeoRecordParser {
    private static final Logger LOGGER = Logger.getLogger(TigerRecordType1Parser.class);

    @Override
    public GeoRecord parse(String entry) throws RecordFormatException {
        GeoRecord geoRecord = new GeoRecord();
        TigerRecordType1 tigerRecord = parseTigerRecordType1(entry);

        geoRecord.setRecordType1(tigerRecord);

        return geoRecord;
    }

    private TigerRecordType1 parseTigerRecordType1(String row) {
        TigerRecordType1 record = new TigerRecordType1();

        record.setLineId(Integer.parseInt(row.substring(5, 15).trim()));
        record.setPrefix(row.substring(17, 19).trim());
        record.setName(row.substring(19, 49).trim());
        record.setType(row.substring(0, 1).trim());
        record.setDirectionSuffix(row.substring(53, 55).trim());
        record.setFeatureType(row.substring(49, 53).trim());

        record.setStartAddressLeft(tryParseLong(row.substring(58, 69).trim(), 0L));
        record.setEndAddressLeft(tryParseLong(row.substring(79, 80).trim(), 0L));
        record.setStartAddressRight(tryParseLong(row.substring(80, 91).trim(), 0L));
        record.setEndAddressRight(tryParseLong(row.substring(91, 102).trim(), 0L));

        record.setZipLeft(tryParseInt(row.substring(106, 111).trim(), 0));
        record.setZipRight(tryParseInt(row.substring(116, 121).trim(), 0));

        record.setStateCode(tryParseInt(row.substring(130, 132).trim(), 0));

        record.setStartLat(parseGeoCoordinateField(row.substring(200, 209).trim()));
        record.setStartLong(parseGeoCoordinateField(row.substring(190, 200).trim()));
        record.setEndLat(parseGeoCoordinateField(row.substring(219, 228).trim()));
        record.setEndLong(parseGeoCoordinateField(row.substring(209, 219).trim()));

        return record;
    }
}
