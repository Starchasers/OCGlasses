package com.bymarcin.openglasses.item.OpenGlassesNBT;

import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.surface.OCServerSurface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.UUID;

public class OpenGlassesHostsNBT {
    public static void link(ItemStack glassesStack, UUID hostUUID, EntityPlayer player) {
        if(player.world.isRemote)
            return;

        NBTTagCompound newTag = new NBTTagCompound();

        newTag.setUniqueId("host", hostUUID);
        newTag.setUniqueId("userUUID", player.getGameProfile().getId());
        newTag.setString("user", player.getGameProfile().getName());

        HashSet<NBTTagCompound> hosts = getHostsFromNBT(glassesStack);
        hosts.add(newTag);
        writeHostsToNBT(hosts, glassesStack);

        GlassesNBT.syncStackNBT(glassesStack, (EntityPlayerMP) player);
        OCServerSurface.instance().subscribePlayer((EntityPlayerMP) player, hostUUID);
    }

    public static void unlink(UUID uuid, ItemStack glassesStack, EntityPlayer player) {
        if(player.world.isRemote)
            return;

        HashSet<NBTTagCompound> hosts = getHostsFromNBT(glassesStack);
        hosts.remove(getHostFromNBT(uuid, glassesStack));
        writeHostsToNBT(hosts, glassesStack);

        GlassesNBT.syncStackNBT(glassesStack, (EntityPlayerMP) player);
        OCServerSurface.instance().unsubscribePlayer((EntityPlayerMP) player);
    }

    public static NBTTagCompound getHostFromNBT(UUID hostUUID, ItemStack glassesStack){
        if(glassesStack.isEmpty())
            return null;

        return getHostFromNBT(hostUUID, glassesStack.getTagCompound());
    }

    public static NBTTagCompound getHostFromNBT(UUID hostUUID, NBTTagCompound nbt){
        HashSet<NBTTagCompound> hosts = getHostsFromNBT(nbt);
        for(NBTTagCompound hostNBT : hosts)
            if(hostNBT.getUniqueId("host").equals(hostUUID))
                return hostNBT;

        return null;
    }

    private static void writeHostsToNBT(HashSet<NBTTagCompound> hosts, ItemStack glassesStack){
        int i=0;
        NBTTagCompound hostsNBT = new NBTTagCompound();
        for(NBTTagCompound tag : hosts)
            hostsNBT.setTag("host"+i++, tag);
        glassesStack.getTagCompound().setTag("hosts", hostsNBT);
    }

    public static void writeHostToNBT(ItemStack glassesStack, NBTTagCompound hostNBT){
        HashSet<NBTTagCompound> hosts = getHostsFromNBT(glassesStack);
        for(NBTTagCompound host : hosts)
            if(host.getUniqueId("host").equals(hostNBT.getUniqueId("host"))) {
                hosts.remove(host);
                break;
            }

        hosts.add(hostNBT);
        writeHostsToNBT(hosts, glassesStack);
    }

    public static HashSet<NBTTagCompound> getHostsFromNBT(ItemStack glassesStack){
        return getHostsFromNBT(glassesStack.getTagCompound());
    }

    public static HashSet<NBTTagCompound> getHostsFromNBT(NBTTagCompound tag){
        HashSet<NBTTagCompound> hosts = new HashSet<>();
        if(tag.hasKey("hosts")) {
            NBTTagCompound nbt = tag.getCompoundTag("hosts");
            for (int i = 0; nbt.hasKey("host" + i); i++)
                hosts.add(nbt.getCompoundTag("host" + i));
        }
        return hosts;
    }

    public static void setRenderResolution(Vec3d resolution, ItemStack glassesStack, UUID hostUUID){
        NBTTagCompound nbt = new NBTTagCompound();

        if(resolution.equals(new Vec3d(0, 0, 0))){ // dont import clientsurface static
            removeHostInformation(glassesStack, hostUUID, "resolution");
        }
        else {
            nbt.setDouble("x", resolution.x);
            nbt.setDouble("y", resolution.y);
            addHostInformation(glassesStack, hostUUID, "resolution", nbt);
        }
    }

    private static void addHostInformation(ItemStack glassesStack, UUID hostUUID, String name, NBTTagCompound tag){
        NBTTagCompound nbt = getHostFromNBT(hostUUID, glassesStack);
        if(nbt != null) {
            nbt.setTag(name, tag);
            writeHostToNBT(glassesStack, nbt);
        }
    }

    private static void removeHostInformation(ItemStack glassesStack, UUID hostUUID, String name){
        NBTTagCompound nbt = getHostFromNBT(hostUUID, glassesStack);
        if(nbt != null && nbt.hasKey(name)) {
            nbt.removeTag(name);
            writeHostToNBT(glassesStack, nbt);
        }
    }


}
