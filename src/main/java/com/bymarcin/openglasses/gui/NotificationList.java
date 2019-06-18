package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyElement;
import ben_mkiv.guitoolkit.client.widget.prettyGuiList;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesNotificationsNBT;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.UUID;

import static ben_mkiv.rendertoolkit.surface.ClientSurface.vec3d000;

public class NotificationList extends prettyGuiList {

    ArrayList<ArrayList<prettyElement>> guiElements = new ArrayList<>();
    ArrayList<NBTTagCompound> elements = new ArrayList<>();


    public NotificationList(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(width, height, top, top + height, left, entryHeight, screenWidth, screenHeight);
    }

    public void update(){
        if(elements.equals(OpenGlassesNotificationsNBT.getNotifications(OCClientSurface.instance().glasses.get())))
            return;


        elements.clear();
        guiElements.clear();

        elements.addAll(OpenGlassesNotificationsNBT.getNotifications(OCClientSurface.instance().glasses.get()));

        for(NBTTagCompound tag : elements) {
            ArrayList<prettyElement> listElements = new ArrayList<>();

            switch(OpenGlassesNotificationsNBT.NotifiactionType.values()[tag.getInteger("type")]) {
                case LINKREQUEST:
                    listElements.add(new LinkRequestButton(tag,  0, 22, 60, 20, "accept link", true));
                    listElements.add(new LinkRequestButton(tag, 60, 22, 60, 20, "deny link", false));
                    break;
            }

            guiElements.add(listElements);
        }
    }

    class LinkRequestButton extends hostGuiElement.hostButton implements hostGuiElement.hostAction {
        boolean acceptLinkRequest;

        public LinkRequestButton(NBTTagCompound tag, int x, int y, int width, int height, String label, boolean acceptLink){
            super(tag.getUniqueId("host"), x, y, width, height, label);
            acceptLinkRequest = acceptLink;
        }

        @Override
        public void clicked(){
            if(!enabled)
                return;

            if(acceptLinkRequest)
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(getUniqueId(), GlassesEventPacket.EventType.ACCEPT_LINK));
            else
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(getUniqueId(), GlassesEventPacket.EventType.DENY_LINK));
        }
    }


    @Override
    public int getSize(){
        return elements.size();
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

        switch(OpenGlassesNotificationsNBT.NotifiactionType.values()[elements.get(slotIdx).getInteger("type")]) {
            case LINKREQUEST:
                drawLinkRequest(elements.get(slotIdx), slotTop, isSelected(slotIdx));
                break;
        }

        Minecraft mc = Minecraft.getMinecraft();

        for(prettyElement element : guiElements.get(slotIdx)) {
            element.setRenderY(slotTop);
            element.setRenderX(left);
            if(element instanceof GuiButton) {
                ((GuiButton) element).enabled = isSelected(slotIdx);
                ((GuiButton) element).drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks());
            }
        }
    }


    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        for(prettyElement element : guiElements.get(index)) {
            if(element instanceof hostGuiElement.hostAction && element.isMouseOver())
                element.clicked();
        }

        super.elementClicked(index, doubleClick);
    }


    void drawLinkRequest(NBTTagCompound tag, int slotTop, boolean isSelected){
        UUID hostUUID = tag.getUniqueId("host");
        OpenGlassesHostClient host = OCClientSurface.instance().getHost(hostUUID);

        Vec3d renderPosition = host.getRenderPosition(0.5f);

        String text = "link request";

        if(host.terminalName.length() > 0)
            text+=" from '"+host.terminalName+"'";


        if(renderPosition.equals(vec3d000))
            text+=" (distance: unknown)";
        else {
            int distance = (int) Math.ceil(Minecraft.getMinecraft().player.getPositionVector().distanceTo(renderPosition));
            text+=" (distance: "+ distance +" blocks)";
        }




        int textColor = isSelected ? 0xFFFFFF : 0xDADADA;

        Minecraft.getMinecraft().fontRenderer.drawString(text, left + 3, slotTop + 2, textColor);
        Minecraft.getMinecraft().fontRenderer.drawString(hostUUID.toString(), left + 3, slotTop + 13, textColor);
    }
}
