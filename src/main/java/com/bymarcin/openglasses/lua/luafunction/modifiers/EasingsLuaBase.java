package com.bymarcin.openglasses.lua.luafunction.modifiers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EasingsLuaBase extends ModifierLuaObject {
    String list;


    public EasingsLuaBase(int widgetIndex, int modifierIndex, String list, UUID host){
        super(widgetIndex, modifierIndex, host);
        this.list = list;
    }

    @Callback(doc = "function():", direct = true)
    public Object[] set(Context context, Arguments args){
        String type = args.checkString(0);
        String typeIO = args.checkString(1);
        float duration = (float) args.checkDouble(2);
        float min = (float) args.checkDouble(3);
        float max = (float) args.checkDouble(4);
        String mode = args.checkString(5);

        getWidget().WidgetModifierList.addEasing(modifier, type, typeIO, duration, list, min, max, mode);

        return new Object[]{ true };
    }

    @Callback(doc = "function():", direct = true)
    public Object[] get(Context context, Arguments args){
        ArrayList easing = get().getEasings().get(list);

        if(easing == null)
            return new Object[]{ false, "no easing set" };

        HashMap<String, Object> data = new HashMap<>();
        data.put("type", easing.get(0));
        data.put("typeIO", easing.get(1));
        data.put("duration", easing.get(2));
        data.put("min", easing.get(3));
        data.put("max", easing.get(4));
        data.put("mode", easing.get(5));
        data.put("progress", easing.get(6));

        return new Object[]{ data };
    }

    @Callback(doc = "function():", direct = true)
    public Object[] remove(Context context, Arguments args){
        get().removeEasing(list);
        return new Object[]{ true };
    }
}
