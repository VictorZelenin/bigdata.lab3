package ua.kpi.cad.lab3.core.parser;

import org.junit.Assert;
import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class PopParserTest {
    private GeoRecordParser parser;

    public void setUp() {
        parser = new PopParser();
    }

    public void testParseValidWithQuotes() throws Exception {
        String entry = " 53,Aberdeen city,\"16,150\",\"16,213\",\"16,195\",\"16,197\",\"16,175\",\"16,212\",\"16,253\",\"16,426\",\"16,461\",\"16,461\"";

        GeoRecord result = parser.parse(entry);

        assertNotNull(result);
        Assert.assertEquals(result.getRecordType(), GeoConstants.RECORD_TYPE_POP);
        assertNotNull(result.getPopRecord());
        assertEquals(result.getPopRecord().name, "Aberdeen city");
        assertEquals(result.getPopRecord().population, 16150);
        assertEquals(result.getPopRecord().state, "WA");
    }

    public void testParseValidNoQuotes() throws Exception {
        String entry = " 53,Wilkeson town,408,401,403,397,397,399,395,395,395,395";

        GeoRecord result = parser.parse(entry);

        assertNotNull(result);
        assertEquals(result.getRecordType(), GeoConstants.RECORD_TYPE_POP);
        assertNotNull(result.getPopRecord());
        assertEquals(result.getPopRecord().name, "Wilkeson town");
        assertEquals(result.getPopRecord().population, 408);
        assertEquals(result.getPopRecord().state, "WA");
    }

    public void testParseInvalid() {
        String invalidEntry = "klsajf98wauaoisdfhashfdlahfrlakwehfhteaogt09a8gryag";

        try {
            parser.parse(invalidEntry);
        } catch (RecordFormatException exn) {
            assertEquals(exn.getRecordType(), GeoConstants.RECORD_TYPE_POP);
            return;
        }
        fail("Parser parsed invalid entry");
    }

}
