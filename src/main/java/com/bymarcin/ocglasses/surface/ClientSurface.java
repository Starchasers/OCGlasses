package com.bymarcin.ocglasses.surface;

import java.util.LinkedList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();
	public LinkedList<Widgets> storage = new LinkedList<Widgets>();

	private ClientSurface() {}
	
	
	public void addWiget(IWidget widget){
		
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.type == ElementType.HELMET && evt instanceof RenderGameOverlayEvent.Post) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0/* col */, 128);
			tessellator.addVertex(0, 0, 0);
			tessellator.addVertex(0, 32, 0);
			tessellator.addVertex(32, 32, 0);
			tessellator.addVertex(32, 0, 0);
			tessellator.draw();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}
	
	public void onLookingAt(World world, int x, int y, int z){
		//System.out.println(world.getBlock(x, y, z));
	}
}
