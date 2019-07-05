package com.bymarcin.openglasses.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy implements IThreadListener {
	public void init() {}
	
	public void postInit() {}

	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
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
