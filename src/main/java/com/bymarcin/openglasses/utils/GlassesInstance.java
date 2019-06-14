package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesHostsNBT;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.UUID;

import static ben_mkiv.rendertoolkit.surface.ClientSurface.vec3d000;

public class GlassesInstance {
    private ItemStack stack;
    public Conditions conditions = new Conditions();

    public UUID glassesUUID = null;

    private HashMap<UUID, HostClient> hosts = new HashMap<>();

    public GlassesInstance(ItemStack glassesStack){
        stack = OpenGlasses.isGlassesStack(glassesStack) ? glassesStack : ItemStack.EMPTY;

        if(!get().isEmpty()) {
            conditions.bufferSensors(get());

            glassesUUID = GlassesNBT.getUniqueId(get());

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

        public HostClient(NBTTagCompound nbt){
            updateFromNBT(nbt);
        }

        public void updateFromNBT(NBTTagCompound nbt){
            uuid = nbt.getUniqueId("host");
            ownerName = nbt.getString("user");
            ownerUUID = nbt.getUniqueId("ownerUUID");
            renderWorld = !nbt.hasKey("noWorld");
            renderOverlay = !nbt.hasKey("noOverlay");
            sendOverlayEvents = !nbt.hasKey("noOverlayEvents");
            sendWorldEvents = !nbt.hasKey("noWorldEvents");

            if(nbt.hasKey("resolutionX") && nbt.hasKey("resolutionY"))
                renderResolution = new Vec3d(nbt.getDouble("resolutionX"), nbt.getDouble("resolutionY"), 0);
            //terminalName = nbt.getString("name");
        }

        public NBTTagCompound writeToNBT(NBTTagCompound tag){
            tag.setUniqueId("host", uuid);
            //tag.setString("name", terminalName);
            if(!renderWorld)
                tag.setBoolean("noWorld", true);
            if(!renderOverlay)
                tag.setBoolean("noOverlay", true);
            if(!sendOverlayEvents)
                tag.setBoolean("noOverlayEvents", true);
            if(!sendWorldEvents)
                tag.setBoolean("noWorldEvents", true);
            if(!renderResolution.equals(vec3d000)){
                tag.setDouble("resolutionX", renderResolution.x);
                tag.setDouble("resolutionY", renderResolution.y);
            }

            tag.setUniqueId("userUUID", ownerUUID);

            tag.setString("user", ownerName);

            return tag;
        }

        public OpenGlassesHostClient getHost(){
            return OCClientSurface.instance().getHost(uuid);
        }


    }
}
