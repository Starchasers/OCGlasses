package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCServerSurface;


public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage>{
	public enum EventType{
		EQUIPED_GLASSES, UNEQUIPED_GLASSES,
		TOGGLE_NIGHTVISION,
		ACTIVATE_OVERLAY, DEACTIVATE_OVERLAY,
		INTERACT_WORLD_RIGHT, INTERACT_WORLD_LEFT, INTERACT_WORLD_BLOCK_RIGHT, INTERACT_WORLD_BLOCK_LEFT,
		INTERACT_OVERLAY,
		GLASSES_SCREEN_SIZE
	}

	EventType eventType;
	String player;
	BlockPos eventPos;
	EnumFacing facing;
	int x, y;
	double mb;
	
	public GlassesEventPacket(EventType eventType) {
		this.player = Minecraft.getMinecraft().player.getGameProfile().getId().toString();
		this.eventType = eventType;
	}

	public GlassesEventPacket(EventType eventType, BlockPos eventPosition, EnumFacing face) {
		this(eventType);
		this.eventPos = eventPosition;
		this.facing = face;
	}

	public GlassesEventPacket(EventType eventType, int x, int y, int mb) {
		this(eventType);
		this.x = x;
		this.y = y;
		this.mb = mb;
	}

	public GlassesEventPacket(){} //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		this.player = readString();
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
				this.eventPos = new BlockPos(readInt(), readInt(), readInt());
				this.facing = EnumFacing.values()[readInt()];
				return;
		}
	}

	@Override
	protected void write() throws IOException {
		writeString(player);
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
				writeInt(this.eventPos.getX());
				writeInt(this.eventPos.getY());
				writeInt(this.eventPos.getZ());
				writeInt(this.facing.ordinal());
				break;
		}
	}

	@Override
	protected IMessage executeOnClient() {
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		EntityPlayerMP playerMP;
		IOpenGlassesHost host;
		Vec3d look;

		switch(eventType) {
			case EQUIPED_GLASSES:
				((OCServerSurface) OCServerSurface.instances).subscribePlayer(player);
				//request client resolution once the player puts glasses on
				//UUID.getTerminal().requestResolutionEvent(OCServerSurface.instances.checkUUID(player));
				break;
			case UNEQUIPED_GLASSES:
				((OCServerSurface) OCServerSurface.instances).unsubscribePlayer(player);
				break;
			case INTERACT_WORLD_BLOCK_LEFT:
			case INTERACT_WORLD_BLOCK_RIGHT:
				playerMP = OCServerSurface.instances.checkUUID(player);
				look = playerMP.getLookVec();
				host = OCServerSurface.getHost(OpenGlassesItem.getHostUUID(playerMP));
				if (host != null)
					host.getComponent().sendInteractEventWorldBlock(eventType.name(),
							playerMP.getName(),
							playerMP.posX, playerMP.posY, playerMP.posZ,
							look.x, look.y, look.z,
							playerMP.getEyeHeight(), this.eventPos, this.facing
					);
				break;
			case INTERACT_WORLD_LEFT:
			case INTERACT_WORLD_RIGHT:
				playerMP = OCServerSurface.instances.checkUUID(player);
				look = playerMP.getLookVec();
				host = OCServerSurface.getHost(OpenGlassesItem.getHostUUID(playerMP));
				if (host != null)
					host.getComponent().sendInteractEventWorld(eventType.name(),
							playerMP.getName(),
							playerMP.posX, playerMP.posY, playerMP.posZ,
							look.x, look.y, look.z,
							playerMP.getEyeHeight()
					);
				break;
			case INTERACT_OVERLAY:
				playerMP = OCServerSurface.instances.checkUUID(player);
				host = OCServerSurface.getHost(OpenGlassesItem.getHostUUID(playerMP));
				if(playerMP != null && host != null)
					host.getComponent().sendInteractEventOverlay(eventType.name(), playerMP.getName(), mb, x, y);
				break;
			case GLASSES_SCREEN_SIZE:
				playerMP = OCServerSurface.instances.checkUUID(player);
				if(playerMP != null) {
					PlayerStatsOC stats = (PlayerStatsOC) OCServerSurface.instances.playerStats.get(playerMP.getUniqueID());
					host = OCServerSurface.getHost(OpenGlassesItem.getHostUUID(playerMP));
					if(stats != null)
						stats.setScreen(x, y, mb);
					if(host != null)
						host.getComponent().sendChangeSizeEvent(eventType.name(), playerMP.getName(), x, y, mb);
				}
				break;
			case TOGGLE_NIGHTVISION:
				playerMP = OCServerSurface.instances.checkUUID(player);
				if(playerMP != null) {
					PlayerStatsOC stats = (PlayerStatsOC) OCServerSurface.instances.playerStats.get(playerMP.getUniqueID());

					if(stats == null)
						stats = new PlayerStatsOC(playerMP);

					stats.toggleNightvisionMode(playerMP);
				}
				break;

			case ACTIVATE_OVERLAY:
			case DEACTIVATE_OVERLAY:
				playerMP = OCServerSurface.instances.checkUUID(player);
				if(playerMP != null) {
					PlayerStatsOC stats = (PlayerStatsOC) OCServerSurface.instances.playerStats.get(playerMP.getUniqueID());
					if(stats != null) {
						if(eventType.equals(EventType.ACTIVATE_OVERLAY))
							stats.conditions.setOverlay(true);
						if(eventType.equals(EventType.DEACTIVATE_OVERLAY))
							stats.conditions.setOverlay(false);
					}
				}
				break;

			default:
				break;
		}
		return null;
	}
}
