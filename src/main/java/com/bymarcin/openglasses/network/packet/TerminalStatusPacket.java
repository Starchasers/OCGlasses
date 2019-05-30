package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import com.bymarcin.openglasses.network.Packet;

import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TerminalStatusPacket extends Packet<TerminalStatusPacket, IMessage>{
	public enum TerminalEvent{
		SYNC_SCREEN_SIZE,
		ASYNC_SCREEN_SIZES,
		SET_RENDER_RESOLUTION,
		LINK_REQUEST
	}

	TerminalEvent terminalEvent;

	public float x, y;

	IOpenGlassesHost host;
	UUID uuid;
	BlockPos pos;

	public TerminalStatusPacket(TerminalEvent status) {
		this.terminalEvent = status;
	}

	public TerminalStatusPacket(TerminalEvent status, IOpenGlassesHost host) {
		this(status);
		this.host = host;
	}

	public TerminalStatusPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		this.terminalEvent = TerminalEvent.values()[readInt()];

		switch(this.terminalEvent){
			case SET_RENDER_RESOLUTION:
				this.x = readFloat();
				this.y = readFloat();

				if(this.x > 0 && this.y > 0) {
					OCClientSurface.instances.renderResolution = new Vec3d(this.x, this.y, 0);
				}
				else
					OCClientSurface.instances.renderResolution = null;
				break;

			case LINK_REQUEST:
				uuid = new UUID(readLong(), readLong());
				pos = new BlockPos(readInt(), readInt(), readInt());
		}
	}

	@Override
	protected void write() throws IOException {
		writeInt(this.terminalEvent.ordinal());

		switch(this.terminalEvent){
			case SET_RENDER_RESOLUTION:
				writeFloat(this.x);
				writeFloat(this.y);
				break;

			case LINK_REQUEST:
				writeLong(host.getUUID().getMostSignificantBits());
				writeLong(host.getUUID().getLeastSignificantBits());
				writeInt((int) host.getRenderPosition().x);
				writeInt((int) host.getRenderPosition().y);
				writeInt((int) host.getRenderPosition().z);
				break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected IMessage executeOnClient() {
 		switch(this.terminalEvent){
			case ASYNC_SCREEN_SIZES:
				((OCClientSurface) OCClientSurface.instances).sendResolution();
				break;
			case LINK_REQUEST:
				new OCClientSurface.LinkRequest(uuid, pos);
				break;
		}

		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}
