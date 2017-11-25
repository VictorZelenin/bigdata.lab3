package ua.kpi.cad.lab3.core.renderer;

import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.protocol.*;
import ua.kpi.cad.lab3.core.divider.TileSetDivider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This abstract class forms a base for the tile renderer class
 * you will implement to render your tiles.
 */
public abstract class TileRenderer {
    /**
     * The side size in pixels of a single map tile
     */
    public static final int TILE_SZ = 256;

    protected double minLat;
    protected double minLong;
    protected int zoomLevel;

    protected double scaleFactor;

    protected final BufferedImage image;

    protected List<TigerRecordType1> rt1 = new ArrayList<>();
    protected List<TigerRecordType2> rt2 = new ArrayList<TigerRecordType2>();
    protected List<BgnRecord> rbgn = new ArrayList<BgnRecord>();
    protected List<PopRecord> rp = new ArrayList<PopRecord>();

    public TileRenderer() {
        image = new BufferedImage(TILE_SZ, TILE_SZ, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Sets the tile id of the tile that will be rendered given a tile set divider.
     * <p>
     * What this means is: Once we have loaded all the geographic features into a
     * renderer, we reuse it for all the tiles in the tile set. To render a different
     * tile we simply call this method to select which tile to render (by tile id) with
     * respect to the tile id's of the specified divider.
     */
    public void setTile(TileSetDivider divider, TileSetDivider.TileID tileId) {
        this.minLat = divider.getLatLong(tileId).getLat();
        this.minLong = divider.getLatLong(tileId).getLong();
        this.zoomLevel = divider.getZoomLevel();

        scaleFactor = divider.getTileSize() / (double) TILE_SZ;
    }

    /**
     * This is the method you must implement in your derived class to render
     * the specified tile. Examine FakeRenderer as a simple example.
     */
    public abstract byte[] renderTile() throws IOException;

    /**
     * This method loads a GeoRecord into the renderer. Once all the records for a
     * tile set are loaded we can use the renderer repeatedly to render different tiles
     * in the tile set. The TileSetDivider class implements the latter functionality.
     */
    public void addRecord(GeoRecord record) {
        if (record.getRecordType().startsWith(GeoConstants.RECORD_TYPE_1)) {
            rt1.add(record.getRecordType1());
        } else if (record.getRecordType().startsWith(GeoConstants.RECORD_TYPE_2)) {
            rt2.add(record.getRecordType2());
        } else if (record.getRecordType().startsWith(GeoConstants.RECORD_TYPE_BGN)) {
            rbgn.add(record.getBgnRecord());
        } else if (record.getRecordType().startsWith(GeoConstants.RECORD_TYPE_POP)) {
            rp.add(record.getPopRecord());
        } else {
            // no-op for now
        }
    }

    /**
     * This method will get the x position in the tile given a longitude
     */
    protected int getXFromLon(double lon) {
        return (int) ((lon - minLong) / scaleFactor);
    }

    /**
     * This method will get the y position in the tile given a latitude
     */
    protected int getYFromLat(double lat) {
        return (int) (TILE_SZ - ((lat - minLat) / scaleFactor));
    }
}
