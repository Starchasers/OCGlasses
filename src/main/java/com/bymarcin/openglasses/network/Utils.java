package com.bymarcin.openglasses.network;

import com.bymarcin.openglasses.OpenGlasses;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class Utils {
	public static TileEntity getTileEntity(int dimensionId, BlockPos pos) {
		World world = OpenGlasses.proxy.getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(pos);
	}

	public static TileEntity getTileEntityServer(int dimensionId, BlockPos pos) {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(pos);
	}
}
