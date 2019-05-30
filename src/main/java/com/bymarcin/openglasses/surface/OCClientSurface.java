package com.bymarcin.openglasses.surface;

import java.util.*;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.component.face.Text2D;
import ben_mkiv.rendertoolkit.common.widgets.component.world.EntityTracker3D;
import ben_mkiv.rendertoolkit.surface.ClientSurface;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.gui.GlassesGui;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.utils.Conditions;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
	static{
		instances = new OCClientSurface();
	}

	public boolean isInternalComponent;
	public Vec3d renderPosition = new Vec3d(0, 0, 0);

    public static ClientEventHandler eventHandler;

	public Conditions conditions = new Conditions();

	public ItemStack glassesStack = ItemStack.EMPTY;
	public UUID lastBind;

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
		this.glassesStack = ItemStack.EMPTY;
		this.lastBind = null;
	}

	public void initLocalGlasses(ItemStack glassesStack){
		this.glassesStack = glassesStack;
		this.lastBind = OpenGlassesItem.getHostUUID(glassesStack);

		conditions.bufferSensors(this.glassesStack);
	}

	public void refreshConditions(){
		if(glassesStack.isEmpty()) return;

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
		if(glassesStack.isEmpty()) return;
		if(lastBind == null) return;

		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.GLASSES_SCREEN_SIZE, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaleFactor()));
	}


	public static class GlassesNotifications {
		public static HashSet<GlassesNotification> notifications = new HashSet<>();

		public static void update(){
			for(GlassesNotification notification : notifications)
				notification.update();
		}

		public interface GlassesNotification{
			void update();
			void cancel();
			void submit();
		}
	}

	public static class LinkRequest implements GlassesNotifications.GlassesNotification {
		private long linkRequestActive = 0;
		public UUID host;
		public BlockPos pos;
		int timeout = 120;

		public LinkRequest(UUID hostUUID, BlockPos hostPosition){
			linkRequestActive = System.currentTimeMillis();
			host = hostUUID;
			pos = hostPosition;

			GlassesNotifications.notifications.add(this);

			ItemStack glassesStack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);
			if(!glassesStack.isEmpty()) {
				if(!glassesStack.getTagCompound().hasKey("nopopups"))
					Minecraft.getMinecraft().displayGuiScreen(new GlassesGui());
			}
		}

		@Override
		public void update(){
			if(System.currentTimeMillis() - linkRequestActive > timeout * 1000) {
				cancel();
			}
		}

		@Override
		public void submit(){
			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.ACCEPT_LINK));
			remove();
		}

		@Override
		public void cancel(){
			remove();
			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.DENY_LINK));
		}

		void remove(){
			linkRequestActive = 0;
			GlassesNotifications.notifications.remove(this);

			ItemStack glassesStack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);
			if(!glassesStack.isEmpty()) {
				if(!glassesStack.getTagCompound().hasKey("nopopups"))
					if(Minecraft.getMinecraft().currentScreen instanceof GlassesGui)
						Minecraft.getMinecraft().currentScreen = null;
			}
		}
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

		GlStateManager.translate(getRenderPosition().x, getRenderPosition().y, getRenderPosition().z);

		GlStateManager.depthMask(true);
		renderWidgets(renderablesWorld.values());
		postRender(RenderType.WorldLocated);
	}

	public Vec3d getRenderPosition(){
		if(!isInternalComponent)
			return renderPosition;

		return new Vec3d(0, 0, 0);
	}

	void renderWidgets(Collection<IRenderableWidget> widgets){
		String uuid = glassesStack.getTagCompound().getString("userUUID");

		long renderConditions = conditions.get();

		Vector3f offset = new Vector3f((float) getRenderPosition().x, (float) getRenderPosition().y, (float) getRenderPosition().z);

		for(IRenderableWidget renderable : widgets) {
			if(!renderable.shouldWidgetBeRendered(Minecraft.getMinecraft().player, offset))
				continue;

			if(!renderable.isWidgetOwner(uuid))
				continue;

			if(renderable instanceof EntityTracker3D.RenderEntityTracker)
				renderWidget(renderable, renderConditions, getRenderPosition().scale(-1));
			else
				renderWidget(renderable, renderConditions);
		}
	}


	public boolean shouldRenderStart(RenderType renderEvent){
		if(!super.shouldRenderStart(renderEvent))
			return false;

		if(this.glassesStack.isEmpty())
			return false;

		if(OpenGlassesItem.getEnergyStored(glassesStack) == 0){
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
