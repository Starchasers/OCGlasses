#version 120

attribute vec4 in_color;
attribute vec2 in_uv;
uniform vec2 time; // x-model y-part
uniform mat4[2] matrices; // [0]-model [1]-part
uniform vec4[16] animation; //4*(scale, translate, rotation)

varying vec4 color;
varying vec2 uv;

mat4 lerp(mat4 v0, mat4 v1, float t) {
	mat4 a = (1 - t) * v0;
	mat4 b = t * v1;
  	return a+b;
}

vec4 lerp(vec4 v0, vec4 v1, float t) {
	vec4 a = (1 - t) * v0;
	vec4 b = t * v1;
  	return a+b;
}

mat4 lerp2(mat4 v0, mat4 v1, float t) {
  return v0 + t * (v1 - v0);
}

//Convert quaternion to rotation matrix
mat4 toMat(vec4 q) {
	mat4 matrix = mat4(1f);
	matrix[0][0] = 1.0f - 2.0f * ( q.y * q.y + q.z * q.z );
	matrix[0][1] = 2.0f * (q.x * q.y + q.z * q.w);
	matrix[0][2] = 2.0f * (q.x * q.z - q.y * q.w);
	// Second row
	matrix[1][0] = 2.0f * ( q.x * q.y - q.z * q.w );
	matrix[1][1] = 1.0f - 2.0f * ( q.x * q.x + q.z * q.z );
	matrix[1][2] = 2.0f * (q.z * q.y + q.x * q.w );
	// Third row
	matrix[2][0] = 2.0f * ( q.x * q.z + q.y * q.w );
	matrix[2][1] = 2.0f * ( q.y * q.z - q.x * q.w );
	matrix[2][2] = 1.0f - 2.0f * ( q.x * q.x + q.y * q.y );
	return matrix;
}

vec4 slerp(vec4 start, vec4 end, float percent) {
	 start = normalize(start);
	 end = normalize(end);
     float dot = dot(start, end);

	 bool test_threshold = abs(dot) > 0.9995;
     vec4 lerp_result = normalize(start + percent*(end-start));

	 //bool test_dot = dot < 0.0f;
    // float test_dot_mult = (float(test_dot)*-2 + 1);
    // end = end * test_dot_mult;
     //dot = dot * test_dot_mult;

     dot = clamp(dot, -1.0, 1.0);
     float theta = acos(dot)*percent;
     end.x += float(test_threshold);//avoid NaN in normalize
     vec4 relative_vec = normalize(end - (start*dot));
     vec4 slerp_result = (start*cos(theta)) + (relative_vec*sin(theta));

     return  (slerp_result*float(!test_threshold)) + (lerp_result*int(test_threshold));
}

mat4 toScaleMatrix(vec4 vec) {
	mat4 result = mat4(0);
	result[0][0] = vec.x;
	result[1][1] = vec.y;
	result[2][2] = vec.z;
	result[3][3] = vec.w;
	return result;
}

mat4 toTranslateMatrix(vec4 vec){
	return mat4(
	vec4(1,0,0,0),
	vec4(0,1,0,0),
	vec4(0,0,1,0),
	vec);
}

void main(){
   mat4 deltaModel =
   lerp(toTranslateMatrix(animation[0]),toTranslateMatrix(animation[1]),time.x)// translate
   *toMat(slerp(animation[2],animation[3], time.x))//rotation
   *lerp(toScaleMatrix(animation[4]),toScaleMatrix(animation[5]),time.x);// scale

   mat4 deltaPart = mat4(1f);
   lerp(toTranslateMatrix(animation[8]),toTranslateMatrix(animation[9]),time.y)// translate
   *toMat(slerp(animation[10],animation[11], time.y))//rotation
   *lerp(toScaleMatrix(animation[12]),toScaleMatrix(animation[13]),time.y);// scale

   gl_Position =  gl_ModelViewProjectionMatrix * (matrices[0] * deltaModel) * (matrices[1] + deltaPart) * gl_Vertex;
   color = in_color * lerp(animation[6],animation[7], time.x) * lerp(animation[14],animation[15], time.x);
   uv = in_uv;
}



