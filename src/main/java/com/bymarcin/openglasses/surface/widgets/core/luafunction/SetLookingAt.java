package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ILookable;

public class SetLookingAt extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof ILookable){
			if(arguments.isBoolean(0)){
				((ILookable) widget).setLookingAtEnable(arguments.checkBoolean(0));
			}else{
				((ILookable) widget).setLookingAt(arguments.checkInteger(0),arguments.checkInteger(1), arguments.checkInteger(2));
			}
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "setLookingAt";
	}

}
