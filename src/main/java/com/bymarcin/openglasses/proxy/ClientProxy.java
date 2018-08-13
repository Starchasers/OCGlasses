package com.bymarcin.openglasses.proxy;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.manual.ManualPathProvider;
import com.bymarcin.openglasses.render.BaublesRenderLayer;
import com.bymarcin.openglasses.surface.ClientSurface;

import com.bymarcin.openglasses.utils.PlayerStats;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;
import java.util.UUID;

public class ClientProxy extends CommonProxy {	
	@Override
	public void registermodel(Item item, int meta){
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@Override
	public void init() {
		ClientSurface.eventHandler = new ClientEventHandler();
		MinecraftForge.EVENT_BUS.register(ClientSurface.eventHandler);
		MinecraftForge.EVENT_BUS.register(ClientSurface.instances);  //register client events
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
	public PlayerStats getPlayerStats(UUID uuid) {
		PlayerStats s = new PlayerStats(getPlayer(""));
		s.setScreen(ClientSurface.resolution.getScaledWidth(), ClientSurface.resolution.getScaledHeight(), (double) ClientSurface.resolution.getScaleFactor());
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

}
