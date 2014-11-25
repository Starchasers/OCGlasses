package com.bymarcin.openglasses.surface;

import java.util.HashMap;

import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.surface.widgets.core.AttributeRegistry;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAttribute;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class Widget implements IAttribute{
	boolean isVisable = true;
	
	public abstract void writeData(ByteBuf buff);

	public abstract void readData(ByteBuf buff);
	
	public final void write(ByteBuf buff){
		buff.writeBoolean(isVisable);
		writeData(buff);
	}

	public final void read(ByteBuf buff){
		isVisable = buff.readBoolean();
		readData(buff);
	}

	public void writeToNBT(NBTTagCompound nbt){
		ByteBuf buff = Unpooled.buffer();
		write(buff);
		nbt.setByteArray("WidgetData", buff.array());
	};

	public void readFromNBT(NBTTagCompound nbt){
		if(!nbt.hasKey("WidgetData")) return;
		byte[] b = nbt.getByteArray("WidgetData");
		ByteBuf buff = Unpooled.copiedBuffer(b);
		read(buff);
	};

	public Object[] getLuaObject(LuaReference ref) {
		HashMap<String, Object> luaObject = new HashMap<String, Object>();
		Class<?> current = getClass();
		do {
			for (Class<?> a : current.getInterfaces()) {
				if (IAttribute.class.isAssignableFrom(a)) {
					luaObject.putAll(AttributeRegistry.getFunctions(a.asSubclass(IAttribute.class), ref));
				}
			}
			current = current.getSuperclass();
		} while (!current.equals(Object.class));

		return new Object[] { luaObject };
	}

	public abstract WidgetType getType();
	
	@SideOnly(Side.CLIENT)
	public abstract IRenderableWidget getRenderable();

	public boolean isVisible() {
		return isVisable;
	}
	
	public void setVisable(boolean isVisable) {
		this.isVisable = isVisable;
	}
}
