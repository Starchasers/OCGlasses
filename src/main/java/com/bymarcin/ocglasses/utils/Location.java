package com.bymarcin.ocglasses.utils;

public class Location {
	public int x,y,z,dimID;
	public Location(int x, int y, int z, int dimID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimID = dimID;
	}
	
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
}
