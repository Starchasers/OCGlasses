package com.bymarcin.openglasses.lua.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class RemoveWidget extends LuaFunction{

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget();
		if(widget != null){
			getSelf().getTerminal().removeWidget(getSelf().getWidgetRef());
			return new Object[]{ true };
		}
		throw new RuntimeException("widget does not exists!");
	}

	@Override
	public String getName() {
		return "removeWidget";
	}

}
