package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.WidgetGLOverlay;
import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.utils.Conditions;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import static com.bymarcin.openglasses.component.OpenGlassesHostComponent.vec3D_to_map;

public class GetRenderPosition extends LuaFunction{

    @Override
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        WidgetGLOverlay widget = (WidgetGLOverlay) getSelf().getWidget();
        if(widget != null){
            String playerName = arguments.checkString(0);
            int width = arguments.checkInteger(1);
            int height = arguments.checkInteger(2);

            EntityPlayer player = OpenGlasses.proxy.getPlayer(playerName);
            Conditions conditions = new Conditions();
            conditions.bufferSensors(OpenGlasses.getGlassesStack(player));

            Vec3d offset = new Vec3d(0, 0, 0);
            Vec3d renderPosition = widget.WidgetModifierList.getRenderPosition(conditions.getConditionStates(player), offset, width, height, 0);

            return new Object[] { vec3D_to_map(renderPosition) };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "getRenderPosition";
    }

}
