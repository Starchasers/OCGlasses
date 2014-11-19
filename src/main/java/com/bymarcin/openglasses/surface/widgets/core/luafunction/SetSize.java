package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IResizable;

public class SetSize extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof IResizable){
			((IResizable) widget).setSize(arguments.checkDouble(0), arguments.checkDouble(1));
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "setSize";
	}

}
