package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ICustomShape;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class SetVertex extends LuaFunction{

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getWidget();
		if(widget instanceof ICustomShape){
			if(arguments.checkInteger(0)>0 && arguments.checkInteger(0)<=((ICustomShape) widget).getVertexCount()){
				((ICustomShape) widget).setVertex(arguments.checkInteger(0)-1, (float) arguments.checkDouble(1), (float) arguments.checkDouble(2), (float) arguments.checkDouble(3));
				updateWidget();
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
