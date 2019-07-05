#version 120

uniform float red;
uniform float green;
uniform float blue;
uniform float alpha;

// Gold Noise ©2015 dcerisano@standard3d.com
//  - based on the Golden Ratio, PI and Square Root of Two
//  - superior distribution
//  - fastest noise generator function
//  - works with all chipsets (including low precision)

float PHI = 1.61803398874989484820459 * 00000.1; // Golden Ratio
float PI  = 3.14159265358979323846264 * 00000.1; // PI
float SQ2 = 1.41421356237309504880169 * 10000.0; // Square Root of Two

float rand(in vec2 coordinate, in float seed){
    return 0.001 * fract(tan(distance(coordinate*(seed+PHI), vec2(PHI, PI)))*SQ2);
}
// end of Gold Noise


float rand2D(in vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}



void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    float colorOffset = rand2D(vec2(int(texcoord.x / 512), int(texcoord.y / 512)));
    //float colorOffset = 0.1;

    gl_FragColor = vec4(red * 0.9 + 0.1/red * colorOffset, green * 0.9 + 0.1/green * colorOffset, blue * 0.9 + 0.1/blue * colorOffset, alpha);
}