package ua.kpi.cad.lab3.core.divider;

/**
 * This is a simple Tile Set Divider that splits the Mappable Range into
 * tile sets by assigning every row of tiles a set id modulo the number
 * of render tasks.
 */
public class SimpleDivider extends TileSetDivider {

    public SimpleDivider(double minLat, double minLong, double maxLat,
                         double maxLong, int zoomLevel) {
        super(minLat, minLong, maxLat, maxLong, zoomLevel);
    }

    /**
     * This divider takes the range and divides it into tile sets
     * by assigning every row of tiles to a tile set. (modulo the
     * number of render tasks)
     */
    @Override
    public void assignTileSetIds(int numRenderTasks) {
        for (int i = 0; i < getNumTilesPerSide(); i++) {
            for (int j = 0; j < getNumTilesPerSide(); j++) {
                tileSetIds[i][j] = i % numRenderTasks;
            }
        }
    }
}
