package com.bymarcin.ocglasses.lua;

import java.util.HashMap;

import com.bymarcin.ocglasses.utils.Location;

public class LuaObjectBuilder {
	LuaReference ref;
	HashMap<String, LuaFunction> object = new HashMap<String, LuaFunction>();
	public LuaObjectBuilder(int id, Location loc) {
		ref = new LuaReference(id, loc);
	}
	
	/**
	 * Add function to lua object.
	 * @param name name of function in lua
	 * @param your function
	 * @return lua object for Open Computers
	 */
	public void addFunction(String name, LuaFunction a){
		a.setRef(ref);
		object.put(name, a);
	}
	
	/**
	 * Creates lua object and clear buffer(remove all added elements).
	 * @return lua object for Open Computers
	 */
	public Object[] createLuaObject(){
		HashMap<String, LuaFunction> ret = object;
		object = new HashMap<String, LuaFunction>();
		return new Object[]{ret};
	}
}
