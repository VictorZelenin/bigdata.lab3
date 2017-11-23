package ua.kpi.cad.lab3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * This class implements the tile renderer abstract class.
 * This does not actually render any of the map features, instead
 * it just demonstrates a couple of the drawing primitives of
 * Java's Graphics2D class. For more information, refer to the
 * Javadocs on Graphics2D
 */
public class FakeRenderer extends TileRenderer {

    @Override
    public byte[] renderTile() throws IOException {
        render(image.createGraphics());
        javax.imageio.ImageWriter writer = javax.imageio.ImageIO
                .getImageWritersBySuffix("png").next();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(image);
        return baos.toByteArray();
    }

    public void render(Graphics2D g) {
        // turn on anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // clear the background to white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, TILE_SZ, TILE_SZ);

        // set the line width for drawing
        g.setStroke(new BasicStroke(1.2f));
        // sets the color to be used
        g.setColor(Color.RED);
        // draw a line from (10,15) to (30,30)
        g.drawLine(10, 15, 200, 30);

        // draw a text string in a box
        String text = "Hello World";
        // set the font
        g.setFont(new Font("Serif", Font.PLAIN, 12));
        // set the color
        g.setColor(Color.BLACK);
        // draw the string
        g.drawString(text, 20, 50);

        // compute the bounding box of the string
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        // draw the rectangle around the string
        g.drawRect(20, 50 - height, width, height);

        // draw polygon
        Polygon p = new Polygon();
        p.addPoint(80, 80);
        p.addPoint(90, 80);
        p.addPoint(90, 90);
        p.addPoint(85, 95);
        p.addPoint(80, 90);
        g.fillPolygon(p);
    }
}
