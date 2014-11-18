package com.bymarcin.ocglasses.surface.widgets.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.widgets.atribute.IColorizable;

public class GetColor extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		IWidget widget = getSelf().getWidget(); 
		if(widget instanceof IColorizable){
			return new Object[]{((IColorizable) widget).getColorR(),((IColorizable) widget).getColorG(),((IColorizable) widget).getColorB()};
		}
		throw new RuntimeException("Component does not exists!");
	}

}
