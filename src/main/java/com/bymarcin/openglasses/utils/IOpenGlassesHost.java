package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import li.cil.oc.api.Persistable;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.EnvironmentHost;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public interface IOpenGlassesHost extends Environment, ITickable, Persistable {
    OpenGlassesHostComponent getComponent();

    Vec3d getRenderPosition();

    EnvironmentHost getHost();

    default String getName(){
        return getComponent().getName();
    }

    default boolean renderAbsolute(){
        return getComponent().renderAbsolute();
    }

    default TileEntity getTileEntity(){
        return null;
    }

    default TileEntity getEntity(){
        return null;
    }

    default UUID getUUID(){
        return getComponent().getUUID();
    }

    default void remove(){
        getComponent().remove();
    }

    default boolean isInternalComponent(){
        return true;
    }

    default void sync(EntityPlayerMP player){
        getComponent().sync(player);
    }


}
