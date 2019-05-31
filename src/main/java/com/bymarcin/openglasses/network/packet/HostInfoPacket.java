package com.bymarcin.openglasses.network.packet;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class HostInfoPacket extends Packet<HostInfoPacket, IMessage> {

    boolean isInternal;
    int x,y,z;

    public HostInfoPacket(IOpenGlassesHost host) {
        isInternal = host.isInternalComponent();
        if(!isInternal){
            x = (int) host.getRenderPosition().x;
            y = (int) host.getRenderPosition().y;
            z = (int) host.getRenderPosition().z;
        }
    }

    public HostInfoPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

    @Override
    protected void read() throws IOException {
        isInternal = readBoolean();

        if(!isInternal) {
            OCClientSurface.instance().renderPosition = new Vec3d(readInt(), readInt(), readInt());
        }

        OCClientSurface.instance().isInternalComponent = isInternal;

    }

    @Override
    protected void write() throws IOException {
        writeBoolean(isInternal);
        if(!isInternal){
            writeInt(x);
            writeInt(y);
            writeInt(z);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected IMessage executeOnClient() { return null; }

    @Override
    protected IMessage executeOnServer() {
        return null;
    }
}
