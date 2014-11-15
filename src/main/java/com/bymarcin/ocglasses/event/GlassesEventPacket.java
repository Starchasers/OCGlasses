package com.bymarcin.ocglasses.event;

import java.io.IOException;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.ocglasses.OCGlasses;
import com.bymarcin.ocglasses.network.Packet;
import com.bymarcin.ocglasses.surface.ServerSurface;
import com.bymarcin.ocglasses.utils.Location;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

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
		this.player = player.getGameProfile().getId().toString();
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
		this.UUID = new Location(readInt(), readInt(), readInt(), readInt());
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
	}

	@Override
	protected IMessage executeOnClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		OCGlasses.logger.log(Level.INFO,"PACKET:" + eventType);
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
