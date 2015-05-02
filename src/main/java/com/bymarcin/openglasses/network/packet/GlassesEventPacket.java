package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.utils.Location;
import com.bymarcin.openglasses.utils.OGUtils;
import com.bymarcin.openglasses.vbo.ServerLayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage>{
	public static enum EventType{
		EQUIPED_GLASSES,
		UNEQUIPED_GLASSES
		;
	}
	
	EventType eventType;
	Location location;
	String player;
	
	public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player) {
		this.player = player.getGameProfile().getName();
		this.eventType = eventType;
		this.location = UUID;
	}
	
	public GlassesEventPacket() {
	}
	
	@Override
	protected void read() throws IOException {
		this.player = readString();
		this.eventType = EventType.values()[readInt()];
		if(EventType.UNEQUIPED_GLASSES == eventType) return;
		this.location = new Location(readInt(), readInt(), readInt(), readInt(), readLong());
	}

	@Override
	protected void write() throws IOException {
		writeString(player);
	    writeInt(eventType.ordinal());
	    if(EventType.UNEQUIPED_GLASSES == eventType) return;
	    writeInt(location.x);	
	    writeInt(location.y);
	    writeInt(location.z);
	    writeInt(location.dimID);
	    writeLong(location.uniqueKey);
	}

	@Override
	protected IMessage executeOnClient() {
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		OpenGlasses.logger.log(Level.INFO,"PACKET:" + eventType +":"+ player);
		switch(eventType){
		case EQUIPED_GLASSES: ServerLayer.instance().subscribePlayer(location, OGUtils.getPlayerMP(player));
			break;
		case UNEQUIPED_GLASSES: ServerLayer.instance().unsubscribePlayer(OGUtils.getPlayerMP(player));
			break;
		default:
			break;
		
		}
		return null;
	}

}
