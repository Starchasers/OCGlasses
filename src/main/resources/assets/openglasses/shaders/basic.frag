#version 120

uniform sampler2D textureSampler;
varying vec4 color;
varying vec2 uv;

void main(){
    gl_FragColor = texture2D(textureSampler, uv)*color;
}