package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import li.cil.oc.api.network.Environment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public interface IOpenGlassesHost extends Environment, ITickable {
    OpenGlassesHostComponent getComponent();

    void sync(EntityPlayerMP player);

    Vec3d getRenderPosition();

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

}
