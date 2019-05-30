package com.bymarcin.openglasses.gui;

import ben_mkiv.guitoolkit.client.widget.prettyButton;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

public class GlassesGui extends GuiScreen {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 146;

    int xSize, ySize, guiLeft, guiTop;

    prettyButton acceptLink, denyLink, clearLink;
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
        addButton(clearLink = new prettyButton(buttonList.size(), guiLeft + 5, guiTop + 40, 100, 20, "clear link"));

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
        if(currentHost != null){
            linkedTo = "host: " + currentHost.toString();
            clearLink.visible = true;
        }
        else {
            linkedTo = "not linked";
            clearLink.visible = false;
        }

        Minecraft.getMinecraft().fontRenderer.drawString(title, guiLeft+5, guiTop+5, 0x0);
        Minecraft.getMinecraft().fontRenderer.drawString(linkedTo, guiLeft+5, guiTop+15, 0x0);


        if(activeLinkRequest != null){
            int distance = (int) Math.round(Minecraft.getMinecraft().player.getPosition().getDistance(activeLinkRequest.pos.getX(), activeLinkRequest.pos.getY(), activeLinkRequest.pos.getZ()));
            Minecraft.getMinecraft().fontRenderer.drawString("link request (distance: "+ distance +" blocks)", guiLeft+5, guiTop+153, 0x0);
            Minecraft.getMinecraft().fontRenderer.drawString(activeLinkRequest.host.toString(), guiLeft+5, guiTop+165, 0x0);
        }
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
            if(button.equals(denyLink))
                activeLinkRequest.cancel();
            if(button.equals(clearLink))
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(GlassesEventPacket.EventType.CLEAR_LINK));
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