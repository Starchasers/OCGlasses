package com.bymarcin.openglasses.proxy;

import net.minecraft.item.Item;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy {
	
	public void registermodel(Item item, int meta){}
	
	public void init() {}
	
	public void postInit() {}

	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
	}

	public int getCurrentClientDimension() {
		return -9001;
	}
}
