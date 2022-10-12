package org.example;

import java.io.IOException;

public class Bullet extends Entity {
    float xVel, yVel;
    Texture[] bullets;
    Engine engine;
    float lifetime;

    Bullet(float x, float y, float _xVel, float _yVel, float _lifetime, Engine _engine) throws IOException {
        super(x, y, 100);
        xVel = _xVel;
        yVel = _yVel;
        engine = _engine;
        lifetime = _lifetime;

        bullets = new Texture[]{engine.loadTex("src/main/resources/assets/images/bullet1.png"), engine.loadTex("src/main/resources/assets/images/bullet2.png"), 
        engine.loadTex("src/main/resources/assets/images/bullet3.png"), engine.loadTex("src/main/resources/assets/images/bullet4.png"),
        engine.loadTex("src/main/resources/assets/images/bullet5.png")};
    }

    @Override
    public void draw(Main game) {
        animationIndex = super.animate(bullets, animationIndex, 15);
        bullets[animationIndex / 15].render(x - game.player.camera[0], y - game.player.camera[1], 100, 100);
    }

    @Override
    public void update(Main game) {
        lifetime -= game.engine.getDeltaTime();
        x += xVel;
        y += yVel;

        draw(game);
    }
}
