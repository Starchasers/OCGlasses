package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IPositionable;

public class GetPosition extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof IPositionable){
			return new Object[]{((IPositionable) widget).getPosX(), ((IPositionable) widget).getPosY()};
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "getPosition";
	}

}
