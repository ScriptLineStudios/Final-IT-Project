package org.example;

import java.io.IOException;

public class Food extends Entity{
    
    Texture food;
    boolean used;
    
    Food(Engine en) throws IOException {
        super(0, 700, 10);
        food = en.loadTex("src/main/resources/assets/images/BOSS.png");
        used = false;
    }

    @Override
    public void draw(Main game) {
        if (!used) {
            float[] _this = new float[]{(float)x, (float)y, 128,  128};
            float[] playerRect = new float[]{(float)game.player.x, (float)-200, 128, 128};
            if (game.engine.collideRects(_this, playerRect)) {
                if (game.player.hunger < 100)
                    game.player.hunger += 1;
                used = true;
            }
    
            if (y > -200)
                y -= moveSpeed;
            food.render(x, y, 128, 128);
        }
    }

    @Override
    public void update(Main game) {
        draw(game);
    }
}
