package com.bymarcin.openglasses.surface.vbo;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.bymarcin.openglasses.surface.rendering.AnimationFrame;
import com.bymarcin.openglasses.surface.rendering.BufferElement;
import com.bymarcin.openglasses.surface.rendering.Model;
import com.bymarcin.openglasses.surface.rendering.ModelPart;


public class RenderManager {
	BasicShader shader;
	List<Model> models = new LinkedList<>();
	
	public void init() {
		shader = new BasicShader();
		//addModel(createModel());
	}
	
	
	public void bind(int bufferId) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
	}
	
	
	public void render(RenderWorldLastEvent event) {
		//objLoadr("assets/openglasses/shaders/ss.obj");
		if (models.size() == 0) {
			addModel(createModel());
		}
		shader.bind();
		//				if(models.size()>1){
		//					Iterator i = models.iterator();
		//					i.next();
		//					i.remove();
		//				}
		
		
		for (Model m : models) {
			if (m.modelID - System.currentTimeMillis() < 0) {
				AnimationFrame frame = new AnimationFrame();
				frame.duration = 2 * 1000;
				Matrix4f rotate = Matrix4f.rotate((float) Math.toRadians(m.modelID2 % 2 == 0 ? 45 : -45), new Vector3f(0, 1, 0), new Matrix4f(), null);
				Matrix4f Oldrotate = Matrix4f.rotate((float) Math.toRadians(m.modelID2 % 2 != 0 ? 45 : -45), new Vector3f(0, 1, 0), new Matrix4f(), null);
				frame.stopRotation = Quaternion.setFromMatrix(rotate, new Quaternion());
				frame.startRotation = Quaternion.setFromMatrix(Oldrotate, new Quaternion());
				frame.startColor = m.modelID2 % 2 == 0 ? new Vector4f(1f, 1f, 1f, 1f) : new Vector4f(.5f, .1f, .1f, 0.5f);
				frame.stopColor = m.modelID2 % 2 != 0 ? new Vector4f(1f, 1f, 1f, 1f) : new Vector4f(.5f, .1f, .1f, 0.5f);
				//frame.startScale = m.modelID2 % 2 == 0 ? new Vector4f(2, 2, 2, 1) : new Vector4f(1, 1, 1, 1);
				//frame.stopScale = m.modelID2 % 2 != 0 ? new Vector4f(2, 2, 2, 1) : new Vector4f(1, 1, 1, 1);
				
				
				//frame.startTranslate = m.modelID2 % 2 == 0 ? new Vector4f(0, 10, 0, 1) : new Vector4f(0, 0, 0, 1);
				//frame.stopTranslate = m.modelID2 % 2 != 0 ? new Vector4f(0, 10, 0, 1) : new Vector4f(0, 0, 0, 1);
				
				m.animationFrames.add(frame);
				m.modelID = System.currentTimeMillis() + (2000);
				m.modelID2++;
			}
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m.bufferID);
			GL20.glVertexAttribPointer(shader.getInColorAttrib(), 4, GL11.GL_FLOAT, false, BufferElement.SIZE * 4, BufferElement.COLOR_POINTER_OFFSET * 4);
			GL20.glVertexAttribPointer(shader.getInUVAttrib(), 2, GL11.GL_FLOAT, false, BufferElement.SIZE * 4, BufferElement.TEXCOORD_POINTER_OFFSET * 4);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, BufferElement.SIZE * 4, BufferElement.VERTEX_POINTER_OFFSET * 4);
			for (ModelPart part : m.parts.values()) {
				
				FloatBuffer buffer = BufferUtils.createFloatBuffer(16 * 2);
				FloatBuffer bufferAnim = BufferUtils.createFloatBuffer(32 * 2);
				m.matrix.store(buffer);
				part.matrix.store(buffer);
				float t1 = 1;
				float t2 = 0;
				if (m.currAnimationFrame == null) {
					m.currAnimationFrame = m.animationFrames.poll();
					m.currAnimationFrame.endtime = System.currentTimeMillis() + m.currAnimationFrame.duration;
				}
				
				if (m.currAnimationFrame != null) {
					if (m.currAnimationFrame.endtime <= System.currentTimeMillis() && m.animationFrames.size() > 0) {
						AnimationFrame frame = m.currAnimationFrame;
						m.currAnimationFrame = m.animationFrames.poll();
						m.currAnimationFrame.endtime = System.currentTimeMillis() + m.currAnimationFrame.duration;
						//m.currAnimationFrame.start = Matrix4f.mul(frame.stop, m.currAnimationFrame.start, m.currAnimationFrame.start);
					}
					m.currAnimationFrame.store(bufferAnim);
				} else {
					new AnimationFrame().store(bufferAnim);
				}
				
				t1 = Math.max(Math.min(1, 1 - ((m.currAnimationFrame.endtime - System.currentTimeMillis()) / (float) m.currAnimationFrame.duration)), 0);
				
				
				AnimationFrame animationFramePart = part.animationFrames.peek();
				if (animationFramePart == null) {
					new AnimationFrame().store(bufferAnim);
				}
				
				GL20.glUniform2f(shader.getInTime(), t1, t2);
				
				
				buffer.flip();
				GL20.glUniformMatrix4(shader.getInMatrix(), false, buffer);
				bufferAnim.flip();
				GL20.glUniform4(shader.getInAnimation(), bufferAnim);
				
				GL11.glDrawArrays(GL11.GL_TRIANGLES, part.startBufferPosition, part.getElements());
			}
		}
		shader.unBind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void removeModel(Model m) {
		models.remove(m);
		GL15.glDeleteBuffers(m.bufferID);
	}
	
	public void addModel(Model model) {
		model.bufferID = GL15.glGenBuffers();
		FloatBuffer buff = BufferUtils.createFloatBuffer(model.calculateBufferSize());
		int offset = 0;
		for (ModelPart part : model.parts.values()) {
			part.startBufferPosition = offset;
			offset += part.getElements();
			part.bufferElements.forEach(bufferElement -> buff.put(bufferElement.get()));
		}
		buff.flip();
		vertexBufferData(model.bufferID, buff);
		models.add(model);
	}
	
	public static void vertexBufferData(int id, FloatBuffer buffer) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	public Model createModel() {
		Model model = new Model();
		ModelPart part = new ModelPart();
		BufferElement v1 = new BufferElement().setX(0).setY(0).setA(0.5f).setR(1).setG(0).setB(0).setZ(0);
		BufferElement v2 = new BufferElement().setX(0).setY(10).setA(0.5f).setR(0).setG(1).setB(0).setZ(0);
		BufferElement v3 = new BufferElement().setX(10).setY(0).setA(0.5f).setR(0).setG(0).setB(1).setZ(0);
		
		BufferElement v4 = new BufferElement().setX(0).setY(10).setA(1f).setR(1).setG(0).setB(0).setZ(0);
		BufferElement v5 = new BufferElement().setX(10).setY(10).setA(1f).setR(0).setG(1).setB(0).setZ(0);
		BufferElement v6 = new BufferElement().setX(10).setY(0).setA(1f).setR(0).setG(0).setB(1).setZ(0);
		part.addTriangle(v1, v2, v3);
		part.addTriangle(v4, v5, v6);
		model.parts.put("Quad", part);
		return model;
	}
	
	
}
