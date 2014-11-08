package com.bymarcin.ocglasses.event;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.ocglasses.network.Packet;
import com.bymarcin.ocglasses.utils.Vec3;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage>{
	public static enum EventType{
		EQUIPED_GLASSES,
		UNEQUIPED_GLASSES
		;
	}
	
	EventType eventType;
	Vec3 UUID;
	String player;
	
	public GlassesEventPacket(EventType eventType, Vec3 UUID, EntityPlayer player) {
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
		this.UUID = new Vec3(readInt(), readInt(), readInt());
	}

	@Override
	protected void write() throws IOException {
		writeString(player);
	    writeInt(eventType.ordinal());
	    if(EventType.UNEQUIPED_GLASSES == eventType) return;
	    writeInt(UUID.x);	
	    writeInt(UUID.y);
	    writeInt(UUID.z);
	}

	@Override
	protected IMessage executeOnClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		System.out.println("PACKET:" + eventType);
		return null;
	}

}
