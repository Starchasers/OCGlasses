package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import com.bymarcin.openglasses.event.glasses.LinkRequest;
import com.bymarcin.openglasses.network.Packet;

import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
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

	private TerminalEvent terminalEvent;

	public float x, y;

	private String terminalName = "";

	private IOpenGlassesHost host;
	private UUID uuid;
	private BlockPos pos;

	public TerminalStatusPacket(TerminalEvent status) {
		this.terminalEvent = status;
	}

	public TerminalStatusPacket(TerminalEvent status, IOpenGlassesHost host) {
		this(status);
		this.host = host;
		this.terminalName = host.getName();
	}

	public TerminalStatusPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		terminalEvent = TerminalEvent.values()[readInt()];
		terminalName = readString();

		switch(this.terminalEvent){
			case SET_RENDER_RESOLUTION:
				x = readFloat();
				y = readFloat();
				break;

			case LINK_REQUEST:
				uuid = readUUID();
				pos = new BlockPos(readVec3d());
		}
	}

	@Override
	protected void write() throws IOException {
		writeInt(terminalEvent.ordinal());
		writeString(terminalName);

		switch(this.terminalEvent){
			case SET_RENDER_RESOLUTION:
				writeFloat(x);
				writeFloat(y);
				break;

			case LINK_REQUEST:
				writeUUID(host.getUUID());
				writeVec3d(host.getRenderPosition());
				break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected IMessage executeOnClient() {
 		switch(terminalEvent){
			case SET_RENDER_RESOLUTION:
				OCClientSurface.renderResolution = x > 0 && y > 0 ? new Vec3d(this.x, this.y, 0) : null;
				return null;

			case ASYNC_SCREEN_SIZES:
				OCClientSurface.instance().sendResolution();
				return null;

			case LINK_REQUEST:
				for(OpenGlassesHostClient host : OCClientSurface.instance().getHosts())
					if(host.getUniqueId().equals(uuid))
						return null;

				new LinkRequest(uuid, pos, terminalName);
				return null;
		}

		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}
