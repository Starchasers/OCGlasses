package com.bymarcin.openglasses.vbo;

import java.util.HashMap;

import com.bymarcin.openglasses.network.WidgetPacket;
import com.bymarcin.openglasses.utils.Location;
import com.bymarcin.openglasses.vbo.model.Model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ClientLayer {
	private static ClientLayer instance = new ClientLayer();
	VBOManager vbo = new VBOManager();
	HashMap<String, Model> models = new HashMap<String, Model>();
	boolean isPowered = false;
	public boolean haveGlasses = false;
	public Location lastBind;
	
	
	private ClientLayer() {
		
	}

	public static ClientLayer getInstance() {
		return instance;
	}
	
	public void setPowered(boolean isPowered) {
		this.isPowered = isPowered;
	}

	public void addModel(Model m) {
		models.put(m.getId(), m);
		vbo.addModel(m);
		System.out.println("ADDING");
	}

	public void updateModel(int action, String id, float... args) {
		Model m = models.get(id);
		if (m != null) {
			switch (action) {
			case WidgetPacket.VISIBLE:
				m.setVisible(true);
				break;
			case WidgetPacket.INVISIBLE:
				m.setVisible(false);
				break;
			case WidgetPacket.RESET:
				m.resetModelTransformation();
				break;
			case WidgetPacket.ROTATE:
				m.rotateModel(args[0], args[1], args[2], args[3]);
				break;
			case WidgetPacket.SCALE:
				m.scaleModel(args[0], args[1], args[2]);
				break;
			case WidgetPacket.TRANSLATE:
				m.translateModel(args[0], args[1], args[2]);
				break;
			}
			vbo.updateModel(m);
		}
	}

	public void removeModel(String id) {
		Model m = models.remove(id);
		if (m != null) {
			vbo.removeModel(m);
		}
	}

	public void clear() {
		vbo.clear();
		models.clear();
	}

	@SubscribeEvent
	public void renderInWorld(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		// GL11.glDepthMask(false);
		GL11.glPushMatrix();
		GL11.glTranslated(-playerX, -playerY, -playerZ);
		// Start render
		vbo.render();
		// End render
		GL11.glPopMatrix();
		// GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	@SubscribeEvent
	public void renderOnGameOverlay(RenderGameOverlayEvent event) {

	}

}
