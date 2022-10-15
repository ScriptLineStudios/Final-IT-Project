package org.example;

import static org.lwjgl.glfw.GLFW.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public class Button {
    Texture texture;
    Button (Texture _texture) {
        texture = _texture;
    }

    public void onClick(float x, float y, float width, float height, Main game) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        double[] mousePos = game.engine.getMousePos();

        float[] mouseRect = new float[]{(float)mousePos[0], (float)-mousePos[1], 16,  16};
        float[] buttonRect = new float[]{(float)x, (float)y, (float)width, (float)height};
        if (glfwGetMouseButton(game.engine.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            if (game.engine.collideRects(mouseRect, buttonRect)) {
                game.player.kills = 0;
                game.currentMap = game.random.ints(0, 4).findFirst().getAsInt();
                try {
                    game.world = game.genMap(game.currentMap);
                    game.player.x = 200.0f;
                    game.player.y = -2000.0f;

                    for (Object[] pos:game.world) {
                        if (((String)pos[2]).equals("slime.png")) {
                            game.slimes.add(new Slime((float)pos[0], (float)pos[1], game.engine));
                            game.enemys.add(pos);
                        }
                        
                    }
                    game.world.removeAll(game.enemys);

                }
                catch (Exception e) {
                    
                }
            }
        }
    }   

    public void update(float x, float y, float width, float height, Main game) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        onClick(x, y, width, height, game);
        texture.render(x, y, width, height);
    }
}
