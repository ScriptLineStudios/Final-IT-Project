package org.example;
import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

public class Main {
    Engine engine = new Engine();
    
    private void run() throws IOException {
        engine.init();
        Player player = new Player(engine);
        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);

            player.draw();
            engine.update();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }
}
