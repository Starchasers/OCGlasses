package com.bymarcin.openglasses.proxy;

import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.utils.PlayerStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class CommonProxy {
	
	public void registermodel(Item item, int meta){}
	
	public void init() {}
	
	public void postInit() {}

	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
	}

	public EntityPlayer getPlayer(String username){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username);
	}

	public PlayerStats getPlayerStats(UUID uuid) {
		return ServerSurface.instance.playerStats.get(uuid);
	}

	public int getCurrentClientDimension() {
		return -9001;
	}
}
