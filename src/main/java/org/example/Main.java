package org.example;

import java.io.IOException;
import java.util.List;

public class Main {
    Engine engine = new Engine();
    
    private void run() throws IOException {
        engine.init();
        List<float[]> world = World.generateArea();
        Player player = new Player(0.0f, 0.0f, engine);

        Texture block = new Texture("src/main/resources/assets/images/block.png", engine);
        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);

            for (float[] pos:world) {
                block.render(pos[0] - player.camera[0], pos[1] - player.camera[1], 256, 256, false, 0);
            }

            player.update();
            engine.update();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }
}
