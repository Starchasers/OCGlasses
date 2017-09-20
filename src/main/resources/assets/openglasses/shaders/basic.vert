#version 120

attribute vec4 in_color;
attribute vec2 in_uv;

varying vec4 color;
varying vec2 uv;

void main(){
   gl_Position=ftransform();
   color = in_color;
   uv = in_uv;
}