package com.bymarcin.openglasses.utils;

import ben_mkiv.rendertoolkit.surface.WidgetCollection;
import com.bymarcin.openglasses.network.packet.HostInfoPacket;
import li.cil.oc.api.internal.Robot;
import li.cil.oc.common.tileentity.RobotProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.UUID;

import static com.bymarcin.openglasses.surface.OCClientSurface.getEntityLocation;

public class OpenGlassesHostClient extends WidgetCollection {
    public Vec3d renderPosition = new Vec3d(0, 0, 0);
    public Entity renderEntity = null;
    public UUID renderEntityUUID = null;
    public int renderEntityID = -1;
    public int renderEntityDimension;
    public Robot renderEntityRobot;
    public boolean absoluteRenderPosition = false;

    public UUID hostUUID;

    public String terminalName = "";


    public HostInfoPacket.HostType hostType = HostInfoPacket.HostType.TERMINAL;

    public boolean isInternal = false;

    public OpenGlassesHostClient(UUID hostUUID){
        this.hostUUID = hostUUID;
    }

    public Robot getRobotEntity(){
        if(renderEntityRobot == null && renderEntityDimension == Minecraft.getMinecraft().world.provider.getDimension()){
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(renderPosition));
            if(tile instanceof li.cil.oc.common.tileentity.RobotProxy)
                renderEntityRobot = ((RobotProxy) tile).robot();
        }

        return renderEntityRobot;
    }

    private Entity getRenderEntity(){
        ArrayList<Entity> entityArrayList = new ArrayList<>();
        switch(hostType){
            case TABLET:
            case DRONE:
                if(renderEntity == null && renderEntityID != -1
                        && renderEntityDimension == Minecraft.getMinecraft().player.world.provider.getDimension()){
                    renderEntity = Minecraft.getMinecraft().world.getEntityByID(renderEntityID);
                    entityArrayList.addAll(Minecraft.getMinecraft().world.getLoadedEntityList());
                }
                return renderEntity;

            default:
                return null;
        }
    }

    Vec3d getRobotLocation(Robot robot, float partialTicks){
        if(robot == null)
            return renderPosition;

        li.cil.oc.common.tileentity.Robot casted = (li.cil.oc.common.tileentity.Robot) robot;
        Vec3d offset = new Vec3d(0, 0, 0);

        if(casted != null && casted.isAnimatingMove()){
            double remaining = (casted.animationTicksLeft() - partialTicks) / (double) casted.animationTicksTotal();
            Vec3d location = new Vec3d(casted.position().x(), casted.position().y(), casted.position().z());
            Vec3d moveFrom = new Vec3d(casted.moveFrom().get()).subtract(location);

            offset = moveFrom.scale(remaining);
        }

        return renderPosition = new Vec3d(robot.xPosition(), robot.yPosition(), robot.zPosition()).add(offset);
    }

    private final static Vec3d renderOffsetTabletDrone = new Vec3d(0.5, 0, 0.5);
    public final static Vec3d renderOffsetRobotCaseMicroController = new Vec3d(0.5, 0.5, 0.5);

    public Vec3d getRenderPosition(float partialTicks){
        switch (hostType){
            case DRONE:
            case TABLET:
                return getEntityLocation(getRenderEntity(), partialTicks).subtract(renderOffsetTabletDrone);

            case ROBOT: // render offset for case/microcontroller is resolved when HostInfoPacket is received
                return getRobotLocation(getRobotEntity(), partialTicks).subtract(renderOffsetRobotCaseMicroController);

            default:
                return renderPosition;
        }
    }

}
