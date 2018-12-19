package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.component.world.EntityTracker3D;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITracker;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class SetTrackingType extends LuaFunction {

        @Override
        @Callback(direct = true)
        public Object[] call(Context context, Arguments arguments) {
            super.call(context, arguments);
            Widget widget = getSelf().getWidget();
            if(widget instanceof ITracker){
                EntityTracker3D.EntityType trackerType = EntityTracker3D.EntityType.valueOf(arguments.checkString(0).toUpperCase());
                int range = arguments.checkInteger(1);

                ((ITracker) widget).setupTracking(trackerType, range);
                getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
                return null;
            }
            throw new RuntimeException("Component does not exists!");
        }

        @Override
        public String getName() {
            return "setTrackingType";
        }

}
