package com.bymarcin.openglasses.surface.vbo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.bymarcin.openglasses.surface.ShaderProgram;

public class BasicShader {
	private ShaderProgram shaderProgram = null;
	private int inUVAttrib;
	private int inColorAttrib;
	private int inMatrix;
	private int inTime;
	
	public BasicShader() {
		shaderProgram = new ShaderProgram("assets/openglasses/shaders/basic.vert","assets/openglasses/shaders/basic.frag");
		inUVAttrib = shaderProgram.getAttirbLocation("in_uv");
		inColorAttrib = shaderProgram.getAttirbLocation("in_color") ;
		inTime = shaderProgram.getUniformLocation("time");
		inMatrix = shaderProgram.getUniformLocation("matrices");
	}
	
	public void bind(){
		GL11.glEnable(GL11.GL_VERTEX_ARRAY);
		shaderProgram.useProgram();
		GL20.glEnableVertexAttribArray(inColorAttrib);
		GL20.glEnableVertexAttribArray(inUVAttrib);
	}
	
	public void unBind(){
		GL20.glUseProgram(0);
		GL20.glDisableVertexAttribArray(inColorAttrib);
		GL20.glDisableVertexAttribArray(inUVAttrib);
		GL11.glDisable(GL11.GL_VERTEX_ARRAY);
	}
	
	public int getInColorAttrib() {
		return inColorAttrib;
	}
	
	public int getInUVAttrib() {
		return inUVAttrib;
	}
	
	public int getInTime() {
		return inTime;
	}
	
	public int getInMatrix() {
		return inMatrix;
	}
}

