package org.example;
import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

public class Main {
    Engine engine = new Engine();
    
    private void run() throws IOException {
        engine.init();
        Texture tex = new Texture("src/main/resources/assets/images/BOSS.png", engine);
        Texture tex2 = new Texture("src/main/resources/assets/images/pg_retro.png", engine);

        float x = 0.0f;
        float time = 0.0f;
        while (engine.windowOpen()) {
            time += 1f;
            engine.clear(0.1f, 0.1f, 0.1f);
            if (engine.getKey(1)) {
                x += 0.01f;
            }
            System.out.println(engine.getKey(GLFW_KEY_D));
            tex.render(x, 0.0f, time, time);
            tex2.render(-0.4f, 0.0f, time, time);

            engine.update();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }
}
