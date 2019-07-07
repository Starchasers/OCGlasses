package com.bymarcin.openglasses.network.packet;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.UUID;

public class GlassesStackNBT extends Packet<GlassesStackNBT, IMessage> {

    NBTTagCompound tagCompound;

    public GlassesStackNBT(ItemStack glassesStack) {
        tagCompound = glassesStack.getTagCompound();
    }

    public GlassesStackNBT() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

    @Override
    protected void read() throws IOException {
        tagCompound = readNBT();
    }

    @Override
    protected void write() throws IOException {
        writeNBT(tagCompound);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected IMessage executeOnClient() {
        UUID tagUUID = tagCompound.getUniqueId("uuid");

        // try to find the glasses stack that should be updated
        ItemStack glasses = findGlassesStack(tagUUID);


        if(!glasses.isEmpty()) {
            glasses.setTagCompound(tagCompound);

            if(!OCClientSurface.glasses.get().isEmpty() && GlassesNBT.getUniqueId(OCClientSurface.glasses.get()).equals(tagUUID))
                OCClientSurface.instance().initLocalGlasses(glasses);
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    // 1. check helmet/baubles slot, if this fails check the main/off hand
    private ItemStack findGlassesStack(UUID uniqueId){
        ItemStack glassesStack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);

        if(!glassesStack.isEmpty() && GlassesNBT.getUniqueId(glassesStack).equals(uniqueId))
            return glassesStack;

        ItemStack mainHand = Minecraft.getMinecraft().player.getHeldItemMainhand();
        if(OpenGlasses.isGlassesStack(mainHand) && GlassesNBT.getUniqueId(mainHand).equals(uniqueId))
            return mainHand;

        ItemStack offHand = Minecraft.getMinecraft().player.getHeldItemOffhand();
        if(OpenGlasses.isGlassesStack(offHand) && GlassesNBT.getUniqueId(offHand).equals(uniqueId))
            return offHand;

        return ItemStack.EMPTY;
    }

    @Override
    protected IMessage executeOnServer(EntityPlayerMP player) {
        return null;
    }
}


