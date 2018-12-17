package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class SetAntialias extends LuaFunction {
    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget instanceof ITextable){
            ((ITextable) widget).setAntialias(arguments.checkBoolean(0));
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return new Object[]{ widget.isVisible() };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setAntialias";
    }

}

