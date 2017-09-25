#version 120

attribute vec4 in_color;
attribute vec2 in_uv;
uniform mat4 in_model_matrix;


varying vec4 color;
varying vec2 uv;

void main(){
   gl_Position=  gl_ModelViewProjectionMatrix * in_model_matrix * gl_Vertex;//ftransform();
   color = in_color;
   uv = in_uv;
}