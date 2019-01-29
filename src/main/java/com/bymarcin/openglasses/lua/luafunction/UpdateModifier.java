package com.bymarcin.openglasses.lua.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class UpdateModifier extends LuaFunction{

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget != null){
            int modifierIndex = (arguments.checkInteger(0)-1);

            float[] f = new float[(arguments.count()-1)];

            f[0] = (float) arguments.checkDouble(1);
            f[1] = (float) arguments.checkDouble(2);

            if(arguments.count() >= 4)
                f[2] = (float) arguments.checkDouble(3);

            if(arguments.count() >= 5)
                f[3] = (float) arguments.checkDouble(4);

            widget.WidgetModifierList.update(modifierIndex, f);

            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return new Object[]{ true };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "updateModifier";
    }

}
