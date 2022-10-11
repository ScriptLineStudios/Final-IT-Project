#version 330 core

in vec2 texCoord;
uniform sampler2D tex0;   
uniform float time = 0;

out vec4 color;

void main()
{
    color = texture(tex0,texCoord);
}
