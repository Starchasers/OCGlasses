package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

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
		UNEQUIPED_GLASSES
		;
	}
	
	EventType eventType;
	Location UUID;
	String player;
	
	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player) {
		this.player = player.getGameProfile().getName();
		this.eventType = eventType;
		this.UUID = UUID;
	}
	
	public GlassesEventPacket() {
	}
	
	@Override
	protected void read() throws IOException {
		this.player = readString();
		this.eventType = EventType.values()[readInt()];
		if(EventType.UNEQUIPED_GLASSES == eventType) return;
		this.UUID = new Location(new BlockPos(readInt(), readInt(), readInt()), readInt(), readLong());
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
	}

	@Override
	protected IMessage executeOnClient() {
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		OpenGlasses.logger.log(Level.INFO,"PACKET:" + eventType +":"+ player);
		switch(eventType){
		case EQUIPED_GLASSES: ServerSurface.instance.subscribePlayer(player, UUID);
			break;
		case UNEQUIPED_GLASSES: ServerSurface.instance.unsubscribePlayer(player);
			break;
		default:
			break;
		
		}
		return null;
	}

}
