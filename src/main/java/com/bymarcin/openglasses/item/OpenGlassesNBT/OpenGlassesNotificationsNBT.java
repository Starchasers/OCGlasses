package com.bymarcin.openglasses.item.OpenGlassesNBT;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.UUID;

public class OpenGlassesNotificationsNBT {
    public enum NotifiactionType { LINKREQUEST }

    public static void addLinkRequest(ItemStack glassesStack, UUID hostUUID){
        removeLinkRequest(glassesStack, hostUUID);

        HashSet<NBTTagCompound> list = getNotifications(glassesStack);

        NBTTagCompound notificationNBT = new NBTTagCompound();
        notificationNBT.setUniqueId("host", hostUUID);
        notificationNBT.setInteger("type", NotifiactionType.LINKREQUEST.ordinal());

        list.add(notificationNBT);

        writeNotificationsToNBT(glassesStack, list);
    }

    public static void writeNotificationsToNBT(ItemStack glassesStack, HashSet<NBTTagCompound> notifications){
        NBTTagCompound notificationsNBT = new NBTTagCompound();

        int i=0;
        for(NBTTagCompound tag : notifications){
            notificationsNBT.setTag("n"+i, tag);
            i++;
        }

        glassesStack.getTagCompound().setTag("notifications", notificationsNBT);
    }


    public static HashSet<NBTTagCompound> getNotifications(ItemStack glassesStack){
        HashSet<NBTTagCompound> list = new HashSet<>();
        NBTTagCompound nbt = glassesStack.getTagCompound();
        NBTTagCompound notificationsNBT = nbt.hasKey("notifications") ? nbt.getCompoundTag("notifications") : new NBTTagCompound();

        for(int i=0; notificationsNBT.hasKey("n"+i); i++){
            list.add(notificationsNBT.getCompoundTag("n"+i));
        }

        return list;
    }

    public static void removeLinkRequest(ItemStack glassesStack, UUID hostUUID){
        NBTTagCompound linkRequest = getLinkRequest(glassesStack, hostUUID);

        HashSet<NBTTagCompound> list = getNotifications(glassesStack);

        if(linkRequest != null) {
            list.remove(linkRequest);
            writeNotificationsToNBT(glassesStack, list);
        }
    }

    public static NBTTagCompound getLinkRequest(ItemStack glassesStack, UUID hostUUID){
        for(NBTTagCompound tag : getNotifications(glassesStack)){
            switch(NotifiactionType.values()[tag.getInteger("type")]){
                case LINKREQUEST:
                    if(hostUUID.equals(tag.getUniqueId("host")))
                        return tag;
                    break;
                default:
                    break;
            }
        }

        return null;
    }
}
