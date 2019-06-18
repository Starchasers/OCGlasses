package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import com.bymarcin.openglasses.lua.luafunction.modifiers.*;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class Modifiers extends LuaFunction{

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();

        if(widget != null){
            UUID hostUUID = getSelf().getHost().getUUID();
            int widgetId = getSelf().getWidgetRef();

            ArrayList<Object> values = new ArrayList<>();
            int i=0;

            for(WidgetModifier mod : widget.WidgetModifierList.modifiers){
                switch(mod.getType()){
                    case COLOR:
                        values.add(new ModifierColor(widgetId, i, hostUUID));
                        break;
                    case ROTATE:
                        values.add(new ModifierRotate(widgetId, i, hostUUID));
                        break;
                    case SCALE:
                        values.add(new ModifierScale(widgetId, i, hostUUID));
                        break;
                    case TRANSLATE:
                        values.add(new ModifierTranslate(widgetId, i, hostUUID));
                        break;
                    case AUTOTRANSLATE:
                        values.add(new ModifierAutoTranslate(widgetId, i, hostUUID));
                        break;
                }

                i++;
            }

            return new Object[]{ values };
        }

        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "modifiers";
    }

}

