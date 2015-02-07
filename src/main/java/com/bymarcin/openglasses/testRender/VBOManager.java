package com.bymarcin.openglasses.testRender;

import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.common.primitives.Floats;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class VBOManager {
	ArrayList<Float> vcBuffer = new ArrayList<Float>();
	int vcHandle;
	HashMap<Model, Memory> models = new HashMap<Model, Memory>();
	HashMap<Integer, Integer> freeMemory = new HashMap<Integer, Integer>();
	int vertexCount;
	int lastBuffferSize;

	public VBOManager() {
		vcHandle = glGenBuffersARB();
	}

	private Entry<Integer, Integer> findFreeMemory(int size) {
		Entry<Integer, Integer> free = null;
		for (Entry<Integer, Integer> mem : freeMemory.entrySet()) {
			if (mem.getValue() >= size) {
				free = mem;
				break;
			}
		}
		return free;
	}

	private int getMemory(int size) {
		Entry<Integer, Integer> free = findFreeMemory(size);
		if (free != null) {
			if (free.getValue() - size != 0) {
				freeMemory.put(free.getKey() + size, free.getValue() - size);
			}
			freeMemory.remove(free.getKey());
			System.out.println("free found");
			return free.getKey();
		} else {
			System.out.println("realokacja");
			int offset = vcBuffer.size();
			vcBuffer.addAll(Floats.asList(new float[size]));
			return offset;
		}
	}

	private void freeMemory(int index, int size) {
		float[] buffer = new float[size];
		Arrays.fill(buffer, 31000000);
		setBuffer(index, buffer);
		updateVBO(index, size);
		freeMemory.put(index, size);
	}

	private void setBuffer(int offset, float[] data) {
		for (int i = 0; i < data.length; i++) {
			vcBuffer.set(i + offset, data[i]);
		}
	}

	public int addDataToBuffer(float[] buffer) {
		int offset = getMemory(buffer.length);
		setBuffer(offset, buffer);
		if (lastBuffferSize != vcBuffer.size()) {
			recreateVBO();
			lastBuffferSize = vcBuffer.size();
		} else {
			updateVBO(offset, buffer.length);
		}
		return offset;
	}

	public void addModel(Model m) {
		float[] buffer = m.generateBuffer();
		int offset = addDataToBuffer(buffer);
		models.put(m, new Memory(offset, buffer.length));
	}

	public boolean updateModel(Model m) {
		if (models.containsKey(m)) {
			float[] b = m.generateBuffer();
			setBuffer(models.get(m).offset, b);
			updateVBO(models.get(m).offset, b.length);
			return true;
		}
		return false;
	}

	public boolean removeModel(Model m) {
		if (models.containsKey(m)) {
			freeMemory(models.get(m).offset, models.get(m).size);
			models.remove(m);
			return true;
		}
		return false;
	}

	public void recreateVBO() {
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vcBuffer.size());
		buffer.put(Floats.toArray(vcBuffer));
		buffer.flip();
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, buffer, GL15.GL_STREAM_DRAW);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
	}

	public void updateVBO(int offset, int size) {
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size);
		buffer.put(Floats.toArray(vcBuffer.subList(offset, offset + size)));
		buffer.flip();
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset * 4, buffer);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

	}

	public void bind() {
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
	}

	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		bind();
		glVertexPointer(3, GL_FLOAT, 7 * 4, 0);
		glColorPointer(4, GL_FLOAT, 7 * 4, 3 * 4);

		glDrawArrays(GL_TRIANGLES, 0, vcBuffer.size() / 7);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	class Memory {
		private int offset;
		private int size;

		public Memory(int offset, int size) {
			this.offset = offset;
			this.size = size;
		}

		public int getOffset() {
			return offset;
		}

		public int getSize() {
			return size;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Memory && ((Memory) obj).offset == offset && ((Memory) obj).size == size;
		}
	}
}
