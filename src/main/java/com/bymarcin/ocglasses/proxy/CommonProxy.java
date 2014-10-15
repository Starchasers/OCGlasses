package com.bymarcin.ocglasses.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommonProxy {

	public void init() {

	}

	public World getWorld(int dimensionId) {
		return MinecraftServer.getServer().worldServerForDimension(dimensionId);
	}

	public int getCurrentClientDimension() {
		return -9001;
	}
}
