package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.WidgetGLOverlay;
import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.utils.Conditions;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class GetRenderPosition extends LuaFunction{

    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        WidgetGLOverlay widget = (WidgetGLOverlay) getSelf().getWidget();
        if(widget != null){
            String playerName = arguments.checkString(0);
            EntityPlayer player = OpenGlasses.proxy.getPlayer(playerName);
            Conditions conditions = new Conditions();
            conditions.getConditionStates(player);
            conditions.bufferSensors(OpenGlasses.getGlassesStack(player));
            //widget.updateRenderPosition(conditions.get());
            //Vec3d renderPosition = widget.getRenderPosition(playerName);
            Vec3d renderPosition = new Vec3d(0, 0, 0);

            return new Object[] { renderPosition.x, renderPosition.y, renderPosition.z };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "getRenderPosition";
    }

}
