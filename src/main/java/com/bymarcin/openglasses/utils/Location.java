package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class Location {
	public int x,y,z,dimID;
	public long uniqueKey;
	public Location(BlockPos pos, int dimID, long uniqueKey) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.dimID = dimID;
		this.uniqueKey = uniqueKey;
	}
	
	
	public Location() {}


	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Location){
			if( ((Location)arg0).x == x && ((Location)arg0).y == y && ((Location)arg0).z == z && 
					((Location)arg0).dimID == dimID && ((Location)arg0).uniqueKey == uniqueKey){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "X:" +x +" Y:" + y + " Z:" + z + " DIM:"+dimID + "\n Key:"+uniqueKey;
	}
	
	public String[] toArrayString() {
		return new String[]{"X:" +x + " Y:" + y + " Z:" + z, "DIM:"+dimID, "Key:"+uniqueKey};
	}
	
	public TileEntity getTileEntity(){
		World world  = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimID);
		if(world==null) 
			return null;
		return world.getTileEntity(new BlockPos(x, y, z));
	}
	
	public OpenGlassesTerminalTileEntity getTerminal(){
		TileEntity te = getTileEntity();
		if(te instanceof OpenGlassesTerminalTileEntity){
			return (OpenGlassesTerminalTileEntity) te;
		}
		return null;
	}
	
	public Location readFromNBT(NBTTagCompound nbt) {
		x = nbt.getInteger("locX");
		y = nbt.getInteger("locY");
		z = nbt.getInteger("locZ");
		dimID = nbt.getInteger("locDim");
		uniqueKey = nbt.getLong("uniqueKey");
		return this;
	}

	public Location writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("locX", x);
		nbt.setInteger("locY", y);
		nbt.setInteger("locZ", z);
		nbt.setInteger("locDIM", dimID);
		nbt.setLong("uniqueKey", uniqueKey);
		return this;
	}
}
