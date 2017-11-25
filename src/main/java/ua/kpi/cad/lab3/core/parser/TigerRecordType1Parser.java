package ua.kpi.cad.lab3.core.parser;

import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

/**
 * Parser chooses needed fields from first type of record
 *
 * It accepts single line from tiger RT1 file, and convert
 * it to GeoRecord.
 */
public class TigerRecordType1Parser extends GeoRecordParser {
    @Override
    public GeoRecord parse(String entry) throws RecordFormatException {


        return null;
    }
}
