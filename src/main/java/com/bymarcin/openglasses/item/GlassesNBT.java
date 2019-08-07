package com.bymarcin.openglasses.item;

import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesStackNBT;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class GlassesNBT {


    public static void setConfigFlag(String flagName, boolean enabled, ItemStack glassesStack, EntityPlayerMP player){
        if(player.world.isRemote)
            return;

        // config flags default to true if the tag doesnt exist, so tags are only set to DISABLE features

        if(enabled)
            glassesStack.getTagCompound().removeTag(flagName);
        else
            glassesStack.getTagCompound().setBoolean(flagName, true);

        syncStackNBT(glassesStack, player);
    }

    public static void syncStackNBT(ItemStack glassesStack, EntityPlayerMP player){
        if(glassesStack.isEmpty() || !glassesStack.hasTagCompound())
            return;

        NetworkRegistry.packetHandler.sendTo(new GlassesStackNBT(glassesStack), player);
    }

    public static UUID getUniqueId(ItemStack glassesStack){
        UUID glassesUUID = glassesStack.getTagCompound().getUniqueId("uuid");

        if(glassesUUID == null || glassesUUID.equals(new UUID(0, 0)))
            glassesStack.getTagCompound().setUniqueId("uuid", glassesUUID = UUID.randomUUID());

        return glassesUUID;
    }



}
