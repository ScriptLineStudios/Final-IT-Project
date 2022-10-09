package org.example;


import java.io.*;
import java.util.*;
import org.json.simple.parser.*;
import java.util.HashMap;

public class Main {
    Engine engine = new Engine();
    
    private void run() throws ParseException, IOException, FileNotFoundException {
        engine.init();
        List<Object[]> world = World.generateArea();
        Player player = new Player(0.0f, 0.0f, engine);

        Texture block = engine.loadTex("src/main/resources/assets/images/block.png");
        Texture grass = engine.loadTex("src/main/resources/assets/images/grass.png");
        Texture grass_left = engine.loadTex("src/main/resources/assets/images/left.png");
        Texture grass_left_corner = engine.loadTex("src/main/resources/assets/images/left_corner.png");
        Texture top = engine.loadTex("src/main/resources/assets/images/top.png");
        Texture grass_right_corner = engine.loadTex("src/main/resources/assets/images/right_corner.png");
        Texture grass_right = engine.loadTex("src/main/resources/assets/images/right.png");

        


        HashMap<String, Texture> blockLookup = new HashMap<String, Texture>();
        blockLookup.put("block.png", block);
        blockLookup.put("grass.png", grass);
        blockLookup.put("left.png", grass_left);
        blockLookup.put("left_corner.png", grass_left_corner);
        blockLookup.put("top.png", top);
        blockLookup.put("right.png", grass_right);
        blockLookup.put("right_corner.png", grass_right_corner);
        


        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);

            for (Object[] pos:world) {
                blockLookup.get((String)pos[2]).render((float)pos[0] - player.camera[0], (float)pos[1] - player.camera[1], 16 * 4, 16 * 4);
            }

            player.update();
            engine.update();
        }
    }

    public static void main(String[] args) throws ParseException, IOException, FileNotFoundException {
        new Main().run();
    }
}
