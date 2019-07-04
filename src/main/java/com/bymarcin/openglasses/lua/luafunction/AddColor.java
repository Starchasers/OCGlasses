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
			float r = Math.max(0, Math.min(1, (float) arguments.checkDouble(0)));
			float g = Math.max(0, Math.min(1, (float) arguments.checkDouble(1)));
			float b = Math.max(0, Math.min(1, (float) arguments.checkDouble(2)));
			float alpha = Math.max(0, Math.min(1, (float) arguments.optDouble(3, 1)));

			int index = widget.WidgetModifierList.addColor(r, g, b, alpha);
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
