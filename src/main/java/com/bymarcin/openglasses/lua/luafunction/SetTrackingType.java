package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.component.world.EntityTracker3D;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ITracker;
import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class SetTrackingType extends LuaFunction {

        @Override
        @Callback(direct = true)
        public Object[] call(Context context, Arguments arguments) {
            super.call(context, arguments);
            Widget widget = getWidget();
            if(widget instanceof ITracker){
                EntityTracker3D.EntityType trackerType = EntityTracker3D.EntityType.valueOf(arguments.checkString(0).toUpperCase());
                int range = arguments.checkInteger(1);

                ((ITracker) widget).setupTracking(trackerType, range);
                updateWidget();
                return null;
            }
            throw new RuntimeException("Component does not exists!");
        }

        @Override
        public String getName() {
            return "setTrackingType";
        }

}
