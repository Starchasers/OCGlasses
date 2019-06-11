package com.bymarcin.openglasses.surface;

import java.util.*;

import ben_mkiv.commons0815.utils.utilsCommon;
import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.component.face.Text2D;
import ben_mkiv.rendertoolkit.common.widgets.component.world.EntityTracker3D;
import ben_mkiv.rendertoolkit.surface.ClientSurface;

import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.utils.Conditions;

import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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

	private HashMap<UUID, OpenGlassesHostClient> hosts = new HashMap<>();

    public static ClientEventHandler eventHandler;

	public GlassesInstance glasses = new GlassesInstance(ItemStack.EMPTY);

	public static OCClientSurface instance(){
		return (OCClientSurface) instances;
	}

	private static IRenderableWidget noPowerRender = getNoPowerRender();
	private static IRenderableWidget noLinkRender = getNoLinkRender();
	private static IRenderableWidget widgetLimitRender = getWidgetLimitRender();

	public static class GlassesInstance {
		ItemStack stack;
		public Conditions conditions = new Conditions();

		public GlassesInstance(ItemStack glassesStack){
			stack = glassesStack;
			if(!stack.isEmpty())
				conditions.bufferSensors(stack);
		}

		public ItemStack get(){
			return stack;
		}

		public void refreshConditions(){
			if(!stack.isEmpty())
				conditions.getConditionStates(stack, Minecraft.getMinecraft().player);
		}

	}


	public OpenGlassesHostClient getHost(UUID hostUUID){
		if(!hosts.containsKey(hostUUID)){
			NBTTagCompound hostNBT = OpenGlassesItem.getHostFromNBT(hostUUID, glasses.get());
			if(hostNBT == null) {
				hostNBT = new OpenGlassesHostClient.HostClient(hostUUID, Minecraft.getMinecraft().player).writeToNBT(new NBTTagCompound());
				System.out.println("this shouldnt happen!");
				return new OpenGlassesHostClient(hostNBT);
			}

			hosts.put(hostUUID, new OpenGlassesHostClient(hostNBT));
		}
		return hosts.get(hostUUID);
	}

	public Collection<OpenGlassesHostClient> getHosts(){
		return hosts.values();
	}

	public void onLeave(){
		initLocalGlasses(ItemStack.EMPTY);
	}

	public void initLocalGlasses(ItemStack glassesStack){
		glasses = new GlassesInstance(glassesStack.copy());

		if(glassesStack.isEmpty()) {
			hosts.clear();
			return;
		}

		HashMap<UUID, OpenGlassesHostClient> newHosts = new HashMap<>();

		for(NBTTagCompound tag : OpenGlassesItem.getHostsFromNBT(glassesStack)) {
			OpenGlassesHostClient host = new OpenGlassesHostClient(tag);

			if(hosts.containsKey(host.getUniqueId())){
				hosts.get(host.getUniqueId()).data().updateFromNBT(tag);
				newHosts.put(host.getUniqueId(), hosts.get(host.getUniqueId()));
			}
			else {
				newHosts.put(host.getUniqueId(), host);
			}

			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.getUniqueId(), GlassesEventPacket.EventType.REQUEST_WIDGETLIST));
		}

		hosts = newHosts;
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
		if(glasses.get().isEmpty()) return;
		if(hosts.size() == 0) return;

		for(OpenGlassesHostClient host : getHosts())
			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.getUniqueId(), GlassesEventPacket.EventType.GLASSES_SCREEN_SIZE, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaleFactor()));
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

		for(OpenGlassesHostClient host : getHosts()) {
			if(!host.data().renderOverlay)
				continue;

			GlStateManager.pushMatrix();
			renderWidgets(host.getWidgetsOverlay().values(), evt.getPartialTicks(), vec3d000, host);
			GlStateManager.popMatrix();
		}

		postRender(RenderType.GameOverlayLocated);
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)	{
		if(!shouldRenderStart(RenderType.WorldLocated)) return;

		preRender(RenderType.WorldLocated, event.getPartialTicks());
		GlStateManager.depthMask(true);

		for(OpenGlassesHostClient host : getHosts()) {
			if(!host.data().renderWorld)
				continue;

			GlStateManager.pushMatrix();
			Vec3d renderPos = host.getRenderPosition(event.getPartialTicks());

			if(!host.absoluteRenderPosition) {
				GlStateManager.translate(renderPos.x, renderPos.y, renderPos.z);
			}

			renderWidgets(host.getWidgetsWorld().values(), event.getPartialTicks(), renderPos, host);

			GlStateManager.popMatrix();
		}

		postRender(RenderType.WorldLocated);

		//GlStateManager.depthMask(false);
		GlStateManager.enableDepth();
	}


	public static Vec3d getEntityLocation(Entity entityIn, float partialTicks){
		if(entityIn == null)
			return new Vec3d(0, 0, 0);

		double[] location = getEntityPlayerLocation(entityIn, partialTicks);
		return new Vec3d(location[0], location[1], location[2]);
	}



	void renderWidgets(Collection<IRenderableWidget> widgets, float partialTicks, Vec3d renderPos, OpenGlassesHostClient host){
		long renderConditions = glasses.conditions.get();

		Vector3f offset = new Vector3f((float) renderPos.x, (float) renderPos.y, (float) renderPos.z);

		for(IRenderableWidget renderable : widgets) {
			if(host.absoluteRenderPosition) {
				if(!shouldAbsoluteWidgetBeRendered(Minecraft.getMinecraft().player, offset, renderable))
					continue;
			}
			else if(!renderable.shouldWidgetBeRendered(Minecraft.getMinecraft().player, offset))
				continue;

			if(!renderable.isWidgetOwner(host.data().ownerUUID.toString()))
				continue;

			if(renderable instanceof EntityTracker3D.RenderEntityTracker)
				renderWidget(renderable, renderConditions, renderPos.scale(-1));
			else
				renderWidget(renderable, renderConditions);
		}
	}

	public boolean shouldAbsoluteWidgetBeRendered(EntityPlayer player, Vector3f offset, IRenderableWidget renderable) {
		if(!utilsCommon.inRange(Minecraft.getMinecraft().player, new Vec3d(offset.x, offset.y, offset.z), viewDistance))
			return false;

		if(renderable.isLookingAtEnabled()){
			RayTraceResult pos = ClientSurface.getBlockCoordsLookingAt(player);
			if(pos != null && !pos.getBlockPos().equals(new BlockPos(renderable.lookingAtVector())))
				return false;
		}

		return renderable.isVisible();
	}


	public boolean shouldRenderStart(RenderType renderEvent){
		if(getWidgetCount(null, renderEvent) < 1)
			return false;

		if(this.glasses.get().isEmpty())
			return false;

		if(OpenGlassesItem.getEnergyStored(glasses.get()) == 0){
			if(renderEvent.equals(RenderType.GameOverlayLocated) && noPowerRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(noPowerRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		if(getWidgetCount(null, renderEvent) > glasses.get().getTagCompound().getInteger("widgetLimit")){
			if(renderEvent.equals(RenderType.GameOverlayLocated) && widgetLimitRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(widgetLimitRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		if(hosts.size() == 0) {
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


	private static IRenderableWidget getNoPowerRender(){
		Text2D t = new Text2D();
		t.setText(new TextComponentTranslation("openglasses.infotext.noenergy").getUnformattedText());
		t.WidgetModifierList.addColor(1F, 0F, 0F, 0.5F);
		t.WidgetModifierList.addTranslate(5, 5, 0);
		return t.getRenderable();
	}

	private static IRenderableWidget getNoLinkRender(){
		Text2D t = new Text2D();
		t.setText(new TextComponentTranslation("openglasses.infotext.nolink").getUnformattedText());
		t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
		t.WidgetModifierList.addTranslate(5, 5, 0);
		return t.getRenderable();
	}

	private static IRenderableWidget getWidgetLimitRender(){
		Text2D t = new Text2D();
		t.setText(new TextComponentTranslation("openglasses.infotext.widgetlimitexhausted").getUnformattedText());
		t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
		t.WidgetModifierList.addTranslate(5, 5, 0);
		return t.getRenderable();
	}

	@Override
	public void updateWidgets(UUID hostUUID, Set<Map.Entry<Integer, Widget>> widgets){
		if(hostUUID != null)
			instance().getHost(hostUUID).updateWidgets(widgets);
	}

	@Override
	public void removeWidgets(UUID hostUUID, List<Integer> ids){
		if(hostUUID != null)
			getHost(hostUUID).removeWidgets(ids);
	}

	@Override
	public void removeAllWidgets(UUID hostUUID){
		if(hostUUID != null)
			getHost(hostUUID).removeAllWidgets();
	}

	@Override
	public int getWidgetCount(UUID hostUUID, RenderType renderEvent) {
		if(hostUUID != null)
			return getHost(hostUUID).getWidgetCount(renderEvent);
		else {
			int count = 0;
			for(OpenGlassesHostClient host : hosts.values())
				count+=host.getWidgetCount(renderEvent);

			return count;
		}
	}

}
