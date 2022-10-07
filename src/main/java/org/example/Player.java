package org.example;

import java.io.IOException;

public class Player extends Entity{
    Engine engine;

    Texture idleAnimations[];

    Player(Engine _engine) throws IOException {
        super();

        this.engine = _engine;
        idleAnimations = new Texture[]{engine.loadTex("src/main/resources/assets/images/player/player_idle1.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle2.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle3.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle4.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle5.png"),
                                    engine.loadTex("src/main/resources/assets/images/player/player_idle6.png"),
        };

    }
    
    @Override
    public void draw() {
        animationIndex = super.animate(idleAnimations, animationIndex, 8);
        idleAnimations[animationIndex / 8].render(0, 0, 256, 256);
    }

}
