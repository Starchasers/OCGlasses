package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.EnergyBar;
import ben_mkiv.guitoolkit.client.widget.prettyButton;
import ben_mkiv.guitoolkit.client.widget.prettyCheckbox;
import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
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
import java.util.HashSet;
import java.util.UUID;

public class GlassesGui extends GuiScreen {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 146;

    int xSize, ySize, guiLeft, guiTop;

    prettyButton acceptLink, denyLink, clearLink;
    prettyCheckbox enablePopupNotifications, enableWorldRender, enableOverlayRender;

    EnergyBar energyBar;

    OCClientSurface.LinkRequest activeLinkRequest;

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
        addButton(clearLink = new prettyButton(buttonList.size(), guiLeft + 5, guiTop + 35, 100, 20, "clear link"));
        addButton(enablePopupNotifications = new prettyCheckbox(buttonList.size(), guiLeft + 5, guiTop + 65, "popup notifications", false));
        addButton(enableWorldRender= new prettyCheckbox(buttonList.size(), guiLeft + 5, guiTop + 85, "world render", true));
        addButton(enableOverlayRender = new prettyCheckbox(buttonList.size(), guiLeft + 5, guiTop + 105, "overlay render", true));

        addButton(energyBar = new EnergyBar(buttonList.size(), guiLeft + xSize - 105, guiTop + 5, 100, 7));

        acceptLink.visible = false;
        denyLink.visible = false;
        clearLink.visible = false;
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

        UUID currentHost = OpenGlassesItem.getHostUUID(stack);
        String linkedToDistance;

        if(currentHost != null){
            linkedTo = "host: " + currentHost.toString();
            linkedToDistance = "host distance: " + (int) Math.round(OCClientSurface.instance().getRenderPosition().distanceTo(Minecraft.getMinecraft().player.getPositionVector())) + " blocks";
            clearLink.visible = true;
        }
        else {
            linkedTo = "not linked";
            linkedToDistance = "";
            clearLink.visible = false;
        }

        NBTTagCompound glassesNBT = stack.getTagCompound();

        enablePopupNotifications.setEnabled(!glassesNBT.hasKey("nopopups"));
        enableWorldRender.setEnabled(!glassesNBT.hasKey("noWorld"));
        enableOverlayRender.setEnabled(!glassesNBT.hasKey("noOverlay"));

        energyBar.drawBar(0, 0, 1-stack.getItem().getDurabilityForDisplay(stack), null);

        Minecraft.getMinecraft().fontRenderer.drawString(title, guiLeft+5, guiTop+5, 0x0);
        String energyStored = formatNumber((int) OpenGlassesItem.getEnergyStored(stack)) + " FE";
        Minecraft.getMinecraft().fontRenderer.drawString(energyStored, guiLeft - 5 + xSize - fontRenderer.getStringWidth(energyStored), guiTop+13, 0x0);



        Minecraft.getMinecraft().fontRenderer.drawString(linkedToDistance, guiLeft+5, guiTop+15, 0x0);
        Minecraft.getMinecraft().fontRenderer.drawString(linkedTo, guiLeft+5, guiTop+25, 0x0);


        if(activeLinkRequest != null){
            int distance = (int) Math.round(Minecraft.getMinecraft().player.getPosition().getDistance(activeLinkRequest.pos.getX(), activeLinkRequest.pos.getY(), activeLinkRequest.pos.getZ()));
            Minecraft.getMinecraft().fontRenderer.drawString("link request (distance: "+ distance +" blocks)", guiLeft+5, guiTop+153, 0x0);
            Minecraft.getMinecraft().fontRenderer.drawString(activeLinkRequest.host.toString(), guiLeft+5, guiTop+165, 0x0);
        }
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
            else if(button.equals(clearLink))
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.CLEAR_LINK));
            else if(button.equals(enablePopupNotifications)) {
                GlassesEventPacket.EventType eventType = enablePopupNotifications.isEnabled() ? GlassesEventPacket.EventType.DISABLE_NOTIFICATIONS : GlassesEventPacket.EventType.ENABLE_NOTIFICATIONS;
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(eventType));
            }
            else if(button.equals(enableWorldRender)) {
                GlassesEventPacket.EventType eventType = enableWorldRender.isEnabled() ? GlassesEventPacket.EventType.DISABLE_WORLD_RENDER : GlassesEventPacket.EventType.ENABLE_WORLD_RENDER;
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(eventType));
            }
            else if(button.equals(enableOverlayRender)) {
                GlassesEventPacket.EventType eventType = enableOverlayRender.isEnabled() ? GlassesEventPacket.EventType.DISABLE_OVERLAY_RENDER : GlassesEventPacket.EventType.ENABLE_OVERLAY_RENDER;
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(eventType));
            }
        }
        else
            super.actionPerformed(button);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        activeLinkRequest = null;
        HashSet<OCClientSurface.GlassesNotifications.GlassesNotification> notifications = new HashSet<>();
        notifications.addAll(OCClientSurface.GlassesNotifications.notifications);

        for(OCClientSurface.GlassesNotifications.GlassesNotification notification : notifications){
            notification.update();

            if(notification instanceof OCClientSurface.LinkRequest){
                activeLinkRequest = (OCClientSurface.LinkRequest) notification;
            }
        }

        ySize = activeLinkRequest != null ? HEIGHT + 55: HEIGHT;

        acceptLink.visible = activeLinkRequest != null;
        denyLink.visible = activeLinkRequest != null;
    }

    @Override
    public void drawBackground(int tint) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }


}