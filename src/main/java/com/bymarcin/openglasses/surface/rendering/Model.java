package com.bymarcin.openglasses.surface.rendering;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class Model {
	private int bufferID;
	Matrix4f matrix = Matrix4f.setIdentity(new Matrix4f());
	HashMap<String, ModelPart> parts = new HashMap<>();
	Queue<AnimationFrame> animationFrames = new LinkedList();
	
	int calculateBufferSize() {
		int size = 0;
		for (ModelPart part : parts.values()) {
			size += part.bufferElements.size();
		}
		return size * BufferElement.SIZE;
	}
	
	public FloatBuffer matrix() {
		FloatBuffer buff = BufferUtils.createFloatBuffer(16);
		matrix.store(buff);
		buff.flip();
		return buff;
	}
	
	
}
