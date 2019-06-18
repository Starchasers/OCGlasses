package com.bymarcin.openglasses.event.minecraft.client;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.GlassesGui;
import com.bymarcin.openglasses.gui.InteractGui;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.GlassesInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;


@SideOnly(Side.CLIENT)
public class ClientKeyboardEvents {
    public static KeyBinding interactGUIKey = new KeyBinding("key.interact", Keyboard.KEY_C, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public static KeyBinding nightvisionModeKey = new KeyBinding("key.nightvision", Keyboard.KEY_N, "key.categories." + OpenGlasses.MODID.toLowerCase());

    public ClientKeyboardEvents() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
        ClientRegistry.registerKeyBinding(nightvisionModeKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(OCClientSurface.glasses.get().isEmpty())
            return;

        if(interactGUIKey.isPressed()) {
            if(Minecraft.getMinecraft().player.isSneaking()){
                Minecraft.getMinecraft().displayGuiScreen(new GlassesGui(false));
            }
            else {
                OCClientSurface.glasses.getConditions().setOverlay(true);
                Minecraft.getMinecraft().displayGuiScreen(new InteractGui());
                for(GlassesInstance.HostClient host : OCClientSurface.glasses.getHosts().values())
                    if(host.sendOverlayEvents)
                        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.uuid, GlassesEventPacket.EventType.ACTIVATE_OVERLAY));
            }

            return;
        }

        for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
            upgrade.onKeyInput();
    }
}
