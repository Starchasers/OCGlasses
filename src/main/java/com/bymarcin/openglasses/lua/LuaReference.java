package com.bymarcin.openglasses.lua;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import com.bymarcin.openglasses.utils.TerminalLocation;
import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

public class LuaReference {
	int widgetRef;
	TerminalLocation blockRef;
	
	public LuaReference(int id, TerminalLocation loc) {
		blockRef = loc;
		widgetRef = id;
	}

	TerminalLocation getBlockRef() {
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
		blockRef = (TerminalLocation) new TerminalLocation().readFromNBT(nbt.getCompoundTag("loc"));
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
