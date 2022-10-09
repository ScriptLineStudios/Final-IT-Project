#version 330 core
layout (location = 0) in vec3 aPos; 
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTex;

//uniform float angle;
// mat4 transform = mat4(
//                              vec4(1.0, 0.0, 0.0, 0.0), 
//                              vec4(0.0, 1.0, 0.0, 0.0), 
//                              vec4(0.0, 0.0, 1.0, 0.0), 
//                              vec4(0.0, 0.0, 0.0, 1.0)
//                      );

uniform mat4 transform = mat4(
                             vec4(1.0, 0.0, 0.0, 0.0), 
                             vec4(0.0, 1.0, 0.0, 0.0), 
                             vec4(0.0, 0.0, 1.0, 0.0), 
                             vec4(0.0, 0.0, 0.0, 1.0)
                     );

out vec4 outColor;

out vec2 texCoord;

void main()
{
    gl_Position = transform * vec4(aPos, 1.0);
    outColor = aColor;
    texCoord = aTex;
}