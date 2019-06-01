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
import com.bymarcin.openglasses.network.packet.HostInfoPacket;
import com.bymarcin.openglasses.utils.Conditions;

import li.cil.oc.api.internal.Robot;
import li.cil.oc.common.tileentity.RobotProxy;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	public Entity renderEntity;
	public UUID renderEntityUUID;
	public int renderEntityID = -1;
	public int renderEntityDimension;
	public Robot renderEntityRobot;

	public HostInfoPacket.HostType hostType = HostInfoPacket.HostType.TERMINAL;

	public boolean isInternal = false;

    public static ClientEventHandler eventHandler;

	public Conditions conditions = new Conditions();

	public ItemStack glassesStack = ItemStack.EMPTY;
	public UUID lastBind;

	private boolean renderWorld = true, renderOverlay = true;

	private IRenderableWidget noPowerRender, noLinkRender, widgetLimitRender;

	private OCClientSurface() {
		super();
		this.noPowerRender = getNoPowerRender();
		this.noLinkRender = getNoLinkRender();
		this.widgetLimitRender = getWidgetLimitRender();
		this.resetLocalGlasses();
	}

	public static OCClientSurface instance(){
		return (OCClientSurface) instances;
	}

	public Robot getRobotEntity(){
		if(renderEntityRobot == null && renderEntityDimension == Minecraft.getMinecraft().world.provider.getDimension()){
			TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(renderPosition));
			if(tile instanceof li.cil.oc.common.tileentity.RobotProxy)
				OCClientSurface.instance().renderEntityRobot = ((RobotProxy) tile).robot();
		}

		return renderEntityRobot;
	}

	private Entity getRenderEntity(){
		switch(hostType){
			case TABLET:
			case DRONE:
				if(renderEntity == null && renderEntityID != -1
						&& renderEntityDimension == Minecraft.getMinecraft().player.world.provider.getDimension()){
					renderEntity = Minecraft.getMinecraft().world.getEntityByID(renderEntityID);
				}
				return renderEntity;

			default:
				return null;
		}
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

		renderWorld = !glassesStack.getTagCompound().hasKey("noWorld");
		renderOverlay = !glassesStack.getTagCompound().hasKey("noOverlay");
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
				if(!glassesStack.getTagCompound().hasKey("nopopups") && !(Minecraft.getMinecraft().currentScreen instanceof GlassesGui))
					Minecraft.getMinecraft().displayGuiScreen(new GlassesGui(true));
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
			if(!glassesStack.isEmpty()
					&& Minecraft.getMinecraft().currentScreen instanceof GlassesGui
					&& GlassesGui.isNotification) {
				Minecraft.getMinecraft().displayGuiScreen(null);
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

		renderWidgets(renderables.values(), evt.getPartialTicks());

		postRender(RenderType.GameOverlayLocated);
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)	{
		if(!shouldRenderStart(RenderType.WorldLocated)) return;

		preRender(RenderType.WorldLocated, event.getPartialTicks());

		Vec3d renderPos = getRenderPosition(event.getPartialTicks());

		GlStateManager.translate(renderPos.x, renderPos.y, renderPos.z);

		GlStateManager.depthMask(true);
		renderWidgets(renderablesWorld.values(), event.getPartialTicks());
		postRender(RenderType.WorldLocated);
	}


	static Vec3d getEntityLocation(Entity entityIn, float partialTicks){
		if(entityIn == null)
			return new Vec3d(0, 0, 0);

		double[] location = getEntityPlayerLocation(entityIn, partialTicks);
		return new Vec3d(location[0], location[1], location[2]);
	}

	Vec3d getRobotLocation(Robot robot, float partialTicks){
		if(robot == null)
			return renderPosition;

		li.cil.oc.common.tileentity.Robot casted = (li.cil.oc.common.tileentity.Robot) robot;
		Vec3d offset = new Vec3d(0, 0, 0);

		if(casted != null && casted.isAnimatingMove()){
			double remaining = (casted.animationTicksLeft() - partialTicks) / (double) casted.animationTicksTotal();
			Vec3d location = new Vec3d(casted.position().x(), casted.position().y(), casted.position().z());
			Vec3d moveFrom = new Vec3d(casted.moveFrom().get()).subtract(location);

			offset = moveFrom.scale(remaining);
		}

		return renderPosition = new Vec3d(robot.xPosition(), robot.yPosition(), robot.zPosition()).add(offset);
	}

	public Vec3d getRenderPosition(float partialTicks){
		switch (hostType){
			case DRONE:
			case TABLET:
				return getEntityLocation(getRenderEntity(), partialTicks).subtract(new Vec3d(0.5, 0, 0.5));

			case ROBOT:
				return getRobotLocation(getRobotEntity(), partialTicks).subtract(new Vec3d(0.5, 0.5, 0.5));

			case MICROCONTROLLER:
			case CASE:
			case TERMINAL:
			default:
				return renderPosition;
		}
	}

	void renderWidgets(Collection<IRenderableWidget> widgets, float partialTicks){
		String uuid = glassesStack.getTagCompound().getString("userUUID");

		long renderConditions = conditions.get();
		Vec3d renderPos = getRenderPosition(partialTicks);
		Vector3f offset = new Vector3f((float) renderPos.x, (float) renderPos.y, (float) renderPos.z);

		for(IRenderableWidget renderable : widgets) {
			if(!renderable.shouldWidgetBeRendered(Minecraft.getMinecraft().player, offset))
				continue;

			if(!renderable.isWidgetOwner(uuid))
				continue;

			if(renderable instanceof EntityTracker3D.RenderEntityTracker)
				renderWidget(renderable, renderConditions, renderPos.scale(-1));
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

		if(!renderWorld && renderEvent.equals(RenderType.WorldLocated))
			return false;

		if(!renderOverlay && renderEvent.equals(RenderType.GameOverlayLocated))
			return false;

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
