package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITracker;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class SetTrackingFilter extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget instanceof ITracker){

            String type = arguments.checkString(0);
            int id = 0;

            if(arguments.count() >= 2)
                id = arguments.checkInteger(1);

            ((ITracker) widget).setupTrackingFilter(type, id);
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setTrackingFilter";
    }

}
