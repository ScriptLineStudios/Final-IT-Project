package org.example;

import static org.lwjgl.glfw.GLFW.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;

public class FeedButton {
    Texture texture;
    Text text;
    FeedButton (Texture _texture, String _text, Engine eng) throws IOException {
        texture = _texture;
        text = new Text(_text, 0, 0, 0, eng);
    }

    public void onClick(float x, float y, float width, float height, Main game) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        double[] mousePos = game.engine.getMousePos();

        float[] mouseRect = new float[]{(float)mousePos[0], (float)-mousePos[1], 16,  16};
        float[] buttonRect = new float[]{(float)x, (float)y, (float)width, (float)height};
        if (glfwGetMouseButton(game.engine.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            if (game.engine.collideRects(mouseRect, buttonRect)) {
                game.petFood.add(new Food(game.engine));
            }
        }
    }   

    public void onClick(float x, float y, float width, float height, Main game, Method method) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        double[] mousePos = game.engine.getMousePos();

        float[] mouseRect = new float[]{(float)mousePos[0], (float)-mousePos[1], 16,  16};
        float[] buttonRect = new float[]{(float)x, (float)y, (float)width, (float)height};
        if (glfwGetMouseButton(game.engine.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            if (game.engine.collideRects(mouseRect, buttonRect)) {
                game.player.kills = 0;
                game.currentMap = game.random.ints(0, 4).findFirst().getAsInt();
                try {
                    method.invoke(this);
                }
                catch (Exception e) {
                    
                }
            }
        }
    }   

    public void update(float x, float y, float width, float height, Main game, Method method) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        onClick(x, y, width, height, game, method);
        texture.render(x - 35, y, width + 35, height);
        text.x = x - 20;
        text.y = y + 35;
        text.size = 145.0f;
        text.render_text();
    }

    public void update(float x, float y, float width, float height, Main game) throws ParseException, IOException, FileNotFoundException, java.text.ParseException {
        onClick(x, y, width, height, game);
        texture.render(x - 35, y, width + 35, height);
        text.x = x - 20;
        text.y = y + 35;
        text.size = 145.0f;
        text.render_text();
    }
}
