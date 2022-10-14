package org.example;

import java.io.IOException;
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

    boolean canAttack;
    Player(float playerX, float playerY, Engine _engine) throws IOException {
        super(playerX, playerY, 500);
        System.out.println(y);

        canAttack = true;

        health = 100;

        this.engine = _engine;
        shadow = engine.loadTex("src/main/resources/assets/images/shadow.png");
        idleAnimations = new Texture[]{loadTex("src/main/resources/assets/images/player/player_idle1.png"),
                                    loadTex("src/main/resources/assets/images/player/player_idle2.png"),
                                    loadTex("src/main/resources/assets/images/player/player_idle3.png"),
                                    loadTex("src/main/resources/assets/images/player/player_idle4.png"),
                                    loadTex("src/main/resources/assets/images/player/player_idle5.png"),
        };

        walkAnimations = new Texture[]{loadTex("src/main/resources/assets/images/player/player_walk1.png"),
                                        loadTex("src/main/resources/assets/images/player/player_walk2.png"),
                                        loadTex("src/main/resources/assets/images/player/player_walk3.png"),
                                        loadTex("src/main/resources/assets/images/player/player_walk4.png"),
        };

        deathAnimations = new Texture[]{
            engine.loadTex("src/main/resources/assets/images/player/player_death1.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death2.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death3.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death4.png"),            
            engine.loadTex("src/main/resources/assets/images/player/player_death5.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death6.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death7.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death8.png"),
            engine.loadTex("src/main/resources/assets/images/player/player_death9.png"),
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
        x += playerMovement[0];
        List<Object[]> collidingTiles = getCollidingTiles(world);
        for (Object[] pos:collidingTiles) {
            if (playerMovement[0] > 0) {
                x = (float)pos[0] - 129;
            }
            else if (playerMovement[0] < 0) {
                x = (float)pos[0] + 129;
            }
        }

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
    }
    Random random = new Random();

    @Override
    public void handleInput(Main game) {
        double dt = engine.getDeltaTime();
        moving = false;
        float[] playerMovement = new float[]{0.0f, 0.0f};

        if (engine.getKey(GLFW_KEY_D)) {
            playerMovement[0] += moveSpeed  * 1 / 60.0f;
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_A)) {
            playerMovement[0] -= moveSpeed * 1 / 60.0f;
            moving = true;
        }

        if (engine.getKey(GLFW_KEY_W)) {
            playerMovement[1] += moveSpeed * 1 / 60.0f;
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_S)) {
            playerMovement[1] -= moveSpeed * 1 / 60.0f;
            moving = true;
        }


        move(playerMovement, game.world);

        camera[0] += (x - camera[0]) / 17;
        camera[1] += (y - camera[1]) / 17;
    }

    public void screenShake() {
        camera[0] += (float)random.ints(-15, 15).findFirst().getAsInt();
        camera[1] += (float)random.ints(-15, 15).findFirst().getAsInt();
    }

    @Override
    public void handleAnimationState() {
        if (moving) {
            currentAnimation = walkAnimations;
        }
        else {
            currentAnimation = idleAnimations;
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

            if (weaponTimer % 2 == 0) playerWhiteImages.add(new float[]{x, y, 1});

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

    @Override
    public void draw(Main game) {        
        if (engine.getMousePos()[0] > 0) {
            flipped = false;
        }
        else {
            flipped = true;
        } 
        for (float[] pos:playerWhiteImages) {
            if (pos[2] < 0) {
            }
        }
        for (float[] pos:playerWhiteImages) {
            pos[2] -= 0.1f;
            whitePlayerImage.render(pos[0] - camera[0], pos[1] - camera[1], 128, 128, false, 0, pos[2]);
        }

        animationIndex = super.animate(currentAnimation, animationIndex, 8);
        shadow.render(x - camera[0], y - camera[1] - 55, 128, 128, flipped, 0, 1.0f);
        currentAnimation[animationIndex / 8].render(x - camera[0], y - camera[1], 128, 128, flipped, 0, 1.0f);
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
            currentAnimation = deathAnimations;
        }
    }

    @Override
    public void update(Main game) {
        handleMouseClicks(game);
        handleInput(game);
        handleAnimationState();
        draw(game);
    }

}
