package com.bymarcin.openglasses.surface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;

import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

import com.bymarcin.openglasses.utils.PlayerStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.bymarcin.openglasses.utils.nightvision.potionNightvision;

public class ServerSurface {
	public static ServerSurface instance = new ServerSurface();

	public HashMap<EntityPlayer, Location> players = new HashMap<>();
	public HashMap<UUID, PlayerStats> playerStats = new HashMap<>();

	static EventHandler eventHandler;

	int playerIndex = 0;

	public ServerSurface(){
		eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}

	public class EventHandler {
		@SubscribeEvent
		public void tickStart(TickEvent.WorldTickEvent event) {
			int i=0;
			for (EntityPlayer player : players.keySet()) {
				if(i == playerIndex) {
					updatePlayer(player);
					break;
				}
				i++;
			}

			playerIndex++;

			if (playerIndex >= players.size()) playerIndex = 0;
		}

		public void updatePlayer(EntityPlayer player){
			playerStats.get(player.getUniqueID()).updateNightvision(player);
		}
	}


	//subscribePlayer to events when he puts glasses on
	public void subscribePlayer(String playerUUID, Location UUID){
		EntityPlayerMP player = checkUUID(playerUUID);
		if(player == null) return;

		OpenGlassesTerminalTileEntity terminal = UUID.getTerminal();
		if(terminal == null) return;
		if(!terminal.getTerminalUUID().equals(UUID)) return;

		players.put(player, UUID);
		playerStats.put(player.getUniqueID(), new PlayerStats(player));
		sendSync(player, UUID, terminal);

		terminal.onGlassesPutOn(player.getDisplayNameString());
		requestResolutionEvent(player);
	}

	//unsubscribePlayer from events when he puts glasses off
	public void unsubscribePlayer(String playerUUID){
		EntityPlayerMP p = checkUUID(playerUUID);

		if (playerStats.get(p.getUniqueID()).nightVisionActive) {
			p.removePotionEffect(potionNightvision);
		}

		Location l = players.remove(p);
		playerStats.remove(p.getUniqueID());
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

	public void requestResolutionEvent(EntityPlayerMP player){
		NetworkRegistry.packetHandler.sendTo(new TerminalStatusPacket(TerminalStatusPacket.TerminalEvent.ASYNC_SCREEN_SIZES), player);
	}

	public EntityPlayerMP checkUUID(String uuid){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));	
	}

	public EntityPlayerMP checkPlayerName(String name){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name);
	}

}
