package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyElement;
import ben_mkiv.guitoolkit.client.widget.prettyGuiList;
import com.bymarcin.openglasses.item.upgrades.UpgradeNavigation;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.GlassesInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

import java.util.*;

import static ben_mkiv.rendertoolkit.surface.ClientSurface.vec3d000;

public class TerminalHostsList extends prettyGuiList {
    private HashMap<Integer, ArrayList<String>> data = new HashMap<>();
    private HashMap<Integer, HashSet<prettyElement>> elements = new HashMap<>();

    private int elementCount = 0;

    public TerminalHostsList(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(width, height, top, top + height, left, entryHeight, screenWidth, screenHeight);
    }

    class SortHostClient implements Comparator<GlassesInstance.HostClient> {
        @Override
        public int compare(GlassesInstance.HostClient a, GlassesInstance.HostClient b){
            return a.uuid.compareTo(b.uuid);
        }
    }

    public void add(Collection<GlassesInstance.HostClient> hosts){
        ArrayList<GlassesInstance.HostClient> list = new ArrayList<>(hosts);
        Collections.sort(list, new SortHostClient());

        for (GlassesInstance.HostClient host : list)
            add(host);
    }

    public void add(GlassesInstance.HostClient host){
        HashSet<prettyElement> listElements = new HashSet<>();

        listElements.add(new hostGuiElement.GlassesEventButton(host.uuid, 170, 10, 60, 20, "clear link", GlassesEventPacket.EventType.CLEAR_LINK));
        listElements.add(new renderWorldButton(host));
        listElements.add(new renderOverlayButton(host));
        listElements.add(new worldEventsButton(host));
        listElements.add(new overlayEventsButton(host));


        ArrayList<String> text = new ArrayList<>();
        if(host.getHost().terminalName.length() > 0)
            text.add("'" + host.getHost().terminalName + "' ("+host.uuid.toString()+")");
        else
            text.add(host.uuid.toString());

        text.add("linked as " + host.ownerName);

        Vec3d renderPosition = host.getHost().getRenderPosition(0.5f);
        if(renderPosition.equals(vec3d000))
            text.add("distance: unknown");
        else
            text.add("distance: " + (int) Math.round(renderPosition.distanceTo(Minecraft.getMinecraft().player.getPositionVector())) + " blocks");

        boolean renderAbsolute = host.getHost().absoluteRenderPosition;

        text.add("renderposition: " + (renderAbsolute ? "absolute" : "relative"));

        if(renderAbsolute && !UpgradeNavigation.hasUpgrade(OCClientSurface.instance().glasses.get()))
            text.add("§cworld widgets require navigation upgrade");

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
        worldEventsButton(GlassesInstance.HostClient host){
            super(host.uuid, 110, 45, "world events", host.sendWorldEvents);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_EVENTS : GlassesEventPacket.EventType.ENABLE_WORLD_EVENTS);
        }
    }

    public static class overlayEventsButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        overlayEventsButton(GlassesInstance.HostClient host){
            super(host.uuid, 110, 60, "overlay events", host.sendOverlayEvents);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_EVENTS : GlassesEventPacket.EventType.ENABLE_OVERLAY_EVENTS);
        }
    }

    public static class renderWorldButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        renderWorldButton(GlassesInstance.HostClient host){
            super(host.uuid, 3, 45, "render world", host.renderWorld);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_RENDER : GlassesEventPacket.EventType.ENABLE_WORLD_RENDER);
        }
    }

    public static class renderOverlayButton extends hostGuiElement.hostCheckbox implements hostGuiElement.hostAction {
        renderOverlayButton(GlassesInstance.HostClient host){
            super(host.uuid, 3, 60, "render overlay", host.renderOverlay);
        }

        public void clicked() {
            sendEvent(isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_RENDER : GlassesEventPacket.EventType.ENABLE_OVERLAY_RENDER);
        }
    }

}
