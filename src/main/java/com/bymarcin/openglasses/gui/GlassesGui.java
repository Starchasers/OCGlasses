package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.*;
import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.text.NumberFormat;

public class GlassesGui extends GuiScreen {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 229; //144

    private int xSize, ySize, guiLeft, guiTop;

    private prettyCheckbox enablePopupNotifications;

    private ItemStack glassesStack = ItemStack.EMPTY;

    private TerminalHostsList list;
    private NotificationList notificationList;

    private EnergyBar energyBar;

    private static boolean isNotification = false;

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

        addButton(enablePopupNotifications = new prettyCheckbox(buttonList.size(), guiLeft + 5, guiTop + 15, "popup notifications", false));
        addButton(energyBar = new EnergyBar(buttonList.size(), guiLeft + xSize - 105, guiTop + 5, 100, 7));


        list = new TerminalHostsList(245, 100, guiTop + 35, guiLeft + 5, 80, xSize, ySize);
        notificationList = new NotificationList(245, 73, guiTop + 150, guiLeft + 5, 45, xSize, ySize);
        updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        drawBackground(0);

        // use the glassesStack from the inventory as the other one doesnt (and shouldnt!) upgrade when energy is used
        ItemStack playerGlasses = OpenGlasses.getGlassesStack(Minecraft.getMinecraft().player);

        list.drawScreen(mouseX, mouseY, partialTicks);

        if(notificationList.getSize() > 0)
            notificationList.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);

        energyBar.drawBar(0, 0, 1-glassesStack.getItem().getDurabilityForDisplay(playerGlasses), null);

        Minecraft.getMinecraft().fontRenderer.drawString("OpenGlasses", guiLeft+5, guiTop+5, 0x0);
        String energyStored = formatNumber((int) OpenGlassesItem.getEnergyStored(playerGlasses)) + " FE";
        Minecraft.getMinecraft().fontRenderer.drawString(energyStored, guiLeft - 5 + xSize - fontRenderer.getStringWidth(energyStored), guiTop+13, 0x0);

        String hosts = list.getSize() + " hosts";
        Minecraft.getMinecraft().fontRenderer.drawString(hosts, guiLeft - 5 + xSize - fontRenderer.getStringWidth(hosts), guiTop+25, 0x0);
    }



    private String formatNumber(double number){
        return NumberFormat.getInstance().format(number);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof prettyButton){
            if(button.equals(enablePopupNotifications)) {
                GlassesEventPacket.EventType eventType = enablePopupNotifications.isEnabled() ? GlassesEventPacket.EventType.DISABLE_NOTIFICATIONS : GlassesEventPacket.EventType.ENABLE_NOTIFICATIONS;
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(null, eventType));
            }
        }
        else
            super.actionPerformed(button);
    }

    @Override
    public void updateScreen() {
        ItemStack newGlassesStack = OCClientSurface.glasses.get();

        boolean glassesChanged = !ItemStack.areItemStackTagsEqual(glassesStack, newGlassesStack);

        glassesStack = newGlassesStack.copy();

        if(glassesStack.isEmpty()){
            Minecraft.getMinecraft().currentScreen = null;
            return;
        }

        super.updateScreen();

        if(glassesChanged) {
            enablePopupNotifications.setEnabled(!glassesStack.getTagCompound().hasKey("nopopups"));

            list.clear();
            list.add(OCClientSurface.glasses.getHosts().values());

            notificationList.update();

            ySize = notificationList.getSize() > 0 ? 229 : 144;
        }

    }

    @Override
    public void drawBackground(int tint) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }


}