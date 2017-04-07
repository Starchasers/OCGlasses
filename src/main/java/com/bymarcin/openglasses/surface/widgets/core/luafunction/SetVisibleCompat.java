package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

/**
 * Created by Marcin on 29.06.2016.
 */
public class SetVisibleCompat extends LuaFunction {
    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget !=null){
            widget.setVisable(arguments.checkBoolean(0));
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setVisable";
    }
}
