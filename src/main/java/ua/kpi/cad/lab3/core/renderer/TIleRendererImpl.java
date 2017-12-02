package ua.kpi.cad.lab3.core.renderer;

import com.google.common.primitives.Ints;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType1;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType2;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import static javax.imageio.ImageIO.getImageWritersBySuffix;

public class TIleRendererImpl extends TileRenderer implements Serializable {
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
        g.setColor(new Color(0xC5E1A5));
        g.fillRect(0, 0, TILE_SZ, TILE_SZ);

        for (Map.Entry<TigerRecordType1, List<TigerRecordType2>> entry : joinedRecords.entrySet()) {
            TigerRecordType1 recordType1 = entry.getKey();
            List<TigerRecordType2> recordTypes2 = entry.getValue();
            String type = recordType1.getFeatureType().substring(0, 1);
            if (type.equals("A") || type.equals("B") || type.equals("H")) {
                int size = Integer.parseInt(recordType1.getFeatureType().substring(1, 2));
                renderLine(g, recordType1, recordTypes2, size, type);
            } else if (type.equals("D") || type.equals("E")) {
                renderPolygon(g, recordType1, recordTypes2, type);
            }
        }
    }

    private void renderPolygon(Graphics2D g, TigerRecordType1 recordType1, List<TigerRecordType2> recordTypes2, String polygonType) {
        Color color;
        switch (polygonType) {
            case "D":
                color = Color.YELLOW;
                break;
            case "E":
                color = Color.BLUE;
                break;
            default:
                color = Color.RED;
        }


        g.setStroke(new BasicStroke(1));
        g.setColor(color);

        List<Integer> xPoints = new ArrayList<>();
        List<Integer> yPoints = new ArrayList<>();

        if (recordTypes2.isEmpty()) {
            xPoints.add(getXFromLon(recordType1.getStartLong()));
            xPoints.add(getXFromLon(recordType1.getEndLong()));
            yPoints.add(getYFromLat(recordType1.getStartLat()));
            yPoints.add(getYFromLat(recordType1.getEndLat()));
        } else {
            double[] lats;
            double[] longs;
            for (TigerRecordType2 recordType2 : recordTypes2) {
                lats = recordType2.getListLat();
                longs = recordType2.getListLong();
                for (int i = 0; i < lats.length; i++) {
                    xPoints.add(getXFromLon(longs[i]));
                    yPoints.add(getYFromLat(lats[i]));
                }
            }
        }

        if (polygonType.equals("D")) {
            g.fillPolygon(Ints.toArray(xPoints), Ints.toArray(yPoints), xPoints.size());
        }

        if (polygonType.equals("E")) {
            g.drawPolygon(Ints.toArray(xPoints), Ints.toArray(yPoints), xPoints.size());
        }
    }

    private void renderLine(Graphics2D g, TigerRecordType1 recordType1, List<TigerRecordType2> recordTypes2, int lineSize, String lineType) {
        Color color;

        switch (lineType) {
            case "A":
                switch (lineSize) {
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
                        color = new Color(0x6D4C41);
                        break;
                    case 6:
                        color = new Color(0x8D6E63);
                        break;
                    case 7:
                        color = new Color(0xBCAAA4);
                        break;
                    default:
                        color = Color.RED;
                }
                break;
            case "B":
                switch (lineSize) {
                    case 1:
                        color = new Color(0x424242);
                        break;
                    case 2:
                        color = new Color(0x616161);
                        break;
                    case 3:
                        color = new Color(0x757575);
                        break;
                    case 4:
                        color = new Color(0x9E9E9E);
                        break;
                    case 5:
                        color = new Color(0xBDBDBD);
                        break;
                    default:
                        color = Color.RED;
                }
                break;
            case "H":
                switch (lineSize) {
                    case 0:
                        color = new Color(0x81D4FA);
                        break;
                    case 1:
                        color = new Color(0x0277BD);
                        break;
                    case 2:
                        color = new Color(0x03A9F4);
                        break;
                    case 3:
                        color = new Color(0x448AFF);
                        break;
                    case 4:
                        color = new Color(0x2962FF);
                        break;
                    case 5:
                        color = new Color(0x00838F);
                        break;
                    case 6:
                        color = new Color(0x536DFE);
                        break;
                    case 7:
                        color = new Color(0x3D5AFE);
                        break;
                    case 8:
                        color = new Color(0x81D4FA);
                        break;
                    default:
                        color = Color.RED;
                }
                break;
            default:
                color = Color.RED;
        }

        g.setStroke(new BasicStroke((float) (8 - lineSize) / 2));
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
