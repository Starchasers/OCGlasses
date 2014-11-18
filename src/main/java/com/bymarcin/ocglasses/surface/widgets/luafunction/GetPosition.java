package com.bymarcin.ocglasses.surface.widgets.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.widgets.atribute.IPositionable;

public class GetPosition extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		IWidget widget = getSelf().getWidget(); 
		if(widget instanceof IPositionable){
			return new Object[]{((IPositionable) widget).getPosX(), ((IPositionable) widget).getPosY()};
		}
		throw new RuntimeException("Component does not exists!");
	}

}
