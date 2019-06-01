package com.bymarcin.openglasses.component;

import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import li.cil.oc.api.network.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class OpenGlassesHostItem implements IOpenGlassesHost, ManagedEnvironment {
    private OpenGlassesHostComponent component;

    EnvironmentHost host;

    public OpenGlassesHostItem(EnvironmentHost container) {
        host = container;
        component = new OpenGlassesHostComponent(this);
    }

    public EnvironmentHost getHost(){
        return host;
    }

    @Override
    public void update(){
        getComponent().update();
    }

    @Override
    public Node node(){
        return getComponent().node();
    }

    @Override
    public void onConnect(Node var1){
        getComponent().onConnect(var1);
    }

    @Override
    public void onDisconnect(Node var1){
        getComponent().onDisconnect(var1);
    }

    @Override
    public void onMessage(Message var1){
        getComponent().onMessage(var1);
    }

    @Override
    public Vec3d getRenderPosition(){
        return new Vec3d(host.xPosition(), host.yPosition(), host.zPosition());
    }

    @Override
    public String getName(){ return getComponent().getName(); }

    @Override
    public OpenGlassesHostComponent getComponent(){
        return component;
    }

    @Override
    public void sync(EntityPlayerMP player){
        getComponent().sync(player);
    }

    @Override
    public boolean isInternalComponent(){
        return true;
    }

    @Override
    public boolean canUpdate(){ return false; }


    @Override
    public void load(NBTTagCompound nbt){
        getComponent().load(nbt);
    }

    @Override
    public void save(NBTTagCompound nbt){
        getComponent().save(nbt);
    }
}
