package com.bymarcin.ocglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.Widget;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IResizable;

public class GetSize extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof IResizable){
			return new Object[]{((IResizable) widget).getWidth(), ((IResizable) widget).getHeight()};
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "getSize";
	}

}
