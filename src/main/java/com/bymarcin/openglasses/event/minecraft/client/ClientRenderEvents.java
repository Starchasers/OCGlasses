package com.bymarcin.openglasses.event.minecraft.client;

import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRenderEvents {
    static boolean fogEnabled = true;

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent evt) {
        if (evt.getType() != RenderGameOverlayEvent.ElementType.HELMET) return;
        if (!(evt instanceof RenderGameOverlayEvent.Post)) return;

        OCClientSurface.instance().renderOverlay(evt.getPartialTicks());
    }

    @SubscribeEvent
    public static void renderWorldLastEvent(RenderWorldLastEvent event) {
        OCClientSurface.instance().renderWorld(event.getPartialTicks());
    }

    @SubscribeEvent
    public static void renderFogEvent(EntityViewRenderEvent.FogDensity event) {
        if(fogEnabled) {
            event.setDensity(0.1f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderFogEvent(EntityViewRenderEvent.FogColors event) {
        if(fogEnabled) {
            event.setBlue(0);
            event.setGreen(1);
            event.setRed(0);
        }
    }

}
