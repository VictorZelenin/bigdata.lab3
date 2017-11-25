package ua.kpi.cad.lab3.core.parser;

import org.junit.Assert;
import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.parser.GeoRecordParser;
import ua.kpi.cad.lab3.core.parser.PopParser;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class PopParserTest {
    GeoRecordParser parser;

    public void setUp() {
        parser = new PopParser();
    }

    public void testParseValidWithQuotes() throws Exception {
        String entry = " 53,Aberdeen city,\"16,150\",\"16,213\",\"16,195\",\"16,197\",\"16,175\",\"16,212\",\"16,253\",\"16,426\",\"16,461\",\"16,461\"";

        GeoRecord result = parser.parse(entry);

        assertNotNull(result);
        Assert.assertEquals(result.recordType, GeoConstants.RECORD_TYPE_POP);
        assertNotNull(result.rp);
        assertEquals(result.rp.name, "Aberdeen city");
        assertEquals(result.rp.population, 16150);
        assertEquals(result.rp.state, "WA");
    }

    public void testParseValidNoQuotes() throws Exception {
        String entry = " 53,Wilkeson town,408,401,403,397,397,399,395,395,395,395";

        GeoRecord result = parser.parse(entry);

        assertNotNull(result);
        assertEquals(result.recordType, GeoConstants.RECORD_TYPE_POP);
        assertNotNull(result.rp);
        assertEquals(result.rp.name, "Wilkeson town");
        assertEquals(result.rp.population, 408);
        assertEquals(result.rp.state, "WA");
    }

    public void testParseInvalid() {
        String invalidEntry = "klsajf98wauaoisdfhashfdlahfrlakwehfhteaogt09a8gryag";

        try {
            parser.parse(invalidEntry);
        } catch (GeoRecordParser.RecordFormatException exn) {
            assertEquals(exn.getRecordType(), GeoConstants.RECORD_TYPE_POP);
            return;
        }
        fail("Parser parsed invalid entry");
    }

}
