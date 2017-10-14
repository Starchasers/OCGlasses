package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IItem;

public class GetItem extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof IItem){
			return new Object[]{((IItem) widget).getItem().getUnlocalizedName() };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "getItem";
	}

}
