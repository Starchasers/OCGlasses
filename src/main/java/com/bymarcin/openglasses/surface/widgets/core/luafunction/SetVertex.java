package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I2DVertex;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DVertex;

public class SetVertex extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		Widget widget = getSelf().getWidget(); 
		if(widget instanceof I3DVertex){
			if(arguments.checkInteger(0)>0 && arguments.checkInteger(0)<=((I3DVertex) widget).getVertexCount()){
				((I3DVertex) widget).setVertex(arguments.checkInteger(0)-1, arguments.checkDouble(1), arguments.checkDouble(2), arguments.checkDouble(3));
				getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
				return null;
			}
			throw new RuntimeException("Vertex not exist!");
		}else if(widget instanceof I2DVertex){
			if(arguments.checkInteger(0)>0 && arguments.checkInteger(0)<=((I2DVertex) widget).getVertexCount()){
				((I2DVertex) widget).setVertex(arguments.checkInteger(0)-1, arguments.checkDouble(1), arguments.checkDouble(2));
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
