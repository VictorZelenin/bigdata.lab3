package ua.kpi.cad.lab3.core.divider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;
import ua.kpi.cad.lab3.core.renderer.TileRenderer;

/**
 * The purpose of this class is to form a base for classes that divide the
 * Mappable Range into Tile Sets.
 *
 * @author Slava Chernyak
 */
public abstract class TileSetDivider implements Serializable {
	/*
     * Here are the approximate zoom levels to linear distance relations for
	 * a side of a tile:
	 *
	 * Zoom Level			  Long (E-W) delta
	 * 1.  						0.006 		(~ 500 m linear distance)
	 * 2. 						0.012  		(~ 1km linear distance)
	 * 3. 						0.024
	 * 4. 						0.048
	 * 5. 						0.096
	 * 6. 						0.192
	 * 7. 						0.384
	 * 8. 						0.768
	 * 9.  						1.536 		(~ 120 km linear distance)
	 * 10.  					3.072
	 * 11.  					6.144
	 * 12.  					12.288
	 * 13.  					24.576
	 * 14.  					49.152
	 */

    /**
     * The side size in degrees of the tile at the highest zoom level
     */
    public static final double SMALLEST_TILE_SIZE = 0.00625; // about 1 km
    /**
     * The numeric value of the highest (closest) zoom level
     */
    public static final int HIGHEST_ZOOMLEVEL = 1; // ~ 1km per tile
    /**
     * The numeric value of the lowest (farthest) zoom level we allow
     */
    public static final int LOWEST_ZOOMLEVEL = 10; // ~ 250 km per tile

    private final double minLat;
    private final double minLong;
    private final int zoomLevel;

    private final double tileSize;
    private final int numTilesPerSide;
    protected final int[][] tileSetIds;

    /**
     * Create a new tile set divider with the specified mappable range and
     * zoom level.
     */
    public TileSetDivider(double minLat, double minLong, double maxLat,
                          double maxLong, int zoomLevel) {
        if (zoomLevel < HIGHEST_ZOOMLEVEL || zoomLevel > LOWEST_ZOOMLEVEL) {
            throw new IllegalArgumentException("Invalid zoomlevel: " + zoomLevel);
        }
        this.minLat = minLat;
        this.minLong = minLong;
        this.zoomLevel = zoomLevel;

        tileSize = getTileSizeForZoom(zoomLevel);
        double deltaLat = Math.abs(maxLat - minLat);
        double deltaLong = Math.abs(maxLong - minLong);
        double xb = deltaLat / tileSize;
        double yb = deltaLong / tileSize;
        numTilesPerSide = (int) Math.ceil(Math.max(xb, yb));
        tileSetIds = new int[numTilesPerSide][numTilesPerSide];
    }

    public int[][] getTileSetIds() {
        return tileSetIds;
    }

    /**
     * Gets the Tile Set ID associated with the specified tile
     *
     * @param id
     * @return
     */
    public int getTileSetId(TileID id) {
        if (id.x < 0 || id.x >= numTilesPerSide || id.y < 0 || id.y >= numTilesPerSide) {
            return -1;
        }
        return tileSetIds[id.x][id.y];
    }

    /**
     * This method must be implemented by subclasses depending on how they
     * choose to divide the Mappable Range into tile sets.
     *
     * @param numRenderTasks
     */
    abstract public void assignTileSetIds(int numRenderTasks);

    /**
     * This method will render all the tiles in a specified tile set using
     * the renderer provided as the argument. The renderer should already have
     * all of the geographic features relevant to this tile set added before
     * calling this method.
     *
     * This method also takes an output collector so rendered tiles can be written
     * directly to the output stream and a reporter object so that the status of
     * the rendering process can be displayed.
     */
    public void renderTileSet(TileRenderer renderer, int tileSetId,
                              Reducer.Context context)
            throws IOException, InterruptedException {
        for (int i = 0; i < getNumTilesPerSide(); i++) {
            for (int j = 0; j < getNumTilesPerSide(); j++) {
                if (this.tileSetIds[i][j] == tileSetId) {
//                    logger.info("Rendering tile at id " + i + ", " + j + " z:" + zoomLevel);
                    RenderedTileKey k = new RenderedTileKey();
                    k.tileIdX = i;
                    k.tileIdY = j;
                    k.zoomLevel = zoomLevel;

                    // render this tile and write it to the DFS
//                    RenderedTile t = new RenderedTile();
                    renderer.setTile(this, new TileID(i, j));

                    File f = new File("tiles/" + k.zoomLevel + "/" + k.tileIdX
                            + "_" + k.tileIdY + "_" + k.zoomLevel + ".png");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(renderer.renderTile());
                    fos.close();
//                    context.write(k, t);
//                    context.getCounter("Rendered Tiles", "Tile Set " + tileSetId).increment(1);
                }
            }
        }
    }

    private double getTileSizeForZoom(int zoomLevel) {
        int zoomFactor = zoomLevel - HIGHEST_ZOOMLEVEL;
        double bucketSize = SMALLEST_TILE_SIZE;
        for (int i = 0; i < zoomFactor; i++) {
            bucketSize *= 2;
        }
        return bucketSize;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMinLong() {
        return minLong;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public double getTileSize() {
        return tileSize;
    }

    public int getNumTilesPerSide() {
        return numTilesPerSide;
    }

    @Data
    @AllArgsConstructor
    @Getter
    public static class TileID {
        private int x;
        private int y;
    }

    public static class LatLong {
        public LatLong(double Lat, double Long) {
            this.Lat = Lat;
            this.Long = Long;
        }

        double Lat;
        double Long;

        public double getLat() {
            return Lat;
        }

        public double getLong() {
            return Long;
        }
    }

    /**
     * Retrieve the Tile id that contains the specified latitude and longitude
     */
    public TileID getTileID(double Lat, double Long) {
        return new TileID((int) Math.floor((Lat - minLat) / tileSize), (int) Math.floor((Long - minLong) / tileSize));
    }

    /**
     * Retrieve the latitude and longitude that correspond to the top left
     * corner of the specified tile
     */
    public LatLong getLatLong(TileID id) {
        return new LatLong(id.x * tileSize + minLat, id.y * tileSize + minLong);
    }

}
