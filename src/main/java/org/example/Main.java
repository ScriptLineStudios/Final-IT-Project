/*
***** Main.java *****

*/


package org.example;

import java.io.*;
import java.lang.reflect.Method;
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

    Random random;
    int currentMap;

    public List<Object[]> genMap(int map) throws ParseException, IOException, FileNotFoundException, java.text.ParseException{
        return World.generateArea(map);
    }
    List<Object> enemys;
    float globalTime;
    public float zoom = 1.0f;
    Texture youDied;
    float gameOverSize;
    Button againButton;
    Button feedButton;

    Text menu;
    public void printHello() {
        System.out.println("Hello World!");
    }
    public void petMenu(Cat cat, Engine menuEngine) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        menu = new Text("pet menu", -300, 300, 128, engine);
        Text feed = new Text("feed: 1", -700, 400, 128, engine);
        List<Food> petFood = new ArrayList<Food>();
        while (menuEngine.windowOpen()) {
            menuEngine.clear(0.5f, 0.5f, 0.5f);
            cat.x -= 5;
            if (cat.x < -1000.0f) {
                cat.x = 850;
            }

            if (engine.getKey(GLFW_KEY_1)) {
                System.out.println("yes");
                petFood.add(new Food(engine));
            }

            for (Food piece:petFood) {
                piece.update(this);
            }
            cat.animationIndex = cat.animate(cat.walkAnimations, cat.animationIndex, 8);
            cat.walkAnimations[cat.animationIndex / 8].render(cat.x, -200, 256, 256);
            menu.y = -600;
            feed.render_text();

            menu.render_text();
            menuEngine.update();
        }
    }

    Text textYou;
    Text textDied;

    public void run() throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        random = new Random();
        currentMap = random.ints(0, 5).findFirst().getAsInt();
        world = World.generateArea(currentMap);
        engine.init();
        globalTime = 0;
        player = new Player(200.0f, -2000.0f, engine);
        cat = new Cat(200.0f, -2000.0f, engine);

        enemyBullets = new ArrayList<Bullet>();

        HashMap<Integer, Integer> enemyLookup = new HashMap<Integer, Integer>();
        enemyLookup.put(0, 6);
        enemyLookup.put(1, 9);
        enemyLookup.put(2, 7);
        enemyLookup.put(3, 8);
        enemyLookup.put(4, 9);

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

        Texture healthBar = engine.loadTex("src/main/resources/assets/images/almost_empty.png");
        Texture health = engine.loadTex("src/main/resources/assets/images/health.png");
        Texture cursor = engine.loadTex("src/main/resources/assets/images/cursor.png");
        
        Texture levelComplete = engine.loadTex("src/main/resources/assets/images/level_complete.png");
        Texture _againButton = engine.loadTex("src/main/resources/assets/images/again_button.png");
        againButton = new Button(_againButton, "again", engine);

        youDied = engine.loadTex("src/main/resources/assets/images/you_died.png");

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

        enemys = new ArrayList<Object>();

        gameOverSize = 1.0f;

        for (Object[] pos:world) {
            if (((String)pos[2]).equals("slime.png")) {
                slimes.add(new Slime((float)pos[0], (float)pos[1], engine));
                enemys.add(pos);
            }
        }

        world.removeAll(enemys);

        textYou = new Text("you", -0.0f, 0.0f, 256.0f, engine);
        textDied = new Text("died", -0.0f, -256.0f, 256.0f, engine);

        System.out.println(engine.textureIndex);
        //petMenu(cat, engine);

        while (engine.windowOpen()) {
            globalTime += engine.getDeltaTime();
            // Measure speed
            double currentTime = glfwGetTime();
            frameCount++;
            // If a second has passed.
            if ( currentTime - previousTime >= 1.0 )
            {
                System.gc();
                // Display the frame count here any way you want.
                // System.out.printf("FPS: %d\n", frameCount);
                frameCount = 0;
                previousTime = currentTime;
            }
            engine.clear(102 / 255.0f, 137 / 255.0f, 117 / 255.0f);  

            for (Object[] pos:world) {
                if (Math.abs((float)pos[0] - player.x) < 1300) {
                    if (((String)pos[2]).equals("tree.png")) {
                    blockLookup.get((String)pos[2]).render(((float)pos[0] - player.camera[0]) * zoom, ((float)pos[1] - player.camera[1]) * zoom, 128 * zoom, 256 * zoom);
                    if (random.ints(0, 40).findFirst().getAsInt() == 1) {
                        engine.particles.add(new float[]{(float)pos[0] + random.ints(10, 64).findFirst().getAsInt(), (float)pos[1] + random.ints(10, 64).findFirst().getAsInt(), 10, 10, random.ints(0, 64).findFirst().getAsInt(), 1, 0});
                    }                    
                    }
                    else {
                        blockLookup.get((String)pos[2]).render(((float)pos[0] - player.camera[0]) * zoom, ((float)pos[1] - player.camera[1]) * zoom, 128 * zoom, 128 * zoom);
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

                        
            if (player.health > 0) {
                for (Slime __slime:slimes) {
                    if (__slime._dead == true) {
                        badSlimes.add(__slime);
                    }
                    __slime._update(this);
                }
                
            }

            slimes.removeAll(badSlimes);
            engine.particles.removeAll(leafParticles);
            enemyBullets.removeAll(badBullets);

            double[] mousePos = engine.getMousePos();

            cursor.render((float)mousePos[0], (float)-mousePos[1], 64, 64);

            healthBar.render(-700, 400+ 128, 256*2, 64*2);
            health.render(-630, 432 + 120, player.health * 4.05f, 72);

            player._update(this);

            if (player.health > 0) {
                cat.update(this);
            }


            if (player.kills >= enemyLookup.get(currentMap)) {
                levelComplete.render(-700, -400, 200* gameOverSize, 128 * gameOverSize);
                if (gameOverSize < 7.0f) {
                    gameOverSize += 0.1;
                }
                else {
                    againButton.update(-200, -500, 128 * 4, 64 * 4, this);
                }
                
            }


            engine.update();
            
        }
    }

    public static void main(String[] args) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        new Main().run();
    }
}
