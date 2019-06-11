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
    private boolean isInternal;
    private Vec3d position;
    private int entityID = -1;
    private UUID hostUUID, entityUUID;
    private int entityDimension;
    private String name;
    private boolean renderAbsolute = false;

    public enum HostType { TERMINAL, ROBOT, DRONE, TABLET, CASE, MICROCONTROLLER }

    private HostType hostType = HostType.TERMINAL;

    public HostInfoPacket(IOpenGlassesHost host) {
        isInternal = host.isInternalComponent();
        name = host.getName();
        hostUUID = host.getUUID();
        renderAbsolute = host.renderAbsolute();

        if(!isInternal){
            position = host.getRenderPosition();
            hostType = HostType.TERMINAL;
            return;
        }

        EnvironmentHost realHost = host.getHost();

        position = new Vec3d(realHost.xPosition(), realHost.yPosition(), realHost.zPosition());

        if(realHost instanceof Robot)
            hostType = HostType.ROBOT;
        else if(realHost instanceof Case)
            hostType = HostType.CASE;
        else if(realHost instanceof Microcontroller)
            hostType = HostType.MICROCONTROLLER;
        else if(realHost instanceof Drone){
            Drone drone = ((Drone) realHost);
            //Entity realEntity = drone; //FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(drone.getUniqueID());
            //realEntity = realEntity != null ? realEntity : drone;
            entityID = drone.getEntityId();
            entityUUID = drone.getUniqueID();
            entityDimension = drone.getEntityWorld().provider.getDimension();
            hostType = HostType.DRONE;
        }
        else if(realHost instanceof Tablet){
            Tablet tablet = ((Tablet) realHost);
            //Entity realEntity = tablet.player(); //FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(tablet.player().getUniqueID());
            //realEntity = realEntity != null ? realEntity : tablet.player();
            entityID = tablet.player().getEntityId();
            entityUUID = tablet.player().getUniqueID();
            entityDimension = tablet.player().getEntityWorld().provider.getDimension();
            hostType = HostType.TABLET;
        }
    }

    public HostInfoPacket() {}  //dont remove, in use by NetworkRegistry.registerPacket in OpenGlasses.java

    @Override
    protected void read() throws IOException {
        hostUUID = readUUID();

        name = readString();

        renderAbsolute = readBoolean();
        isInternal = readBoolean();
        hostType = HostType.values()[readInt()];
        position = readVec3d();

        switch(hostType){
            case ROBOT:
                entityDimension = readInt();
                break;
            case DRONE:
            case TABLET:
                entityID = readInt();
                entityDimension = readInt();
                entityUUID = readUUID();
                break;
            case MICROCONTROLLER:
            case CASE:
                break;
        }
    }

    @Override
    protected void write() throws IOException {
        writeUUID(hostUUID);
        writeString(name);
        writeBoolean(renderAbsolute);
        writeBoolean(isInternal);
        writeInt(hostType.ordinal());
        writeVec3d(position);

        switch(hostType){
            case ROBOT:
                writeInt(entityDimension);
                break;

            case DRONE:
            case TABLET:
                writeInt(entityID);
                writeInt(entityDimension);
                writeUUID(entityUUID);
                break;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected IMessage executeOnClient() {
        OpenGlassesHostClient clientHost = OCClientSurface.instance().getHost(hostUUID);

        clientHost.data().terminalName = name;

        clientHost.renderEntity = null;
        clientHost.absoluteRenderPosition = renderAbsolute;
        clientHost.isInternal = isInternal;
        clientHost.hostType = hostType;
        clientHost.renderPosition = position;

        switch(clientHost.hostType){
            case ROBOT:
                clientHost.renderEntityDimension = entityDimension;
                clientHost.renderEntityRobot = clientHost.getRobotEntity();
                break;
            case DRONE:
            case TABLET:
                clientHost.renderEntityID = entityID;
                clientHost.renderEntityDimension = entityDimension;
                clientHost.renderEntityUUID = entityUUID;
                break;
            case MICROCONTROLLER:
            case CASE:
                clientHost.renderPosition = clientHost.renderPosition.subtract(renderOffsetRobotCaseMicroController);
                break;
        }
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        return null;
    }
}
