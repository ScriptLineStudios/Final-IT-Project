package org.example;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.nio.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_load;

import org.joml.*;

public class Texture {
    public Shader shader;

    public int textureIndex;

    String vertexPath = "src/main/resources/defaultVertex.glsl";
    String fragmentPath = "src/main/resources/defaultFragment.glsl";

    int vao;
    int vbo;
    int ebo;

    Texture(String imagePath, String _vertexPath, String _fragmentPath, Engine engine) throws IOException {
        textureIndex = engine.textureIndex;
        engine.textureIndex += 1;

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.shader = new Shader(
            Engine.loadAsString(_vertexPath), Engine.loadAsString(_fragmentPath)
        );

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
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
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        //Fill the buffers with garbage data in order to setup the attrib pointers
        float[] vertices = new float[]{
            0.0f,                 0.0f, 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 0.0f,
            0.0f,                 0.0f,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 1.0f, 
            0.0f, 0.0f,                  0.0f ,          0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 1.0f,
            0.0f, 0.0f, 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 0.0f
       };

       int[] indices = {
        0, 2, 1,
        0, 3, 2,
        };

        glBindVertexArray(vao);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

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

        glBindVertexArray(0);        
    }

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
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        //Fill the buffers with garbage data in order to setup the attrib pointers
        float[] vertices = new float[]{
            0.0f,                 0.0f, 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 0.0f,
            0.0f,                 0.0f,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 1.0f, 
            0.0f, 0.0f,                  0.0f ,          0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 1.0f,
            0.0f, 0.0f, 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 0.0f
       };

       int[] indices = {
        0, 2, 1,
        0, 3, 2,
        };

        glBindVertexArray(vao);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

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

        glBindVertexArray(0);        
    }

    public void render(float _x, float _y, float width, float height) {
        float x = _x / 800;
        float y = _y / 800;

        float vertices[] = {
             x,                 y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 0.0f,
             x,                 y,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 1.0f, 
             x + (width / 800), y,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 1.0f,
             x + (width / 800), y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 0.0f
        };

        int[] indices = {
            0, 2, 1,
            0, 3, 2,
        };

        backEndRender(vertices, indices);
    }

    public void render(float _x, float _y, float width, float height, boolean flipped, float rotation, float alpha) {
        float x = _x / 800;
        float y = _y / 800;

        float vertices[];
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        try {
            Vector3f center = new Vector3f(x, y, 0.0f);

            FloatBuffer matrixBuffer = new Matrix4f().translate(center)
                .rotate(Engine.degToRad(rotation), 0.0f, 0.0f, 1.0f)
                .translate(center.negate())
                .get(buffer);
                
            shader.uploadMatrix("transform", matrixBuffer);
        } catch (Exception e) {
            System.out.println("Error... " + e);
        }
        shader.uploadFloat("alpha", alpha);
        if (flipped) {
            vertices = new float[]{
                 x,                 y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 0.0f,
                 x,                 y,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 1.0f, 
                 x + (width / 800), y,                  0.0f ,          0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 1.0f,
                 x + (width / 800), y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 0.0f
            };
        }
        else {
            vertices = new float[]{
                x,                 y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 0.0f,
                x,                 y,                  0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           0.0f, 1.0f, 
                x + (width / 800), y,                  0.0f ,          0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 1.0f,
                x + (width / 800), y + (height / 800), 0.0f,           0.0f, 0.0f, 0.0f, 0.0f,           1.0f, 0.0f
           };
        }

        int[] indices = {
            0, 2, 1,
            0, 3, 2,
        };

        backEndRender(vertices, indices);
    }



    private void backEndRender(float[] vertices, int[] indices) {
        glBindVertexArray(vao);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        glUseProgram(shader.shaderProgram);
        glBindTexture(GL_TEXTURE_2D, textureIndex);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }
}
