package com.bymarcin.openglasses.surface.vbo;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

public class RenderManager {
	BasicShader shader;
	List<Model> models = new LinkedList<>();
	
	public void init() {
		shader = new BasicShader();
		//addModel(createModel());
	}
	
	public Model objLoadr(String objFile) {
		Model m = new Model();
		try {
			Obj obj = ObjUtils.convertToRenderable(ObjReader.read(ClassLoader.getSystemResourceAsStream(objFile)));
			ModelPart  p = new ModelPart();
			
			for (int i = 0; i < obj.getNumFaces(); i++) {
				
				
				ObjFace face = obj.getFace(i);
				
				FloatTuple v1 = obj.getVertex(face.getVertexIndex(0));
				FloatTuple v2 = obj.getVertex(face.getVertexIndex(1));
				FloatTuple v3 = obj.getVertex(face.getVertexIndex(2));
				p.addTriangle(
						new BufferElement().setX(v1.getX()).setY(v1.getY()).setZ(v1.getZ()).setA(1f),
						new BufferElement().setX(v2.getX()).setY(v2.getY()).setZ(v2.getZ()).setA(1f),
						new BufferElement().setX(v3.getX()).setY(v3.getY()).setZ(v3.getZ()).setA(1f)
				);
			}
			
			m.parts.put("main",p);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return m;
	}
	
	public void bind(int bufferId) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
	}
	
	public void render() {
		//objLoadr("assets/openglasses/shaders/ss.obj");
		shader.bind();
//				if(models.size()>1){
//					Iterator i = models.iterator();
//					i.next();
//					i.remove();
//				}

		
		for (Model m : models) {
			m.matrix = m.matrix.rotate(1/(120f*15f), new Vector3f(0,1,0) );
			if(m.modelID==0){
				m.matrix = m.matrix.translate(new Vector3f(0,20,0));
				m.modelID=1;
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m.bufferID);
			GL20.glVertexAttribPointer(shader.getInColorAttrib(), 4, GL11.GL_FLOAT, false, BufferElement.SIZE * 4, BufferElement.COLOR_POINTER_OFFSET * 4);
			GL20.glVertexAttribPointer(shader.getInUVAttrib(), 2, GL11.GL_FLOAT, false, BufferElement.SIZE * 4, BufferElement.TEXCOORD_POINTER_OFFSET * 4);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, BufferElement.SIZE * 4, BufferElement.VERTEX_POINTER_OFFSET * 4);
			GL20.glUniformMatrix4(shader.getInModelMatrix(), false, m.matrix());
			for (ModelPart part : m.parts.values()) {
				GL11.glDrawArrays(GL11.GL_TRIANGLES, part.startBufferPosition, part.getElements());
			}
		}
		shader.unBind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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
	*
	*/
	
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
	
	public class ModelPart {
		Matrix4f matrix = Matrix4f.setIdentity(new Matrix4f());
		List<BufferElement> bufferElements = new LinkedList<>();
		int startBufferPosition;
		
		int getElements() {
			return bufferElements.size();
		}
		
		public void addTriangle(BufferElement v1, BufferElement v2, BufferElement v3) {
			bufferElements.add(v1);
			bufferElements.add(v2);
			bufferElements.add(v3);
		}
	}
	
	public class Model {
		int bufferID;
		Matrix4f matrix = Matrix4f.setIdentity(new Matrix4f());
		long modelID;
		HashMap<String, ModelPart> parts = new HashMap<>();
		
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
	
	
	public class BufferElement {
		public static final int SIZE = 9;
		public static final int VERTEX_POINTER_OFFSET = 0;
		public static final int COLOR_POINTER_OFFSET = 3;
		public static final int TEXCOORD_POINTER_OFFSET = 7;
		private float[] floats = new float[SIZE];
		
		public BufferElement setX(float x) {
			floats[0] = x;
			return this;
		}
		
		public BufferElement setY(float y) {
			floats[1] = y;
			return this;
		}
		
		public BufferElement setZ(float z) {
			floats[2] = z;
			return this;
		}
		
		public BufferElement setR(float r) {
			floats[3] = r;
			return this;
		}
		
		public BufferElement setG(float g) {
			floats[4] = g;
			return this;
		}
		
		public BufferElement setB(float b) {
			floats[5] = b;
			return this;
		}
		
		public BufferElement setA(float a) {
			floats[6] = a;
			return this;
		}
		
		public BufferElement setU(float u) {
			floats[7] = u;
			return this;
		}
		
		public BufferElement setV(float v) {
			floats[8] = v;
			return this;
		}
		
		public float[] get() {
			return floats;
		}
	}
	
	
}
