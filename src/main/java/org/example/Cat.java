package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cat extends Entity {
    Texture[] walkAnimations;
    Engine engine;
    boolean flipped;

    Texture shadow;
    Cat (float _x, float _y, Engine _engine) throws IOException {
        super(_x, _y, 600);
        engine = _engine;

        walkAnimations = new Texture[]{
            engine.loadTex("src/main/resources/assets/images/cat-sheet1.png"),
            engine.loadTex("src/main/resources/assets/images/cat-sheet2.png"),
            engine.loadTex("src/main/resources/assets/images/cat-sheet3.png"),
            engine.loadTex("src/main/resources/assets/images/cat-sheet4.png"),
        };
        shadow = engine.loadTex("src/main/resources/assets/images/shadow.png");

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

    @Override
    public void draw(Main game) {
        animationIndex = super.animate(walkAnimations, animationIndex, 8);

        float[] playerMovement = new float[]{0.0f, 0.0f};
        double _angle = Math.toDegrees(Math.atan2(
        (game.player.x - game.player.camera[0]) - (x - game.player.camera[0]), 
        (game.player.y - game.player.camera[1]) + (y - game.player.camera[1])));

        double distance = Math.sqrt((x-game.player.x)*(x-game.player.x) + (y-game.player.y)*(y-game.player.y));
        playerMovement[0] += Math.sin(Math.toRadians(_angle)) * distance / 70;
        playerMovement[1] -= Math.cos(Math.toRadians(_angle)) * distance / 70;       
        move(playerMovement, game.world);

        if (game.player.x > x) {
            flipped = false;
        }
        else {
            flipped = true;
        }

        shadow.render(x - game.player.camera[0], y - game.player.camera[1] - 55, 128, 128, flipped, 0, 1.0f);

        walkAnimations[animationIndex / 8].render(x - game.player.camera[0], y - game.player.camera[1], 128, 128, !flipped, 0.0f, 1.0f);
    }  

    @Override
    public void update(Main game) {
        draw(game);
    }
    
}
