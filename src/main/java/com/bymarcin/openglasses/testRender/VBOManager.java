package com.bymarcin.openglasses.testRender;

import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class VBOManager {
	FloatBuffer vcBuffer = BufferUtils.createFloatBuffer(42);
	int vcHandle;
	HashMap<Integer, Model> models = new HashMap<Integer, Model>();
	HashMap<Integer, Integer> freeMemory = new HashMap<Integer, Integer>();
	int vertexCount;

	
	public VBOManager() {
		vcHandle = glGenBuffersARB();
	}

	public void addModel(Model model) {
		
		float[] buffer = model.generateBuffer();
		System.out.println("dodano " + buffer.length);
		vertexCount += buffer.length / (3 + 4);
		Entry<Integer, Integer> free = null;
		for (Entry<Integer, Integer> mem : freeMemory.entrySet()) {
			if (mem.getValue() >= buffer.length) {
				free = mem;
				break;
			}
		}			
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		bind();

		if (free != null) {
			models.put(free.getKey(), model);
			for (int i = free.getKey(); i < free.getKey() + buffer.length; i++) {
				vcBuffer.put(i, buffer[i - free.getKey()]);
			}
			if (free.getValue() - buffer.length != 0) {
				freeMemory.put(free.getKey() + buffer.length, free.getValue() - buffer.length);
			}
			freeMemory.remove(free.getKey());
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, free.getKey() * 4, FloatBuffer.wrap(buffer));
		} else if (vcBuffer.position() + buffer.length > vcBuffer.capacity()) {
			models.put(vcBuffer.position(), model);
			vcBuffer = BufferUtils.createFloatBuffer(vcBuffer.capacity() + buffer.length).put(vcBuffer).put(buffer);
			glBufferDataARB(GL_ARRAY_BUFFER_ARB, vcBuffer, GL15.GL_STREAM_DRAW);
		}else{
			models.put(vcBuffer.position(), model);
			vcBuffer.put(buffer);
		}
		vcBuffer.flip();
		glBindBufferARB( GL_ARRAY_BUFFER_ARB, 0 );
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		
	}

	public void bind() {
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
	}

	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		
		bind();
		glVertexPointer(3, GL_FLOAT, 7 * 4, 0);
		glColorPointer(4, GL_FLOAT, 7 * 4, 3 * 4 );

		glDrawArrays(GL_TRIANGLES, 0, vertexCount);

		glBindBufferARB( GL_ARRAY_BUFFER_ARB, 0 );
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
}
