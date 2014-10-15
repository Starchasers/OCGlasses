package com.bymarcin.ocglasses.network;

import com.bymarcin.ocglasses.OCGlasses;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Utils {
	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		World world = OCGlasses.proxy.getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(x, y, z);
	}

	public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z) {
		World world = MinecraftServer.getServer().worldServerForDimension(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(x, y, z);
	}
}
