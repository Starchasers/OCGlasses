package com.bymarcin.openglasses.lua;

import com.bymarcin.openglasses.utils.Location;

import net.minecraft.nbt.NBTTagCompound;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;

public abstract class LuaFunction implements Value{
	LuaReference ref;

	public LuaFunction() {}
	
	LuaFunction(int id, Location loc) {
		ref = new LuaReference(id, loc);
	}
	
	public abstract String getName();
	
	public void setRef(LuaReference ref){
		this.ref = ref;
	}
	
	public LuaReference getSelf(){
		return ref;
	}
	
	@Override
	public void load(NBTTagCompound nbt) {
		ref = new LuaReference().readFromNBT(nbt.getCompoundTag("ref"));
	}
	
	@Override
	public void save(NBTTagCompound nbt) {
		NBTTagCompound refTag = new NBTTagCompound();
		ref.writeToNBT(refTag);
		nbt.setTag("ref", refTag);	
	}

	//TODO context.node().canBeReachedFrom(getSelf().getTerminal().node());
	//whut? adding range to glasses or what is this about to do?
	//should only be added if we can add the GlassesTerminal as Update to a Tablet so that
	//mobile use is still a thing

	@Override
	public Object apply(Context context, Arguments arguments) {
		throw new RuntimeException("You can't replace this function");
	}

	@Override
	public void unapply(Context context, Arguments arguments) {
		throw new RuntimeException("You can't replace this function");	
	}

	@Override
	public void dispose(Context context) {}


	@Override
	public Object[] call(Context context, Arguments arguments) {
		if(context.node() ==null || getSelf().getTerminal() == null || getSelf().getTerminal().node() == null) throw new RuntimeException("Terminal is not connected!");
		if(!context.node().canBeReachedFrom(getSelf().getTerminal().node())) throw new RuntimeException("Terminal is not connected!");
		return new Object[]{null};
	}
}
