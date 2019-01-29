package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IItem;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class SetItem extends LuaFunction{
	@Override
	@Callback(direct = true)
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
