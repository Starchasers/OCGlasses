package com.bymarcin.openglasses.proxy;

import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.manual.ManualPathProvider;
import com.bymarcin.openglasses.render.BaublesRenderLayer;
import com.bymarcin.openglasses.surface.ClientSurface;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

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
		if(Loader.isModLoaded("Baubles")){
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
	public int getCurrentClientDimension() {
		return Minecraft.getMinecraft().world.provider.getDimension();
	}
}
