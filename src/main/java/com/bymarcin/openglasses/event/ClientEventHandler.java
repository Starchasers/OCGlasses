package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.InteractGui;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;

import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    public static KeyBinding interactGUIKey = new KeyBinding("key.interact", Keyboard.KEY_C, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public static KeyBinding nightvisionModeKey = new KeyBinding("key.nightvision", Keyboard.KEY_N, "key.categories." + OpenGlasses.MODID.toLowerCase());
    int tick = 0;

    public ClientEventHandler() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
        ClientRegistry.registerKeyBinding(nightvisionModeKey);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent e){

        if(e.player != Minecraft.getMinecraft().player) return;
        tick++;

        ((OCClientSurface) OCClientSurface.instances).refreshConditions();

        if(tick%20 != 0) return;

        checkGlasses(e.player);

        if(tick > 200) tick = 0;
    }



    @SideOnly(Side.CLIENT)
    public boolean checkGlasses(EntityPlayer player) {
        ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

        if(glassesStack != null){
            if (((OCClientSurface) OCClientSurface.instances).glassesStack == null) {
                equiped(player, glassesStack);
                return true;
            }
            else if(glassesStack.equals(((OCClientSurface) OCClientSurface.instances).glassesStack))
                return true;
            else if(glassesStack.getItem() instanceof OpenGlassesItem) {
                ((OCClientSurface) OCClientSurface.instances).initLocalGlasses(glassesStack);
                return true;
            }
        }
        else if(((OCClientSurface) OCClientSurface.instances).glassesStack != null) {
            unEquiped(player);
        }

        return false;
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e){
        if(!e.getEntity().equals(Minecraft.getMinecraft().player)) return;
        if(!e.getWorld().isRemote) return;
        ((OCClientSurface) OCClientSurface.instances).resetLocalGlasses();
    }

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
        onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
        if(((OCClientSurface) OCClientSurface.instances).glassesStack == null) return;
        if(((OCClientSurface) OCClientSurface.instances).glassesStack.getTagCompound().getBoolean("geolyzer"))
            onInteractEvent(EventType.INTERACT_WORLD_BLOCK_LEFT, e);
        else
            onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
    }

    @SubscribeEvent
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e){
        onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem e){
        onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e){
        if(((OCClientSurface) OCClientSurface.instances).glassesStack == null) return;
        if(((OCClientSurface) OCClientSurface.instances).glassesStack.getTagCompound().getBoolean("geolyzer"))
            onInteractEvent(EventType.INTERACT_WORLD_BLOCK_RIGHT, e);
        else
            onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
    }

    private void onInteractEvent(EventType type, PlayerInteractEvent event){
        if(((OCClientSurface) OCClientSurface.instances).glassesStack == null) return;
        if(((OCClientSurface) OCClientSurface.instances).lastBind == null) return;
        if(!event.getSide().isClient()) return;
        if(!event.getHand().equals(EnumHand.MAIN_HAND)) return;

        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(type, event.getPos(), event.getFace()));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(((OCClientSurface) OCClientSurface.instances).glassesStack == null) return;
        if(interactGUIKey.isPressed()) {
            ((OCClientSurface) OCClientSurface.instances).conditions.setOverlay(true);
            Minecraft.getMinecraft().displayGuiScreen(new InteractGui());
            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.ACTIVATE_OVERLAY));
        }
        if(nightvisionModeKey.isPressed() && ((OCClientSurface) OCClientSurface.instances).glassesStack.getTagCompound().getBoolean("nightvision")) {
            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.TOGGLE_NIGHTVISION));
        }
    }

    private void unEquiped(EntityPlayer player){
        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES));
        ((OCClientSurface) OCClientSurface.instances).resetLocalGlasses();
    }

    private void equiped(EntityPlayer player, ItemStack glassesStack){
        ((OCClientSurface) OCClientSurface.instances).initLocalGlasses(glassesStack);
        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES));
    }
}
