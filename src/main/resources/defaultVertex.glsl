#version 330 core
layout (location = 0) in vec4 aPos; 
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTex;

out vec4 outColor;

out vec2 texCoord;

void main()
{
    gl_Position = aPos;
    outColor = aColor;
    texCoord = aTex;
}