package com.bymarcin.openglasses.surface;

import java.util.*;

import ben_mkiv.commons0815.utils.utilsCommon;
import ben_mkiv.rendertoolkit.client.thermalvision.ShaderHelper;
import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.component.world.EntityTracker3D;
import ben_mkiv.rendertoolkit.surface.ClientSurface;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeThermalVision;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;

import com.bymarcin.openglasses.utils.GlassesInstance;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;

import static ben_mkiv.rendertoolkit.client.thermalvision.ShaderHelper.loadOutlineShader;
import static com.bymarcin.openglasses.surface.StaticWidgets.*;

@SideOnly(Side.CLIENT)
public class OCClientSurface extends ClientSurface {
    public static boolean thermalActive = false;

    static{
		instances = new OCClientSurface();
	}

	public static GlassesInstance glasses = new GlassesInstance(ItemStack.EMPTY);

	public static OCClientSurface instance(){
		return (OCClientSurface) instances;
	}

	private static HashMap<UUID, OpenGlassesHostClient> hosts = new HashMap<>();

	public OpenGlassesHostClient getHost(UUID hostUUID){
		if(!hosts.containsKey(hostUUID)){
			hosts.put(hostUUID, new OpenGlassesHostClient(hostUUID));
		}
		return hosts.get(hostUUID);
	}

	public Collection<OpenGlassesHostClient> getHosts(){
		return hosts.values();
	}

	@Override
	public Vec3d getRenderResolution(UUID hostUUID){
		return hostUUID != null ? glasses.getHost(hostUUID).renderResolution : super.getRenderResolution(null);
	}

	@Override
	public void setRenderResolution(Vec3d resolution, UUID instanceUUID){
		if(instanceUUID != null)
			glasses.getHost(instanceUUID).renderResolution = resolution;
		else
			super.setRenderResolution(resolution, instanceUUID);
	}

	public void onLeave(){
		hosts.clear();
		initLocalGlasses(ItemStack.EMPTY);
	}

	public void initLocalGlasses(ItemStack glassesStack){
		glasses = new GlassesInstance(glassesStack.copy());
	}

	public void sendResolution(){
		if(glasses.get().isEmpty()) return;

		for(UUID hostUUID : glasses.getHosts().keySet())
			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(hostUUID, GlassesEventPacket.EventType.GLASSES_SCREEN_SIZE, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaleFactor()));
	}

	public void renderOverlay(float partialTicks) {
		if(!shouldRenderStart(RenderType.GameOverlayLocated)) return;

		preRender(RenderType.GameOverlayLocated, partialTicks);

		GlStateManager.depthMask(false);

		for(GlassesInstance.HostClient host : glasses.getHosts().values()) {
			if(!host.renderOverlay)
				continue;

			GlStateManager.pushMatrix();

			Vec3d renderResolution = getRenderResolution(host.uuid);

			if(!renderResolution.equals(vec3d000))
				GlStateManager.scale(OCClientSurface.resolution.getScaledWidth() / renderResolution.x, OCClientSurface.resolution.getScaledHeight() / renderResolution.y, 1);

			renderWidgets(host.getHost().getWidgetsOverlay().values(), partialTicks, vec3d000, host.getHost());
			GlStateManager.popMatrix();
		}

		postRender(RenderType.GameOverlayLocated);
	}

	public void renderWorld(float partialTicks)	{

		updateThermalVision();

		if(!shouldRenderStart(RenderType.WorldLocated)) return;

		preRender(RenderType.WorldLocated, partialTicks);
		GlStateManager.depthMask(true);

		for(GlassesInstance.HostClient host : glasses.getHosts().values()) {
			if(!host.renderWorld)
				continue;

			if(host.getHost().absoluteRenderPosition && !glasses.getConditions().hasNavigation)
				continue;

			GlStateManager.pushMatrix();
			Vec3d renderPos = host.getHost().getRenderPosition(partialTicks);

			if(!host.getHost().absoluteRenderPosition) {
				GlStateManager.translate(renderPos.x, renderPos.y, renderPos.z);
			}

			renderWidgets(host.getHost().getWidgetsWorld().values(), partialTicks, renderPos, host.getHost());

			GlStateManager.popMatrix();
		}

		postRender(RenderType.WorldLocated);
		GlStateManager.enableDepth();
	}

	private void updateThermalVision(){
		if(updateTicks % 10 != 0 || glasses.get().isEmpty())
			return;

		if(glasses.thermalVisionActive != thermalActive) {
			if (glasses.thermalVisionActive)
				loadOutlineShader(ShaderHelper.ShaderType.THERMAL_VISION);
			else
				loadOutlineShader(ShaderHelper.ShaderType.VANILLA_GLOW);

			thermalActive = glasses.thermalVisionActive;
		}
	}

	public static Vec3d getEntityLocation(Entity entityIn, float partialTicks){
		if(entityIn == null)
			return new Vec3d(0, 0, 0);

		double[] location = getEntityPlayerLocation(entityIn, partialTicks);
		return new Vec3d(location[0], location[1], location[2]);
	}

	private void renderWidgets(Collection<IRenderableWidget> widgets, float partialTicks, Vec3d renderPos, OpenGlassesHostClient host){
		long renderConditions = glasses.getConditions().get();

		Vector3f offset = new Vector3f((float) renderPos.x, (float) renderPos.y, (float) renderPos.z);

		for(IRenderableWidget renderable : widgets) {
			if(host.absoluteRenderPosition) {
				if(!shouldAbsoluteWidgetBeRendered(Minecraft.getMinecraft().player, offset, renderable))
					continue;
			}
			else if(!renderable.shouldWidgetBeRendered(Minecraft.getMinecraft().player, offset))
				continue;

			if(!renderable.isWidgetOwner(glasses.getHost(host.hostUUID).ownerUUID.toString()))
				continue;

			if(renderable instanceof EntityTracker3D.RenderEntityTracker)
				renderWidget(renderable, renderConditions, renderPos.scale(-1));
			else
				renderWidget(renderable, renderConditions);
		}
	}

	private boolean shouldAbsoluteWidgetBeRendered(EntityPlayer player, Vector3f offset, IRenderableWidget renderable) {
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

		ItemStack renderGlasses = glasses.get().copy();

		if(renderGlasses.isEmpty())
			return false;

		if(OpenGlassesItem.getEnergyStored(renderGlasses) == 0){
			if(renderEvent.equals(RenderType.GameOverlayLocated) && noPowerRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(noPowerRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		if(getWidgetCount(null, renderEvent) > renderGlasses.getTagCompound().getInteger("widgetLimit")){
			if(renderEvent.equals(RenderType.GameOverlayLocated) && widgetLimitRender != null) {
				preRender(renderEvent, ~0);
				GlStateManager.depthMask(false);
				renderWidget(widgetLimitRender, ~0);
				postRender(RenderType.GameOverlayLocated);
			}
			return false;
		}

		if(glasses.getHosts().size() == 0) {
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
			hosts.get(hostUUID).removeAllWidgets();
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

	public void equipmentChanged(ItemStack newStack){
		if(!OpenGlasses.isGlassesStack(newStack) && !glasses.get().isEmpty()){
			// avoid unsubscribing if the player wears a helmet, while he has glasses on the baubles slot
			ItemStack glassesStackBaubles = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);
			if(!glassesStackBaubles.isEmpty())
				newStack = glassesStackBaubles;
		}

		if(newStack.isEmpty()){
			if(!glasses.get().isEmpty())
				initLocalGlasses(ItemStack.EMPTY);
		}
		else if(OpenGlasses.isGlassesStack(newStack)){
			if(!GlassesNBT.getUniqueId(newStack).equals(glasses.getUniqueId()))
				initLocalGlasses(newStack);
		}
	}

	private int updateTicks = 10;
	public void update(EntityPlayer player){
		glasses.refreshConditions();

		if(updateTicks % 20 == 0) {
			ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

			if (!glasses.get().equals(glassesStack))
				equipmentChanged(OpenGlasses.getGlassesStack(player));

			updateTicks = 0;
		}

		if(!glasses.get().isEmpty())
			for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
				upgrade.updateClient(player, glasses.get());

		updateTicks++;
	}

}
