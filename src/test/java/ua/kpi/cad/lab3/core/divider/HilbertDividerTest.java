package ua.kpi.cad.lab3.core.divider;

import ua.kpi.cad.lab3.core.divider.TileSetDivider;

import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test for the Hilbert Divider you
 * will have to implement.
 */
public class HilbertDividerTest {
    TileSetDivider divider;

    public void testHilbertDivider() {
        // Set up the divider to be a 8x8 grid
        // TODO: un-comment this line to create a new instance of
        // the Hilbert divider with a 8x8 grid of tiles.
        // divider = new HilbertDivider(0, 0, 0.05, 0.05, 1);

        // initialize it with 64 tasks so each tile has
        // its Hilbert curve id as the set id.
        divider.assignTileSetIds(64);

        // test that the ID's are along the Hilbert curve
        // starting from the top left corner

        // test a few points along the curve. The curve below
        // is the Hibert curve over an 8x8 grid with each vertex
        // at a tile in the grid
        //
        // |0|1|2|3|4|5|6|7|
        //     _ _	 _ _
        //	|_|  _| |_  |_|
        //   _  |_   _|  _
        //  | |_ _| |_ _| |
        //  |_   _ _ _   _|
        //   _| |_   _| |_
        //  |  _  | |  _  |
        //  |_| |_| |_| |_|
        //

        // the first 10 points along the curve
        assertEquals(divider.tileSetIds[0][0], 0);
        assertEquals(divider.tileSetIds[1][0], 1);
        assertEquals(divider.tileSetIds[1][1], 2);
        assertEquals(divider.tileSetIds[0][1], 3);
        assertEquals(divider.tileSetIds[0][2], 4);
        assertEquals(divider.tileSetIds[0][3], 5);
        assertEquals(divider.tileSetIds[1][3], 6);
        assertEquals(divider.tileSetIds[1][2], 7);
        assertEquals(divider.tileSetIds[2][2], 8);
        assertEquals(divider.tileSetIds[2][3], 9);

 //        the last few points
        assertEquals(divider.tileSetIds[1][6], 61);
        assertEquals(divider.tileSetIds[1][7], 62);
        assertEquals(divider.tileSetIds[0][7], 63);


        // Now assign tile set ID's with a smaller number of reduce tasks
        // to make sure we break the curve up into segments properly
        divider.assignTileSetIds(7);

        assertEquals(divider.tileSetIds[0][0], 0);
        assertEquals(divider.tileSetIds[2][3], 0);
        assertEquals(divider.tileSetIds[3][3], 1);
        assertEquals(divider.tileSetIds[5][1], 1);
        assertEquals(divider.tileSetIds[6][1], 2);
    }

}
