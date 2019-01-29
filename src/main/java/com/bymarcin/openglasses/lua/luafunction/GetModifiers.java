package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class GetModifiers extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget(); 
		
		Object[] foo = new Object[widget.WidgetModifierList.modifiers.size()];
		
		if(widget != null){
			int i=0;
			for(WidgetModifier mod : widget.WidgetModifierList.modifiers){
				foo[i] = new Object[]{ (i+1), mod.getType().toString(), mod.getValues(), mod.getConditions() };
				i++;				
			}
			
			return new Object[]{ foo };
		}
		
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "getModifiers";
	}

}
