package com.bymarcin.ocglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;

public class GetID extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		return new Object[]{getSelf().getWidgetRef()};
	}

	@Override
	public String getName() {
		return "getID";
	}

}
