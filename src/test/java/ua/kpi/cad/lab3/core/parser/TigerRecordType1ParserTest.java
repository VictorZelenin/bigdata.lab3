package ua.kpi.cad.lab3.core.parser;

import org.junit.Before;
import org.junit.Test;
import ua.kpi.cad.lab3.core.TestUtils;
import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TigerRecordType1ParserTest {

    private GeoRecordParser parser;

    @Before
    public void init() {
        parser = new TigerRecordType1Parser();
    }

    @Test
    public void testParsingSingleRecord() throws IOException, RecordFormatException {
        List<String> lines = TestUtils.readFileByLines("tiger.rt1");

        GeoRecord parsedRecord = parser.parse(lines.get(80));

        GeoRecord expectedGeoRecord = new GeoRecord();
        TigerRecordType1 recordType1 = new TigerRecordType1();
        recordType1.setLineId(186677790);
        recordType1.setPrefix("");
        recordType1.setName("");
        recordType1.setType("1");
        recordType1.setDirectionSuffix("");
        recordType1.setFeatureType("A41");
        recordType1.setStateCode(53);
        recordType1.setStartLat(47.474544);
        recordType1.setStartLong(-121.717231);
        recordType1.setEndLat(47.47453);
        recordType1.setEndLong(-121.717918);
        expectedGeoRecord.setRecordType1(recordType1);

        assertEquals(expectedGeoRecord, parsedRecord);
    }

    @Test
    public void testParsingAllRecords() throws IOException, RecordFormatException {
        List<String> lines = TestUtils.readFileByLines("tiger.rt1");
        List<GeoRecord> parsedRecords = new ArrayList<>();

        for (String line : lines) {
            GeoRecord geoRecord = parser.parse(line);
            parsedRecords.add(geoRecord);
        }

        assertEquals(100, parsedRecords.size());
    }
}
