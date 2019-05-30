package com.bymarcin.openglasses.gui;

import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
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
        if(((OCClientSurface) OCClientSurface.instances).glassesStack.isEmpty()) return;
        if(OCClientSurface.instances.renderResolution != null){
            mouseX*=(OCClientSurface.instances.renderResolution.x / OCClientSurface.instances.resolution.getScaledWidth());
            mouseY*=(OCClientSurface.instances.renderResolution.y / OCClientSurface.instances.resolution.getScaledHeight());
        }

        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.INTERACT_OVERLAY, mouseX, mouseY, mouseButton));
    }

    @Override
    public void updateScreen() {
        if(!Keyboard.isKeyDown(ClientEventHandler.interactGUIKey.getKeyCode())){
            ((OCClientSurface) OCClientSurface.instances).conditions.setOverlay(false);
            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.DEACTIVATE_OVERLAY));
            mc.displayGuiScreen(null);
        }
    }
}
