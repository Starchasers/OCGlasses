package com.bymarcin.openglasses.surface;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;


public class VBO {
	public static final VBO vbo = new VBO();
	
	
	@SubscribeEvent
	public void renderInWorld(RenderWorldLastEvent event) {
		try {
			EntityPlayer player = Minecraft.getMinecraft().player;
			double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
			double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
			double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();
			
		
			GL11.glPushMatrix();
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
			//GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			 //GL11.glDepthMask(false);
			GL11.glTranslated(-playerX, -playerY, -playerZ);
			// Start render
			vbo.render();
			// End render
			// GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			//GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void vertexBufferData(int id, FloatBuffer buffer) { //Not restricted to FloatBuffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id); //Bind buffer (also specifies type of buffer)
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Send up the data and specify usage hint.
	}
	
	public static int createVBOID() {
		return GL15.glGenBuffers();
	}
	
	
	public class BufferElement{
		public static final int SIZE = 9;
		public static final int VERTEX_POINTER_OFFSET = 0;
		public static final int COLOR_POINTER_OFFSET = 3;
		public static final int TEXCOORD_POINTER_OFFSET = 7;
		private float[] floats = new float[SIZE];
		public BufferElement setX(float x){ floats[0]=x; return this;}
		public BufferElement setY(float y){ floats[1]=y; return this;}
		public BufferElement setZ(float z){ floats[2]=z; return this;}
		public BufferElement setR(float r){ floats[3]=r; return this;}
		public BufferElement setG(float g){ floats[4]=g; return this;}
		public BufferElement setB(float b){ floats[5]=b; return this;}
		public BufferElement setA(float a){ floats[6]=a; return this;}
		public BufferElement setU(float u){ floats[7]=u; return this;}
		public BufferElement setV(float v){ floats[8]=v; return this;}
		public float[] get() {return floats;}
	}
	
	public void render(){
		System.out.println("render");
		int vertexBufferID = createVBOID();
		
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.DISPENSER.getDefaultState());
		TextureAtlasSprite texture = model.getParticleTexture();
		
		
		BufferElement v1 = new BufferElement().setX(0 ).setY(0 ).setA(1f).setR(.5f).setZ(0);
		BufferElement v2 = new BufferElement().setX(0 ).setY(10).setA(1f).setG(.5f).setZ(0);
		BufferElement v3 = new BufferElement().setX(10).setY(0 ).setA(1f).setB(.5f).setZ(0);
		
		BufferElement v4 = new BufferElement().setX(0 ).setY(10).setA(1f).setB(.5f).setZ(0);
		BufferElement v5 = new BufferElement().setX(10).setY(10).setA(1f).setG(.5f).setZ(0);
		BufferElement v6 = new BufferElement().setX(10).setY(0 ).setA(1f).setR(.5f).setB(.5f).setZ(0);
		
		FloatBuffer buff = BufferUtils.createFloatBuffer(6*BufferElement.SIZE);
		
		if(texture!=null){
			v1.setU(texture.getMinU()).setV(texture.getMinV());
			v2.setU(texture.getMinU()).setV(texture.getMaxV());
			v3.setU(texture.getMaxU()).setV(texture.getMinV());
			
			v4.setU(texture.getMinU()).setV(texture.getMaxV());
			v5.setU(texture.getMaxU()).setV(texture.getMaxV());
			v6.setU(texture.getMaxU()).setV(texture.getMinV());
		}
		
		buff.put(v1.get());
		buff.put(v2.get());
		buff.put(v3.get());
		buff.put(v4.get());
		buff.put(v5.get());
		buff.put(v6.get());
		buff.flip();
		vertexBufferData(vertexBufferID, buff);
		
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		GL13.glClientActiveTexture(GL_TEXTURE0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		

		GL11.glVertexPointer(3, GL_FLOAT, BufferElement.SIZE*4, BufferElement.VERTEX_POINTER_OFFSET);
		GL11.glColorPointer(4, GL_FLOAT, BufferElement.SIZE*4, BufferElement.COLOR_POINTER_OFFSET*4);
		GL11.glTexCoordPointer(2, GL_FLOAT, BufferElement.SIZE*4, BufferElement.TEXCOORD_POINTER_OFFSET*4);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		
		//cleanup
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vertexBufferID);
	 /*
	 
	 
	   glBindBuffer(GL_ARRAY_BUFFER, VertexVBOID);
  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3, GL_FLOAT, sizeof(MyVertex), BUFFER_OFFSET(0));   //The starting point of the VBO, for the vertices
  glEnableClientState(GL_NORMAL_ARRAY);
  glNormalPointer(GL_FLOAT, sizeof(MyVertex), BUFFER_OFFSET(12));   //The starting point of normals, 12 bytes away
  glClientActiveTexture(GL_TEXTURE0);
  glEnableClientState(GL_TEXTURE_COORD_ARRAY);
  glTexCoordPointer(2, GL_FLOAT, sizeof(MyVertex), BUFFER_OFFSET(24));   //The starting point of texcoords, 24 bytes away
  
	 
	struct MyVertex
  {
    float x, y, z;        //Vertex
    float nx, ny, nz;     //Normal
    float s0, t0;         //Texcoord0
  };
  
  MyVertex pvertex[3];


  
  glGenBuffers(1, VertexVBOID);
  glBindBuffer(GL_ARRAY_BUFFER, VertexVBOID);
  glBufferData(GL_ARRAY_BUFFER, sizeof(MyVertex)*3, &pvertex[0].x, GL_STATIC_DRAW);
  
  ushort pindices[3];
  pindices[0] = 0;
  pindices[1] = 1;
  pindices[2] = 2;
  
  glGenBuffers(1, &IndexVBOID);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IndexVBOID);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(ushort)*3, pindices, GL_STATIC_DRAW);
  
  //Define this somewhere in your header file
  #define BUFFER_OFFSET(i) ((void*)(i))
  
  glBindBuffer(GL_ARRAY_BUFFER, VertexVBOID);
  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3, GL_FLOAT, sizeof(MyVertex), BUFFER_OFFSET(0));   //The starting point of the VBO, for the vertices
  glEnableClientState(GL_NORMAL_ARRAY);
  glNormalPointer(GL_FLOAT, sizeof(MyVertex), BUFFER_OFFSET(12));   //The starting point of normals, 12 bytes away
  glClientActiveTexture(GL_TEXTURE0);
  glEnableClientState(GL_TEXTURE_COORD_ARRAY);
  glTexCoordPointer(2, GL_FLOAT, sizeof(MyVertex), BUFFER_OFFSET(24));   //The starting point of texcoords, 24 bytes away
  
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IndexVBOID);
  //To render, we can either use glDrawElements or glDrawRangeElements
  //The is the number of indices. 3 indices needed to make a single triangle
  glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_SHORT, BUFFER_OFFSET(0));   //The starting point of the IBO
  //0 and 3 are the first and last vertices
  //glDrawRangeElements(GL_TRIANGLES, 0, 3, 3, GL_UNSIGNED_SHORT, BUFFER_OFFSET(0));   //The starting point of the IBO
  //glDrawRangeElements may or may not give a performance advantage over glDrawElements
		
		
		
		*/
	}
}
