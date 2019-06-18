package com.bymarcin.openglasses.tileentity;

import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.*;

import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment implements ManagedEnvironment, EnvironmentHost {
	private OpenGlassesHostComponent component;

	public OpenGlassesTerminalTileEntity() {
		component = new OpenGlassesHostComponent(this);
		node = component.node();
	}

	public EnvironmentHost getHost(){
		return null;
	}

	@Override
	public void update(){
		getComponent().update();
	}

	@Override
	public boolean canUpdate(){
		return true;
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

	public Vec3d getRenderPosition(){
		return new Vec3d(getPos());
	}

	public OpenGlassesHostComponent getComponent(){
		return component;
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

	@Override
	public World world(){
		return getWorld();
	}

	@Override
	public double xPosition(){
		return getPos().getX();
	}

	@Override
	public double yPosition(){
		return getPos().getY();
	}

	@Override
	public double zPosition(){
		return getPos().getZ();
	}

	@Override
	public void markChanged(){
		markDirty();
	}

}
