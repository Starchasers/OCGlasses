#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;

    vec4 sampleRef = texture2D(DiffuseSampler, texCoord);

    for(float r = -Radius; r <= Radius; r += 1.0) {
           vec4 sample = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);

   		   totalAlpha = totalAlpha + sample.a;
           totalSamples = totalSamples + 1.0;

           blurred.rgb = blurred.rgb + sample.rgb*sample.a;
           blurred.a = blurred.a + sample.a;
    }

    gl_FragColor = vec4(blurred.rgb / totalAlpha, totalAlpha / totalSamples);
}
