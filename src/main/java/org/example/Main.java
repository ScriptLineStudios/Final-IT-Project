package org.example;


import java.io.*;
import java.util.*;
import org.json.simple.parser.*;
import java.util.HashMap;

import org.lwjgl.glfw.*;
import org.lwjgl.glfw.GLFW;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Main {
    Engine engine = new Engine();
    public List<Object[]> world;

    float globalTime;
    private void run() throws ParseException, IOException, FileNotFoundException {
        engine.init();
        world = World.generateArea();
        globalTime = 0;
        Player player = new Player(0.0f, 0.0f, engine);

        Texture block = engine.loadTex("src/main/resources/assets/images/block.png");
        Texture grass = engine.loadTex("src/main/resources/assets/images/grass.png");
        Texture grass_left = engine.loadTex("src/main/resources/assets/images/left.png");
        Texture grass_left_corner = engine.loadTex("src/main/resources/assets/images/left_corner.png");
        Texture top = engine.loadTex("src/main/resources/assets/images/top.png");
        Texture grass_right_corner = engine.loadTex("src/main/resources/assets/images/right_corner.png");
        Texture grass_right = engine.loadTex("src/main/resources/assets/images/right.png");
        Texture water = new Texture("src/main/resources/assets/images/water.png", 
            "src/main/resources/defaultVertex.glsl",
            "src/main/resources/defaultFragment.glsl", engine);

        
        HashMap<String, Texture> blockLookup = new HashMap<String, Texture>();
        blockLookup.put("block.png", block);
        blockLookup.put("grass.png", grass);
        blockLookup.put("left.png", grass_left);
        blockLookup.put("left_corner.png", grass_left_corner);
        blockLookup.put("top.png", top);
        blockLookup.put("right.png", grass_right);
        blockLookup.put("right_corner.png", grass_right_corner);
        blockLookup.put("water.png", water);

        double previousTime = glfwGetTime();
        int frameCount = 0;
        
        while (engine.windowOpen()) 
        {
            globalTime += engine.getDeltaTime();
            // Measure speed
            double currentTime = glfwGetTime();
            frameCount++;
            // If a second has passed.
            if ( currentTime - previousTime >= 1.0 )
            {
                // Display the frame count here any way you want.
                System.out.printf("FPS: %d\n", frameCount);
                frameCount = 0;
                previousTime = currentTime;
            }
            engine.clear(146 / 255.0f, 179 / 255.0f, 117 / 255.0f);  

            for (Object[] pos:world) {
                blockLookup.get((String)pos[2]).render((float)pos[0] - player.camera[0], (float)pos[1] - player.camera[1], 128, 128);
                System.out.println(blockLookup.get((String)pos[2]).fragmentPath);
                blockLookup.get((String)pos[2]).shader.uploadFloat("time", globalTime);
            }
            
            player.update(this);
            engine.update();
        }
    }

    public static void main(String[] args) throws ParseException, IOException, FileNotFoundException {
        new Main().run();
    }
}
