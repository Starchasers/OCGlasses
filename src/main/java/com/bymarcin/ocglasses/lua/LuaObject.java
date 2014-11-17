package com.bymarcin.ocglasses.lua;

import java.util.HashMap;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;

public abstract class LuaObject implements Value {
	public HashMap<String, Object> content = new HashMap<String, Object>();

	@Override
	public Object apply(Context context, Arguments arguments) {
		return content.get(arguments.checkString(0));
	}

	@Override
	public void unapply(Context context, Arguments arguments) {
		throw new RuntimeException("You can't modified this field");
	}

	@Override
	public Object[] call(Context context, Arguments arguments) {
		throw new RuntimeException("trying to call a non-callable value");
	}
	
	@Override
	public void dispose(Context context) {
		
	}
}
