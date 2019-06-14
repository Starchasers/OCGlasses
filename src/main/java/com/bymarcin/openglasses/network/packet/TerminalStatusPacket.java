package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.GlassesGui;
import com.bymarcin.openglasses.network.Packet;

import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TerminalStatusPacket extends Packet<TerminalStatusPacket, IMessage>{
	public enum TerminalEvent{
		SYNC_SCREEN_SIZE,
		ASYNC_SCREEN_SIZES,
		SET_RENDER_RESOLUTION,
		NOTIFICATION
	}

	private TerminalEvent terminalEvent;

	public float x, y;

	private String terminalName = "";

	private IOpenGlassesHost host;
	private UUID hostUUID;

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

			case NOTIFICATION:
				hostUUID = readUUID();
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

			case NOTIFICATION:
				writeUUID(host.getUUID());
				break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected IMessage executeOnClient() {
 		switch(terminalEvent){
			case SET_RENDER_RESOLUTION:
				if(x > 0 && y > 0)
					OCClientSurface.instance().setRenderResolution(new Vec3d(this.x, this.y, 0), hostUUID);
				return null;

			case ASYNC_SCREEN_SIZES:
				OCClientSurface.instance().sendResolution();
				return null;

			case NOTIFICATION:
				ItemStack glassesStack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);
				if(!glassesStack.isEmpty()) {
					if(!glassesStack.getTagCompound().hasKey("nopopups") && !(Minecraft.getMinecraft().currentScreen instanceof GlassesGui))
						Minecraft.getMinecraft().displayGuiScreen(new GlassesGui(true));
				}

				return null;
		}

		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}
