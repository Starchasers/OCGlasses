package com.bymarcin.openglasses.lua.luafunction.modifiers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.HashMap;
import java.util.UUID;

public class ModifierColor extends ModifierEase {
    public ModifierColor(int widgetIndex, int modifierIndex, UUID host){
        super(widgetIndex, modifierIndex, host);
        easingLists.addAll(readValues().keySet());
    }

    @Override
    public HashMap<String, Object> readValues(){
        Object[] values = get().getValues();

        HashMap<String, Object> mappedValues = new HashMap<>();
        mappedValues.put("r", values[0]);
        mappedValues.put("g", values[1]);
        mappedValues.put("b", values[2]);
        mappedValues.put("alpha", values[3]);

        return mappedValues;
    }

    @Callback(doc = "function(Double:red, Double:green, Double:blue[, Double:alpha]):boolean -- sets new values", direct = true)
    public Object[] set(Context context, Arguments args){
        if(!args.isDouble(0) || !args.isDouble(1) || !args.isDouble(2))
            return new Object[]{ false, "3-4 colors(Double) values required! r, g, b, [alpha]" };

        get().update(new float[]{ (float) args.checkDouble(0), (float) args.checkDouble(1), (float) args.checkDouble(2), (float) args.optDouble(3, 1) });

        markDirty();
        return new Object[]{ true };
    }

}
