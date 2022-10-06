package org.example;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    Engine engine = new Engine();
    public Texture tex = new Texture();
    private void run() {
        engine.init();
        while (engine.windowOpen()) {
            engine.clear(0.1f, 0.1f, 0.1f);
            tex.render(0, 0, 0, 0);
            engine.update();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
