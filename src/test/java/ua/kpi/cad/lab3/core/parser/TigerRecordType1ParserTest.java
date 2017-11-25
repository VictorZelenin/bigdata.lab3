package ua.kpi.cad.lab3.core.parser;

import org.junit.Before;
import org.junit.Test;
import ua.kpi.cad.lab3.core.TestUtils;
import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

import java.io.IOException;
import java.util.List;

public class TigerRecordType1ParserTest {

    private GeoRecordParser parser;

    @Before
    public void init() {
        parser = new TigerRecordType1Parser();
    }

    @Test
    public void test() throws IOException, RecordFormatException {
        List<String> lines = TestUtils.readFileByLines("tiger.rt1");

        for (String line : lines) {
            GeoRecord tigerRecord = parser.parse(line);
        }
    }
}
