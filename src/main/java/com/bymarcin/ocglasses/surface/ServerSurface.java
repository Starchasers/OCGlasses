package com.bymarcin.ocglasses.surface;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.bymarcin.ocglasses.utils.Vec3;

public class ServerSurface {
	HashMap<EntityPlayer,Vec3> players;
	
	
	public void subscribePlayer(String playerUUID, Vec3 UUID){
		EntityPlayerMP player = checkUUID(playerUUID);
		if(player!=null){
			players.put(player, UUID);
		}
	}
	
	public void unsubscribePlayer(String playerUUID){
		players.remove(playerUUID);
	}
	
	private EntityPlayerMP checkUUID(String uuid){
		for(Object p : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			if(((EntityPlayerMP)p).getUniqueID().toString().equals(uuid))
				return (EntityPlayerMP) p;
		return null;	
	}
}
