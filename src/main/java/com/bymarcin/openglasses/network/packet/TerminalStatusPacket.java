package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.ArrayList;

import com.bymarcin.openglasses.network.Packet;

import com.bymarcin.openglasses.surface.ClientSurface;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TerminalStatusPacket extends Packet<TerminalStatusPacket, IMessage>{
	public enum TerminalEvent{
		SYNC_SCREEN_SIZE,
		ASYNC_SCREEN_SIZES,
		SET_RENDER_RESOLUTION
	}

	TerminalEvent terminalEvent;

	public float x, y;

	public TerminalStatusPacket(TerminalEvent status) {
		this.terminalEvent = status;
	}

	public TerminalStatusPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		this.terminalEvent = TerminalEvent.values()[readInt()];

		if(this.terminalEvent == TerminalEvent.SET_RENDER_RESOLUTION){
			this.x = readFloat();
			this.y = readFloat();

			if(this.x > 0 && this.y > 0) {
				ClientSurface.instances.renderResolution = new ArrayList<Float>();
				ClientSurface.instances.renderResolution.add(this.x);
				ClientSurface.instances.renderResolution.add(this.y);
			}
			else
				ClientSurface.instances.renderResolution = null;
		}
	}

	@Override
	protected void write() throws IOException {
		writeInt(this.terminalEvent.ordinal());
		if(this.terminalEvent == TerminalEvent.SET_RENDER_RESOLUTION){
			writeFloat(this.x);
			writeFloat(this.y);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected IMessage executeOnClient() {
 		switch(this.terminalEvent){
			case ASYNC_SCREEN_SIZES:
				ClientSurface.instances.sendResolution();
				break;
		}

		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}
