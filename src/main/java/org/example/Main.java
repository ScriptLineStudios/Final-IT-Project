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

    public Player player;

    float globalTime;
    private void run() throws ParseException, IOException, FileNotFoundException {
        engine.init();
        world = World.generateArea();
        globalTime = 0;
        player = new Player(200.0f, -2000.0f, engine);

        Random random = new Random();

        Texture block = engine.loadTex("src/main/resources/assets/images/block.png");
        Texture grass = engine.loadTex("src/main/resources/assets/images/grass.png");
        Texture grass_left = engine.loadTex("src/main/resources/assets/images/left.png");
        Texture grass_left_corner = engine.loadTex("src/main/resources/assets/images/left_corner.png");
        Texture top = engine.loadTex("src/main/resources/assets/images/top.png");
        Texture grass_right_corner = engine.loadTex("src/main/resources/assets/images/right_corner.png");
        Texture grass_right = engine.loadTex("src/main/resources/assets/images/right.png");
        Texture water_dirt = engine.loadTex("src/main/resources/assets/images/water_dirt.png");
        Texture grass_2 = engine.loadTex("src/main/resources/assets/images/grass_2.png");
        Texture leaf = engine.loadTex("src/main/resources/assets/images/leaf.png");

        Texture tree = new Texture("src/main/resources/assets/images/tree.png", 
        "src/main/resources/defaultVertex.glsl",
        "src/main/resources/treeFragment.glsl", engine);


       
        Texture water = new Texture("src/main/resources/assets/images/water.png", 
            "src/main/resources/defaultVertex.glsl",
            "src/main/resources/waterFragment.glsl", engine);

        
        HashMap<String, Texture> blockLookup = new HashMap<String, Texture>();
        blockLookup.put("block.png", block);
        blockLookup.put("grass.png", grass);
        blockLookup.put("left.png", grass_left);
        blockLookup.put("left_corner.png", grass_left_corner);
        blockLookup.put("top.png", top);
        blockLookup.put("right.png", grass_right);
        blockLookup.put("right_corner.png", grass_right_corner);
        blockLookup.put("water.png", water);
        blockLookup.put("water_dirt.png", water_dirt);
        blockLookup.put("grass_2.png", grass_2);
        blockLookup.put("tree.png", tree);


        Slime slime = new Slime(200.0f, -2000.0f, engine);

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
                // System.out.printf("FPS: %d\n", frameCount);
                frameCount = 0;
                previousTime = currentTime;
            }
            engine.clear(102 / 255.0f, 137 / 255.0f, 117 / 255.0f);  

            for (Object[] pos:world) {
                if (Math.abs((float)pos[0] - player.x) < 1300) {
                    blockLookup.get((String)pos[2]).render((float)pos[0] - player.camera[0], (float)pos[1] - player.camera[1], 128, 128);
                    blockLookup.get((String)pos[2]).shader.uploadFloat("time", globalTime);
                    if (((String)pos[2]).equals("tree.png")) {
                        if (random.ints(0, 40).findFirst().getAsInt() == 1) {
                            engine.particles.add(new float[]{(float)pos[0] + random.ints(10, 64).findFirst().getAsInt(), (float)pos[1] + random.ints(10, 64).findFirst().getAsInt(), 10, 10, random.ints(0, 64).findFirst().getAsInt(), 1});
                        }
                    }
                }
            }

            /*
            List<Book> found = new ArrayList<Book>();
            for(Book book : books){
                if(book.getIsbn().equals(isbn)){
                    found.add(book);
                }
            }
            books.removeAll(found);
            */

            List<float[]> leafParticles = new ArrayList<float[]>();
            
            for (float[] particle:engine.particles) {
                particle[1] -= 1;
                particle[0] -= Math.sin(particle[4]);
                particle[4] += engine.getDeltaTime();
                particle[5] -= 0.006;

                if (particle[5] <= 0) {
                    leafParticles.add(particle);
                }


                //System.out.println((float)Math.sin(globalTime)*90);
                leaf.render((float)particle[0] - player.camera[0], (float)particle[1] - player.camera[1], 32, 32, false, (float)Math.sin(globalTime)*90, particle[5]);
            }
            engine.particles.removeAll(leafParticles);
            //System.out.println("SIZE: " + engine.particles.size());
            slime.update(this);
            player.update(this);
            engine.update();
        }
    }

    public static void main(String[] args) throws ParseException, IOException, FileNotFoundException {
        new Main().run();
    }
}
