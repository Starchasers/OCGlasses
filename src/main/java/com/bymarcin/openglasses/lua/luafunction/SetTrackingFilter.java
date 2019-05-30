package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ITracker;
import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class SetTrackingFilter extends LuaFunction {

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getWidget();
        if(widget instanceof ITracker){

            String type = arguments.checkString(0);
            int id = 0;

            if(arguments.count() >= 2)
                id = arguments.checkInteger(1);

            ((ITracker) widget).setupTrackingFilter(type, id);
            updateWidget();
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setTrackingFilter";
    }

}
