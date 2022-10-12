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
    int bulletCooldown;
    float health;
    Texture slime;
    Texture slime_attack;
    Texture img;

    Texture shadow;
    Slime(float x, float y, Engine _engine) throws IOException {
        super(x, y, 700);
        engine = _engine;

        slime = new Texture("src/main/resources/assets/images/slime.png", 
        "src/main/resources/defaultVertex.glsl",
        "src/main/resources/slimeFragment.glsl", engine);
        slime_attack = new Texture("src/main/resources/assets/images/slime_attack.png", 
        "src/main/resources/defaultVertex.glsl",
        "src/main/resources/slimeFragment.glsl", engine);
        shadow = engine.loadTex("src/main/resources/assets/images/shadow.png");


        changeMove = 0;
        moveDir = new float[]{random.ints(-10, 10).findFirst().getAsInt(), random.ints(-10, 10).findFirst().getAsInt()};
        bulletCooldown = 0;

        health = 40.0f;

        img = slime;
    }
    
    private List<Object[]> getCollidingTiles(List<Object[]> world, Main game) {
        List<Object[]> collidingTiles = new ArrayList<Object[]>();
        for (Object[] pos:world) {
            float[] tileRect = new float[]{(float)pos[0], (float)pos[1], 128, 128};
            float[] playerRect = new float[]{(float)x, (float)y, 128, 128};
            if (engine.collideRects(tileRect, playerRect) && !(((String)pos[2]).equals("grass_2.png")) && !(((String)pos[2]).equals("tree.png"))) {
                collidingTiles.add(pos);
                changeMove = -1;
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

    private void moveObject(Main game) throws IOException {
        float[] movement = new float[]{0.0f, 0.0f};

        if (changeMove <= 0) {
            moveDir = new float[]{random.ints(-5, 5).findFirst().getAsInt(), random.ints(-5, 5).findFirst().getAsInt()};
            changeMove = random.ints(50, 70).findFirst().getAsInt();
        }
        else {
            changeMove -= 1;
        }
        float[] playerRect = new float[]{(float)x, (float)y, 128, 128};

        if (game.player.weaponTimer > 0 && engine.collideRects(playerRect, new float[]{game.player.x, game.player.y, 256, 256})) {
            changeMove = -1;
            double mousePos[] = engine.getMousePos();
            double _angle = Math.toDegrees(Math.atan2((game.player.x - game.player.camera[0]) - mousePos[0], (game.player.y - game.player.camera[1]) + mousePos[1]));
            
            movement[0] -= Math.sin(Math.toRadians(_angle)) * (game.player.weaponTimer / 1.1f) * 2;
            movement[1] -= Math.cos(Math.toRadians(_angle)) * (game.player.weaponTimer / 1.1f) * 2;
            slime.shader.uploadFloat("c", 0.0f);
            slime.shader.uploadFloat("Pixels", random.ints(64, 256).findFirst().getAsInt());
            
            /*for (int i = 0; i < 40; i++) {
                engine.particles.add(new float[]{x + random.ints(10, 64).findFirst().getAsInt(), y + random.ints(10, 64).findFirst().getAsInt(), 10, 10, random.ints(0, 64).findFirst().getAsInt(), 1});
            }*/
            if (random.ints(0, 5).findFirst().getAsInt() == 1) {
                engine.attackParticles.add(new float[]{x + random.ints(10, 64).findFirst().getAsInt(), y + random.ints(10, 64).findFirst().getAsInt(), random.ints(-10, 10).findFirst().getAsInt(), random.ints(-10, 10).findFirst().getAsInt(), 1.0f});
            }
            health -= 0.5f;

        
        }
        if (game.player.weaponTimer <= 0) {
            slime.shader.uploadFloat("c", 1.0f);
            slime.shader.uploadFloat("Pixels", 512.0f);

        }
        slime.shader.uploadFloat("time", game.globalTime);

        movement[0] += moveDir[0] / 3;
        movement[1] += moveDir[1] / 3;

        if (bulletCooldown <= 0) {
            game.enemyBullets.add(new Bullet(x, y, 5.0f, 5.0f, 5, game.engine));
            game.enemyBullets.add(new Bullet(x, y, -5.0f, 5.0f, 5, game.engine));
            game.enemyBullets.add(new Bullet(x, y, -5.0f, 0.0f, 5, game.engine));
            game.enemyBullets.add(new Bullet(x, y, 5.0f, 0.0f, 5, game.engine));
            bulletCooldown = random.ints(120, 200).findFirst().getAsInt();
        }
        else {
            bulletCooldown -= 1;
        }

        if (bulletCooldown <= 18) {
            img = slime_attack;
        }
        else {
            img = slime;
        }


        //System.out.printf("X: %f Y: %f \n", moveDir[0], moveDir[1]);

        move(movement, game.world, game);
    }
    
    @Override
    public void draw(Main game) {

        if (health > 0) {
            shadow.render(x - game.player.camera[0], y - game.player.camera[1] - 50, 128, 128, false, (float)Math.sin(game.globalTime*2) * 5, 1.0f);
            img.render(x - game.player.camera[0], y - game.player.camera[1] + (float)Math.abs(Math.sin(game.globalTime*4) * 100), 128, 128, false, 0, 1.0f);
            if (Math.abs((float)Math.sin(game.globalTime*2) * 5) <= 0.4) {
                img.shader.uploadFloat("bounce", 0.8f);
            }
            else {
                img.shader.uploadFloat("bounce", 1.0f);
            }
        }
        else {
            
        }
        
    }

    public void _update(Main game) throws IOException {
        moveObject(game);
        draw(game);
    }
}
