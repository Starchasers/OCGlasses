package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.component.common.CustomShape;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class SetShading extends LuaFunction{

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getWidget();
        if(widget instanceof CustomShape){
            if(arguments.checkString(0).toUpperCase().equals("SMOOTH"))
                ((CustomShape) widget).smooth_shading = true;
            else if(arguments.checkString(0).toUpperCase().equals("FLAT"))
                ((CustomShape) widget).smooth_shading = false;
            else
                return null;

            updateWidget();
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setShading";
    }

}
