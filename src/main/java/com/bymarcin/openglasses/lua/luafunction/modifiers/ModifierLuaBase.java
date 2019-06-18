package com.bymarcin.openglasses.lua.luafunction.modifiers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.HashMap;
import java.util.UUID;

public abstract class ModifierLuaBase extends ModifierLuaObject {
    public ModifierLuaBase(int widgetIndex, int modifierIndex, UUID host){
        super(widgetIndex, modifierIndex, host);
    }

    public abstract HashMap<String, Object> readValues();

    @Callback(doc = "function():boolean removes the widget modifier", direct = true)
    public Object[] remove(Context context, Arguments args){
        getComponent().getWidget(widget).WidgetModifierList.remove(modifier);
        markDirty();
        return new Object[]{ true };
    }

    @Callback(doc = "function():String -- returns the modifier type", direct = true)
    public Object[] type(Context context, Arguments args){
        return new Object[]{ get().getType().toString().toLowerCase() };
    }

    @Callback(doc = "function():table -- returns the current values", direct = true)
    public Object[] get(Context context, Arguments args){
        return new Object[]{ readValues() };
    }

}
