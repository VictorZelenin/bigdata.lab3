package ua.kpi.cad.lab3.main;

import org.apache.commons.io.IOUtils;
import ua.kpi.cad.lab3.core.divider.SimpleDivider;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;
import ua.kpi.cad.lab3.core.renderer.TileRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class TileSetDividerSparkImpl extends SimpleDivider implements Serializable {
    public TileSetDividerSparkImpl(double minLat, double minLong, double maxLat, double maxLong, int zoomLevel) {
        super(minLat, minLong, maxLat, maxLong, zoomLevel);
    }

    public void renderTileSet(TileRenderer renderer, int tileSetId)
            throws IOException, InterruptedException {
        for (int i = 0; i < getNumTilesPerSide(); i++) {
            for (int j = 0; j < getNumTilesPerSide(); j++) {
                if (this.tileSetIds[i][j] == tileSetId) {
                    RenderedTileKey k = new RenderedTileKey();
                    k.tileIdX = i;
                    k.tileIdY = j;
                    k.zoomLevel = getZoomLevel();

                    // render this tile and write it to the DFS
                    RenderedTile t = new RenderedTile();
                    renderer.setTile(this, new TileID(i, j));
                    t.tileData = renderer.renderTile();

                    File f = new File("tiles/" + k.zoomLevel + "/" + k.tileIdX
                            + "_" + k.tileIdY + "_" + k.zoomLevel + ".png");

                    IOUtils.write(t.tileData, new FileOutputStream(f));
                }
            }

        }
    }
}

