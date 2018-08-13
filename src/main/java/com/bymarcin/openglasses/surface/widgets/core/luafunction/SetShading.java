package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.surface.widgets.component.common.CustomShape;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

public class SetShading extends LuaFunction{

    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget instanceof CustomShape){
            if(arguments.checkString(0).toUpperCase().equals("SMOOTH"))
                ((CustomShape) widget).smooth_shading = true;
            if(arguments.checkString(0).toUpperCase().equals("FLAT"))
                ((CustomShape) widget).smooth_shading = false;
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setShading";
    }

}
