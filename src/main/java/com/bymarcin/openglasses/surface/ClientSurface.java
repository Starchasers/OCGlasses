package com.bymarcin.openglasses.surface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.widgets.component.face.Text;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();
	public Map<Integer, IRenderableWidget> renderables = new HashMap<Integer, IRenderableWidget>();
	public Map<Integer, IRenderableWidget> renderablesWorld = new HashMap<Integer, IRenderableWidget>();
	boolean isPowered = false;
	public boolean haveGlasses = false;
	public Location lastBind;
	IRenderableWidget noPowerRender;
	private ClientSurface() {
		noPowerRender = getNoPowerRender();
	}
	
	
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
		if (evt.type == ElementType.HELMET && evt instanceof RenderGameOverlayEvent.Post && haveGlasses) {
			if(!isPowered || !haveGlasses || lastBind == null){ noPowerRender.render(null, 0, 0, 0); return;}
			GL11.glPushMatrix();
			//GL11.glScaled(evt.resolution.getScaledWidth_double()/512D, evt.resolution.getScaledHeight_double()/512D*16D/9D, 0);
			for(IRenderableWidget renderable : renderables.values()){
				if(renderable.shouldWidgetBeRendered())
					renderable.render(null, 0, 0, 0);
			}
			GL11.glPopMatrix();
		}
	}
	
	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)
	{	
		if(!isPowered || !haveGlasses || lastBind == null) return;
		GL11.glPushMatrix();
		EntityPlayer player= Minecraft.getMinecraft().thePlayer;
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks; 
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks;
		GL11.glTranslated(-playerX, -playerY, -playerZ);
		GL11.glTranslated(lastBind.x, lastBind.y, lastBind.z);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		//Start Drawing In World
		
		for(IRenderableWidget renderable : renderablesWorld.values()){
			if(renderable.shouldWidgetBeRendered())
				renderable.render(player, playerX - lastBind.x, playerY - lastBind.y, playerZ - lastBind.z);
		}
		
		
		//Stop Drawing In World
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static MovingObjectPosition  getBlockCoordsLookingAt(EntityPlayer player){
		MovingObjectPosition objectMouseOver;
		objectMouseOver = player.rayTrace(200, 1);	
		if(objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
		{
			return objectMouseOver;
		}
		return null;
	}
	
	public void setPowered(boolean isPowered) {
		this.isPowered = isPowered;
	}
	
	private IRenderableWidget getNoPowerRender(){
		Text t = new Text();
		t.setText("NO POWER");
		t.setAlpha(0.5);
		t.setScale(1);
		t.setColor(1, 0, 0);
		return t.getRenderable();
	}
	
}
