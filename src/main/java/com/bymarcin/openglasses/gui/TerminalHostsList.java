package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyElement;
import ben_mkiv.guitoolkit.client.widget.prettyGuiList;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;

import java.util.*;

public class TerminalHostsList extends prettyGuiList {
    private HashMap<Integer, ArrayList<String>> data = new HashMap<>();
    private HashMap<Integer, HashSet<prettyElement>> elements = new HashMap<>();

    private int elementCount = 0;

    public TerminalHostsList(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(width, height, top, top + height, left, entryHeight, screenWidth, screenHeight);
    }

    class SortOpenGlassesHostClient implements Comparator<OpenGlassesHostClient> {
        @Override
        public int compare(OpenGlassesHostClient a, OpenGlassesHostClient b){
            return a.getUniqueId().compareTo(b.getUniqueId());
        }
    }

    public void add(Collection<OpenGlassesHostClient> hosts){
        ArrayList<OpenGlassesHostClient> list = new ArrayList<>(hosts);
        Collections.sort(list, new SortOpenGlassesHostClient());

        for (OpenGlassesHostClient host : list)
            add(host);
    }

    public void add(OpenGlassesHostClient host){
        HashSet<prettyElement> listElements = new HashSet<>();

        listElements.add(new hostGuiElement.GlassesEventButton(host.getUniqueId(), 170, 10, 60, 20, "clear link", GlassesEventPacket.EventType.CLEAR_LINK));
        listElements.add(new renderWorldButton(host));
        listElements.add(new renderOverlayButton(host));
        listElements.add(new worldEventsButton(host));
        listElements.add(new overlayEventsButton(host));


        ArrayList<String> text = new ArrayList<>();
        if(host.data().terminalName.length() > 0)
            text.add("host '" + host.data().terminalName + "' ("+host.getUniqueId().toString()+")");
        else
            text.add("host " + host.getUniqueId().toString());

        text.add("linked as " + host.data().ownerName);

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
                if(element instanceof GuiButton) {
                    ((GuiButton) element).enabled = isSelected(slotIdx);
                    ((GuiButton) element).drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks());
                }
            }

        int textColor = isSelected(slotIdx) ? 0xFFFFFF : 0xDADADA;

        if(data.containsKey(slotIdx))
            for(String text : data.get(slotIdx)) {
                Minecraft.getMinecraft().fontRenderer.drawString(" " + text, left, slotTop, textColor);
                slotTop+=10;
            }
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if(elements.containsKey(index)){
            for(prettyElement element : elements.get(index)) {
                if(element instanceof hostGuiElement.hostAction && element.isMouseOver()) {

                    if(element instanceof GuiButton && !((GuiButton) element).enabled)
                        continue;

                    element.clicked();
                }
            }
        }

        super.elementClicked(index, doubleClick);
    }


    public static class worldEventsButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        worldEventsButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 110, 25, "world events", host.data().sendWorldEvents);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_EVENTS : GlassesEventPacket.EventType.ENABLE_WORLD_EVENTS);
        }
    }

    public static class overlayEventsButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        overlayEventsButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 110, 40, "overlay events", host.data().sendOverlayEvents);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_EVENTS : GlassesEventPacket.EventType.ENABLE_OVERLAY_EVENTS);
        }
    }

    public static class renderWorldButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        renderWorldButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 3, 25, "render world", host.data().renderWorld);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_RENDER : GlassesEventPacket.EventType.ENABLE_WORLD_RENDER);
        }
    }

    public static class renderOverlayButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        renderOverlayButton(OpenGlassesHostClient host){
            super(host.getUniqueId(), 3, 40, "render overlay", host.data().renderOverlay);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_RENDER : GlassesEventPacket.EventType.ENABLE_OVERLAY_RENDER);
        }
    }

}
