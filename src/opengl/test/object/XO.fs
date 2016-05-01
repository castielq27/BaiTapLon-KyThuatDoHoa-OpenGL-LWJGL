#version 150 core

in vec3 vertexColor;

out vec4 fragColor;

//uniform sampler3D texImage;

void main() {
 
    fragColor = vec4(vertexColor, 1.0);
}