package org.example;

import java.io.IOException;
import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity{
    Engine engine;

    Texture idleAnimations[];
    Texture walkAnimations[];
    Texture currentAnimation[];

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
    }

    @Override
    public void handleInput() {
        double dt = engine.getDeltaTime();
        moving = false;

        if (engine.getKey(GLFW_KEY_D)) {
            x += moveSpeed * dt;
            moving = true;
            flipped = false;
        }
        if (engine.getKey(GLFW_KEY_A)) {
            x -= moveSpeed * dt;
            moving = true;
            flipped = true;
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

    @Override
    public void draw() {
        animationIndex = super.animate(currentAnimation, animationIndex, 8);
        currentAnimation[animationIndex / 8].render(x, y, 256, 256, flipped);
    }

    @Override
    public void update() {
        handleInput();
        handleAnimationState();
        draw();
    }

}