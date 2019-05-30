package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ITextable;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class SetText extends LuaFunction{

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getWidget();
		if(widget instanceof ITextable){
			String text = "";

			if(arguments.isString(0))
				text = arguments.checkString(0);
			else if(arguments.isInteger(0))
				text+= arguments.checkInteger(0);
			else if(arguments.isDouble(0))
				text+= arguments.checkDouble(0);
			else if(arguments.isBoolean(0))
				text+= arguments.checkBoolean(0);

			((ITextable) widget).setText(text);
			updateWidget();
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "setText";
	}

}
