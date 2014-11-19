package com.bymarcin.ocglasses.surface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();
	public Map<Integer, IRenderableWidget> renderables = new HashMap<Integer, IRenderableWidget>();
	public Map<Integer, IRenderableWidget> renderablesWorld = new HashMap<Integer, IRenderableWidget>();
	
	private ClientSurface() {}
	
	
	public void updateWigets(Set<Entry<Integer, Widget>> widgets){
		for(Entry<Integer, Widget> widget : widgets){
			IRenderableWidget r = widget.getValue().getRenderable();
			switch(r.getRenderType()){
			case GameOverlayLocated: renderables.put(widget.getKey(), r);
				break;
			case WorldLocated: renderablesWorld.put(widget.getKey(), r);
				break;
			}
		}
	}
	
	public void removeWidgets(List<Integer> ids){
		for(Integer id : ids){
			renderables.remove(id);
			renderablesWorld.remove(id);
		}
	}
	
	public void removeAllWidgets(){
		renderables.clear();
		renderablesWorld.clear();
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.type == ElementType.HELMET && evt instanceof RenderGameOverlayEvent.Post) {
			GL11.glPushMatrix();
			//GL11.glScaled(evt.resolution.getScaledWidth_double()/512D, evt.resolution.getScaledHeight_double()/512D*16D/9D, 0);
			for(IRenderableWidget renderable : renderables.values()){
				renderable.render();
			}
			GL11.glPopMatrix();
		}
	}
	
	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)
	{	
		GL11.glPushMatrix();
		EntityPlayer player= Minecraft.getMinecraft().thePlayer;
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks; 
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks;
		GL11.glTranslated(-playerX, -playerY, -playerZ);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		//Start Drawing In World
		
		
		for(IRenderableWidget renderable : renderablesWorld.values()){
			renderable.render();
		}
		
		
		//Stop Drawing In World
		GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	

	
	
	public void onLookingAt(World world, int x, int y, int z){
		//System.out.println(world.getBlock(x, y, z));
	}
}
