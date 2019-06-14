package com.bymarcin.openglasses.tileentity;

import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.*;

import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;

public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment implements IOpenGlassesHost {
	private OpenGlassesHostComponent component;

	public OpenGlassesTerminalTileEntity() {
		component = new OpenGlassesHostComponent(this);
		node = component.node();
	}

	@Override
	public EnvironmentHost getHost(){
		return null;
	}

	@Override
	public void update(){
		getComponent().update();
	}

	@Override
	public Node node(){
		return getComponent().node();
	}

	@Override
	public void onLoad() {
		Network.joinOrCreateNetwork(this);
	}

	@Override
	public void onConnect(Node var1){
		getComponent().onConnect(var1);
	}

	@Override
	public void onDisconnect(Node var1){
		getComponent().onDisconnect(var1);
	}

	@Override
	public void onMessage(Message var1){
		getComponent().onMessage(var1);
	}

	@Override
	public Vec3d getRenderPosition(){
		return new Vec3d(getPos());
	}

	@Override
	public OpenGlassesHostComponent getComponent(){
		return component;
	}

	@Override
	public boolean isInternalComponent(){
		return false;
	}

	@Override
	public TileEntity getTileEntity(){
		return this;
	}

	@Override
	public void save(NBTTagCompound tag){
		getComponent().save(tag);
	}

	@Override
	public void load(NBTTagCompound tag){
		getComponent().load(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		save(nbt);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		load(nbt);
	}
}
