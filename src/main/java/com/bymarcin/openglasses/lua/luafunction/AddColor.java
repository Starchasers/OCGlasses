package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;

public class AddColor extends LuaFunction{

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getWidget();
		if(widget != null){
			int index = widget.WidgetModifierList.addColor((float) arguments.checkDouble(0), (float) arguments.checkDouble(1), (float) arguments.checkDouble(2), (float) arguments.checkDouble(3));
			updateWidget();
			return new Object[]{ index+1 };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "addColor";
	}

}
