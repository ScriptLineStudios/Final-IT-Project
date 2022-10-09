package org.example;


import java.io.*;
import java.util.*;
import org.json.simple.parser.*;

public class Main {
    Engine engine = new Engine();
    
    private void run() throws ParseException, IOException, FileNotFoundException {
        engine.init();
        List<float[]> world = World.generateArea();
        Player player = new Player(0.0f, 0.0f, engine);

        Texture block = new Texture("src/main/resources/assets/images/block.png", engine);
        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);

            for (float[] pos:world) {
                block.render(pos[0] - player.camera[0], pos[1] - player.camera[1], 16 * 4, 16 * 3, false, 0);
            }

            player.update();
            engine.update();
        }
    }

    public static void main(String[] args) throws ParseException, IOException, FileNotFoundException {
        new Main().run();
    }
}
