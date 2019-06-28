package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.GlassesGui;
import com.bymarcin.openglasses.network.Packet;

import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
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

	private UUID hostUUID;

	public TerminalStatusPacket(TerminalEvent status, UUID hostUUID) {
		this.terminalEvent = status;
		this.hostUUID = hostUUID;
	}

	public TerminalStatusPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

	@Override
	protected void read() throws IOException {
		terminalEvent = TerminalEvent.values()[readInt()];
		hostUUID = readUUID();

		switch(terminalEvent){
			case SET_RENDER_RESOLUTION:
				x = readFloat();
				y = readFloat();
				break;
		}
	}

	@Override
	protected void write() throws IOException {
		writeInt(terminalEvent.ordinal());
		writeUUID(hostUUID);

		switch(terminalEvent){
			case SET_RENDER_RESOLUTION:
				writeFloat(x);
				writeFloat(y);
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
				else
					OCClientSurface.instance().setRenderResolution(null, hostUUID);
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
	protected IMessage executeOnServer(EntityPlayerMP player) {
		return null;
	}

}
