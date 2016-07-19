#version 150

uniform sampler2D sampler;
uniform int green;
uniform int red;
uniform int blue;
uniform int alpha;

in vec2 tex_coords;

out vec4 fragColor;

void main(){
    // set màu cho từng pixel

    vec4 tex = texture(sampler, tex_coords);
    fragColor = tex + vec4(red, green, blue, alpha);
    
    //  trắng (1,1,1)
    //  đen(0,0,0)
}