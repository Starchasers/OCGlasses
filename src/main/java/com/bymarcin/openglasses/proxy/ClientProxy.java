package com.bymarcin.openglasses.proxy;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.minecraft.client.ClientEventHandler;
import com.bymarcin.openglasses.event.minecraft.client.ClientKeyboardEvents;
import com.bymarcin.openglasses.event.minecraft.client.ClientRenderEvents;
import com.bymarcin.openglasses.event.minecraft.client.ClientWorldInteractionEvents;
import com.bymarcin.openglasses.manual.ManualPathProvider;
import com.bymarcin.openglasses.render.BaublesRenderLayer;

import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void registermodel(Item item, int meta){
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@Override
	public void init() {
		// 'dynamic' because it has to initialize the keybinds
		MinecraftForge.EVENT_BUS.register(new ClientKeyboardEvents());

		// static event classes
		MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
		MinecraftForge.EVENT_BUS.register(ClientRenderEvents.class);
		MinecraftForge.EVENT_BUS.register(ClientWorldInteractionEvents.class);
	}
	
	@Override
	public void postInit() {
		if(OpenGlasses.baubles){
			Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
			RenderPlayer render;
			render = skinMap.get("default");
			render.addLayer(new BaublesRenderLayer());
			render = skinMap.get("slim");
			render.addLayer(new BaublesRenderLayer());
		}

		ManualPathProvider.initialize();
	}

	@Override
	public World getWorld(int dimensionId) {
		if (getCurrentClientDimension() != dimensionId) {
			return null;
		} else
			return Minecraft.getMinecraft().world;
	}

	@Override
	public EntityPlayer getPlayer(String username) {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public PlayerStatsOC getPlayerStats(UUID uuid) {
		PlayerStatsOC s = new PlayerStatsOC(getPlayer(""));
		s.setScreen(OCClientSurface.resolution.getScaledWidth(), OCClientSurface.resolution.getScaledHeight(), (double) OCClientSurface.resolution.getScaleFactor());
		s.conditions = (OCClientSurface.instance()).glasses.conditions;
		return s;
	}

	@Override
	public int getCurrentClientDimension() {
		return Minecraft.getMinecraft().world.provider.getDimension();
	}


	@Override
	public boolean isCallingFromMinecraftThread() {
		return Minecraft.getMinecraft().isCallingFromMinecraftThread();
	}

	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
		return Minecraft.getMinecraft().addScheduledTask(runnable);
	}

	@Override
	public Entity getEntity(UUID uuid){
		for(Entity entity : Minecraft.getMinecraft().world.getLoadedEntityList())
			if(entity.getUniqueID().equals(uuid))
				return entity;

		return null;
	}

	public EntityPlayer getPlayer(UUID uuid){
		return Minecraft.getMinecraft().world.getPlayerEntityByUUID(uuid);
	}

}
