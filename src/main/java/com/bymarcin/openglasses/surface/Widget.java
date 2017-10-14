package com.bymarcin.openglasses.surface;

import java.util.HashMap;
import java.util.UUID;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.surface.widgets.core.AttributeRegistry;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAttribute;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled; 

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class Widget implements IAttribute{
	boolean isVisible = true;
	UUID widgetOwner = null;

	public WidgetModifiers WidgetModifierList = new WidgetModifiers();
	
	public abstract void writeData(ByteBuf buff);

	public abstract void readData(ByteBuf buff);
	
	public UUID string2UUID(String input){
		try {
			return UUID.fromString(input);
		} catch (Exception ex) { return null; }
	}
	
	public final void write(ByteBuf buff){
		buff.writeBoolean(this.isVisible);
		
		if(this.widgetOwner != null)
			ByteBufUtils.writeUTF8String(buff, this.widgetOwner.toString());
		else
			ByteBufUtils.writeUTF8String(buff, "@NONE");
			
		writeData(buff);
	}

	public final void read(ByteBuf buff){
		this.isVisible = buff.readBoolean();
		this.widgetOwner = string2UUID(ByteBufUtils.readUTF8String(buff));			
		readData(buff);
	}

	public void writeToNBT(NBTTagCompound nbt){
		ByteBuf buff = Unpooled.buffer();
		write(buff);
		nbt.setByteArray("WidgetData", buff.array());
	}

	public void readFromNBT(NBTTagCompound nbt){
		if(!nbt.hasKey("WidgetData")) return;
		byte[] b = nbt.getByteArray("WidgetData");
		ByteBuf buff = Unpooled.copiedBuffer(b);
		read(buff);
	}

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
		return isVisible;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public UUID getOwnerUUID() {
		return this.widgetOwner;
	}
	
	public String getOwner() {
		if(this.widgetOwner != null)
			return UsernameCache.getLastKnownUsername(this.widgetOwner);
		else
			return "";
	}
	
	//sets widget owner and returns the uuid
	public UUID setOwner(String playerName) {
		EntityPlayerMP newOwner = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playerName);
		if(newOwner != null)
			this.widgetOwner = newOwner.getGameProfile().getId();
		else
			this.widgetOwner = null;
		
		return this.getOwnerUUID();
	}
}
