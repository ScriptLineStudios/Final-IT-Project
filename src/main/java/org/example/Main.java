package org.example;

import java.io.*;
import java.util.*;
import org.json.simple.parser.*;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    Engine engine = new Engine();
    public List<Object[]> world;
    public List<Bullet> enemyBullets;
    public List<Slime> slimes;

    Texture[] particleImgs;
    Cat cat;
    public Player player;

    float globalTime;
    private void run() throws ParseException, IOException, FileNotFoundException {
        engine.init();
        world = World.generateArea();
        globalTime = 0;
        player = new Player(200.0f, -2000.0f, engine);
        cat = new Cat(200.0f, -2000.0f, engine);

        Random random = new Random();

        enemyBullets = new ArrayList<Bullet>();

        Texture block = engine.loadTex("src/main/resources/assets/images/block.png");
        Texture grass = engine.loadTex("src/main/resources/assets/images/grass.png");
        Texture grass_left = engine.loadTex("src/main/resources/assets/images/left.png");
        Texture grass_left_corner = engine.loadTex("src/main/resources/assets/images/left_corner.png");
        Texture top = engine.loadTex("src/main/resources/assets/images/top.png");
        Texture grass_right_corner = engine.loadTex("src/main/resources/assets/images/right_corner.png");
        Texture grass_right = engine.loadTex("src/main/resources/assets/images/right.png");
        Texture water_dirt = engine.loadTex("src/main/resources/assets/images/water_dirt.png");
        Texture grass_2 = engine.loadTex("src/main/resources/assets/images/grass_2.png");
        Texture _slime = engine.loadTex("src/main/resources/assets/images/slime.png");

        Texture leaf = engine.loadTex("src/main/resources/assets/images/leaf.png");
        Texture circle = engine.loadTex("src/main/resources/assets/images/part.png");


        particleImgs = new Texture[]{leaf, circle};

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
        blockLookup.put("slime.png", _slime);

        //Slime slime = new Slime(200.0f, -2000.0f, engine);
        //Slime slime2 = new Slime(700.0f, -2000.0f, engine);

        slimes = new ArrayList<Slime>();

        double previousTime = glfwGetTime();
        int frameCount = 0;

        List<Object> enemys = new ArrayList<Object>();

        for (Object[] pos:world) {
            if (((String)pos[2]).equals("slime.png")) {
                slimes.add(new Slime((float)pos[0], (float)pos[1], engine));
                enemys.add(pos);
            }
        }

        world.removeAll(enemys);

        System.out.println(slimes.size());
        
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
                    if (((String)pos[2]).equals("tree.png")) {
                        blockLookup.get((String)pos[2]).render((float)pos[0] - player.camera[0], (float)pos[1] - player.camera[1], 128, 256);
                        if (random.ints(0, 40).findFirst().getAsInt() == 1) {
                            engine.particles.add(new float[]{(float)pos[0] + random.ints(10, 64).findFirst().getAsInt(), (float)pos[1] + random.ints(10, 64).findFirst().getAsInt(), 10, 10, random.ints(0, 64).findFirst().getAsInt(), 1, 0});
                        }                    
                    }
                    else {
                        blockLookup.get((String)pos[2]).render((float)pos[0] - player.camera[0], (float)pos[1] - player.camera[1], 128, 128);
                        blockLookup.get((String)pos[2]).shader.uploadFloat("time", globalTime);                        
                    }
                }
            }

            List<float[]> leafParticles = new ArrayList<float[]>();

            
            for (float[] particle:engine.particles) {
                particle[1] -= 1;
                particle[0] -= Math.sin(particle[4]);
                particle[4] += engine.getDeltaTime();
                particle[5] -= 0.006;

                if (particle[5] <= 0) {
                    leafParticles.add(particle);
                }

                leaf.render((float)particle[0] - player.camera[0], (float)particle[1] - player.camera[1], 32, 32, false, (float)Math.sin(globalTime)*90, particle[5]);
            }

            for (float[] _particle:engine.attackParticles) {
                _particle[0] -= _particle[2] * _particle[4] / 4;
                _particle[1] -= _particle[3] * _particle[4] / 4;

                _particle[4] -= 0.006f;

                circle.render((float)_particle[0] - player.camera[0], (float)_particle[1] - player.camera[1], 50, 50, false, _particle[4]*360, _particle[4]);
            }
            //System.out.println(enemyBullets.size());

            List<Bullet> badBullets = new ArrayList<Bullet>();

            for (Bullet bullet:enemyBullets) {
                if (bullet.lifetime <= 0) {
                    badBullets.add(bullet);
                }

                bullet.bullets[bullet.animationIndex / 15].shader.uploadFloat("time", globalTime);

                bullet.update(this);
            }

            List<Slime> badSlimes = new ArrayList<Slime>();

            for (Slime __slime:slimes) {
                if (__slime.health <= 0) {
                    badSlimes.add(__slime);
                }
                __slime._update(this);
            }

            slimes.removeAll(badSlimes);
            engine.particles.removeAll(leafParticles);
            enemyBullets.removeAll(badBullets);

            //slime._update(this);
            //slime2._update(this);
            player.update(this);
            cat.update(this);
            engine.update();
            
        }
    }

    public static void main(String[] args) throws ParseException, IOException, FileNotFoundException {
        new Main().run();
    }
}
