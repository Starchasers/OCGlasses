package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.surface.WidgetGLOverlay;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;

public class SetVerticalAlign extends LuaFunction{
    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        WidgetGLOverlay widget = (WidgetGLOverlay) getSelf().getWidget();
        if(widget != null){
            widget.setVerticalAlignment(arguments.checkString(0));
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setVerticalAlign";
    }

}
