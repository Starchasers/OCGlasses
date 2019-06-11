package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyButton;
import ben_mkiv.guitoolkit.client.widget.prettyCheckbox;
import ben_mkiv.guitoolkit.client.widget.prettyElement;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;

import java.util.UUID;

public class hostGuiElement {
    public interface hostAction extends hostElement, prettyElement {
        default void sendEvent(GlassesEventPacket.EventType event){
            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(getUniqueId(), event));
        }
    }

    public interface hostElement{
        UUID getUniqueId();
    }

    public static class hostButton extends prettyButton implements hostElement{
        private UUID host;

        hostButton(UUID hostUUID, int x, int y, int width, int height, String label){
            super(0, x, y, width, height, label);
            host = hostUUID;
        }

        public UUID getUniqueId(){
            return host;
        }
    }

    public static class hostCheckbox extends prettyCheckbox implements hostElement {
        private UUID host;

        hostCheckbox(UUID hostUUID, int x, int y, String label, boolean state){
            super(0, x, y, label, state);
            host = hostUUID;
            packedFGColour = 0xFFFFFF;
        }

        public UUID getUniqueId(){
            return host;
        }
    }


    public static class GlassesEventButton extends hostButton implements hostAction {
        private GlassesEventPacket.EventType event;

        GlassesEventButton(UUID hostUUID, int x, int y, int width, int height, String label, GlassesEventPacket.EventType event){
            super(hostUUID, x, y, width, height, label);
            this.event = event;
        }

        public void clicked() {
            sendEvent(event);
        }
    }
}
