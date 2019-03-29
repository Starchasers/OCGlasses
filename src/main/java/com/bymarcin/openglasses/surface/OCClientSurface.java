package com.bymarcin.openglasses.surface;

import java.util.*;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.component.face.Text2D;
import ben_mkiv.rendertoolkit.surface.ClientSurface;

import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.utils.Conditions;
import com.bymarcin.openglasses.utils.TerminalLocation;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;

@SideOnly(Side.CLIENT)
public class OCClientSurface extends ClientSurface {
	static {
		instances = new OCClientSurface();
	}

    public static ClientEventHandler eventHandler;

	public Conditions conditions = new Conditions();
	
	public OpenGlassesItem glasses;
	public ItemStack glassesStack;
	public TerminalLocation lastBind;

	private IRenderableWidget noPowerRender, noLinkRender, widgetLimitRender;

	private OCClientSurface() {
		super();
		this.noPowerRender = getNoPowerRender();
		this.noLinkRender = getNoLinkRender();
		this.widgetLimitRender = getWidgetLimitRender();
		this.resetLocalGlasses();
	}

	public void resetLocalGlasses(){
		this.removeAllWidgets();
		this.glasses = null;
		this.glassesStack = null;
		this.lastBind = null;
	}

	public void initLocalGlasses(ItemStack glassesStack){
		this.glassesStack = glassesStack;
		this.glasses = (OpenGlassesItem) glassesStack.getItem();
		this.lastBind = TerminalLocation.getGlassesTerminalUUID(glassesStack);

		conditions.bufferSensors(this.glassesStack);
	}

	public void refreshConditions(){
		if(glassesStack == null) return;

		conditions.getConditionStates(glassesStack, Minecraft.getMinecraft().player);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onSizeChange(RenderGameOverlayEvent event) {
		ScaledResolution newResolution = event.getResolution();

		if  (newResolution.getScaledWidth() != OCClientSurface.resolution.getScaledWidth()
		 || newResolution.getScaledHeight() != OCClientSurface.resolution.getScaledHeight()
		 ||  newResolution.getScaleFactor() != OCClientSurface.resolution.getScaleFactor()) {
			OCClientSurface.resolution = newResolution;
			sendResolution();
		}
	}

	public void sendResolution(){
		if(glasses == null) return;
		if(lastBind == null) return;

		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.GLASSES_SCREEN_SIZE, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaleFactor()));
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.getType() != ElementType.HELMET) return;
		if (!(evt instanceof RenderGameOverlayEvent.Post)) return;

		if(!shouldRenderStart(RenderType.GameOverlayLocated)) return;

		preRender(RenderType.GameOverlayLocated, evt.getPartialTicks());

		if(renderResolution != null)
			GlStateManager.scale(OCClientSurface.resolution.getScaledWidth() / renderResolution.x, OCClientSurface.resolution.getScaledHeight() / renderResolution.y, 1);

		GlStateManager.depthMask(false);
		renderWidgets(renderables.values());
		postRender(RenderType.GameOverlayLocated);
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)	{
		if(!shouldRenderStart(RenderType.WorldLocated)) return;

		preRender(RenderType.WorldLocated, event.getPartialTicks());

		GlStateManager.translate(lastBind.pos.getX(), lastBind.pos.getY(), lastBind.pos.getZ());

		GlStateManager.depthMask(true);
		renderWidgets(renderablesWorld.values());
		postRender(RenderType.WorldLocated);
	}

	void renderWidgets(Collection<IRenderableWidget> widgets){
		String uuid = glassesStack.getTagCompound().getString("userUUID");

		long renderConditions = conditions.get();

		Vector3f renderOffset = new Vector3f((float) lastBind.pos.getX(), (float) lastBind.pos.getY(), (float) lastBind.pos.getZ());

		for(IRenderableWidget renderable : widgets) {
			if(!renderable.shouldWidgetBeRendered(Minecraft.getMinecraft().player, renderOffset))
				continue;

			if(!renderable.isWidgetOwner(uuid))
				continue;

			renderWidget(renderable, renderConditions);
		}
	}


	public boolean shouldRenderStart(RenderType renderEvent){
		if(!super.shouldRenderStart(renderEvent))
			return false;

		if(this.glassesStack == null || this.glasses == null)
			return false;

		if(glasses.getEnergyStored(glassesStack) == 0){
			if(renderEvent.equals(RenderType.GameOverlayLocated) && noPowerRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(noPowerRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		if(getWidgetCount() > glassesStack.getTagCompound().getInteger("widgetLimit")){
			if(renderEvent.equals(RenderType.GameOverlayLocated) && widgetLimitRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(widgetLimitRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		if(lastBind == null) {
			if(renderEvent.equals(RenderType.GameOverlayLocated) && noLinkRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(noLinkRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		return true;
	}


	private IRenderableWidget getNoPowerRender(){
		Text2D t = new Text2D();
		t.setText(new TextComponentTranslation("openglasses.infotext.noenergy").getUnformattedText());
		t.WidgetModifierList.addColor(1F, 0F, 0F, 0.5F);
		t.WidgetModifierList.addTranslate(5, 5, 0);
		return t.getRenderable();
	}

	private IRenderableWidget getNoLinkRender(){
		Text2D t = new Text2D();
		t.setText(new TextComponentTranslation("openglasses.infotext.nolink").getUnformattedText());
		t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
		t.WidgetModifierList.addTranslate(5, 5, 0);
		return t.getRenderable();
	}

	private IRenderableWidget getWidgetLimitRender(){
		Text2D t = new Text2D();
		t.setText(new TextComponentTranslation("openglasses.infotext.widgetlimitexhausted").getUnformattedText());
		t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
		t.WidgetModifierList.addTranslate(5, 5, 0);
		return t.getRenderable();
	}

}
