package com.bymarcin.ocglasses.surface;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public interface IWidget {
	public void write(ByteBuf buff);
	public void read(ByteBuf buff);
	public Widgets getType();
	
	@SideOnly(Side.CLIENT)
	public IRenderableWidget getRenderable();
}
