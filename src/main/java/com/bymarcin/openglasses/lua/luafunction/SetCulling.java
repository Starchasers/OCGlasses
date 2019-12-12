package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.WidgetGLOverlay;
import ben_mkiv.rendertoolkit.common.widgets.component.common.CustomShape;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class SetCulling extends LuaFunction{

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getWidget();
        if(widget instanceof WidgetGLOverlay){
            if(arguments.checkBoolean(0))
                ((WidgetGLOverlay) widget).cullFaces = true;
            else
                ((WidgetGLOverlay) widget).cullFaces = false;

            updateWidget();
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setCulling";
    }

}
