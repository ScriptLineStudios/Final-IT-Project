package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity{
    Engine engine;

    Texture idleAnimations[];
    Texture walkAnimations[];
    Texture currentAnimation[];
    Texture deathAnimations[];

    Texture weaponImg;

    float[] camera;

    int weaponOffsetX;
    int weaponOffsetY;
    int weaponTimer = 0;
    int attackX, attackY;

    Texture weaponAttack;
    Texture whitePlayerImage;

    List<float[]> playerWhiteImages;

    float attack[];
    Texture shadow;

    double[] capMouse;
    float health;

    Texture loadTex(String path) throws IOException{
        return new Texture(path, "src/main/resources/defaultVertex.glsl", "src/main/resources/playerFragment.glsl", engine);
    }

    int kills;

    boolean canAttack;

    Texture cat;
    Player(float playerX, float playerY, Engine _engine) throws IOException {
        super(playerX, playerY, 500);

        canAttack = true;

        health = 1;
        kills = 0;

        this.engine = _engine;
        shadow = engine.loadTex("src/main/resources/assets/images/shadow.png");
        idleAnimations = new Texture[]{loadTex("src/main/resources/assets/images/player/0.png"),
                                    loadTex("src/main/resources/assets/images/player/1.png"),
                                    loadTex("src/main/resources/assets/images/player/2.png"),
                                    loadTex("src/main/resources/assets/images/player/3.png"),
                                    loadTex("src/main/resources/assets/images/player/4.png"),
                                    loadTex("src/main/resources/assets/images/player/5.png"),
                                    loadTex("src/main/resources/assets/images/player/6.png"),
                                    loadTex("src/main/resources/assets/images/player/7.png"),

        };

        walkAnimations = new Texture[]{loadTex("src/main/resources/assets/images/0.png"),
                                        loadTex("src/main/resources/assets/images/1.png"),
                                        loadTex("src/main/resources/assets/images/2.png"),
                                        loadTex("src/main/resources/assets/images/3.png"),
                                        loadTex("src/main/resources/assets/images/4.png"),
                                        loadTex("src/main/resources/assets/images/5.png"),
                                        loadTex("src/main/resources/assets/images/6.png"),
        };

        cat = engine.loadTex("src/main/resources/assets/images/cat.png");

        deathAnimations = new Texture[]{
            engine.loadTex("src/main/resources/assets/images/death1.png"),
            engine.loadTex("src/main/resources/assets/images/death2.png"),
            engine.loadTex("src/main/resources/assets/images/death3.png"),
            engine.loadTex("src/main/resources/assets/images/death4.png"),
            engine.loadTex("src/main/resources/assets/images/death5.png"),
            engine.loadTex("src/main/resources/assets/images/death6.png"),
            engine.loadTex("src/main/resources/assets/images/death7.png"),
            engine.loadTex("src/main/resources/assets/images/death8.png"),
            engine.loadTex("src/main/resources/assets/images/death9.png"),
            engine.loadTex("src/main/resources/assets/images/death10.png"),
            engine.loadTex("src/main/resources/assets/images/death11.png"),
            engine.loadTex("src/main/resources/assets/images/death12.png"),
            engine.loadTex("src/main/resources/assets/images/death13.png"),
            engine.loadTex("src/main/resources/assets/images/death14.png"),
            engine.loadTex("src/main/resources/assets/images/death15.png"),
            engine.loadTex("src/main/resources/assets/images/death16.png"),
            engine.loadTex("src/main/resources/assets/images/death17.png"),
            engine.loadTex("src/main/resources/assets/images/death18.png"),
            engine.loadTex("src/main/resources/assets/images/death19.png"),
            engine.loadTex("src/main/resources/assets/images/death20.png"),



        };

        whitePlayerImage = engine.loadTex("src/main/resources/assets/images/player_white.png");
        
        currentAnimation = idleAnimations;
        weaponImg = engine.loadTex("src/main/resources/assets/images/weapons/basic_sword.png");
        weaponAttack = engine.loadTex("/home/scriptline/Final-IT-Project/src/main/resources/assets/images/weapon_attack.png");
        camera = new float[]{0.0f, 0.0f};
        weaponOffsetX = 0;
        weaponOffsetY = 0;
        attack = new float[]{0, 0};
        playerWhiteImages = new ArrayList<float[]>();
        capMouse = new double[]{0.0, 0.0};
    }

    private List<Object[]> getCollidingTiles(List<Object[]> world) {
        List<Object[]> collidingTiles = new ArrayList<Object[]>(); 
        for (Object[] pos:world) {
            float[] tileRect = new float[]{(float)pos[0], (float)pos[1], 128, 128};
            float[] playerRect = new float[]{(float)x, (float)y, 128, 128};
            if (engine.collideRects(tileRect, playerRect) && !(((String)pos[2]).equals("grass_2.png")) && !(((String)pos[2]).equals("tree.png"))) {
                collidingTiles.add(pos);
            }
        }
        return collidingTiles;
    }

    private void move(float[] playerMovement, List<Object[]> world) {

        ////////////////////////////// X
        x += playerMovement[0];
        List<Object[]> collidingTiles = getCollidingTiles(world);
        for (Object[] pos:collidingTiles) {
            if (playerMovement[0] > 0) { //right: x > 0 playerMovement: [-3, 0]
                x = (float)pos[0] - 129;
            }
            else if (playerMovement[0] < 0) { //left: x < 0
                x = (float)pos[0] + 129;
            }
        }
        ////////////////////////////// END OF X


        ///////////////////////////// Y
        y += playerMovement[1]; 
        collidingTiles = getCollidingTiles(world);
        for (Object[] pos:collidingTiles) {
            if (playerMovement[1] > 0) {
                y = (float)pos[1] - 128;
            }
            else if (playerMovement[1] < 0) {
                y = (float)pos[1] + 129;
            }
        }
        ///////////////////////////// END OF Y
    }
    Random random = new Random();

    @Override
    public void handleInput(Main game) {
        double dt = engine.getDeltaTime();
        moving = false;
        float[] playerMovement = new float[]{0.0f, 0.0f};

        if (engine.getKey(GLFW_KEY_D)) {
            playerMovement[0] += moveSpeed  * 1 / 60.0f; //x
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_A)) {
            playerMovement[0] -= moveSpeed * 1 / 60.0f; //x
            moving = true;
        }

        if (engine.getKey(GLFW_KEY_W)) {
            playerMovement[1] += moveSpeed * 1 / 60.0f; //y
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_S)) {
            playerMovement[1] -= moveSpeed * 1 / 60.0f; //y
            moving = true;
        }

        // -> direction of our player

        move(playerMovement, game.world);

        camera[0] += (x - camera[0]) / 27;
        camera[1] += (y - camera[1]) / 27;
    }

    public void screenShake() {
        camera[0] += (float)random.ints(-10, 10).findFirst().getAsInt();
        camera[1] += (float)random.ints(-10, 10).findFirst().getAsInt();
    }

    @Override
    public void handleAnimationState() {
        if (health > 0) {
            if (moving) {
                currentAnimation = walkAnimations;
            }
            else {
                currentAnimation = idleAnimations;
            }
        }
    }

    public void handleMouseClicks(Main game) {
        double mousePos[] = engine.getMousePos();
        double angle = Math.toDegrees(Math.atan2(mousePos[0] - (x - camera[0]), mousePos[1] + (y - camera[1])));
        double _angle = Math.toDegrees(Math.atan2((x - camera[0]) - mousePos[0], (y - camera[1]) + mousePos[1]));


        attack[0] -= Math.sin(Math.toRadians(_angle)) * weaponTimer * 0.7;
        attack[1] -= Math.cos(Math.toRadians(_angle)) * weaponTimer * 0.7;
        if (weaponTimer > 0) {
            weaponAttack.render(attack[0] - camera[0], attack[1] - camera[1], 200, 200, false, 
            (float)angle - 140, weaponTimer / 11.0f);
            float[] playerMovement = new float[]{0.0f, 0.0f};

            playerMovement[0] -= Math.sin(Math.toRadians(_angle)) * (weaponTimer / 1.5f);
            playerMovement[1] -= Math.cos(Math.toRadians(_angle)) * (weaponTimer / 1.5f);

            move(playerMovement, game.world);

            if (weaponTimer % 2 == 0) playerWhiteImages.add(new float[]{x, y, 1, 10.0f});

            weaponTimer--;
        }
        else {
            weaponOffsetX = 0;
            weaponOffsetY = 0;
        }
        if (glfwGetMouseButton(game.engine.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            if (weaponTimer <= 0 && canAttack) {
                weaponOffsetX = 0;
                weaponOffsetY = 0;
                mousePos = engine.getMousePos();
                angle = Math.toDegrees(Math.atan2((x - camera[0]) - mousePos[0], (y - camera[1]) + mousePos[1]));
                weaponOffsetX -= Math.sin(Math.toRadians(angle)) * 60;
                weaponOffsetY -= Math.cos(Math.toRadians(angle)) * 60;
                canAttack = false;
                attack = new float[]{x + weaponOffsetX + 50, y + 20 + weaponOffsetY};
                weaponTimer = 20;
                capMouse = engine.getMousePos();
            }
        }
        if (glfwGetMouseButton(game.engine.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) canAttack = true;
    }

    public void _draw(Main game) throws ParseException, IOException, FileNotFoundException, ParseException {        
        if (engine.getMousePos()[0] > 0) {
            flipped = false;
        }
        
        else {
            flipped = true;
        }

        List<float[]> badPlayerWhiteImages = new ArrayList<float[]>();
        for (float[] pos:playerWhiteImages) {
            pos[2] -= 0.1f;
            pos[3] -= 1.0f;
            if (pos[3] <= 0) {
                badPlayerWhiteImages.add(pos);
            } 
            whitePlayerImage.render(pos[0] - camera[0], pos[1] - camera[1], 128, 128, false, 0, pos[2]);
        }
        badPlayerWhiteImages.removeAll(badPlayerWhiteImages);

        if (health > 0) {
            animationIndex = super.animate(currentAnimation, animationIndex, 8);
        }

        shadow.render(x - camera[0], y - camera[1] - 55, 128, 128, flipped, 0, 1.0f);
        currentAnimation[animationIndex / 8].render(x - camera[0], y - camera[1], 128 * game.zoom, 128 * game.zoom, !flipped, 0, 1.0f);
        currentAnimation[animationIndex / 8].shader.uploadFloat("c", 1.0f);


        double mousePos[] = engine.getMousePos();
        double angle = Math.toDegrees(Math.atan2(mousePos[0] - (x - camera[0]), mousePos[1] + (y - camera[1])));
        if (!flipped) {
            weaponImg.render(x - camera[0] + weaponOffsetX + 50, y + 20 - camera[1] + weaponOffsetY, 128, 128, false, (float)angle - 140, 1.0f);
        }
        else {
            weaponImg.render(x - camera[0] + weaponOffsetX + 80, y + 20 - camera[1] + weaponOffsetY, 128, 128, false, (float)angle - 140, 1.0f);
        }

        if (health <= 0) {
            if (game.zoom < 3.0f) {
                game.zoom += 0.01f;
                screenShake();
            }
            if (game.zoom >= 3.0f) {
                if (animationIndex < 159) {
                    animationIndex = super.animate(currentAnimation, animationIndex, 8);
                }
                //game.youDied.render(-700, -400, 200* game.gameOverSize, 128 * game.gameOverSize);
                game.textYou.size = 32 * game.gameOverSize;
                game.textDied.size = 32 * game.gameOverSize;

                game.textYou.render_text();
                game.textDied.render_text();

                if (game.gameOverSize < 7.0f) {
                    game.gameOverSize += 0.1;
                }
                else {
                    game.againButton.update(-200, -560, 128 * 4, 64 * 4, game);
                }
            }
            currentAnimation = deathAnimations;
        }
    }

    public void _update(Main game) throws ParseException, IOException, FileNotFoundException, ParseException {
        handleMouseClicks(game);
        handleInput(game);
        handleAnimationState();
        _draw(game);
    }

}
