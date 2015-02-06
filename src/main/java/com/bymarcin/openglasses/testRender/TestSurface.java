package com.bymarcin.openglasses.testRender;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class TestSurface {
	VBOManager manager = new VBOManager();
	boolean init = false;
	
	public TestSurface() {

	}
	
	public void init(){
		Model quad = new Model();
		quad.setColor(1, 0, 0, 0.5f);
		quad.addVertex(50, 50, 50);
		quad.addVertex(40, 50, 50);
		quad.addVertex(50, 60, 50);
		
		quad.addVertex(0, 100, 0);
		quad.addVertex(100, 0, 0);
		quad.addVertex(0, 0, 0);

		manager.addModel(quad);
	}

	@SubscribeEvent
	public void renderInWorld(RenderWorldLastEvent event) {
		if(!init){
			init();
			init=true;
		}
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glDepthMask(false);
		GL11.glPushMatrix();
		GL11.glTranslated(-playerX, -playerY, -playerZ);
		// Start render
		GL11.glColor4f(1,0,0,1);    // Color RED
		manager.render();

//		GL11.glScalef(20, 20, 20);
//		GL11.glBegin(GL11.GL_QUADS);        // Draw The Cube Using quads
//	 	GL11.glColor4f(1,0,0,1);    // Color Blue
//	 	GL11.glVertex3f( 1.0f, 1.0f,0f);    // Top Right Of The Quad (Top)
//	 	GL11.glVertex3f(0f, 1.0f,0f);    // Top Left Of The Quad (Top)
//	 	GL11.glVertex3f(0f, 1.0f, 1.0f);    // Bottom Left Of The Quad (Top)
//	 	GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Bottom Right Of The Quad (Top)
//
//	 	GL11.glVertex3f( 1.0f,0f, 1.0f);    // Top Right Of The Quad (Bottom)
//	 	GL11.glVertex3f(0f,0f, 1.0f);    // Top Left Of The Quad (Bottom)
//	 	GL11.glVertex3f(0f,0f,0f);    // Bottom Left Of The Quad (Bottom)
//	 	GL11.glVertex3f( 1.0f,0f,0f);    // Bottom Right Of The Quad (Bottom)
//
//	 	GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Top Right Of The Quad (Front)
//	 	GL11.glVertex3f(0f, 1.0f, 1.0f);    // Top Left Of The Quad (Front)
//	 	GL11.glVertex3f(0f,0f, 1.0f);    // Bottom Left Of The Quad (Front)
//	 	GL11.glVertex3f( 1.0f,0f, 1.0f);    // Bottom Right Of The Quad (Front)
//
//	    GL11.glVertex3f( 1.0f,0f,0f);    // Top Right Of The Quad (Back)
//	    GL11.glVertex3f(0f,0f,0f);    // Top Left Of The Quad (Back)
//	    GL11.glVertex3f(0f, 1.0f,0f);    // Bottom Left Of The Quad (Back)
//	    GL11.glVertex3f( 1.0f, 1.0f,0f);    // Bottom Right Of The Quad (Back)
//
//	    GL11.glVertex3f(0f, 1.0f, 1.0f);    // Top Right Of The Quad (Left)
//	    GL11.glVertex3f(0f, 1.0f,0f);    // Top Left Of The Quad (Left)
//	    GL11.glVertex3f(0f,0f,0f);    // Bottom Left Of The Quad (Left)
//	    GL11.glVertex3f(0f,0f, 1.0f);    // Bottom Right Of The Quad (Left)
//
//	    GL11.glVertex3f( 1.0f, 1.0f,0f);    // Top Right Of The Quad (Right)
//	    GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Top Left Of The Quad (Right)
//	    GL11.glVertex3f( 1.0f,0f, 1.0f);    // Bottom Left Of The Quad (Right)
//	    GL11.glVertex3f( 1.0f,0f,0f);    // Bottom Right Of The Quad (Right)
//    GL11.glEnd(); 
		
		// End render
		GL11.glPopMatrix();
	//	GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	@SubscribeEvent
	public void renderOnGameOverlay(RenderGameOverlayEvent event) {

	}
}
