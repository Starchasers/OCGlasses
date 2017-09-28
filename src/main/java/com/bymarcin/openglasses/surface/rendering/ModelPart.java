package com.bymarcin.openglasses.surface.rendering;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.util.vector.Matrix4f;

public class ModelPart {
	Matrix4f matrix = Matrix4f.setIdentity(new Matrix4f());
	List<BufferElement> bufferElements = new LinkedList<>();
	int startBufferPosition;
	Queue<AnimationFrame> animationFrames = new LinkedList();
	
	int getElements() {
		return bufferElements.size();
	}
	
	public void setMatrix(Matrix4f matrix) {
		this.matrix = matrix;
	}
	
	public void setStartBufferPosition(int startBufferPosition) {
		this.startBufferPosition = startBufferPosition;
	}
	
	public Matrix4f getMatrix() {
		return matrix;
	}
	
	public List<BufferElement> getBufferElements() {
		return bufferElements;
	}
	
	public int getStartBufferPosition() {
		return startBufferPosition;
	}
	
	public Queue<AnimationFrame> getAnimationFrames() {
		return animationFrames;
	}
	
	public void addTriangle(BufferElement v1, BufferElement v2, BufferElement v3) {
		bufferElements.add(v1);
		bufferElements.add(v2);
		bufferElements.add(v3);
	}
}