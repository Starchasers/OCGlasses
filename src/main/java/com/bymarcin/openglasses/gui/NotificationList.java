package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyElement;
import ben_mkiv.guitoolkit.client.widget.prettyGuiList;
import com.bymarcin.openglasses.event.glasses.GlassesNotifications;
import com.bymarcin.openglasses.event.glasses.LinkRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;

import java.util.ArrayList;
import java.util.HashSet;

import static com.bymarcin.openglasses.event.glasses.GlassesNotifications.notifications;

public class NotificationList extends prettyGuiList {

    ArrayList<GlassesNotifications.GlassesNotification> elements = new ArrayList<>();
    ArrayList<ArrayList<prettyElement>> guiElements = new ArrayList<>();

    public NotificationList(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(width, height, top, top + height, left, entryHeight, screenWidth, screenHeight);
    }

    public void update(){
        //avoid concurrent modifications
        HashSet<GlassesNotifications.GlassesNotification> tempList = new HashSet<>();
        tempList.addAll(GlassesNotifications.notifications);

        for(GlassesNotifications.GlassesNotification notification : tempList){
            notification.update();
        }

        if(elements.equals(notifications))
            return;

        elements.clear();
        guiElements.clear();

        for(GlassesNotifications.GlassesNotification notification : notifications) {
            elements.add(notification);


            ArrayList<prettyElement> listElements = new ArrayList<>();

            if(notification instanceof LinkRequest) {
                listElements.add(new LinkRequestButton((LinkRequest) notification,  0, 22, 60, 20, "accept link", true));
                listElements.add(new LinkRequestButton((LinkRequest) notification, 60, 22, 60, 20, "deny link", false));
            }

            guiElements.add(listElements);
        }
    }

    class LinkRequestButton extends hostGuiElement.hostButton implements hostGuiElement.hostAction {
        boolean acceptLinkRequest;
        LinkRequest linkRequest;

        public LinkRequestButton(LinkRequest linkRequest, int x, int y, int width, int height, String label, boolean acceptLink){
            super(linkRequest.host, x, y, width, height, label);
            acceptLinkRequest = acceptLink;
            this.linkRequest = linkRequest;
        }

        @Override
        public void clicked(){
            if(!enabled)
                return;

            if(acceptLinkRequest)
                linkRequest.submit();
            else
                linkRequest.cancel();
        }
    }


    @Override
    public int getSize(){
        return elements.size();
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if(elements.get(slotIdx) instanceof LinkRequest)
            drawLinkRequest((LinkRequest) elements.get(slotIdx), slotTop, isSelected(slotIdx));

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


    void drawLinkRequest(LinkRequest linkRequest, int slotTop, boolean isSelected){
        int distance = linkRequest.getDistance(Minecraft.getMinecraft().player.getPositionVector());
        String text = "link request";

        if(linkRequest.hostName.length() > 0)
            text+=" from '"+linkRequest.hostName+"'";

        text+=" (distance: "+ distance +" blocks)";

        int textColor = isSelected ? 0xFFFFFF : 0xDADADA;

        Minecraft.getMinecraft().fontRenderer.drawString(text, left + 3, slotTop + 2, textColor);
        Minecraft.getMinecraft().fontRenderer.drawString(linkRequest.host.toString(), left + 3, slotTop + 13, textColor);
    }
}
