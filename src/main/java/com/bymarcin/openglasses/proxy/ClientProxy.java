package com.bymarcin.openglasses.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.surface.ClientSurface;

import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void init() {
		ClientEventHandler eh = new ClientEventHandler();
		FMLCommonHandler.instance().bus().register(eh);
	 	MinecraftForge.EVENT_BUS.register(eh);
		MinecraftForge.EVENT_BUS.register(ClientSurface.instances);
	}

	@Override
	public World getWorld(int dimensionId) {
		if (getCurrentClientDimension() != dimensionId) {
			return null;
		} else
			return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public int getCurrentClientDimension() {
		return Minecraft.getMinecraft().theWorld.provider.dimensionId;
	}
}
