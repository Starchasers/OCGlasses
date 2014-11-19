package com.bymarcin.ocglasses.utils;

import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Location {
	public int x,y,z,dimID;
	public Location(int x, int y, int z, int dimID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimID = dimID;
	}
	
	
	public Location() {}


	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Location){
			if( ((Location)arg0).x == x && ((Location)arg0).y == y && ((Location)arg0).z == z && ((Location)arg0).dimID == dimID){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "X:" +x +" Y:" + y + " Z:" + z + " DIM:"+dimID;
	}
	
	public TileEntity getTileEntity(){
		World world  = MinecraftServer.getServer().worldServerForDimension(dimID);
		if(world==null) 
			return null;
		return world.getTileEntity(x, y, z);
	}
	
	public OCGlassesTerminalTileEntity getTerminal(){
		TileEntity te = getTileEntity();
		if(te instanceof OCGlassesTerminalTileEntity){
			return (OCGlassesTerminalTileEntity) te;
		}
		return null;
	}
	
	public Location readFromNBT(NBTTagCompound nbt) {
		x = nbt.getInteger("locX");
		y = nbt.getInteger("locY");
		z = nbt.getInteger("locZ");
		dimID = nbt.getInteger("locDim");
		return this;
	}

	public Location writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("locX", x);
		nbt.setInteger("locY", y);
		nbt.setInteger("locZ", z);
		nbt.setInteger("locDIM", dimID);
		return this;
	}
}
