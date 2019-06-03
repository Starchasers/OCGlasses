package com.bymarcin.openglasses.network.packet;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.IOpenGlassesHost;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
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

import static com.bymarcin.openglasses.utils.OpenGlassesHostClient.renderOffsetRobotCaseMicroController;

public class HostInfoPacket extends Packet<HostInfoPacket, IMessage> {

    boolean isInternal;
    double x = 0, y = 0, z = 0;
    int entityID = -1;
    UUID hostUUID, entityUUID;
    int entityDimension;
    String name;

    public enum HostType { TERMINAL, ROBOT, DRONE, TABLET, CASE, MICROCONTROLLER }

    HostType hostType = HostType.TERMINAL;

    public HostInfoPacket(IOpenGlassesHost host) {
        isInternal = host.isInternalComponent();
        name = host.getName();
        hostUUID = host.getUUID();

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
        hostUUID = new UUID(readLong(), readLong()); 
        
        OpenGlassesHostClient clientHost = OCClientSurface.instance().getHost(hostUUID);

        clientHost.data().terminalName = readString();

        clientHost.renderEntity = null;
        clientHost.isInternal = readBoolean();
        clientHost.hostType = HostType.values()[readInt()];
        clientHost.renderPosition = new Vec3d(readDouble(), readDouble(), readDouble());;

        switch(clientHost.hostType){
            case ROBOT:
                clientHost.renderEntityDimension = readInt();
                clientHost.renderEntityRobot = clientHost.getRobotEntity();
                break;
            case DRONE:
            case TABLET:
                clientHost.renderEntityID = readInt();
                clientHost.renderEntityDimension = readInt();
                clientHost.renderEntityUUID = new UUID(readLong(), readLong());
                break;
            case MICROCONTROLLER:
            case CASE:
                clientHost.renderPosition = clientHost.renderPosition.subtract(renderOffsetRobotCaseMicroController);
                break;
        }
    }

    @Override
    protected void write() throws IOException {
        writeLong(hostUUID.getMostSignificantBits());
        writeLong(hostUUID.getLeastSignificantBits());
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
