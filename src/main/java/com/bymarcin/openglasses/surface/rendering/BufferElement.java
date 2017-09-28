package com.bymarcin.openglasses.surface.rendering;

import java.nio.FloatBuffer;

public class BufferElement {
	public static final int SIZE = 9;
	public static final int VERTEX_POINTER_OFFSET = 0;
	public static final int COLOR_POINTER_OFFSET = 3;
	public static final int TEXCOORD_POINTER_OFFSET = 7;
	private float[] floats = new float[SIZE];
	
	public BufferElement setX(float x) {
		floats[0] = x;
		return this;
	}
	
	public BufferElement setY(float y) {
		floats[1] = y;
		return this;
	}
	
	public BufferElement setZ(float z) {
		floats[2] = z;
		return this;
	}
	
	public BufferElement setR(float r) {
		floats[3] = r;
		return this;
	}
	
	public BufferElement setG(float g) {
		floats[4] = g;
		return this;
	}
	
	public BufferElement setB(float b) {
		floats[5] = b;
		return this;
	}
	
	public BufferElement setA(float a) {
		floats[6] = a;
		return this;
	}
	
	public BufferElement setU(float u) {
		floats[7] = u;
		return this;
	}
	
	public BufferElement setV(float v) {
		floats[8] = v;
		return this;
	}
	
	public void store(FloatBuffer buffer) {
		buffer.put(floats);
	}
}