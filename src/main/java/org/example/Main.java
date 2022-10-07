package org.example;
import static org.lwjgl.glfw.GLFW.*;


public class Main {
    Engine engine = new Engine();
    public Texture tex = new Texture();

    private void run() {
        engine.init();
        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);
            System.out.println(engine.getKey(GLFW_KEY_A));
            tex.render(0, 0, 0, 0);
            engine.update();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
