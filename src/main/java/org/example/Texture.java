package org.example;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.io.*;
import java.nio.*;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImageResize.*;

public class Texture {
    public Shader shader;

    public int textureIndex;

    String vertexPath = "src/main/resources/defaultVertex.glsl";
    String fragmentPath = "src/main/resources/defaultFragment.glsl";

    Texture(String imagePath, Engine engine) throws IOException {
        textureIndex = engine.textureIndex;
        engine.textureIndex += 1;

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.shader = new Shader(
            Engine.loadAsString(vertexPath), Engine.loadAsString(fragmentPath)
        );

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(imagePath, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (Texture) Unknown number of channesl '" + channels.get(0) + "'";
            }
        } else {
            assert false : "Error: (Texture) Could not load image '" + imagePath + "'";
        }

//        stbi_image_free(image);

        shader.uploadTexture("tex0", textureIndex);
    }

    public void render(float x, float y, float width, float height) {
        float vertices[] = {
             x,                 y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 0.0f,
             x,                 y,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 1.0f, 
             x + (width / 800), y,                  0.0f ,          0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 1.0f,
             x + (width / 800), y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 0.0f
        };

        int[] indices = {
            0, 2, 1,
            0, 3, 2,
        };

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorSize = 4;
        int texCoordSize = 2;

        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize + texCoordSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, texCoordSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);

        glUseProgram(shader.shaderProgram);
        glBindTexture(GL_TEXTURE_2D, textureIndex);
        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }
}
