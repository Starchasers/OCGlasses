package com.bymarcin.ocglasses.surface.widgets.square;

import com.bymarcin.ocglasses.lua.LuaObject;
import com.bymarcin.ocglasses.surface.widgets.square.luafunction.SetPosition;
import com.bymarcin.ocglasses.utils.Location;

import net.minecraft.nbt.NBTTagCompound;

public class SquareLuaObject extends LuaObject{
	Location loc;
	public SquareLuaObject() {
		
	}
	
	public SquareLuaObject(Location loc, int id) {
		this();
		this.loc = loc;
		content.put("id", id);
		initFunctions();
	}
	
	void initFunctions(){
		content.put("setPos", new SetPosition(this, loc));
	}

	@Override
	public void load(NBTTagCompound nbt) {
		loc = new Location(nbt.getInteger("locX"), nbt.getInteger("locY"), nbt.getInteger("locZ"), nbt.getInteger("locDim"));
		content.put("id", nbt.getInteger("widgetID"));
		initFunctions();		
	}

	@Override
	public void save(NBTTagCompound nbt) {
		nbt.setInteger("widgetID", (Integer) content.get("id"));
		nbt.setInteger("locX", loc.x);
		nbt.setInteger("locY", loc.y);
		nbt.setInteger("locZ", loc.z);
		nbt.setInteger("locDIM", loc.dimID);
	}

}
