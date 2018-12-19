package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITracker;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.UUID;

public class SetTrackingEntity extends LuaFunction {

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget instanceof ITracker){
            String input = arguments.checkString(0);

            if(input.length() > 0 && !input.equals("none"))
                ((ITracker) widget).setupTrackingEntity(UUID.fromString(input));
            else
                ((ITracker) widget).setupTrackingEntity(null);

            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setTrackingEntity";
    }

}
