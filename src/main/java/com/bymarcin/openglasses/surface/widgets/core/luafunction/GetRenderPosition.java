package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.surface.WidgetGLOverlay;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import net.minecraft.util.math.Vec3d;

public class GetRenderPosition extends LuaFunction{

    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        WidgetGLOverlay widget = (WidgetGLOverlay) getSelf().getWidget();
        if(widget instanceof WidgetGLOverlay){
            Vec3d renderPosition = widget.getRenderPosition(arguments.checkString(0));

            return new Object[] { renderPosition.x, renderPosition.y, renderPosition.z };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "getRenderPosition";
    }

}
