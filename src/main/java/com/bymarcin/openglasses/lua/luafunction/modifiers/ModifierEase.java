package com.bymarcin.openglasses.lua.luafunction.modifiers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class ModifierEase extends ModifierLuaBase {
    ArrayList<String> easingLists = new ArrayList<>();

    public ModifierEase(int widgetIndex, int modifierIndex, UUID host){
        super(widgetIndex, modifierIndex, host);
    }

    @Callback(doc = "function():", direct = true)
    public Object[] easings(Context context, Arguments args){
        HashMap<String, Object> easings = new HashMap<>();

        for(String list : easingLists)
            easings.put(list, new EasingsLuaBase(widget, modifier, list, host));

        return new Object[]{ easings };
    }



}
