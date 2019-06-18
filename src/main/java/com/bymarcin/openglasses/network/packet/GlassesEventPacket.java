package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesHostsNBT;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesNotificationsNBT;
import com.bymarcin.openglasses.item.upgrades.UpgradeGeolyzer;
import com.bymarcin.openglasses.item.upgrades.UpgradeNightvision;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCServerSurface;


public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage>{
	public enum EventType{
		TOGGLE_NIGHTVISION,
		ACTIVATE_OVERLAY, DEACTIVATE_OVERLAY,
		INTERACT_WORLD_RIGHT, INTERACT_WORLD_LEFT, INTERACT_WORLD_BLOCK_RIGHT, INTERACT_WORLD_BLOCK_LEFT,
		INTERACT_OVERLAY,
		GLASSES_SCREEN_SIZE,
		ACCEPT_LINK, DENY_LINK, CLEAR_LINK,
		ENABLE_NOTIFICATIONS, DISABLE_NOTIFICATIONS,
		ENABLE_WORLD_RENDER, DISABLE_WORLD_RENDER,
		ENABLE_OVERLAY_RENDER, DISABLE_OVERLAY_RENDER,
		DISABLE_WORLD_EVENTS, ENABLE_WORLD_EVENTS,
		DISABLE_OVERLAY_EVENTS, ENABLE_OVERLAY_EVENTS
	}

	EventType eventType;
	UUID hostUUID;
	UUID playerUUID;
	BlockPos eventPos;
	EnumFacing facing;
	ItemStack glasses = ItemStack.EMPTY;

	int x, y;
	double mb;
	
	public GlassesEventPacket(UUID host, EventType eventType) {
		this.playerUUID = Minecraft.getMinecraft().player.getUniqueID();
		this.eventType = eventType;
		this.hostUUID = host;
	}


	public GlassesEventPacket(UUID host, EventType eventType, BlockPos eventPosition, EnumFacing face) {
		this(host, eventType);
		this.eventPos = eventPosition;
		this.facing = face;
	}

	public GlassesEventPacket(UUID host, EventType eventType, int x, int y, int mb) {
		this(host, eventType);
		this.x = x;
		this.y = y;
		this.mb = mb;
	}

	public GlassesEventPacket(){} //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		if(readBoolean())
			this.hostUUID = readUUID();

		this.playerUUID = readUUID();
		this.eventType = EventType.values()[readInt()];

		switch(eventType){
			case INTERACT_OVERLAY:
			case GLASSES_SCREEN_SIZE:
				this.x = readInt();
				this.y = readInt();
				this.mb = readDouble();
				return;
			case INTERACT_WORLD_BLOCK_LEFT:
			case INTERACT_WORLD_BLOCK_RIGHT:
				this.eventPos = new BlockPos(readVec3i());
				this.facing = EnumFacing.values()[readInt()];
				return;
		}
	}

	@Override
	protected void write() throws IOException {
		writeBoolean(hostUUID != null);
		if(hostUUID != null) {
			writeUUID(hostUUID);
		}
		writeUUID(playerUUID);
	    writeInt(eventType.ordinal());
	    
		switch(eventType){
			case GLASSES_SCREEN_SIZE:
			case INTERACT_OVERLAY:
				writeInt(x);
				writeInt(y);
				writeDouble(mb);
				break;
			case INTERACT_WORLD_BLOCK_LEFT:
			case INTERACT_WORLD_BLOCK_RIGHT:
				writeVec3i(this.eventPos);
				writeInt(this.facing.ordinal());
				break;
		}
	}

	@Override
	protected IMessage executeOnClient() {
		return null;
	}

	private ItemStack getGlasses(EntityPlayer player){
		return glasses.isEmpty() ? glasses = OpenGlasses.getGlassesStack(player) : glasses;
	}

	@Override
	protected IMessage executeOnServer() {
		EntityPlayerMP playerMP = OCServerSurface.instance().checkUUID(playerUUID);
		IOpenGlassesHost host;
		Vec3d look = new Vec3d(0, 0, 0);
		double eyeHeight = 0;
		double playerRotation = 0;
		double playerPitch = 0;
		PlayerStatsOC stats;
		ItemStack glasses;

		if(playerMP == null)
			return null;

		Vec3d playerPos = new Vec3d(playerMP.posX, playerMP.posY, playerMP.posZ);

		if(UpgradeGeolyzer.hasUpgrade(getGlasses(playerMP))) {
			Vec3d lookVector = playerMP.getLookVec();
			look = new Vec3d(Math.round(lookVector.x*1000)/1000d, Math.round(lookVector.y*1000)/1000d, Math.round(lookVector.z*1000)/1000d);
			eyeHeight = Math.round(playerMP.getEyeHeight()*1000)/1000d;
			playerRotation = Math.round(playerMP.rotationYaw*1000)/1000d;
			playerPitch = Math.round(playerMP.rotationPitch*1000)/1000d;
		}

		switch(eventType){
			case INTERACT_WORLD_BLOCK_LEFT:
			case INTERACT_WORLD_BLOCK_RIGHT:
				host = OCServerSurface.getHost(hostUUID);

				if (host != null)
					host.getComponent().sendInteractEventWorldBlock(eventType.name(), playerMP.getName(), playerPos, look, eyeHeight, this.eventPos, this.facing, playerRotation, playerPitch);
				return null;

			case INTERACT_WORLD_LEFT:
			case INTERACT_WORLD_RIGHT:
				host = OCServerSurface.getHost(hostUUID);
				if (host != null)
					host.getComponent().sendInteractEventWorld(eventType.name(), playerMP.getName(), playerPos, look, eyeHeight, playerRotation, playerPitch);
				return null;

			case INTERACT_OVERLAY:
				host = OCServerSurface.getHost(hostUUID);
				if(host != null)
					host.getComponent().sendInteractEventOverlay(eventType.name(), playerMP.getName(), mb, x, y, look, eyeHeight, playerRotation, playerPitch);
				return null;

			case GLASSES_SCREEN_SIZE:
				stats = (PlayerStatsOC) OCServerSurface.instances.playerStats.get(playerMP.getUniqueID());
				host = OCServerSurface.getHost(hostUUID);
				if(stats != null)
					stats.setScreen(x, y, mb);
				if(host != null)
					host.getComponent().sendChangeSizeEvent(eventType.name(), playerMP.getName(), x, y, mb);

				return null;

			case TOGGLE_NIGHTVISION:
				UpgradeNightvision.toggleNightvisionMode(playerMP);
				return null;

			case ACTIVATE_OVERLAY:
			case DEACTIVATE_OVERLAY:
				stats = OCServerSurface.getStats(playerMP);
				if(stats != null) {
					if(eventType.equals(EventType.ACTIVATE_OVERLAY))
						stats.conditions.setOverlay(true);
					if(eventType.equals(EventType.DEACTIVATE_OVERLAY))
						stats.conditions.setOverlay(false);
				}
				return null;

			case ACCEPT_LINK:
			case DENY_LINK:
				glasses = getGlasses(playerMP);
				if(!glasses.isEmpty())
					for(NBTTagCompound notification : OpenGlassesNotificationsNBT.getNotifications(getGlasses(playerMP))){
						switch(OpenGlassesNotificationsNBT.NotifiactionType.values()[notification.getInteger("type")]){
							case LINKREQUEST:
								if(notification.getUniqueId("host").equals(hostUUID)){
									if(eventType.equals(EventType.ACCEPT_LINK))
										OpenGlassesHostsNBT.link(glasses, hostUUID, playerMP);

									OpenGlassesNotificationsNBT.removeLinkRequest(glasses, hostUUID);
									GlassesNBT.syncStackNBT(glasses, playerMP);
									return null;
								}
								break;
						}
					}
				return null;

			case CLEAR_LINK:
				OpenGlassesHostsNBT.unlink(hostUUID, getGlasses(playerMP), playerMP);
				return null;

			case ENABLE_NOTIFICATIONS:
			case DISABLE_NOTIFICATIONS:
				GlassesNBT.setConfigFlag("nopopups", eventType.equals(EventType.ENABLE_NOTIFICATIONS), getGlasses(playerMP), playerMP);
				return null;

			case ENABLE_WORLD_RENDER:
			case DISABLE_WORLD_RENDER:
			case ENABLE_OVERLAY_RENDER:
			case DISABLE_OVERLAY_RENDER:
			case DISABLE_OVERLAY_EVENTS:
			case DISABLE_WORLD_EVENTS:
			case ENABLE_OVERLAY_EVENTS:
			case ENABLE_WORLD_EVENTS:
				glasses = getGlasses(playerMP);
				NBTTagCompound hostNBT = OpenGlassesHostsNBT.getHostFromNBT(hostUUID, glasses);
				switch(eventType) {
					case ENABLE_WORLD_RENDER:
						hostNBT.removeTag("noWorld");
						break;
					case DISABLE_WORLD_RENDER:
						hostNBT.setBoolean("noWorld", true);
						break;
					case ENABLE_OVERLAY_RENDER:
						hostNBT.removeTag("noOverlay");
						break;
					case DISABLE_OVERLAY_RENDER:
						hostNBT.setBoolean("noOverlay", true);
						break;
					case DISABLE_OVERLAY_EVENTS:
						hostNBT.setBoolean("noOverlayEvents", true);
						break;
					case DISABLE_WORLD_EVENTS:
						hostNBT.setBoolean("noWorldEvents", true);
						break;
					case ENABLE_OVERLAY_EVENTS:
						hostNBT.removeTag("noOverlayEvents");
						break;
					case ENABLE_WORLD_EVENTS:
						hostNBT.removeTag("noWorldEvents");
						break;
				}

				OpenGlassesHostsNBT.writeHostToNBT(glasses, hostNBT);
				GlassesNBT.syncStackNBT(glasses, playerMP);
				return null;
		}

		return null;
	}
}

