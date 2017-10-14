package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

public class GetColor extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget();
		if(widget != null){
			int index = arguments.checkInteger(0);
			float[] col = widget.WidgetModifierList.getCurrentColorFloat(index);

			return new Object[]{ col[0], col[1], col[2], col[3] };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "getColor";
	}
}
