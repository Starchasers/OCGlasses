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
	int frame;
	Model quad = new Model();
	
	public TestSurface() {

	}
	
	public void init(){
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
		
		if(frame%100==0){
			quad.rotateModel(1, 0, 1, 0);
			manager.updateModel(quad);
		}
		
		frame++;
		
		
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
		manager.render();
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
