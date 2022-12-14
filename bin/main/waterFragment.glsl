#version 330 core

in vec2 texCoord;        // holds the Vertex position <-1,+1> !!!
uniform sampler2D tex0;    // used texture unit
uniform float time;

out vec4 color;

vec2 SineWave( vec2 p )
{
    // convert Vertex position <-1,+1> to texture coordinate <0,1> and some shrinking so the effect dont overlap screen
    p.x=( 0.8*p.x)+0.8;
    p.y=(-0.8*p.y)+0.8;
    // wave distortion
    float x = sin( 25.0*p.y + 30.0*p.x + 6.28*time) * 0.01;
    float y = sin( 25.0*p.y + 30.0*p.x + 6.28*time) * 0.01;
    return vec2(p.x+x, p.y+y);
}

void main()
{
    color = texture2D(tex0,SineWave(texCoord));
}