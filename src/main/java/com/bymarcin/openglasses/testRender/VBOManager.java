package com.bymarcin.openglasses.testRender;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import static org.lwjgl.opengl.GL11.*;

public class VBOManager {
	FloatBuffer vcBuffer = BufferUtils.createFloatBuffer(0);
	int vcHandle;
	HashMap<Integer, Model> models = new HashMap<Integer, Model>();
	HashMap<Integer, Integer> freeMemory = new HashMap<Integer, Integer>();

	public VBOManager() {
		vcHandle = glGenBuffersARB();
	}

	public void addModel(Model model) {
		float[] buffer = model.generateBuffer();
		Entry<Integer, Integer> free = null;
		for(Entry<Integer, Integer> mem: freeMemory.entrySet()){
			if(mem.getValue()>=buffer.length){
				free = mem;
				break;
			}
		}
		bind();
		
		if(free!=null){
			models.put(free.getKey(), model);
			for(int i=free.getKey(); i< free.getKey()+buffer.length; i++){
				vcBuffer.put(i, buffer[i-free.getKey()]);
			}
			if(free.getValue()-buffer.length!=0){
				freeMemory.put(free.getKey()+buffer.length, free.getValue()-buffer.length);
			}
			freeMemory.remove(free.getKey());
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, free.getKey()*4, FloatBuffer.wrap(buffer) );
		}else if (vcBuffer.position() + buffer.length > vcBuffer.capacity()) {
			models.put(vcBuffer.position(), model);
			vcBuffer = BufferUtils.createFloatBuffer(vcBuffer.capacity() + buffer.length).put(vcBuffer).put(buffer);
			glBufferDataARB(GL_ARRAY_BUFFER_ARB, vcBuffer, GL15.GL_STREAM_DRAW);
		}
		
		
	}

	public void bind() {
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
	}

}
