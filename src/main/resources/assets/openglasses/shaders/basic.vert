#version 120

attribute vec4 in_color;
attribute vec2 in_uv;
uniform vec2 time; // x-model y-part
uniform mat4[6] matrices; // [0]-model [1]-part [2]-anim model start  [3]-anim model stop [4]-anim part start  [5]-anim part stop


varying vec4 color;
varying vec2 uv;

mat4 lerp(mat4 v0, mat4 v1, float t) {
	mat4 a = (1 - t) * v0;
	mat4 b = t * v1;
  	return a+b;
}

mat4 lerp2(mat4 v0, mat4 v1, float t) {
  return v0 + t * (v1 - v0);
}

void main(){
   mat4 deltaModel = lerp(matrices[2],matrices[3],time.x);
   mat4 deltaPart = lerp(matrices[4],matrices[5],time.y);
   gl_Position=  gl_ModelViewProjectionMatrix * (matrices[0] + deltaModel) * (matrices[1] + deltaPart) * gl_Vertex;
   color = in_color;
   uv = in_uv;
}

