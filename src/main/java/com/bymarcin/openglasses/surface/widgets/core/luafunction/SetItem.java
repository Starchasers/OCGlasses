package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IItem;

public class SetItem extends LuaFunction{
	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget();
		if(widget instanceof IItem){
			int metaIndex = 0;
			if(arguments.count() >= 2)
				metaIndex = arguments.checkInteger(1);

			boolean ret = ((IItem) widget).setItem(arguments.checkString(0), metaIndex);
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
			return new Object[]{ ret };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "setItem";
	}

}
