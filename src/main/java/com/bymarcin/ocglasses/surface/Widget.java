package com.bymarcin.ocglasses.surface;

import java.util.HashMap;

import com.bymarcin.ocglasses.lua.LuaReference;
import com.bymarcin.ocglasses.surface.widgets.core.AttributeRegistry;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IAttribute;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public abstract class Widget {
	public abstract void write(ByteBuf buff);

	public abstract void read(ByteBuf buff);

	public abstract void writeToNBT(NBTTagCompound nbt);

	public abstract void readFromNBT(NBTTagCompound nbt);

	public Object[] getLuaObject(LuaReference ref) {
		HashMap<String, Object> luaObject = new HashMap<String, Object>();
		Class<?> current = getClass();
		do {
			for (Class<?> a : getClass().getInterfaces()) {
				if (IAttribute.class.isAssignableFrom(a)) {
					luaObject.putAll(AttributeRegistry.getFunctions(a.asSubclass(IAttribute.class), ref));
				}
			}
			current = current.getSuperclass();
		} while (!current.equals(Object.class));

		return new Object[] { luaObject };
	}

	public abstract Widgets getType();

	@SideOnly(Side.CLIENT)
	public abstract IRenderableWidget getRenderable();
}
