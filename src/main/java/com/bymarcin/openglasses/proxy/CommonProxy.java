package com.bymarcin.openglasses.proxy;

import com.bymarcin.openglasses.surface.OCServerSurface;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class CommonProxy implements IThreadListener {
	
	public void registermodel(Item item, int meta){}
	
	public void init() {}
	
	public void postInit() {}

	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
	}

	public EntityPlayer getPlayer(String username){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username);
	}

	public PlayerStatsOC getPlayerStats(UUID uuid) {
		return (PlayerStatsOC) OCServerSurface.instances.playerStats.get(uuid);
	}

	public int getCurrentClientDimension() {
		return -9001;
	}

	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnableToSchedule);
	}

	@Override
	public boolean isCallingFromMinecraftThread() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().isCallingFromMinecraftThread();
	}
}
