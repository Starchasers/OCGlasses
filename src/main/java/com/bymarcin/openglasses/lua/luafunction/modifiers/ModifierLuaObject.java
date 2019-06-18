package com.bymarcin.openglasses.lua.luafunction.modifiers;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import com.bymarcin.openglasses.surface.OCServerSurface;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class ModifierLuaObject implements Value {

    protected UUID host;
    protected int widget;
    protected int modifier;

    int index = -1;

    public ModifierLuaObject(int widgetId, int modifierId, UUID hostUUID){
        this.widget = widgetId;
        this.modifier = modifierId;
        this.host = hostUUID;
    }

    public ModifierLuaObject(){} //required by oc to load the object

    public Widget getWidget(){
        return getComponent().getWidget(widget);
    }

    public WidgetModifier get() { return getWidget().WidgetModifierList.modifiers.get(modifier); }

    public void markDirty(){
        getComponent().syncWidgets();
    }

    public OpenGlassesHostComponent getComponent(){
        return OCServerSurface.getHost(host);
    }

    public Object apply(Context var1, Arguments var2){
        return null;
    }

    public void unapply(Context var1, Arguments var2){}

    public Object[] call(Context var1, Arguments var2){
        return new Object[]{};
    }

    public void dispose(Context var1){}

    public void load(NBTTagCompound nbt){
        index = nbt.getInteger("index");
        widget = nbt.getInteger("widget");
        modifier = nbt.getInteger("modifier");
        host = nbt.getUniqueId("host");
    }

    public void save(NBTTagCompound nbt){
        nbt.setInteger("index", index);
        nbt.setInteger("widget", widget);
        nbt.setInteger("modifier", modifier);
        nbt.setUniqueId("host", host);
    }
}
