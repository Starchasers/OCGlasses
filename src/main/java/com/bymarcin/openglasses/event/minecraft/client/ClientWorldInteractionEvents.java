package com.bymarcin.openglasses.event.minecraft.client;

import com.bymarcin.openglasses.item.upgrades.UpgradeGeolyzer;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.GlassesInstance;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientWorldInteractionEvents {
    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
        onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_LEFT, e);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(UpgradeGeolyzer.hasUpgrade(OCClientSurface.instance().glasses.get()))
            onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_BLOCK_LEFT, e);
        else
            onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_LEFT, e);
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e){
        onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_RIGHT, e);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem e){
        onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_RIGHT, e);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock e){
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(UpgradeGeolyzer.hasUpgrade(OCClientSurface.instance().glasses.get()))
            onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_BLOCK_RIGHT, e);
        else
            onInteractEvent(GlassesEventPacket.EventType.INTERACT_WORLD_RIGHT, e);
    }

    private static void onInteractEvent(GlassesEventPacket.EventType type, PlayerInteractEvent event){
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(OCClientSurface.instance().getHosts().size() == 0) return;
        if(!event.getSide().isClient()) return;
        if(!event.getHand().equals(EnumHand.MAIN_HAND)) return;

        for(GlassesInstance.HostClient host : OCClientSurface.instance().glasses.getHosts().values())
            if(host.sendWorldEvents)
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.uuid, type, event.getPos(), event.getFace()));
    }

}
