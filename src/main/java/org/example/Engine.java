package org.example;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Engine {
    public long window;
    int[] keys = new int[256];
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(1000, 800, "Game", NULL, NULL);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            keys[key] = action;
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
    }

    public void clear(float r, float g, float b) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(r, g, b, 1.0f);
    }

    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public boolean windowOpen() {
        return !glfwWindowShouldClose(window);
    }

    public boolean getKey(int keyCode) {
        if (keys[keyCode] == 1) return true;
        return false;
    }
}
