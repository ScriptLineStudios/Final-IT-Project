package org.example;

import java.io.IOException;

public class Main {
    Engine engine = new Engine();
    
    private void run() throws IOException {
        engine.init();
        Player player = new Player(0.0f, 0.0f, engine);
        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);

            player.update();
            engine.update();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }
}
