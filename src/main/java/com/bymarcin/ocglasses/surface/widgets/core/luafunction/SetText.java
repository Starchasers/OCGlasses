package com.bymarcin.ocglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.Widget;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.ITextable;

public class SetText extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof ITextable){
			((ITextable) widget).setText(arguments.checkString(0));
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
			System.out.println(arguments.checkString(0));
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "setText";
	}

}
