package ua.kpi.cad.lab3.core.parser;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import ua.kpi.cad.lab3.core.exception.RecordFormatException;
import ua.kpi.cad.lab3.core.exception.WrongRecordTypeException;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TigerRecordType2ParserTest {

    private GeoRecordParser parser;

    @Before
    public void init() {
        this.parser = new TigerRecordType2Parser();
    }

    @Test(expected = WrongRecordTypeException.class)
    public void passingWrongRecordTypeResultsInException() throws RecordFormatException {
        val entryWithWrongRecordType = "1*******";

        parser.parse(entryWithWrongRecordType);
    }

    @Test
    public void parseRecordWithOnePoint() throws RecordFormatException {
        val entry = "21006 186564355  1-121655169+47436472" +
                "+000000000+00000000+000000000+00000000+000000000+00000000" +
                "+000000000+00000000+000000000+00000000+000000000+00000000" +
                "+000000000+00000000+000000000+00000000+000000000+00000000";
        GeoRecord geoRecord = parser.parse(entry);
        assertNotNull(geoRecord.getRecordType2());
        assertEquals(186564355, geoRecord.getRecordType2().getLineId());
        assertEquals(new Double(-121.655169), new Double(geoRecord.getRecordType2().getListLong()[0]));
        assertEquals(new Double(47.436472), new Double(geoRecord.getRecordType2().getListLat()[0]));
        assertEquals(1, geoRecord.getRecordType2().getListLat().length);
        assertEquals(1, geoRecord.getRecordType2().getListLong().length);

    }

    @Test
    public void parseRecordWithTwoPointsAndSequenceNumIs2() throws RecordFormatException {
        val entry = "21006 186564313  2-121647615+47489532-121646333+47491359" +
                "+000000000+00000000+000000000+00000000+000000000+00000000" +
                "+000000000+00000000+000000000+00000000+000000000+00000000" +
                "+000000000+00000000+000000000+00000000";

        GeoRecord geoRecord = parser.parse(entry);

        assertNotNull(geoRecord.getRecordType2());

        assertEquals(186564313, geoRecord.getRecordType2().getLineId());
        assertEquals(2, geoRecord.getRecordType2().getSequenceNum());
        assertEquals(new Double(-121.647615), new Double(geoRecord.getRecordType2().getListLong()[0]));
        assertEquals(new Double(47.489532), new Double(geoRecord.getRecordType2().getListLat()[0]));
        assertEquals(new Double(-121.646333), new Double(geoRecord.getRecordType2().getListLong()[1]));
        assertEquals(new Double(47.491359), new Double(geoRecord.getRecordType2().getListLat()[1]));
        assertEquals(2, geoRecord.getRecordType2().getListLat().length);
        assertEquals(2, geoRecord.getRecordType2().getListLong().length);
    }

}
