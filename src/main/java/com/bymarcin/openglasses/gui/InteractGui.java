package com.bymarcin.openglasses.gui;

import com.bymarcin.openglasses.event.minecraft.client.ClientKeyboardEvents;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.GlassesInstance;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class InteractGui extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(OCClientSurface.instances.getRenderResolution(null) != null){
            mouseX*=(OCClientSurface.instances.getRenderResolution(null).x / OCClientSurface.instances.resolution.getScaledWidth());
            mouseY*=(OCClientSurface.instances.getRenderResolution(null).y / OCClientSurface.instances.resolution.getScaledHeight());
        }

        for(GlassesInstance.HostClient host : OCClientSurface.instance().glasses.getHosts().values())
            if(host.sendOverlayEvents)
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.uuid, GlassesEventPacket.EventType.INTERACT_OVERLAY, mouseX, mouseY, mouseButton));
    }

    @Override
    public void updateScreen() {
        if(!Keyboard.isKeyDown(ClientKeyboardEvents.interactGUIKey.getKeyCode())){
            OCClientSurface.instance().glasses.conditions.setOverlay(false);

            for(GlassesInstance.HostClient host : OCClientSurface.instance().glasses.getHosts().values())
                if(host.sendOverlayEvents)
                    NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.uuid, GlassesEventPacket.EventType.DEACTIVATE_OVERLAY));

            mc.displayGuiScreen(null);
        }
    }
}
