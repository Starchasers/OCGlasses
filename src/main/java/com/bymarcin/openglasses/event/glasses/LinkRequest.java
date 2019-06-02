package com.bymarcin.openglasses.event.glasses;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.GlassesGui;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class LinkRequest implements GlassesNotifications.GlassesNotification {
    private long linkRequestActive = 0;
    public UUID host;
    public BlockPos pos;
    public String hostName = "";
    int timeout = 120;

    public LinkRequest(UUID hostUUID, BlockPos hostPosition, String terminalName){
        linkRequestActive = System.currentTimeMillis();
        host = hostUUID;
        pos = hostPosition;
        hostName = terminalName;

        for(GlassesNotifications.GlassesNotification notification : GlassesNotifications.notifications){
            if(notification instanceof LinkRequest && ((LinkRequest) notification).host.equals(hostUUID))
                return;
        }

        GlassesNotifications.notifications.add(this);

        ItemStack glassesStack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);
        if(!glassesStack.isEmpty()) {
            if(!glassesStack.getTagCompound().hasKey("nopopups") && !(Minecraft.getMinecraft().currentScreen instanceof GlassesGui))
                Minecraft.getMinecraft().displayGuiScreen(new GlassesGui(true));
        }
    }

    @Override
    public void update(){
        if(System.currentTimeMillis() - linkRequestActive > timeout * 1000) {
            cancel();
        }
    }

    @Override
    public void submit(){
        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host, GlassesEventPacket.EventType.ACCEPT_LINK));
        remove();
    }

    @Override
    public void cancel(){
        remove();
        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host, GlassesEventPacket.EventType.DENY_LINK));
    }

    void remove(){
        linkRequestActive = 0;
        GlassesNotifications.notifications.remove(this);

        ItemStack glassesStack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);
        if(!glassesStack.isEmpty()
                && Minecraft.getMinecraft().currentScreen instanceof GlassesGui
                && GlassesGui.isNotification) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    public int getDistance(Vec3d target){
        return (int) Math.round(target.distanceTo(new Vec3d(pos)));
    }
}
