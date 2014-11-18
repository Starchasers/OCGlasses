package com.bymarcin.ocglasses.surface.widgets.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.widgets.atribute.IAlpha;

public class SetAlpha extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		IWidget widget = getSelf().getWidget(); 
		if(widget instanceof IAlpha){
			((IAlpha) widget).setAlpha(arguments.checkDouble(0));
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

}
