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
	boolean visable = true;
	boolean depthTest = true;
	
	
	public Queue<AnimationFrame> getAnimationFrames() {
		return animationFrames;
	}
	
	public void setAnimationFrames(Queue<AnimationFrame> animationFrames) {
		this.animationFrames = animationFrames;
	}
	
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
	
	public boolean isVisable() {
		return visable;
	}
	
	public void setVisable(boolean visable) {
		this.visable = visable;
	}
	
	public boolean isDepthTest() {
		return depthTest;
	}
	
	public void setDepthTest(boolean depthTest) {
		this.depthTest = depthTest;
	}
	
	public int getBufferID() {
		return bufferID;
	}
	
	public void setBufferID(int bufferID) {
		this.bufferID = bufferID;
	}
	
	public Matrix4f getMatrix() {
		return matrix;
	}
	
	public void setMatrix(Matrix4f matrix) {
		this.matrix = matrix;
	}
	
	public HashMap<String, ModelPart> getParts() {
		return parts;
	}
	
	public void setParts(HashMap<String, ModelPart> parts) {
		this.parts = parts;
	}
}
