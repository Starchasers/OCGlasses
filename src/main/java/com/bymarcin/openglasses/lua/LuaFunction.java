package com.bymarcin.openglasses.lua;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import net.minecraft.nbt.NBTTagCompound;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;

import java.util.UUID;

public abstract class LuaFunction implements Value{
	LuaReference ref;

	public LuaFunction() {}
	
	LuaFunction(int id, UUID uuid) {
		ref = new LuaReference(id, uuid);
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

	protected void updateWidget(){
		getSelf().getHost().getComponent().updateWidget(getSelf().getWidgetRef());
	}

	protected Widget getWidget(){
		return getSelf().getWidget();
	}

	@Override
	public Object[] call(Context context, Arguments arguments) {
		if(context.node() ==null || getSelf().getHost() == null || getSelf().getHost().node() == null) throw new RuntimeException("host is not connected!");
		if(!context.node().canBeReachedFrom(getSelf().getHost().node())) throw new RuntimeException("host is not connected!");
		return new Object[]{null};
	}
}
