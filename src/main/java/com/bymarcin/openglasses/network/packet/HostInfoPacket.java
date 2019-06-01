package com.bymarcin.openglasses.network.packet;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import li.cil.oc.api.internal.Case;
import li.cil.oc.api.internal.Microcontroller;
import li.cil.oc.api.internal.Robot;
import li.cil.oc.api.internal.Tablet;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.common.entity.Drone;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.UUID;

import static com.bymarcin.openglasses.surface.OCClientSurface.renderOffsetRobotCaseMicroController;

public class HostInfoPacket extends Packet<HostInfoPacket, IMessage> {

    boolean isInternal;
    double x = 0, y = 0, z = 0;
    int entityID = -1;
    UUID entityUUID;
    int entityDimension;
    String name;

    public enum HostType { TERMINAL, ROBOT, DRONE, TABLET, CASE, MICROCONTROLLER }

    HostType hostType = HostType.TERMINAL;

    public HostInfoPacket(IOpenGlassesHost host) {
        isInternal = host.isInternalComponent();
        name = host.getName();

        if(!isInternal){
            x = host.getRenderPosition().x;
            y = host.getRenderPosition().y;
            z = host.getRenderPosition().z;
            hostType = HostType.TERMINAL;
            return;
        }

        EnvironmentHost realHost = host.getComponent().getHost();

        x = realHost.xPosition();
        y = realHost.yPosition();
        z = realHost.zPosition();

        if(realHost instanceof Robot)
            hostType = HostType.ROBOT;
        else if(realHost instanceof Case)
            hostType = HostType.CASE;
        else if(realHost instanceof Microcontroller)
            hostType = HostType.MICROCONTROLLER;
        else if(realHost instanceof Drone){
            Drone drone = ((Drone) realHost);
            entityID = drone.getEntityId();
            entityUUID = drone.getUniqueID();
            entityDimension = drone.getEntityWorld().provider.getDimension();
            hostType = HostType.DRONE;
        }
        else if(realHost instanceof Tablet){
            Tablet tablet = ((Tablet) realHost);
            entityID = tablet.player().getEntityId();
            entityUUID = tablet.player().getUniqueID();
            entityDimension = tablet.player().getEntityWorld().provider.getDimension();
            hostType = HostType.TABLET;
        }
    }

    public HostInfoPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

    @Override
    protected void read() throws IOException {
        OCClientSurface.instance().terminalName = readString();

        OCClientSurface.instance().renderEntity = null;
        OCClientSurface.instance().isInternal = readBoolean();
        OCClientSurface.instance().hostType = HostType.values()[readInt()];
        OCClientSurface.instance().renderPosition = new Vec3d(readDouble(), readDouble(), readDouble());;

        switch(OCClientSurface.instance().hostType){
            case ROBOT:
                OCClientSurface.instance().renderEntityDimension = readInt();
                OCClientSurface.instance().renderEntityRobot = OCClientSurface.instance().getRobotEntity();
                break;
            case DRONE:
            case TABLET:
                OCClientSurface.instance().renderEntityID = readInt();
                OCClientSurface.instance().renderEntityDimension = readInt();
                OCClientSurface.instance().renderEntityUUID = new UUID(readLong(), readLong());
                break;
            case MICROCONTROLLER:
            case CASE:
                OCClientSurface.instance().renderPosition = OCClientSurface.instance().renderPosition.subtract(renderOffsetRobotCaseMicroController);
                break;
        }
    }

    @Override
    protected void write() throws IOException {
        writeString(name);
        writeBoolean(isInternal);
        writeInt(hostType.ordinal());
        writeDouble(x);
        writeDouble(y);
        writeDouble(z);

        switch(hostType){
            case ROBOT:
                writeInt(entityDimension);
                break;

            case DRONE:
            case TABLET:
                writeInt(entityID);
                writeInt(entityDimension);
                writeLong(entityUUID.getMostSignificantBits());
                writeLong(entityUUID.getLeastSignificantBits());
                break;
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
