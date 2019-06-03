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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashSet;

public class GlassesGui extends GuiScreen {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 146;

    int xSize, ySize, guiLeft, guiTop;

    prettyButton acceptLink, denyLink;
    prettyCheckbox enablePopupNotifications;

    ItemStack glassesStack = ItemStack.EMPTY;

    TerminalHostsList list;

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
        glassesStack = ItemStack.EMPTY;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        addButton(acceptLink = new prettyButton(buttonList.size(), guiLeft + 5, guiTop + 175, 70, 20, "accept"));
        addButton(denyLink = new prettyButton(buttonList.size(), guiLeft + 80, guiTop + 175, 70, 20, "deny"));
        addButton(enablePopupNotifications = new prettyCheckbox(buttonList.size(), guiLeft + 5, guiTop + 15, "popup notifications", false));

        addButton(energyBar = new EnergyBar(buttonList.size(), guiLeft + xSize - 105, guiTop + 5, 100, 7));

        acceptLink.visible = false;
        denyLink.visible = false;

        list = new TerminalHostsList(245, 100, guiTop + 35, guiLeft + 5, 60, xSize, ySize);
        updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        drawBackground(0);

        list.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);

        energyBar.drawBar(0, 0, 1-glassesStack.getItem().getDurabilityForDisplay(glassesStack), null);

        Minecraft.getMinecraft().fontRenderer.drawString("OpenGlasses", guiLeft+5, guiTop+5, 0x0);
        String energyStored = formatNumber((int) OpenGlassesItem.getEnergyStored(glassesStack)) + " FE";
        Minecraft.getMinecraft().fontRenderer.drawString(energyStored, guiLeft - 5 + xSize - fontRenderer.getStringWidth(energyStored), guiTop+13, 0x0);

        String hosts = list.getSize() + " hosts";
        Minecraft.getMinecraft().fontRenderer.drawString(hosts, guiLeft - 5 + xSize - fontRenderer.getStringWidth(hosts), guiTop+25, 0x0);

        if(activeLinkRequest != null){
            drawLinkRequest(guiLeft, guiTop, activeLinkRequest);
        }
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
        }
        else
            super.actionPerformed(button);
    }

    @Override
    public void updateScreen() {
        ItemStack newGlassesStack = OCClientSurface.instance().glassesStack;


        boolean glassesChanged = !ItemStack.areItemStackTagsEqual(glassesStack, newGlassesStack);

        glassesStack = newGlassesStack.copy();

        if(glassesStack.isEmpty()){
            Minecraft.getMinecraft().currentScreen = null;
            return;
        }

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

        if(glassesChanged) {
            enablePopupNotifications.setEnabled(!glassesStack.getTagCompound().hasKey("nopopups"));

            list.clear();
            list.add(OCClientSurface.instance().getHosts());
        }
    }

    @Override
    public void drawBackground(int tint) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }


}