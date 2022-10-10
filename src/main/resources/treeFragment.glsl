#version 330 core
out vec4 FragColor;
in vec4 outColor;

in vec2 texCoord;

uniform sampler2D tex0;
uniform float alpha = 1;
uniform float time = 1;

float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() 
{
    FragColor = texture(tex0, texCoord) * alpha;
}