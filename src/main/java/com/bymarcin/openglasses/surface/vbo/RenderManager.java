package com.bymarcin.openglasses.surface.vbo;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;

import com.bymarcin.openglasses.surface.VBO;

public class RenderManager {
	int bufferId;
	BasicShader shader;
	List<Float> localBuffer = new ArrayList<>();
	
	private void init() {
		shader = new BasicShader();
		bufferId = GL15.glGenBuffers();
	}
	
	public void bind(boolean bind) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bind ? bufferId : 0);
	}
	
	
	/*  rendering scheme
	public void render() {
		useProgram();
		for(Model model : Models){
			bindBuffer(model.bufferID);
			setUniform("ModelMatrix",model.matrix);
			for(ModelPart part : model.parts){
				setUniform("PartMatrix", part.matrix);
				drawArrays(part.startBufferPosition,part.getElements());
			}
		}
	}
	*/
	
	public class ModelPart {
		Matrix4f matrix;
		List<VBO.BufferElement> bufferElements;
		int startBufferPosition;
		
		int getElements() {
			return bufferElements.size() / 3;
		}
		
		public void addTriangle(VBO.BufferElement v1, VBO.BufferElement v2, VBO.BufferElement v3) {
			bufferElements.add(v1);
			bufferElements.add(v2);
			bufferElements.add(v3);
		}
	}
	
	public class Model {
		int bufferID;
		Matrix4f matrix;
		long modelID;
		List<ModelPart> parts;
	}
	
	
	public class ModelBuilder {
	}
	
	
	
}
