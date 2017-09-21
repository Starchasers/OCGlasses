package com.bymarcin.openglasses.surface.vbo;

import com.google.common.primitives.Floats;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import com.bymarcin.openglasses.surface.VBO;

public class RenderManager {
	int bufferId;
	BasicShader shader;
	List<Float> localBuffer = new ArrayList<>();
	
	private void init(){
		shader = new BasicShader();
		bufferId = GL15.glGenBuffers();
	}
	
	public void bind(boolean bind){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bind?bufferId:0);
	}
	
	
	
	
	
//	public void recreateVBO() {
//		FloatBuffer buffer = BufferUtils.createFloatBuffer(localBuffer.size());
//		buffer.put(Floats.toArray(localBuffer));
//		buffer.flip();
//		bind(true);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
//		bind(false);
//	}
//
//	public void updateVBO(int offset, int size) {
//		bind(true);
//		FloatBuffer buffer = BufferUtils.createFloatBuffer(size);
//		buffer.put(Floats.toArray(localBuffer.subList(offset, offset + size)));
//		buffer.flip();
//		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset * 4, buffer);
//		bind(false);
//	}
	
	
	
	
	
	public void render(){
	
	}
	

	public static class Element{
		VBO.BufferElement[] vertices = new VBO.BufferElement[3];
		int startPos;
	}
	
	public class Model{
		List<Element> elements = new LinkedList<>();
	}
	
	public static class Free{
		int startPos;
		int length;
	}
	
	
}
