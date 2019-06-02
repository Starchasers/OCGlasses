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
import java.util.UUID;

import static com.bymarcin.openglasses.item.upgrades.UpgradeNightvision.potionNightvision;

public class OCServerSurface extends ben_mkiv.rendertoolkit.surface.ServerSurface {
	static {
		instances = new OCServerSurface();
	}

	static EventHandler eventHandler;

	public static HashMap<UUID, IOpenGlassesHost> components = new HashMap<>();

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
	public void subscribePlayer(String playerUUID) {
		subscribePlayer(checkUUID(playerUUID));
	}

	public void subscribePlayer(EntityPlayerMP player) {
		if (player == null) return;

		for(NBTTagCompound nbt : OpenGlassesItem.getHostsFromNBT(OpenGlasses.getGlassesStack(player))){
			UUID uuid = nbt.getUniqueId("host");
			if (!uuid.equals(players.get(player)))
				subscribePlayer(player, uuid);
		}
	}

	public void subscribePlayer(EntityPlayerMP player, UUID hostUUID){
		removePlayerSubscription(player);

		players.put(player, hostUUID);
		PlayerStatsOC stats = new PlayerStatsOC(player);
		stats.conditions.bufferSensors(OpenGlasses.getGlassesStack(player));
		playerStats.put(player.getUniqueID(), stats);

		IOpenGlassesHost host = getHost(players.get(player));
		if (host != null){
			host.sync(player);
			host.getComponent().onGlassesPutOn(player.getDisplayNameString());
		}

		requestResolutionEvent(player);
	}

	//unsubscribePlayer from events when he puts glasses off
	public void unsubscribePlayer(String playerUUID){
		EntityPlayerMP player = checkUUID(playerUUID);

		if (playerStats.get(player.getUniqueID()) != null && ((PlayerStatsOC) playerStats.get(player.getUniqueID())).nightVisionActive) {
			player.removePotionEffect(potionNightvision);
		}

		IOpenGlassesHost host = getHost(players.get(player));
		if (host != null){
			host.sync(player);
			host.getComponent().onGlassesPutOff(player.getDisplayNameString());
		}

		removePlayerSubscription(player);
	}

	public void removePlayerSubscription(EntityPlayer player){
		playerStats.remove(player.getUniqueID());
		players.remove(player);
	}

}

