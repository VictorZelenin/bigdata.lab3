package ua.kpi.cad.lab3.core.renderer;

import java.io.File;
import java.io.FileOutputStream;

public class  FakeRendererTester {

    /**
     * Run the fake renderer by itself and save the
     * output to a file
     */
    public static void main(String[] args) throws Exception {
        TileRenderer renderer = new FakeRenderer();
        File f = new File("fake_render.png");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(renderer.renderTile());
        fos.close();
    }
}
