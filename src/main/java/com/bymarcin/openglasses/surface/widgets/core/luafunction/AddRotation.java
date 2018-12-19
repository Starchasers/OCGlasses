package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

public class AddRotation extends LuaFunction{

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget();
		if(widget != null){
			int index = widget.WidgetModifierList.addRotate((float) arguments.checkDouble(0),
												(float) arguments.checkDouble(1), 
												(float) arguments.checkDouble(2), 
												(float) arguments.checkDouble(3));
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
			return new Object[]{ index };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "addRotation";
	}

}
