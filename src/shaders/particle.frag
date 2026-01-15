#version 330 core
out vec4 FragColor;
in vec3 ourNormal;
in vec2 texCoord;
in float instanceOpacity;

uniform sampler2D tex;
uniform vec4 color;
uniform vec4 noTex;

void main(){
    vec4 texel = texture(tex, texCoord);
    vec4 finalColor = (texel+noTex) * color;
    finalColor.a *= instanceOpacity;
    FragColor = finalColor;
}
