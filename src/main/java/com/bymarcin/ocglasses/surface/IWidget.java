package com.bymarcin.ocglasses.surface;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public interface IWidget {
	public void write(ByteBuf buff);
	public void read(ByteBuf buff);
	public void writeToNBT(NBTTagCompound nbt);
	public void readFromNBT(NBTTagCompound nbt);
	
	public Widgets getType();
	
	@SideOnly(Side.CLIENT)
	public IRenderableWidget getRenderable();
}
