package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.surface.widgets.core.attribute.ICustomShape;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

public class SetVertex extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof ICustomShape){
			if(arguments.checkInteger(0)>0 && arguments.checkInteger(0)<=((ICustomShape) widget).getVertexCount()){
				((ICustomShape) widget).setVertex(arguments.checkInteger(0)-1, (float) arguments.checkDouble(1), (float) arguments.checkDouble(2), (float) arguments.checkDouble(3));
				getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
				return null;
			}
			throw new RuntimeException("Vertex not exist!");
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "setVertex";
	}

}
