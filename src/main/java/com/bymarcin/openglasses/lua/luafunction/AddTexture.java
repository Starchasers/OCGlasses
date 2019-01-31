package com.bymarcin.openglasses.lua.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class AddTexture extends LuaFunction{

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget();
		if(widget != null){
			int index = widget.WidgetModifierList.addTexture(arguments.checkString(0));
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
			return new Object[]{ index+1 };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "addTexture";
	}

}
