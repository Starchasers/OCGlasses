package com.bymarcin.ocglasses.surface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();
	public Map<Integer, IRenderableWidget> renderables = new HashMap<Integer, IRenderableWidget>();

	
	private ClientSurface() {}
	
	
	public void updateWigets(Set<Entry<Integer, Widget>> widgets){
		for(Entry<Integer, Widget> widget : widgets){
			renderables.put(widget.getKey(), widget.getValue().getRenderable());
		}
	}
	
	public void removeWidgets(List<Integer> ids){
		for(Integer id : ids){
			renderables.remove(id);
		}
	}
	
	public void removeAllWidgets(){
		renderables.clear();
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
	
	public void onLookingAt(World world, int x, int y, int z){
		//System.out.println(world.getBlock(x, y, z));
	}
}
