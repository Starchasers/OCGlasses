package com.bymarcin.ocglasses.surface;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import com.bymarcin.ocglasses.network.NetworkRegistry;
import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;
import com.bymarcin.ocglasses.utils.Vec3;

public class ServerSurface {
	public static ServerSurface instance  = new ServerSurface();
	
	HashMap<EntityPlayer,Vec3> players = new HashMap<EntityPlayer, Vec3>();
	
	public void subscribePlayer(String playerUUID, Vec3 UUID){
		EntityPlayerMP player = checkUUID(playerUUID);
		if(player!=null){
			players.put(player, UUID);
			sendSync(player, UUID);
		}
	}
	
	public void unsubscribePlayer(String playerUUID){
		players.remove(playerUUID);
	}
	
	public void sendSync(EntityPlayer p,Vec3 coords){
		TileEntity t = p.worldObj.getTileEntity(coords.x, coords.y, coords.z);
		if(t instanceof OCGlassesTerminalTileEntity){
			WidgetUpdatePacket packet = new WidgetUpdatePacket( ((OCGlassesTerminalTileEntity)t).widgetList, WidgetUpdatePacket.Action.AddWigets );
			NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) p);
		}
	}
	
	public void sendToUUID(WidgetUpdatePacket packet, Vec3 UUID){
		for(Entry<EntityPlayer, Vec3> e :players.entrySet()){
			if(e.getValue().equals(UUID)){
				NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) e.getKey());
			}
		}
	}
	
	private EntityPlayerMP checkUUID(String uuid){
		for(Object p : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			if(((EntityPlayerMP)p).getUniqueID().toString().equals(uuid))
				return (EntityPlayerMP) p;
		return null;	
	}

}
