package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

import com.bymarcin.openglasses.surface.WidgetModifierConditionType;

public class SetCondition extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		Widget widget = getSelf().getWidget();
		if(widget != null){
			
			int modifierIndex = (int) arguments.checkInteger(0) - 1;
			boolean state = arguments.checkBoolean(2);
			short conditionIndex = WidgetModifierConditionType.getIndex(arguments.checkString(1));
			
			widget.WidgetModifierList.setCondition(modifierIndex, conditionIndex, state);			
			
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		return "setCondition";
	}

}
