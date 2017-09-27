#version 120

	//vec4 scale;
	//vec4 translate;
	//vec4 rotation;



attribute vec4 in_color;
attribute vec2 in_uv;
uniform vec2 time; // x-model y-part
uniform mat4[2] matrices; // [0]-model [1]-part
uniform vec4[12] animation; //[0]-anim model start  [1]-anim model stop [2]-anim part start  [3]-anim part stop

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


mat4 toMat(vec4 q)
{
	mat4 matrix = mat4(1f);
	matrix[0][0] = 1.0f - 2.0f * ( q.y * q.y + q.z * q.z );
	matrix[0][1] = 2.0f * (q.x * q.y + q.z * q.w);
	matrix[0][2] = 2.0f * (q.x * q.z - q.y * q.w);
	matrix[0][3] = 0.0f;

	// Second row
	matrix[1][0] = 2.0f * ( q.x * q.y - q.z * q.w );
	matrix[1][1] = 1.0f - 2.0f * ( q.x * q.x + q.z * q.z );
	matrix[1][2] = 2.0f * (q.z * q.y + q.x * q.w );
	matrix[1][3] = 0.0f;

	// Third row
	matrix[2][0] = 2.0f * ( q.x * q.z + q.y * q.w );
	matrix[2][1] = 2.0f * ( q.y * q.z - q.x * q.w );
	matrix[2][2] = 1.0f - 2.0f * ( q.x * q.x + q.y * q.y );
	matrix[2][3] = 0.0f;

	return matrix;
}

vec4 slerp(vec4 start, vec4 end, float percent)
{
     float dot = dot(start, end);
     dot = clamp(dot, -1.0, 1.0);
     float theta = acos(dot)*percent;
     vec4 RelativeVec = normalize(end - start*dot);
     return (start*cos(theta)) + (RelativeVec*sin(theta));
}

mat4 toScaleMatrix(vec4 vec){
	mat4 result = mat4(0);
	result[0][0] = vec.x;
	result[1][1] = vec.y;
	result[2][2] = vec.z;
	result[3][3] = vec.w;
	return result;
}

mat4 toTranslateMatrix(vec4 vec){
	return mat4(
	vec4(0),
	vec4(0),
	vec4(0),
	vec);
}

void main(){
   mat4 deltaModel =
   +lerp2(toScaleMatrix(animation[0]),toScaleMatrix(animation[3]),time.x)// scale
   +lerp2(toTranslateMatrix(animation[1]),toTranslateMatrix(animation[4]),time.x)// translate
   +toMat(slerp(animation[2],animation[5], time.x));//rotation

//TransformedVector = TranslationMatrix * RotationMatrix * ScaleMatrix * OriginalVector;
   mat4 deltaPart = mat4(1f);
   gl_Position=  gl_ModelViewProjectionMatrix * (matrices[0] + deltaModel) * (matrices[1] + deltaPart) * gl_Vertex;
   color = in_color;
   uv = in_uv;
}



