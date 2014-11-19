package com.bymarcin.ocglasses.surface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bymarcin.ocglasses.network.NetworkRegistry;
import com.bymarcin.ocglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;
import com.bymarcin.ocglasses.utils.Location;

public class ServerSurface {
	public static ServerSurface instance  = new ServerSurface();
	
	HashMap<EntityPlayer,Location> players = new HashMap<EntityPlayer, Location>();
	
	public void subscribePlayer(String playerUUID, Location UUID){
		EntityPlayerMP player = checkUUID(playerUUID);
		if(player!=null){
			players.put(player, UUID);
			sendSync(player, UUID);
			OCGlassesTerminalTileEntity terminal = UUID.getTerminal();
			if(terminal != null){
				terminal.onGlassesPutOn(player.getDisplayName());
			}
		}
	}
	
	public void unsubscribePlayer(String playerUUID){
		EntityPlayerMP p = checkUUID(playerUUID);
		Location l = players.remove( p );
		if(l!=null){
			OCGlassesTerminalTileEntity terminal = l.getTerminal();
			if(terminal!=null){
				terminal.onGlassesPutOff(p.getDisplayName());
			}
		}
	}
	
	public String[] getActivePlayers(Location l){
		LinkedList<String> players = new LinkedList<String>();
		for(Entry<EntityPlayer, Location> p: this.players.entrySet()){
			if(p.getValue().equals(l)){
				players.add(p.getKey().getDisplayName());
			}
		}
		return players.toArray(new String[]{});
	}
	
	public void sendSync(EntityPlayer p,Location coords){
		World w  = MinecraftServer.getServer().worldServerForDimension(coords.dimID);
		if(w==null) return;
		TileEntity t = w.getTileEntity(coords.x, coords.y, coords.z);
		if(t instanceof OCGlassesTerminalTileEntity){
			WidgetUpdatePacket packet = new WidgetUpdatePacket( ((OCGlassesTerminalTileEntity)t).widgetList);
			NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) p);
		}
	}
	
	public void sendToUUID(WidgetUpdatePacket packet, Location UUID){
		for(Entry<EntityPlayer, Location> e :players.entrySet()){
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
