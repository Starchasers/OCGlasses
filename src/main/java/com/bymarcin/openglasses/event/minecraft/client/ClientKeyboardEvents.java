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
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;


@SideOnly(Side.CLIENT)
public class ClientKeyboardEvents {
    public static KeyBinding interactGUIKey = new KeyBinding("key.openglasses.interact", KeyConflictContext.IN_GAME, Keyboard.KEY_C, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public static KeyBinding glassesConfigKey = new KeyBinding("key.openglasses.config", KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_C, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public static KeyBinding nightvisionModeKey = new KeyBinding("key.openglasses.nightvision", KeyConflictContext.IN_GAME, Keyboard.KEY_N, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public static KeyBinding thermalvisionModeKey = new KeyBinding("key.openglasses.thermalvision", KeyConflictContext.IN_GAME, Keyboard.KEY_B, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public static KeyBinding openSecurityModeKey = new KeyBinding("key.openglasses.opensecurity", KeyConflictContext.IN_GAME, Keyboard.KEY_O, "key.categories." + OpenGlasses.MODID.toLowerCase());

    public ClientKeyboardEvents() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
        ClientRegistry.registerKeyBinding(nightvisionModeKey);
        ClientRegistry.registerKeyBinding(thermalvisionModeKey);
        ClientRegistry.registerKeyBinding(glassesConfigKey);

        if(OpenGlasses.opensecurity)
            ClientRegistry.registerKeyBinding(openSecurityModeKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(OCClientSurface.glasses.get().isEmpty())
            return;

        if(glassesConfigKey.isPressed()){
            Minecraft.getMinecraft().displayGuiScreen(new GlassesGui(false));
            return;
        }

        if(interactGUIKey.isPressed()) {
            OCClientSurface.glasses.getConditions().setOverlay(true);
            Minecraft.getMinecraft().displayGuiScreen(new InteractGui());
            for(GlassesInstance.HostClient host : OCClientSurface.glasses.getHosts().values())
                if(host.sendOverlayEvents)
                    NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.uuid, GlassesEventPacket.EventType.ACTIVATE_OVERLAY));

            return;
        }

        for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
            upgrade.onKeyInput();
    }
}
