package ua.kpi.cad.lab3;

import junit.framework.TestCase;

public class SimpleDividerTest extends TestCase {
    TileSetDivider divider;

    public void testSimpleDivider() {
        // create a divider with an 8x8 grid of tiles
        divider = new SimpleDivider(0, 0, 0.05, 0.05, 1);

        // check assignment with 8 render tasks
        divider.assignTileSetIds(5);

        // task 0 is row 0
        assertEquals(divider.tileSetIds[0][0], 0);
        assertEquals(divider.tileSetIds[0][1], 0);
        assertEquals(divider.tileSetIds[0][2], 0);
        assertEquals(divider.tileSetIds[0][3], 0);
        assertEquals(divider.tileSetIds[0][4], 0);
        assertEquals(divider.tileSetIds[0][7], 0);

        // task 3 is row 3
        assertEquals(divider.tileSetIds[3][0], 3);
        assertEquals(divider.tileSetIds[3][1], 3);
        assertEquals(divider.tileSetIds[3][2], 3);
        assertEquals(divider.tileSetIds[3][3], 3);
        assertEquals(divider.tileSetIds[3][4], 3);
        assertEquals(divider.tileSetIds[3][7], 3);

        // row 7 is task 7 % 5 = 2
        assertEquals(divider.tileSetIds[7][0], 2);
        assertEquals(divider.tileSetIds[7][1], 2);
        assertEquals(divider.tileSetIds[7][2], 2);
        assertEquals(divider.tileSetIds[7][3], 2);
        assertEquals(divider.tileSetIds[7][4], 2);
        assertEquals(divider.tileSetIds[7][7], 2);
    }
}
