package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesHostsNBT;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.UUID;

public class GlassesInstance {
    private ItemStack stack;
    private Conditions conditions = new Conditions();

    private UUID glassesUUID = null;
    public boolean thermalVisionActive = false;

    private HashMap<UUID, HostClient> hosts = new HashMap<>();

    public GlassesInstance(ItemStack glassesStack){
        stack = OpenGlasses.isGlassesStack(glassesStack) ? glassesStack : ItemStack.EMPTY;

        if(!get().isEmpty()) {
            NBTTagCompound nbt = get().getTagCompound();

            conditions.bufferSensors(get());

            glassesUUID = GlassesNBT.getUniqueId(get());

            thermalVisionActive = nbt.hasKey("thermalActive") && nbt.getBoolean("thermalActive");

            for(NBTTagCompound tag : OpenGlassesHostsNBT.getHostsFromNBT(stack))
                hosts.put(tag.getUniqueId("host"), new HostClient(tag));
        }
    }

    public ItemStack get(){
        return stack;
    }

    public void refreshConditions(){
        if(!get().isEmpty())
            conditions.getConditionStates(Minecraft.getMinecraft().player);
    }

    public HashMap<UUID, HostClient> getHosts(){
        return hosts;
    }

    public UUID getUniqueId(){
        return glassesUUID;
    }

    public Conditions getConditions(){
        return conditions;
    }

    public HostClient getHost(UUID hostUUID){
        return hosts.get(hostUUID);
    }


    public static class HostClient {
        public UUID uuid = null;
        public boolean renderWorld = true, renderOverlay = true;
        public boolean sendOverlayEvents = true, sendWorldEvents = true;
        public UUID ownerUUID;
        public String ownerName = "";
        public Vec3d renderResolution = new Vec3d(0, 0, 0);

        public HostClient(UUID host, EntityPlayer owner){
            uuid = host;
            ownerUUID = owner.getUniqueID();
            ownerName = owner.getDisplayNameString();
        }

        HostClient(NBTTagCompound nbt){
            updateFromNBT(nbt);
        }

        void updateFromNBT(NBTTagCompound nbt){
            uuid = nbt.getUniqueId("host");
            ownerName = nbt.getString("user");
            ownerUUID = nbt.getUniqueId("ownerUUID");
            renderWorld = !nbt.hasKey("noWorld");
            renderOverlay = !nbt.hasKey("noOverlay");
            sendOverlayEvents = !nbt.hasKey("noOverlayEvents");
            sendWorldEvents = !nbt.hasKey("noWorldEvents");

            if(nbt.hasKey("resolution")) {
                NBTTagCompound resolutionTag = nbt.getCompoundTag("resolution");
                renderResolution = new Vec3d(resolutionTag.getDouble("x"), resolutionTag.getDouble("y"), 0);
            }
        }

        public OpenGlassesHostClient getHost(){
            return OCClientSurface.instance().getHost(uuid);
        }


    }
}
