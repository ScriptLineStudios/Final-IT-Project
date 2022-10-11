package org.example;

import java.io.IOException;

public class Bullet extends Entity {
    float xVel, yVel;
    Texture bullet;
    Engine engine;
    Bullet(float x, float y, float _xVel, float _yVel, Engine _engine) throws IOException {
        super(x, y, 100);
        xVel = _xVel;
        yVel = _yVel;
        engine = _engine;

        bullet = engine.loadTex("src/main/resources/assets/images/bullet.png");
    }

    @Override
    public void draw(Main game) {
        bullet.render(x - game.player.camera[0], y - game.player.camera[1], 128, 128);
    }

    @Override
    public void update(Main game) {
        x += xVel;
        y += yVel;

        draw(game);
    }
}
