package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyButton;
import ben_mkiv.guitoolkit.client.widget.prettyCheckbox;
import ben_mkiv.guitoolkit.client.widget.prettyElement;
import ben_mkiv.guitoolkit.client.widget.prettyGuiList;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;

import java.util.*;

public class TerminalHostsList extends prettyGuiList {
    HashMap<Integer, ArrayList<String>> data = new HashMap<>();
    HashMap<Integer, HashSet<prettyElement>> elements = new HashMap<>();

    int elementCount = 0;

    public TerminalHostsList(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(width, height, top, top + height, left, entryHeight, screenWidth, screenHeight);
    }

    public void add(Collection<OpenGlassesHostClient> hosts){
        for (OpenGlassesHostClient host : hosts)
            add(host);
    }

    public void add(OpenGlassesHostClient host){
        HashSet<prettyElement> listElements = new HashSet<>();

        listElements.add(new clearLinkButton(host));
        listElements.add(new renderWorldButton(host));
        listElements.add(new renderOverlayButton(host));
        listElements.add(new worldEventsButton(host));
        listElements.add(new overlayEventsButton(host));


        ArrayList<String> text = new ArrayList<>();
        if(host.data().terminalName.length() > 0)
            text.add("host '" + host.data().terminalName + "' ("+host.getUniqueId().toString()+")");
        else
            text.add("host " + host.getUniqueId().toString());

        text.add("distance: " + (int) Math.round(host.getRenderPosition(0.5f).distanceTo(Minecraft.getMinecraft().player.getPositionVector())) + " blocks");

        elements.put(elementCount, listElements);
        data.put(elementCount, text);

        elementCount++;
    }

    public void clear(){
        elementCount = 0;
        elements.clear();
        data.clear();
    }

    @Override
    public int getSize() {
        return elementCount;
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        Minecraft mc = Minecraft.getMinecraft();

        if(elements.containsKey(slotIdx))
            for(prettyElement element : elements.get(slotIdx)) {
                element.setRenderY(slotTop);
                element.setRenderX(left);
                if(element instanceof GuiButton)
                    ((GuiButton) element).drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks());
            }

        if(data.containsKey(slotIdx))
            for(String text : data.get(slotIdx)) {
                Minecraft.getMinecraft().fontRenderer.drawString(" " + text, left, slotTop, 0xFFFF00);
                slotTop+=10;
            }
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if(elements.containsKey(index)){
            for(prettyElement element : elements.get(index)) {
                if(element instanceof hostAction && ((hostAction) element).isMouseOver())
                    ((hostAction) element).clicked();
            }
        }

        super.elementClicked(index, doubleClick);
    }

    public static class clearLinkButton extends hostButton implements hostAction {
        clearLinkButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 170, 10, 60, 20, "clear link");
        }

        public void clicked() {
            sendEvent(GlassesEventPacket.EventType.CLEAR_LINK);
        }
    }

    public static class worldEventsButton extends hostCheckbox implements hostAction {
        worldEventsButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 110, 25, "world events", host.data().sendWorldEvents);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_EVENTS : GlassesEventPacket.EventType.ENABLE_WORLD_EVENTS);
        }
    }

    public static class overlayEventsButton extends hostCheckbox implements hostAction {
        overlayEventsButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 110, 40, "overlay events", host.data().sendOverlayEvents);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_EVENTS : GlassesEventPacket.EventType.ENABLE_OVERLAY_EVENTS);
        }
    }

    public static class renderWorldButton extends hostCheckbox implements hostAction {
        renderWorldButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 3, 25, "render world", host.data().renderWorld);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_RENDER : GlassesEventPacket.EventType.ENABLE_WORLD_RENDER);
        }
    }

    public static class renderOverlayButton extends hostCheckbox implements hostAction {
        renderOverlayButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 3, 40, "render overlay", host.data().renderOverlay);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_RENDER : GlassesEventPacket.EventType.ENABLE_OVERLAY_RENDER);
        }
    }

    public interface hostAction extends hostElement {
        void clicked();
        boolean isMouseOver();

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
}
