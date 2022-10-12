package org.example;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;

public class Engine {
    public long window;
    int[] keys = new int[512];
    int[] mouseClicks = new int[3];

    public int textureIndex = 1;

    double lastFrame;
    double deltaTime;

    List<float[]> particles = new ArrayList<float[]>();
    List<float[]> attackParticles = new ArrayList<float[]>();


    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(800, 800, "Game", NULL, NULL);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS)
            {
                keys[key] = 1;
            }
            else if (action == GLFW_RELEASE){
                keys[key] = 0;
            }
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            mouseClicks[button] = action;
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        lastFrame = glfwGetTime();
    }

    public void clear(float r, float g, float b) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(r, g, b, 1.0f);
    }

    public void update() {
        double currentFrame = glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

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

    public double getDeltaTime() {
        return deltaTime;
    }

    public static String loadAsString(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        String str = Files.readString(fileName);
        return str;
    }

    public Texture loadTex(String path) throws IOException {
        return new Texture(path, this);
    }

    public double[] getMousePos() {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window, xPos, yPos);
        double mousePos[] = {(xPos.get(0) - 400) * 2, (yPos.get(0) - 400) * 2};
        return mousePos;
    }
    
    public boolean getMouseClicks(int mouseCode) {
        if (mouseClicks[mouseCode] == 1) {
            return true;
        } 
        else {
            return false;
        }
    }

    public static float degToRad(float deg) {
        return (float)(deg * 3.14159265 / 180);
    }

    public boolean collideRects(float[] rect1, float[] rect2) {
        float rect1_x = rect1[0];
        float rect1_y = rect1[1];
        float rect1_w = rect1[2];
        float rect1_h = rect1[3];
        
        float rect2_x = rect2[0];
        float rect2_y = rect2[1];
        float rect2_w = rect2[2];
        float rect2_h = rect2[3];

        if (rect1_x     < rect2_x     + rect2_w &&
        rect1_x     + rect1_w > rect2_x     &&
        rect1_y    < rect2_y     + rect2_h &&
        rect1_h + rect1_y     > rect2_y) {
            return true;
        }
        
        return false;
    }
}
