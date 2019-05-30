package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.WidgetGLOverlay;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;

public class SetHorizontalAlign extends LuaFunction{
    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        WidgetGLOverlay widget = (WidgetGLOverlay) getWidget();
        if(widget != null){
            widget.setHorizontalAlignment(arguments.checkString(0));
            updateWidget();
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setHorizontalAlign";
    }

}
