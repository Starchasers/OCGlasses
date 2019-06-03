package com.bymarcin.openglasses.utils;

import ben_mkiv.rendertoolkit.surface.WidgetCollection;
import com.bymarcin.openglasses.network.packet.HostInfoPacket;
import li.cil.oc.api.internal.Robot;
import li.cil.oc.common.tileentity.RobotProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

import static com.bymarcin.openglasses.surface.OCClientSurface.getEntityLocation;

public class OpenGlassesHostClient extends WidgetCollection {
    public Vec3d renderPosition = new Vec3d(0, 0, 0);
    public Entity renderEntity = null;
    public UUID renderEntityUUID = null;
    public int renderEntityID = -1;
    public int renderEntityDimension;
    public Robot renderEntityRobot;

    HostClient host;

    public static class HostClient {
        public UUID uuid = null;
        public String terminalName = "";
        public boolean renderWorld = true, renderOverlay = true;
        public boolean sendOverlayEvents = true, sendWorldEvents = true;

        public HostClient(UUID host){
            uuid = host;
        }

        public HostClient(NBTTagCompound nbt){
            updateFromNBT(nbt);
        }

        public void updateFromNBT(NBTTagCompound nbt){
            uuid = nbt.getUniqueId("host");
            renderWorld = !nbt.hasKey("noWorld");
            renderOverlay = !nbt.hasKey("noOverlay");
            sendOverlayEvents = !nbt.hasKey("noOverlayEvents");
            sendWorldEvents = !nbt.hasKey("noWorldEvents");
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

            return tag;
        }
    }

    public HostInfoPacket.HostType hostType = HostInfoPacket.HostType.TERMINAL;

    public boolean isInternal = false;

    public OpenGlassesHostClient(NBTTagCompound nbt){
        host = new HostClient(nbt);
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
        switch(hostType){
            case TABLET:
            case DRONE:
                if(renderEntity == null && renderEntityID != -1
                        && renderEntityDimension == Minecraft.getMinecraft().player.world.provider.getDimension()){
                    renderEntity = Minecraft.getMinecraft().world.getEntityByID(renderEntityID);
                }
                return renderEntity;

            default:
                return null;
        }
    }

    public UUID getUniqueId(){
        return host.uuid;
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

    public HostClient data(){
        return host;
    }


}
