package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

import com.bymarcin.openglasses.utils.PlayerStatsOC;
import com.bymarcin.openglasses.utils.TerminalLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.bymarcin.openglasses.utils.nightvision.potionNightvision;

public class OCServerSurface extends ben_mkiv.rendertoolkit.surface.ServerSurface {
	public static OCServerSurface instance = new OCServerSurface();


	static EventHandler eventHandler;

	int playerIndex = 0;

	public OCServerSurface(){
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
			PlayerStatsOC stats = (PlayerStatsOC) playerStats.get(player.getUniqueID());
			if(stats != null) stats.updateNightvision(player);
		}
	}


	//subscribePlayer to events when he puts glasses on
	public void subscribePlayer(String playerUUID, TerminalLocation uuid){
		EntityPlayerMP player = checkUUID(playerUUID);
		if(player == null) return;

		OpenGlassesTerminalTileEntity terminal = uuid.getTerminal();
		if(terminal == null) return;
		if(!terminal.getTerminalUUID().equals(uuid)) return;

		players.put(player, uuid);
		PlayerStatsOC stats = new PlayerStatsOC(player);
		stats.conditions.bufferSensors(OpenGlasses.getGlassesStack(player));
		playerStats.put(player.getUniqueID(), stats);
		sendSync(player, uuid.getTerminal().widgetList);

		terminal.onGlassesPutOn(player.getDisplayNameString());
		requestResolutionEvent(player);
	}

	//unsubscribePlayer from events when he puts glasses off
	public void unsubscribePlayer(String playerUUID){
		EntityPlayerMP p = checkUUID(playerUUID);

		if (((PlayerStatsOC) playerStats.get(p.getUniqueID())).nightVisionActive) {
			p.removePotionEffect(potionNightvision);
		}

		TerminalLocation l = (TerminalLocation) players.remove(p);
		playerStats.remove(p.getUniqueID());
		if(l == null) return;

		OpenGlassesTerminalTileEntity terminal = l.getTerminal();
		if(terminal == null) return;

		terminal.onGlassesPutOff(p.getDisplayNameString());
	}
	


}
