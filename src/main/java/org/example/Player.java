package org.example;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity{
    Engine engine;

    Texture idleAnimations[];
    Texture walkAnimations[];
    Texture currentAnimation[];

    Texture weaponImg;

    float[] camera;

    Player(float playerX, float playerY, Engine _engine) throws IOException {
        super(playerX, playerY, 700);

        this.engine = _engine;
        idleAnimations = new Texture[]{engine.loadTex("src/main/resources/assets/images/player/player_idle1.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle2.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle3.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle4.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle5.png"),
        };

        walkAnimations = new Texture[]{engine.loadTex("src/main/resources/assets/images/player/player_walk1.png"),
                                        engine.loadTex("src/main/resources/assets/images/player/player_walk2.png"),
                                        engine.loadTex("src/main/resources/assets/images/player/player_walk3.png"),
                                        engine.loadTex("src/main/resources/assets/images/player/player_walk4.png"),
        };

        currentAnimation = idleAnimations;
        weaponImg = engine.loadTex("src/main/resources/assets/images/weapons/basic_sword.png");
        camera = new float[]{0.0f, 0.0f};
    }

    private List<Object[]> getCollidingTiles(List<Object[]> world) {
        List<Object[]> collidingTiles = new ArrayList<Object[]>();
        for (Object[] pos:world) {
            float[] tileRect = new float[]{(float)pos[0], (float)pos[1], 64, 64};
            float[] playerRect = new float[]{(float)x, (float)y, 128, 128};
            
            if (engine.collideRects(tileRect, playerRect)) {
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
                x = (float)pos[0] + 65;
            }
        }

        y += playerMovement[1];
        collidingTiles = getCollidingTiles(world);
        for (Object[] pos:collidingTiles) {
            if (playerMovement[1] > 0) {
                y = (float)pos[1] - 128;
            }
            else if (playerMovement[1] < 0) {
                y = (float)pos[1] + 65;
            }
        }
    }

    @Override
    public void handleInput(Main game) {
        double dt = engine.getDeltaTime();
        moving = false;
        float[] playerMovement = new float[]{0.0f, 0.0f};

        if (engine.getKey(GLFW_KEY_D)) {
            playerMovement[0] += moveSpeed * dt;
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_A)) {
            playerMovement[0] -= moveSpeed * dt;
            moving = true;
        }

        if (engine.getKey(GLFW_KEY_W)) {
            playerMovement[1] += moveSpeed * dt;
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_S)) {
            playerMovement[1] -= moveSpeed * dt;
            moving = true;
        }

        move(playerMovement, game.world);

        camera[0] += (x - camera[0]) / 17;
        camera[1] += (y - camera[1]) / 17;
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

    public void handleMouseClicks() {
        if (engine.getMouseClicks(GLFW_MOUSE_BUTTON_LEFT)) {
            double mousePos[] = engine.getMousePos();
            double angle = Math.toDegrees(Math.atan2(mousePos[0] - x, mousePos[1] + y));
        }
    }

    @Override
    public void draw() {
        if (engine.getMousePos()[0] > x - camera[0]) {
            flipped = false;
        }
        else {
            flipped = true;
        } 

        animationIndex = super.animate(currentAnimation, animationIndex, 8);
        currentAnimation[animationIndex / 8].render(x - camera[0], y - camera[1], 128, 128, flipped, 0);

        double mousePos[] = engine.getMousePos();
        double angle = Math.toDegrees(Math.atan2(mousePos[0] - (x - camera[0]), mousePos[1] + (y - camera[1])));
        weaponImg.render(x+100 - camera[0], y+50 - camera[1], 128, 128, false, (float)angle - 110);
    }

    @Override
    public void update(Main game) {
        handleMouseClicks();
        handleInput(game);
        handleAnimationState();
        draw();
    }

}
