package com.bymarcin.openglasses.network.packet;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

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
        ItemStack glasses = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);

        if(!glasses.isEmpty()) {
            glasses.setTagCompound(tagCompound);
            OCClientSurface.instance().initLocalGlasses(glasses);
        }
        return null;
    }

    @Override
    protected IMessage executeOnServer(EntityPlayerMP player) {
        return null;
    }
}


