package com.bymarcin.openglasses.event.minecraft.client;

import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    /* all methods  have to be static to be registered */

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent e){
        if(e.player.equals(Minecraft.getMinecraft().player))
            OCClientSurface.instance().update(e.player);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        OCClientSurface.instance().onLeave();
    }

    @SubscribeEvent
    public static void onSizeChange(RenderGameOverlayEvent event) {
        if(event.getResolution().getScaledWidth() != OCClientSurface.resolution.getScaledWidth()
        || event.getResolution().getScaledHeight() != OCClientSurface.resolution.getScaledHeight()) {
            OCClientSurface.resolution = event.getResolution();
            OCClientSurface.instance().sendResolution();
        }
    }

    @SubscribeEvent
    public static void equipmentChanged(LivingEquipmentChangeEvent event){
        if(!event.getEntityLiving().equals(Minecraft.getMinecraft().player))
            return;

        if(event.getSlot().equals(EntityEquipmentSlot.HEAD))
            OCClientSurface.instance().equipmentChanged(event.getTo());
    }

}
