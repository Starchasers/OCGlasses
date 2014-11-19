package com.bymarcin.ocglasses.lua;

import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.ocglasses.surface.Widget;
import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;
import com.bymarcin.ocglasses.utils.Location;

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
	
	public OCGlassesTerminalTileEntity getTerminal(){
		return blockRef.getTerminal();
	}
	
	public Widget getWidget(){
		OCGlassesTerminalTileEntity terminal = getTerminal();
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
