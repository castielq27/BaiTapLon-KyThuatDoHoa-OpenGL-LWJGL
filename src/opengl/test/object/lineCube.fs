#version 150 core

in vec3 vertexColor;
in vec3 textureCoord;

out vec4 fragColor;

//uniform sampler3D texImage;

void main() {
    //vec4 textureColor = texture(texImage, textureCoord);
    fragColor = vec4(vertexColor, 1.0);// * textureColor;
}