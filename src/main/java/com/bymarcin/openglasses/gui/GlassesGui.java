package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.*;
import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.glasses.GlassesNotifications;
import com.bymarcin.openglasses.event.glasses.LinkRequest;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class GlassesGui extends GuiScreen {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 146;

    int xSize, ySize, guiLeft, guiTop;

    prettyButton acceptLink, denyLink;
    prettyCheckbox enablePopupNotifications;


    prettyList hostList;

    EnergyBar energyBar;

    LinkRequest activeLinkRequest;

    public static boolean isNotification = false;

    private static final ResourceLocation background = new ResourceLocation(OpenGlasses.MODID, "textures/gui/glasses.png");

    public GlassesGui(boolean notification){
        super();
        isNotification = notification;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui(){
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        addButton(acceptLink = new prettyButton(buttonList.size(), guiLeft + 5, guiTop + 175, 70, 20, "accept"));
        addButton(denyLink = new prettyButton(buttonList.size(), guiLeft + 80, guiTop + 175, 70, 20, "deny"));
        addButton(enablePopupNotifications = new prettyCheckbox(buttonList.size(), guiLeft + 5, guiTop + 15, "popup notifications", false));

        addButton(energyBar = new EnergyBar(buttonList.size(), guiLeft + xSize - 105, guiTop + 5, 100, 7));

        hostList =  new prettyList("hostList", guiLeft + 5, guiTop + 50);
        hostList.setDisplayElements(3);

        acceptLink.visible = false;
        denyLink.visible = false;


        updateTaskList();
    }

    private void updateTaskList(){

        if(true || hostList.elements.size() != OCClientSurface.instance().getHosts().size()) {
            for(ArrayList<prettyElement> entry : hostList.elements)
                buttonList.removeAll(entry);

            hostList.elements.clear();

            for (OpenGlassesHostClient host : OCClientSurface.instance().getHosts()) {
                ArrayList<prettyElement> listElements = new ArrayList<>();

                prettyButton button = new prettyButton(buttonList.size(), 0, 3, 245, 15, host.getUniqueId().toString());
                button.enabled = false;

                listElements.add(button);
                listElements.add(new hostCheckbox(host.getUniqueId(), buttonList.size() + 1, 0, 16, "world", host.data().renderWorld));
                listElements.add(new hostCheckbox(host.getUniqueId(), buttonList.size() + 2, 60, 16, "overlay", host.data().renderOverlay));
                listElements.add(new hostButton(host.getUniqueId(), buttonList.size() + 3, 184, 17, 60, 20, "clear link"));

                hostList.add(listElements);

                for (prettyElement el : listElements)
                    if (el instanceof GuiButton)
                        buttonList.add((GuiButton) el);
            }
        }



        hostList.setY(guiTop + 50);
        hostList.setX(guiLeft + 5);
        hostList.update();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        drawBackground(0);

        super.drawScreen(mouseX, mouseY, partialTicks);

        String title = "OpenGlasses", linkedTo="";


        ItemStack stack = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);

        if(stack.isEmpty()){
            Minecraft.getMinecraft().currentScreen = null;
            return;
        }

        int i=0;
        //for(OpenGlassesHostClient host : OCClientSurface.instance().getHosts())
        //    drawHost(host, guiTop + 15 + 30 * i++);

        NBTTagCompound glassesNBT = stack.getTagCompound();

        enablePopupNotifications.setEnabled(!glassesNBT.hasKey("nopopups"));
        //enableWorldRender.setEnabled(!glassesNBT.hasKey("noWorld"));
        //enableOverlayRender.setEnabled(!glassesNBT.hasKey("noOverlay"));

        energyBar.drawBar(0, 0, 1-stack.getItem().getDurabilityForDisplay(stack), null);

        Minecraft.getMinecraft().fontRenderer.drawString(title, guiLeft+5, guiTop+5, 0x0);
        String energyStored = formatNumber((int) OpenGlassesItem.getEnergyStored(stack)) + " FE";
        Minecraft.getMinecraft().fontRenderer.drawString(energyStored, guiLeft - 5 + xSize - fontRenderer.getStringWidth(energyStored), guiTop+13, 0x0);

        if(activeLinkRequest != null){
            drawLinkRequest(guiLeft, guiTop, activeLinkRequest);
        }
    }

    private void drawHost(OpenGlassesHostClient hostClient, int y){
        String linkedToDistance = "", linkedTo = "";

        if(hostClient.terminalName.length() > 0)
           linkedTo = "host: " + hostClient.terminalName + " ("+hostClient.getUniqueId().toString().substring(0, 18)+"...)";
        else
            linkedTo = "host: " + hostClient.getUniqueId().toString();

        linkedToDistance = "host distance: " + (int) Math.round(hostClient.getRenderPosition(0.5f).distanceTo(Minecraft.getMinecraft().player.getPositionVector())) + " blocks";
        //clearLink.visible = true;

        Minecraft.getMinecraft().fontRenderer.drawString(linkedToDistance, guiLeft+5, y, 0x0);
        Minecraft.getMinecraft().fontRenderer.drawString(linkedTo, guiLeft+5, y+10, 0x0);
    }

    private void drawLinkRequest(int x, int y, LinkRequest linkRequest){
        int distance = linkRequest.getDistance(Minecraft.getMinecraft().player.getPositionVector());
        String text = "link request";

        if(activeLinkRequest.hostName.length() > 0)
            text+=" from '"+activeLinkRequest.hostName+"'";

        text+=" (distance: "+ distance +" blocks)";

        Minecraft.getMinecraft().fontRenderer.drawString(text, x+5, y+153, 0x0);
        Minecraft.getMinecraft().fontRenderer.drawString(activeLinkRequest.host.toString(), x+5, y+165, 0x0);
    }

    private String formatNumber(double number){
        return NumberFormat.getInstance().format(number);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    class hostButton extends prettyButton{
        public UUID host;

        public hostButton(UUID hostUUID, int id, int x, int y, int width, int height, String label){
            super(id, x, y, width, height, label);
            host = hostUUID;
        }
    }

    class hostCheckbox extends prettyCheckbox {
        public UUID host;

        public hostCheckbox(UUID hostUUID, int id, int x, int y, String label, boolean state){
            super(id, x, y, label, state);
            host = hostUUID;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof prettyButton){
            if(button.equals(acceptLink))
                activeLinkRequest.submit();
            else if(button.equals(denyLink))
                activeLinkRequest.cancel();
            else if(button.equals(enablePopupNotifications)) {
                GlassesEventPacket.EventType eventType = enablePopupNotifications.isEnabled() ? GlassesEventPacket.EventType.DISABLE_NOTIFICATIONS : GlassesEventPacket.EventType.ENABLE_NOTIFICATIONS;
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(null, eventType));
            }
            else {
                if(button instanceof hostButton){
                    switch(((hostButton) button).action){
                        case "clear link":
                            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(((hostButton) button).host, GlassesEventPacket.EventType.CLEAR_LINK));
                            break;
                    }
                }
                else if(button instanceof hostCheckbox){
                    GlassesEventPacket.EventType eventType;

                    switch (((hostCheckbox) button).action){
                        case "overlay":
                            eventType = ((hostCheckbox) button).isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_RENDER : GlassesEventPacket.EventType.ENABLE_OVERLAY_RENDER;
                            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(((hostCheckbox) button).host, eventType));
                            break;
                        case "world":
                            eventType = ((hostCheckbox) button).isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_RENDER : GlassesEventPacket.EventType.ENABLE_WORLD_RENDER;
                            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(((hostCheckbox) button).host, eventType));
                            break;
                    }
                }
            }
        }
        else
            super.actionPerformed(button);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        activeLinkRequest = null;
        HashSet<GlassesNotifications.GlassesNotification> notifications = new HashSet<>();
        notifications.addAll(GlassesNotifications.notifications);

        for(GlassesNotifications.GlassesNotification notification : notifications){
            notification.update();

            if(notification instanceof LinkRequest){
                activeLinkRequest = (LinkRequest) notification;
            }
        }

        ySize = activeLinkRequest != null ? HEIGHT + 55: HEIGHT;

        acceptLink.visible = activeLinkRequest != null;
        denyLink.visible = activeLinkRequest != null;

        updateTaskList();
    }

    @Override
    public void drawBackground(int tint) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }


}