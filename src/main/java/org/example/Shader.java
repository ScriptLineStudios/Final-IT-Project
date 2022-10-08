package org.example;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    String vertexSource, fragmentSource;

    int shaderProgram;
    Shader(String vertexSrc, String fragmentSrc) {
        vertexSource = vertexSrc;
        fragmentSource = fragmentSrc;

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
    }

    public void uploadTexture(String name, int index) {
        int texVar = glGetUniformLocation(shaderProgram, name);
        glUseProgram(shaderProgram);
        glUniform1i(texVar, 0);
    }

    public void uploadFloat(String name, float value) {
        int texVar = glGetUniformLocation(shaderProgram, name);
        glUseProgram(shaderProgram);
        glUniform1f(texVar, value);
    }
}
