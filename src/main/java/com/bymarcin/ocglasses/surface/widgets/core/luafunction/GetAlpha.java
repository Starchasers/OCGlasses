package com.bymarcin.ocglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.Widget;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IAlpha;

public class GetAlpha extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof IAlpha){
			return new Object[]{((IAlpha) widget).getAlpha()};
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "getAlpha";
	}

}
