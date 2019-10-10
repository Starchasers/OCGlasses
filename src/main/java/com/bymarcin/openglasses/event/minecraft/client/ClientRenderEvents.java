package com.bymarcin.openglasses.event.minecraft.client;

import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRenderEvents {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre evt) {
        if(!evt.getType().equals(RenderGameOverlayEvent.ElementType.HELMET)) return;

        OCClientSurface.instance().renderOverlay(evt.getPartialTicks());
    }

    @SubscribeEvent
    public static void renderWorldLastEvent(RenderWorldLastEvent event) {

        OCClientSurface.instance().renderWorld(event.getPartialTicks());
    }
}
