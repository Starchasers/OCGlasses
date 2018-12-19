package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.PlayerStats;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.utils.Location;


public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage>{
	public enum EventType{
		EQUIPED_GLASSES, UNEQUIPED_GLASSES,
		TOGGLE_NIGHTVISION,
		INTERACT_WORLD_RIGHT, INTERACT_WORLD_LEFT, INTERACT_WORLD_BLOCK_RIGHT, INTERACT_WORLD_BLOCK_LEFT,
		INTERACT_OVERLAY,
		GLASSES_SCREEN_SIZE
	}

	EventType eventType;
	Location UUID;
	String player;
	BlockPos eventPos;
	EnumFacing facing;
	int x, y;
	double mb;
	
	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player) {
		this.player = player.getGameProfile().getId().toString();
		this.eventType = eventType;
		this.UUID = UUID;
	}

	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player, BlockPos eventPosition, EnumFacing face) {
		this(eventType, UUID, player);
		this.eventPos = eventPosition;
		this.facing = face;
	}

	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player, int x, int y, int mb) {
		this(eventType, UUID, player);
		this.x = x;
		this.y = y;
		this.mb = mb;
	}

	public GlassesEventPacket(){} //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		this.player = readString();
		this.eventType = EventType.values()[readInt()];
		if(EventType.UNEQUIPED_GLASSES == eventType) return;

		this.UUID = new Location(new BlockPos(readInt(), readInt(), readInt()), readInt(), readLong());

		switch(eventType){
			case INTERACT_OVERLAY:
			case GLASSES_SCREEN_SIZE:
				this.x = readInt();
				this.y = readInt();
				this.mb = readDouble();
				break;
			case INTERACT_WORLD_BLOCK_LEFT:
			case INTERACT_WORLD_BLOCK_RIGHT:
				this.eventPos = new BlockPos(readInt(), readInt(), readInt());
				this.facing = EnumFacing.values()[readInt()];
				break;
		}
	}

	@Override
	protected void write() throws IOException {
		writeString(player);
	    writeInt(eventType.ordinal());
	    if(EventType.UNEQUIPED_GLASSES == eventType) return;

	    writeInt(UUID.x);
	    writeInt(UUID.y);
	    writeInt(UUID.z);
	    writeInt(UUID.dimID);
	    writeLong(UUID.uniqueKey);

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
		OpenGlassesTerminalTileEntity terminal;
		Vec3d look;

		switch(eventType) {
			case EQUIPED_GLASSES:
				ServerSurface.instance.subscribePlayer(player, UUID);
				//request client resolution once the player puts glasses on
				//UUID.getTerminal().requestResolutionEvent(ServerSurface.instance.checkUUID(player));
				break;
			case UNEQUIPED_GLASSES:
				ServerSurface.instance.unsubscribePlayer(player);
				break;
			case INTERACT_WORLD_BLOCK_LEFT:
			case INTERACT_WORLD_BLOCK_RIGHT:
				playerMP = ServerSurface.instance.checkUUID(player);
				look = playerMP.getLookVec();
				terminal = UUID.getTerminal();
				if (terminal != null)
					terminal.sendInteractEventWorldBlock(eventType.name(),
							playerMP.getName(),
							playerMP.posX, playerMP.posY, playerMP.posZ,
							look.x, look.y, look.z,
							playerMP.getEyeHeight(), this.eventPos, this.facing
					);
				break;
			case INTERACT_WORLD_LEFT:
			case INTERACT_WORLD_RIGHT:
				playerMP = ServerSurface.instance.checkUUID(player);
				look = playerMP.getLookVec();
				terminal = UUID.getTerminal();
				if (terminal != null)
					terminal.sendInteractEventWorld(eventType.name(),
							playerMP.getName(),
							playerMP.posX, playerMP.posY, playerMP.posZ,
							look.x, look.y, look.z,
							playerMP.getEyeHeight()
					);
				break;
			case INTERACT_OVERLAY:
				playerMP = ServerSurface.instance.checkUUID(player);
				terminal = UUID.getTerminal();
				if(playerMP != null && terminal != null)
					terminal.sendInteractEventOverlay(eventType.name(), playerMP.getName(), mb, x, y);
				break;
			case GLASSES_SCREEN_SIZE:
				playerMP = ServerSurface.instance.checkUUID(player);
				if(playerMP != null) {
					PlayerStats stats = ServerSurface.instance.playerStats.get(playerMP.getUniqueID());
					terminal = UUID.getTerminal();
					if(stats != null)
						stats.setScreen(x, y, mb);
					if(terminal != null)
						terminal.sendChangeSizeEvent(eventType.name(), playerMP.getName(), x, y, mb);
				}
				break;
			case TOGGLE_NIGHTVISION:
				playerMP = ServerSurface.instance.checkUUID(player);
				if(playerMP != null) {
					PlayerStats stats = ServerSurface.instance.playerStats.get(playerMP.getUniqueID());
					terminal = UUID.getTerminal();
					if(stats != null)
						stats.toggleNightvisionMode(playerMP);
				}
				break;
			default:
				break;
		}
		return null;
	}
}
