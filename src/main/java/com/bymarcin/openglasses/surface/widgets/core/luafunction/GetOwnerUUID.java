package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IPrivate;

import java.util.UUID;

public class GetOwnerUUID extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof IPrivate){
			return new Object[] { ((UUID) ((IPrivate) widget).getOwnerUUID()).toString()  };
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "getOwnerUUID";
	}

}
