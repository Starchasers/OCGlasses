package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.OpenGlasses;

import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesHostsNBT;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static com.bymarcin.openglasses.item.upgrades.UpgradeNightvision.potionNightvision;
import static com.bymarcin.openglasses.network.packet.TerminalStatusPacket.TerminalEvent.*;

public class OCServerSurface extends ben_mkiv.rendertoolkit.surface.ServerSurface {
	static {
		instances = new OCServerSurface();
	}

	// player (UUID) to glasses (UUID) mapping
	public static HashMap<UUID, UUID> playerGlasses = new HashMap<>();

	// hosts (UUID) to glassesComponent(terminal, card, upgrade) (IOpenGlassesHost) mapping
	public static HashMap<UUID, OpenGlassesHostComponent> components = new HashMap<>();

	public static OCServerSurface instance(){
		return (OCServerSurface) instances;
	}

	// flush static caches when server gets stopped, this is required so that no data is
	// carried over from one singleplayer world to another
	public static void onServerStopped(){
		components.clear();
		playerGlasses.clear();
		instance().players.clear();
		instance().playerStats.clear();
	}

	public static void removeHost(UUID hostUUID){
		if(components.containsKey(hostUUID)) {
			components.get(hostUUID).remove();
			components.remove(hostUUID);
		}
	}

	public static void addHost(OpenGlassesHostComponent component){
		if(!components.containsKey(component.getUUID())) {
			components.put(component.getUUID(), component);
			for(Map.Entry<UUID, HashSet<UUID>> entry : instance().players.entrySet()){
				component.sync(instance().checkUUID(entry.getKey()));
			}
		}
	}

	public static OpenGlassesHostComponent getHost(UUID hostUUID){
		return components.get(hostUUID);
	}

	public boolean isSubscribedTo(UUID playerUUID, UUID hostUUID){
		return players.containsKey(playerUUID) && players.get(playerUUID).contains(hostUUID);
	}

	public void subscribePlayer(EntityPlayerMP player, UUID hostUUID) {
		NetworkRegistry.packetHandler.sendTo(new TerminalStatusPacket(GLASSES_EQUIPPED, UUID.randomUUID()), player);

		playerGlasses.remove(player.getUniqueID());

		ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

		if(!glassesStack.isEmpty()) {
			UUID glassesUUID = GlassesNBT.getUniqueId(glassesStack);
			playerGlasses.put(player.getUniqueID(), glassesUUID);
			getStats(player).conditions.bufferSensors(OpenGlasses.getGlassesStack(player));
		}

		if (isSubscribedTo(player.getUniqueID(), hostUUID))
			return;

		if (!players.containsKey(player.getUniqueID()))
			players.put(player.getUniqueID(), new HashSet<>());

		if(hostUUID != null) {
			players.get(player.getUniqueID()).add(hostUUID);

			if (!glassesStack.isEmpty()) {
				OpenGlassesHostComponent host = getHost(hostUUID);
				if (host != null)
					host.onGlassesPutOn(player);

				requestResolutionEvent(player, hostUUID);
			}
		}
	}

	//unsubscribePlayer from events when he puts glasses off
	public void unsubscribePlayer(EntityPlayerMP player){
		NetworkRegistry.packetHandler.sendTo(new TerminalStatusPacket(GLASSES_UNEQUIPPED, UUID.randomUUID()), player);

		if (getStats(player).nightVisionActive) {
			player.removePotionEffect(potionNightvision);
		}

		if(!players.containsKey(player.getUniqueID()))
			return;

		for(UUID hostUUID : players.get(player.getUniqueID())) {
			OpenGlassesHostComponent host = getHost(hostUUID);
			if (host != null) {
				host.onGlassesPutOff(player);
			}
		}

		players.remove(player.getUniqueID());
		playerGlasses.remove(player.getUniqueID());
	}

	public static void equipmentChanged(EntityPlayerMP player, ItemStack newStack){
		UUID oldUUID = playerGlasses.getOrDefault(player.getUniqueID(), null);
		UUID newUUID = null;

		if(!OpenGlasses.isGlassesStack(newStack) && playerGlasses.containsKey(player.getUniqueID())) {
			// avoid unsubscribing if the player wears a helmet, while he has glasses on the baubles slot
			ItemStack glassesStackBaubles = OpenGlasses.getGlassesStack(player);
			if(!glassesStackBaubles.isEmpty())
				newStack = glassesStackBaubles;
		}

		if(OpenGlasses.isGlassesStack(newStack)){
			newUUID = GlassesNBT.getUniqueId(newStack);

			if (newUUID.equals(oldUUID))
				return;
		}

		if (oldUUID != null) {
			// preInit with empty stack to force unequipped signal and to flush the caches
			OCServerSurface.instance().unsubscribePlayer(player);
		}

		if (newUUID != null) {
			instance().subscribePlayer(player, null);

			for(NBTTagCompound nbt : OpenGlassesHostsNBT.getHostsFromNBT(newStack))
				instance().subscribePlayer(player, nbt.getUniqueId("host"));
		}
	}

	public static PlayerStatsOC getStats(EntityPlayerMP player){
		if(!OCServerSurface.instance().playerStats.containsKey(player.getUniqueID()))
			OCServerSurface.instance().playerStats.put(player.getUniqueID(), new PlayerStatsOC(player));

		return (PlayerStatsOC) OCServerSurface.instance().playerStats.get(player.getUniqueID());
	}

	public void requestResolutionEvent(EntityPlayerMP player, UUID instanceUUID){
        if(player != null)
            NetworkRegistry.packetHandler.sendTo(new TerminalStatusPacket(ASYNC_SCREEN_SIZES, instanceUUID), player);
    }

}

