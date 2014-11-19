package com.bymarcin.openglasses.lua;

import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

public class LuaReference {
	int widgetRef;
	Location blockRef;
	
	public LuaReference(int id, Location loc) {
		blockRef = loc;
		widgetRef = id;
	}
	
	Location getBlockRef() {
		return blockRef;
	}
	
	public int getWidgetRef() {
		return widgetRef;
	}
	
	public OpenGlassesTerminalTileEntity getTerminal(){
		return blockRef.getTerminal();
	}
	
	public Widget getWidget(){
		OpenGlassesTerminalTileEntity terminal = getTerminal();
		if(terminal!=null){
			return terminal.getWidget(widgetRef);
		}
		return null;
	}
	
	public LuaReference() {}
	
	public LuaReference readFromNBT(NBTTagCompound nbt) {
		blockRef = new Location().readFromNBT(nbt.getCompoundTag("loc"));
		widgetRef = nbt.getInteger("id");
		return this;
	}

	public LuaReference writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound loc = new NBTTagCompound();
		blockRef.writeToNBT(loc);
		nbt.setTag("loc", loc);
		nbt.setInteger("id", widgetRef);
		return this;
	}
}
