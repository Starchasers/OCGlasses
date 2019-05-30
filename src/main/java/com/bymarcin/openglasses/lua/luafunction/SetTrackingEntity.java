package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ITracker;
import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.UUID;

public class SetTrackingEntity extends LuaFunction {

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getWidget();
        if(widget instanceof ITracker){
            String input = arguments.checkString(0);

            if(input.length() > 0 && !input.equals("none"))
                ((ITracker) widget).setupTrackingEntity(UUID.fromString(input));
            else
                ((ITracker) widget).setupTrackingEntity(null);

            updateWidget();
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setTrackingEntity";
    }

}
