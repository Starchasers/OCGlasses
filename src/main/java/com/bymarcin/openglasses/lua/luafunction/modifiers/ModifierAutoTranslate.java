package com.bymarcin.openglasses.lua.luafunction.modifiers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.HashMap;
import java.util.UUID;

public class ModifierAutoTranslate extends ModifierLuaBase {
    public ModifierAutoTranslate(int widgetIndex, int modifierIndex, UUID host){
        super(widgetIndex, modifierIndex, host);
    }

    @Override
    public HashMap<String, Object> readValues(){
        Object[] values = get().getValues();

        HashMap<String, Object> mappedValues = new HashMap<>();
        mappedValues.put("x", values[0]);
        mappedValues.put("y", values[1]);

        return mappedValues;
    }

    @Callback(doc = "function(Double:x, Double:y, Double:z):boolean -- sets new values", direct = true)
    public Object[] set(Context context, Arguments args){
        if(!args.isDouble(0) || !args.isDouble(1) || !args.isDouble(2))
            return new Object[]{ false, "3 values(Double) required! x,y,z" };

        get().update(new float[]{ (float) args.checkDouble(0), (float) args.checkDouble(1) });

        markDirty();
        return new Object[]{ true };
    }

}
