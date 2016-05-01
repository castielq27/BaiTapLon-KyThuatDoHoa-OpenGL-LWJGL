#version 150 core

in vec3 position;
in vec3 color;
in vec3 texcoord;

out vec3 vertexColor;
out vec3 textureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColor = color;
    textureCoord = texcoord;
    gl_Position = projection * (view * (model * vec4(position, 1.0)));
}