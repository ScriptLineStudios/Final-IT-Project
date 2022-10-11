#version 330 core

in vec2 texCoord;
uniform sampler2D tex0;   
uniform float time = 1;
uniform float bounce = 1;


out vec4 color;

uniform float c = 1;
uniform float Pixels = 512;

void main()
{
    float dx = 15.0 * (1.0 / Pixels);
    float dy = 10.0 * (1.0 / Pixels);
    vec2 Coord = vec2(dx * floor(texCoord.x / dx),
                        dy * floor(texCoord.y / dy));
    color = texture(tex0,vec2(Coord.x, Coord.y / bounce)) * vec4(1, c, c, 1);
}
