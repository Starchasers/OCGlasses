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


mat4 toMat(vec4 q)
{
	mat4 matrix = mat4(0);
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

	// Fourth row
	matrix[3][0] = 0;
	matrix[3][1] = 0;
	matrix[3][2] = 0;
	matrix[3][3] = 1.0f;

	return matrix;
}

vec4 fromMat(mat4 m) {
	vec4 result = vec4(0);
	float s;
	float tr = m[0][0] + m[1][1] + m[2][2];
	if (tr >= 0.0) {
		s = sqrt(tr + 1.0);
		result.w = s * 0.5f;
		s = 0.5f / s;
		result.x = (m[2][1] - m[1][2]) * s;
		result.y = (m[0][2] - m[2][0]) * s;
		result.z = (m[1][0] - m[0][1]) * s;
	} else {
		float max = max(max(m[0][0], m[1][1]), m[2][2]);
		if (max == m[0][0]) {
			s = sqrt(m[0][0] - (m[1][1] + m[2][2]) + 1.0);
			result.x = s * 0.5f;
			s = 0.5f / s;
			result.y = (m[0][1] + m[1][0]) * s;
			result.z = (m[2][0] + m[0][2]) * s;
			result.w = (m[2][1] - m[1][2]) * s;
		} else if (max == m[1][1]) {
			s = sqrt(m[1][1] - (m[2][2] + m[0][0]) + 1.0);
			result.y = s * 0.5f;
			s = 0.5f / s;
			result.z = (m[1][2] + m[2][1]) * s;
			result.x = (m[0][1] + m[1][0]) * s;
			result.w = (m[0][2] - m[2][0]) * s;
		} else {
			s = sqrt(m[2][2] - (m[0][0] + m[1][1]) + 1.0);
			result.z = s * 0.5f;
			s = 0.5f / s;
			result.x = (m[2][0] + m[0][2]) * s;
			result.y = (m[1][2] + m[2][1]) * s;
			result.w = (m[1][0] - m[0][1]) * s;
		}
	}
	return result;
}

vec4 slerp(vec4 start, vec4 end, float percent)
{
     float dot = dot(start, end);
     dot = clamp(dot, -1.0, 1.0);
     float theta = acos(dot)*percent;
     vec4 RelativeVec = normalize(end - start*dot);
     return (start*cos(theta)) + (RelativeVec*sin(theta));
}

void main(){
   mat4 deltaModel = toMat(slerp(fromMat(matrices[2]),fromMat(matrices[3]),time.x));//lerp(matrices[2],matrices[3],time.x);
   mat4 deltaPart = lerp(matrices[4],matrices[5],time.y);
   gl_Position=  gl_ModelViewProjectionMatrix * (matrices[0] + deltaModel) * (matrices[1] + deltaPart) * gl_Vertex;
   color = in_color;
   uv = in_uv;
}



