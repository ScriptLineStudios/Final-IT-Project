package org.example;

import java.io.IOException;

public class Food extends Entity{
    
    Texture food;
    
    Food(Engine en) throws IOException {
        super(0, 700, 10);
        food = en.loadTex("src/main/resources/assets/images/BOSS.png");
    }

    @Override
    public void draw(Main game) {
        y -= moveSpeed;
        food.render(x, y, 128, 128);
    }

    @Override
    public void update(Main game) {
        draw(game);
    }
}
