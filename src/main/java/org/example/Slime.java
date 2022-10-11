package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Slime extends Entity {
    Engine engine;
    int changeMove;
    float[] moveDir;

    Random random = new Random();

    Texture slime;
    Slime(float x, float y, Engine _engine) throws IOException {
        super(x, y, 700);
        engine = _engine;

        slime = new Texture("src/main/resources/assets/images/tree.png", 
        "src/main/resources/defaultVertex.glsl",
        "src/main/resources/slimeFragment.glsl", engine);

        changeMove = 0;
        moveDir = new float[]{random.ints(-10, 10).findFirst().getAsInt(), random.ints(-10, 10).findFirst().getAsInt()};
    }
    
    private List<Object[]> getCollidingTiles(List<Object[]> world, Main game) {
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

    private void move(float[] playerMovement, List<Object[]> world, Main game) {
        x += playerMovement[0];
        List<Object[]> collidingTiles = getCollidingTiles(world, game);
        for (Object[] pos:collidingTiles) {
            if (playerMovement[0] > 0) {
                x = (float)pos[0] - 129;
            }
            else if (playerMovement[0] < 0) {
                x = (float)pos[0] + 129;
            }
        }

        y += playerMovement[1];
        collidingTiles = getCollidingTiles(world, game);
        for (Object[] pos:collidingTiles) {
            if (playerMovement[1] > 0) {
                y = (float)pos[1] - 128;
            }
            else if (playerMovement[1] < 0) {
                y = (float)pos[1] + 129;
            }
        }
    }

    private void moveObject(Main game) {
        float[] movement = new float[]{0.0f, 0.0f};

        if (changeMove <= 0) {
            moveDir = new float[]{random.ints(-5, 5).findFirst().getAsInt(), random.ints(-5, 5).findFirst().getAsInt()};
            changeMove = 50;
        }
        else {
            changeMove -= 1;
        }
        float[] playerRect = new float[]{(float)x, (float)y, 128, 128};

        if (game.player.weaponTimer > 0 && engine.collideRects(playerRect, new float[]{game.player.x, game.player.y, 128, 128})) {
            changeMove = -1;
            double mousePos[] = engine.getMousePos();
            double _angle = Math.toDegrees(Math.atan2((game.player.x - game.player.camera[0]) - mousePos[0], (game.player.y - game.player.camera[1]) + mousePos[1]));
            
            movement[0] -= Math.sin(Math.toRadians(_angle)) * (game.player.weaponTimer / 1.1f) * 2;
            movement[1] -= Math.cos(Math.toRadians(_angle)) * (game.player.weaponTimer / 1.1f) * 2;
            slime.shader.uploadFloat("c", 0.0f);
        }
        if (game.player.weaponTimer <= 0) {
            slime.shader.uploadFloat("c", 1.0f);
        }
        movement[0] += moveDir[0];
        movement[1] += moveDir[1];

        //System.out.printf("X: %f Y: %f \n", moveDir[0], moveDir[1]);

        move(movement, game.world, game);
    }
    
    @Override
    public void draw(Main game) {
        slime.render(x - game.player.camera[0], y - game.player.camera[1], 128, 128, false, (float)Math.sin(game.globalTime*2) * 15, 1.0f);
    }

    @Override
    public void update(Main game) {
        moveObject(game);
        draw(game);
    }
}
