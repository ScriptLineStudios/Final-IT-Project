#version 330 core

in vec2 texCoord;
uniform sampler2D tex0;   

out vec4 color;

uniform float c = 1;

void main()
{
    color = texture(tex0, texCoord) * vec4(1, c, c, 1);
}
