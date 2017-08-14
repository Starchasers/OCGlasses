package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.utils.Location;


public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage>{
	public static enum EventType{
		EQUIPED_GLASSES,
		UNEQUIPED_GLASSES,
		INTERACT_WORLD_RIGHT,
		INTERACT_WORLD_LEFT,
		INTERACT_OVERLAY
		;
	}
	
	EventType eventType;
	Location UUID;
	String player;
	int x, y, mb, width, height;
	
	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player) {
		this.player = player.getGameProfile().getId().toString();
		this.eventType = eventType;
		this.UUID = UUID;
	}
	
	public GlassesEventPacket() {
	}

	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player, int x, int y, int mb, int width, int height) {
		this(eventType, UUID, player);
		this.x = x;
		this.y = y;
		this.mb = mb;
		this.width = width;
		this.height = height;
	}

	@Override
	protected void read() throws IOException {
		this.player = readString();
		this.eventType = EventType.values()[readInt()];
		if(EventType.UNEQUIPED_GLASSES == eventType) return;
		this.UUID = new Location(new BlockPos(readInt(), readInt(), readInt()), readInt(), readLong());
		if(EventType.INTERACT_OVERLAY == eventType){
			this.x = readInt();
			this.y = readInt();
			this.mb = readInt();
			this.width = readInt();
			this.height = readInt();
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
		if(EventType.INTERACT_OVERLAY == eventType){
			writeInt(x);
			writeInt(y);
			writeInt(mb);
			writeInt(width);
			writeInt(height);
		}
	}

	@Override
	protected IMessage executeOnClient() {
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		//OpenGlasses.logger.log(Level.INFO,"PACKET:" + eventType +":"+ player);
		EntityPlayerMP playerMP;
		OpenGlassesTerminalTileEntity terminal;

		switch(eventType){
		case EQUIPED_GLASSES: ServerSurface.instance.subscribePlayer(player, UUID);
			break;
		case UNEQUIPED_GLASSES: ServerSurface.instance.unsubscribePlayer(player);
			break;
		case INTERACT_WORLD_LEFT:
		case INTERACT_WORLD_RIGHT:
			playerMP =  ServerSurface.instance.checkUUID(player);
			Vec3d look = playerMP.getLookVec();
			terminal = UUID.getTerminal();
			if(playerMP!=null && terminal!=null)
				terminal.sendInteractEvent(eventType.name(),
					playerMP.getName(),
					playerMP.posX, playerMP.posY, playerMP.posZ,
					look.x, look.y, look.z,
					playerMP.getEyeHeight()
				);
			break;
		case INTERACT_OVERLAY:
			playerMP = ServerSurface.instance.checkUUID(player);
			terminal = UUID.getTerminal();
			if(playerMP!=null && terminal!=null)
				terminal.sendInteractEvent(eventType.name(), playerMP.getName(), x, y, mb, width, height);
			break;
		default:
			break;
		
		}
		return null;
	}

}
