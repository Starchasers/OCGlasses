package com.bymarcin.ocglasses.surface;

import java.util.ArrayList;
import java.util.LinkedList;

import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();
	public LinkedList<IRenderableWidget> renderables = new LinkedList<IRenderableWidget>();

	private ClientSurface() {}
	
	
	public void addWiget(ArrayList<IWidget> widgets){
		for(IWidget widget : widgets){
			renderables.add(widget.getRenderable());
		}
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.type == ElementType.HELMET && evt instanceof RenderGameOverlayEvent.Post) {
			for(IRenderableWidget renderable : renderables){
				renderable.render();
			}
		}
	}
	
	public void onLookingAt(World world, int x, int y, int z){
		//System.out.println(world.getBlock(x, y, z));
	}
}
