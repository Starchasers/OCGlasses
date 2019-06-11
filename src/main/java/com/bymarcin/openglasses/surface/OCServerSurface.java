package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.OpenGlasses;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static com.bymarcin.openglasses.item.upgrades.UpgradeNightvision.potionNightvision;

public class OCServerSurface extends ben_mkiv.rendertoolkit.surface.ServerSurface {
	static {
		instances = new OCServerSurface();
	}

	static EventHandler eventHandler;

	public static HashMap<UUID, IOpenGlassesHost> components = new HashMap<>();

	public static void removeHost(UUID hostUUID){
		if(components.containsKey(hostUUID)) {
			components.get(hostUUID).remove();
			components.remove(hostUUID);
		}
	}

	public static void addHost(IOpenGlassesHost host){
		if(!components.containsKey(host.getUUID())) {
			components.put(host.getUUID(), host);
			for(Map.Entry<UUID, HashSet<UUID>> entry : instance().players.entrySet()){
				host.sync(instance().checkUUID(entry.getKey()));
			}
		}
	}

	public static void onServerStopped(){
		components.clear();
		instance().players.clear();
		instance().playerStats.clear();
	}


	int playerIndex = 0;

	public OCServerSurface(){
		eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}

	public static OCServerSurface instance(){
		return (OCServerSurface) instances;
	}

	public class EventHandler {
		@SubscribeEvent
		public void tickStart(TickEvent.WorldTickEvent event) {
			int i=0;
			for (UUID player : players.keySet()) {
				if(i == playerIndex) {
					updatePlayer(checkUUID(player));
					break;
				}
				i++;
			}

			playerIndex++;

			if (playerIndex >= players.size()) playerIndex = 0;
		}

		void updatePlayer(EntityPlayer player){
			ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

			if(OpenGlasses.isGlassesStack(glassesStack))
				for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
					upgrade.update(player, glassesStack);
		}
	}

	public static IOpenGlassesHost getHost(UUID hostUUID){
		return components.get(hostUUID);
	}


	//subscribePlayer to events when he puts glasses on
	public void subscribePlayer(UUID playerUUID) {
		subscribePlayer(checkUUID(playerUUID));
	}

	public void subscribePlayer(EntityPlayerMP player) {
		if (player == null) return;

		for(NBTTagCompound nbt : OpenGlassesItem.getHostsFromNBT(OpenGlasses.getGlassesStack(player)))
			subscribePlayer(player, nbt.getUniqueId("host"));
	}

	boolean isSubscribedTo(UUID playerUUID, UUID hostUUID){
		return players.containsKey(playerUUID) && players.get(playerUUID).contains(hostUUID);
	}

	public void subscribePlayer(EntityPlayerMP player, UUID hostUUID) {
		if (isSubscribedTo(player.getUniqueID(), hostUUID))
			return;

		if (!players.containsKey(player.getUniqueID()))
			players.put(player.getUniqueID(), new HashSet<>());

		players.get(player.getUniqueID()).add(hostUUID);

		if (!playerStats.containsKey(player.getUniqueID())){
			PlayerStatsOC stats = new PlayerStatsOC(player);
			stats.conditions.bufferSensors(OpenGlasses.getGlassesStack(player));
			playerStats.put(player.getUniqueID(), stats);
		}

		IOpenGlassesHost host = getHost(hostUUID);
		if(host != null)
			host.getComponent().onGlassesPutOn(player);

		requestResolutionEvent(player);
	}

	//unsubscribePlayer from events when he puts glasses off
	public void unsubscribePlayer(UUID playerUUID){
		EntityPlayerMP player = checkUUID(playerUUID);

		if(!players.containsKey(playerUUID))
			return;

		if (playerStats.get(player.getUniqueID()) != null && ((PlayerStatsOC) playerStats.get(player.getUniqueID())).nightVisionActive) {
			player.removePotionEffect(potionNightvision);
		}

		for(UUID hostUUID : players.get(playerUUID)) {
			IOpenGlassesHost host = getHost(hostUUID);
			if (host != null)
				host.getComponent().onGlassesPutOff(player);

		}

		removePlayerSubscription(player);
	}

	public void removePlayerSubscription(EntityPlayer player){
		playerStats.remove(player.getUniqueID());
		players.remove(player.getUniqueID());
	}

}

