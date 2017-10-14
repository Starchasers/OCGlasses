package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

public class SetEasing extends LuaFunction{

    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget != null){
            int modifierIndex = (arguments.checkInteger(0) - 1);
            widget.WidgetModifierList.addEasing(modifierIndex, arguments.checkString(1), arguments.checkString(2), (float) arguments.checkDouble(3), arguments.checkString(4), (float) arguments.checkDouble(5), (float) arguments.checkDouble(6), arguments.checkString(7));
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setEasing";
    }

}
