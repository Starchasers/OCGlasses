package com.bymarcin.openglasses.surface;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.bymarcin.openglasses.surface.vbo.BasicShader;
import com.bymarcin.openglasses.surface.vbo.RenderManager;
import com.bymarcin.openglasses.surface.vbo.TextureManager;
import static org.lwjgl.opengl.GL11.GL_FLOAT;

@SideOnly(Side.CLIENT)
public class VBO {
	public static final VBO instances = new VBO();
	BasicShader basicShader;
	TextureManager textureManager;
	RenderManager rm = new RenderManager();
	
	public void compileShader(){
		basicShader = new BasicShader();
		textureManager = new TextureManager();
		rm.init();
	}
	
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
			//GL11.glDisable(GL11.GL_DEPTH_TEST);
			 //GL11.glDepthMask(false);
			GL11.glTranslated(-playerX, -playerY, -playerZ);
			// Start render
			instances.render(event);
			// End render
			// GL11.glDepthMask(true);
			//GL11.glEnable(GL11.GL_LIGHTING);
			//GL11.glDisable(GL11.GL_BLEND);
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
	
	

	
	public void render(RenderWorldLastEvent event){
		//System.out.println("render");
		rm.render(event);
//
//		int vertexBufferID = createVBOID();
//
//
//		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//
//		TextureAtlasSprite texture =  textureManager.findTexture("minecraft:chest");
//
//		BufferElement v1 = new BufferElement().setX(0 ).setY(0 ).setA(0.5f).setR(1).setG(0).setB(0).setZ(0);
//		BufferElement v2 = new BufferElement().setX(0 ).setY(10).setA(0.5f).setR(0).setG(1).setB(0).setZ(0);
//		BufferElement v3 = new BufferElement().setX(10).setY(0 ).setA(0.5f).setR(0).setG(0).setB(1).setZ(0);
//
//		BufferElement v4 = new BufferElement().setX(0 ).setY(10).setA(1f).setR(1).setG(0).setB(0).setZ(0);
//		BufferElement v5 = new BufferElement().setX(10).setY(10).setA(1f).setR(0).setG(1).setB(0).setZ(0);
//		BufferElement v6 = new BufferElement().setX(10).setY(0 ).setA(1f).setR(0).setG(0).setB(1).setZ(0);
//
//		FloatBuffer buff = BufferUtils.createFloatBuffer(6*BufferElement.SIZE);
//
//		if(texture!=null){
//			v1.setU(texture.getMinU()).setV(texture.getMinV());
//			v2.setU(texture.getMinU()).setV(texture.getMaxV());
//			v3.setU(texture.getMaxU()).setV(texture.getMinV());
//
//			v4.setU(texture.getMinU()).setV(texture.getMaxV());
//			v5.setU(texture.getMaxU()).setV(texture.getMaxV());
//			v6.setU(texture.getMaxU()).setV(texture.getMinV());
//		}
//
//		buff.put(v1.get());
//		buff.put(v2.get());
//		buff.put(v3.get());
//		buff.put(v4.get());
//		buff.put(v5.get());
//		buff.put(v6.get());
//		buff.flip();
//		vertexBufferData(vertexBufferID, buff);
		

		//GL13.glClientActiveTexture(GL_TEXTURE0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
//		basicShader.bind();
//
//		GL11.glVertexPointer(3, GL_FLOAT, BufferElement.SIZE*4, BufferElement.VERTEX_POINTER_OFFSET*4);
//		GL20.glVertexAttribPointer(basicShader.getInColorAttrib(),4,GL_FLOAT,false,BufferElement.SIZE*4,BufferElement.COLOR_POINTER_OFFSET*4);
//		GL20.glVertexAttribPointer(basicShader.getInUVAttrib(),2,GL_FLOAT,false,BufferElement.SIZE*4,BufferElement.TEXCOORD_POINTER_OFFSET*4);
//
//
//		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
//
//		//cleanup
//		basicShader.unBind();
//
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		GL15.glDeleteBuffers(vertexBufferID);
	}
}
