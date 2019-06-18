package com.bymarcin.openglasses.lua.luafunction.modifiers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import java.util.HashMap;
import java.util.UUID;

public class ModifierRotate extends ModifierEase {
    public ModifierRotate(int widgetIndex, int modifierIndex, UUID host){
        super(widgetIndex, modifierIndex, host);
        easingLists.addAll(readValues().keySet());
    }

    @Override
    public HashMap<String, Object> readValues(){
        Object[] values = get().getValues();

        HashMap<String, Object> mappedValues = new HashMap<>();
        mappedValues.put("deg", values[0]);
        mappedValues.put("x", values[1]);
        mappedValues.put("y", values[2]);
        mappedValues.put("z", values[3]);

        return mappedValues;
    }

    @Callback(doc = "function(Double:x, Double:y, Double:z):boolean -- sets new values", direct = true)
    public Object[] set(Context context, Arguments args){
        if(!args.isDouble(0) || !args.isDouble(1) || !args.isDouble(2))
            return new Object[]{ false, "4 values(Double) required! deg, x,y,z" };

        get().update(new float[]{ (float) args.checkDouble(0), (float) args.checkDouble(1), (float) args.checkDouble(2), (float) args.checkDouble(3) });

        markDirty();
        return new Object[]{ true };
    }

}
