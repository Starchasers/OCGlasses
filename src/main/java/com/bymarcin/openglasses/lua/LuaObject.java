package com.bymarcin.openglasses.lua;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAttribute;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class LuaObject implements Value {
    private static HashMap<Class<?>, HashSet<Class>> cachedMethods = new HashMap<>();

    private HashMap<String, Object> luaObject = new HashMap<>();

    private Class<?> widgetClass;
    private LuaReference ref;
    private WidgetType type;

    public LuaObject(int widgetId, Widget widget, UUID hostUUID){
        this.widgetClass = widget.getClass();
        this.ref = new LuaReference(widgetId, hostUUID);
        this.type = widget.getType();
    }

    @Override
    public void load(NBTTagCompound nbt) {
        ref = new LuaReference().readFromNBT(nbt.getCompoundTag("ref"));
    }

    @Override
    public void save(NBTTagCompound nbt) {
        NBTTagCompound refTag = new NBTTagCompound();
        ref.writeToNBT(refTag);
        nbt.setTag("ref", refTag);
    }

    @Override
    public Object apply(Context context, Arguments arguments) {
        throw new RuntimeException("You can't replace this function");
    }

    @Override
    public void unapply(Context context, Arguments arguments) {
        throw new RuntimeException("You can't replace this function");
    }

    @Override
    public void dispose(Context context) {
    }

    @Override
    public Object[] call(Context context, Arguments arguments) {
        if(!cachedMethods.containsKey(widgetClass)){
            HashSet<Class> classes = new HashSet<>();

            Class<?> current = widgetClass;
            do {
                for (Class<?> interfaceClass : current.getInterfaces()) {
                    if (IAttribute.class.isAssignableFrom(interfaceClass)) {
                        classes.add(interfaceClass.asSubclass(IAttribute.class));
                    }
                }
                current = current.getSuperclass();
            } while (!current.equals(Object.class));

            cachedMethods.put(widgetClass, classes);
        }

        for(Class clazz : cachedMethods.get(widgetClass))
            luaObject.putAll(AttributeRegistry.getFunctions(clazz, ref));

        return new Object[] { luaObject };
    }

    @Callback(doc = "function():String -- returns the widget type", direct = true)
    public Object[] type(Context context, Arguments args){
        return new Object[]{ type.toString().toUpperCase() };
    }

}
