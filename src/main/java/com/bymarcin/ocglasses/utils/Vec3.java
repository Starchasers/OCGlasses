package com.bymarcin.ocglasses.utils;

public class Vec3 {
	public int x,y,z;
	public Vec3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Vec3){
			if( ((Vec3)arg0).x == x && ((Vec3)arg0).y == y && ((Vec3)arg0).z == z ){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "X:" +x +" Y:" + y + " Z:" + z;
	}
}
