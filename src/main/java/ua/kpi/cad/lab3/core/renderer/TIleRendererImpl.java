package ua.kpi.cad.lab3.core.renderer;

import ua.kpi.cad.lab3.core.protocol.TigerRecordType1;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType2;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.imageio.ImageIO.getImageWritersBySuffix;

public class TIleRendererImpl extends TileRenderer {
    private final Map<TigerRecordType1, List<TigerRecordType2>> joinedRecords = new HashMap<>();

    @Override
    public byte[] renderTile() throws IOException {
        joinRecords();
        render(image.createGraphics());
        ImageWriter writer = getImageWritersBySuffix("png").next();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(image);

        return baos.toByteArray();
    }

    private void joinRecords() {
        for (TigerRecordType1 recordType1 : rt1) {
            List<TigerRecordType2> recordTypes2 = new ArrayList<>();
            for (TigerRecordType2 recordType2 : rt2) {
                if (recordType1.getLineId() == recordType2.getLineId()) {
                    recordTypes2.add(recordType2);
                }
            }

            joinedRecords.put(recordType1, recordTypes2);
        }
    }

    private void render(Graphics2D g) {
        // turn on anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // clear the background to white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, TILE_SZ, TILE_SZ);

        for (Map.Entry<TigerRecordType1, List<TigerRecordType2>> entry : joinedRecords.entrySet()) {
            TigerRecordType1 recordType1 = entry.getKey();
            List<TigerRecordType2> recordTypes2 = entry.getValue();
            if (recordType1.getFeatureType().substring(0, 1).equals("A")) {
                renderRoad(g, recordType1, recordTypes2, Integer.parseInt(recordType1.getFeatureType().substring(1, 2)));
            }
        }
    }

    private void renderRoad(Graphics2D g, TigerRecordType1 recordType1, List<TigerRecordType2> recordTypes2, int roadType) {
        Color color;

        switch (roadType) {
            case 1:
                color = new Color(0xFF6F00);
                break;
            case 2:
                color = new Color(0xF57F17);
                break;
            case 3:
                color = new Color(0xFFA000);
                break;
            case 4:
                color = new Color(0xFFC107);
                break;
            case 5:
                color = new Color(0xFFD54F);
                break;
            case 6:
                color = new Color(0xFFECB3);
                break;
            case 7:
                color = new Color(0x8D6E63);
                break;
            default:
                color = Color.RED;

        }

        g.setStroke(new BasicStroke(8 - roadType));
        g.setColor(color);

        if (recordTypes2.isEmpty()) {
            g.drawLine(getXFromLon(recordType1.getStartLong()), getYFromLat(recordType1.getStartLat()),
                    getXFromLon(recordType1.getEndLong()), getYFromLat(recordType1.getEndLat()));
        } else {
            double[] lats;
            double[] longs;
            for (TigerRecordType2 recordType2 : recordTypes2) {
                lats = recordType2.getListLat();
                longs = recordType2.getListLong();
                for (int i = 1; i < lats.length; i++) {
                    g.drawLine(getXFromLon(longs[i - 1]), getYFromLat(lats[i - 1]), getXFromLon(longs[i]), getYFromLat(lats[i]));
                }
            }
        }
    }
}
