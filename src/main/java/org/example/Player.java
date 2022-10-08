package org.example;

import java.io.IOException;
import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity{
    Engine engine;

    Texture idleAnimations[];
    Texture walkAnimations[];
    Texture currentAnimation[];

    Texture weaponImg;

    Player(float playerX, float playerY, Engine _engine) throws IOException {
        super(playerX, playerY, 600);

        this.engine = _engine;
        idleAnimations = new Texture[]{engine.loadTex("src/main/resources/assets/images/player/player_idle1.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle2.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle3.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle4.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle5.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle6.png"),
        };

        walkAnimations = new Texture[]{engine.loadTex("src/main/resources/assets/images/player/player_walk1.png"),
                                        engine.loadTex("src/main/resources/assets/images/player/player_walk2.png"),
                                        engine.loadTex("src/main/resources/assets/images/player/player_walk3.png"),
        };

        currentAnimation = idleAnimations;
        weaponImg = engine.loadTex("src/main/resources/assets/images/weapons/basic_sword.png");
    }

    @Override
    public void handleInput() {
        double dt = engine.getDeltaTime();
        moving = false;

        if (engine.getKey(GLFW_KEY_D)) {
            x += moveSpeed * dt;
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_A)) {
            x -= moveSpeed * dt;
            moving = true;
        }

        if (engine.getKey(GLFW_KEY_W)) {
            y += moveSpeed * dt;
            moving = true;
        }
        if (engine.getKey(GLFW_KEY_S)) {
            y -= moveSpeed * dt;
            moving = true;
        }
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
            double angle = Math.toDegrees(Math.atan2(x - mousePos[0], y - mousePos[1]));

            x += Math.cos(angle) * 10;
            y += Math.sin(angle) * 10;
        }
    }

    @Override
    public void draw() {
        if (engine.getMousePos()[0] > x) {
            flipped = false;
        }
        else {
            flipped = true;
        } 

        animationIndex = super.animate(currentAnimation, animationIndex, 8);
        currentAnimation[animationIndex / 8].render(x, y, 256, 256, flipped, 0);

        currentAnimation[animationIndex / 8].render(10, 10, 256, 256, flipped, 0);

        double mousePos[] = engine.getMousePos();
        double angle = Math.toDegrees(Math.atan2(mousePos[0], mousePos[1]));
        weaponImg.render(x+100, y+50, 128, 128, false, (float)angle + 220);
    }

    @Override
    public void update() {
        handleMouseClicks();
        handleInput();
        handleAnimationState();
        draw();
    }

}
