package com.bymarcin.openglasses.surface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;

import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class ServerSurface {
	public static ServerSurface instance  = new ServerSurface();
	
	public HashMap<EntityPlayer,Location> players = new HashMap<EntityPlayer, Location>();
	
	public void subscribePlayer(String playerUUID, Location UUID){
		EntityPlayerMP player = checkUUID(playerUUID);
		if(player == null) return;

		OpenGlassesTerminalTileEntity terminal = UUID.getTerminal();
		if(terminal == null) return;
		if(!terminal.getTerminalUUID().equals(UUID)) return;

		players.put(player, UUID);
		sendSync(player, UUID, terminal);
		terminal.onGlassesPutOn(player.getDisplayNameString());
	}
	
	public void unsubscribePlayer(String playerUUID){
		EntityPlayerMP p = checkUUID(playerUUID);

		Location l = players.remove( p );
		if(l == null) return;

		OpenGlassesTerminalTileEntity terminal = l.getTerminal();
		if(terminal == null) return;

		terminal.onGlassesPutOff(p.getDisplayNameString());
	}
	
	public String[] getActivePlayers(Location l){
		LinkedList<String> players = new LinkedList<String>();
		for(Entry<EntityPlayer, Location> p: this.players.entrySet()){
			if(p.getValue().equals(l)){
				players.add(p.getKey().getGameProfile().getName());
			}
		}
		return players.toArray(new String[]{});
	}
	
	public void sendSync(EntityPlayer p,Location coords, OpenGlassesTerminalTileEntity t){
			WidgetUpdatePacket packet = new WidgetUpdatePacket(t.widgetList);
			NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) p);
	}
	
	public void sendToUUID(WidgetUpdatePacket packet, Location UUID){
		for(Entry<EntityPlayer, Location> e :players.entrySet()){
			if(e.getValue().equals(UUID)){
				NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) e.getKey());
			}
		}
	}

	public EntityPlayerMP checkUUID(String uuid){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));	
	}

}
