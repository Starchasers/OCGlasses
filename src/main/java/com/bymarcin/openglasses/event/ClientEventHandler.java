package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.GlassesGui;
import com.bymarcin.openglasses.gui.InteractGui;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;

import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import static com.bymarcin.openglasses.item.upgrades.UpgradeNightvision.nightvisionModeKey;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    public static KeyBinding interactGUIKey = new KeyBinding("key.interact", Keyboard.KEY_C, "key.categories." + OpenGlasses.MODID.toLowerCase());
    private int playerTick = 0;

    public ClientEventHandler() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
        ClientRegistry.registerKeyBinding(nightvisionModeKey);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent e){

        if(e.player != Minecraft.getMinecraft().player) return;
        playerTick++;

        OCClientSurface.instance().glasses.refreshConditions();

        if(playerTick %20 != 0) return;

        checkGlasses(e.player);

        if(playerTick > 200) playerTick = 0;
    }



    @SideOnly(Side.CLIENT)
    private boolean checkGlasses(EntityPlayer player) {
        ItemStack newStack = OpenGlasses.getGlassesStack(player);
        ItemStack oldStack = OCClientSurface.instance().glasses.get().copy();

        if(ItemStack.areItemsEqualIgnoreDurability(oldStack, newStack))
            return !newStack.isEmpty();
        else {
            if (!oldStack.isEmpty()) {
                unEquiped();
            }

            if (!newStack.isEmpty()) {
                equiped(newStack);
                return true;
            }
        }

        return false;
    }

    /*
    @SubscribeEvent
    public void onLeave(EntityJoinWorldEvent event){
        if(!event.getEntity().equals(Minecraft.getMinecraft().player)) return;
        if(!event.getWorld().isRemote) return;
        OCClientSurface.instance().resetLocalGlasses();
    }*/

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        OCClientSurface.instance().onLeave();
    }

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
        onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(OCClientSurface.instance().glasses.get().getTagCompound().getBoolean("geolyzer"))
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
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(OCClientSurface.instance().glasses.get().getTagCompound().getBoolean("geolyzer"))
            onInteractEvent(EventType.INTERACT_WORLD_BLOCK_RIGHT, e);
        else
            onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
    }

    private void onInteractEvent(EventType type, PlayerInteractEvent event){
        if(OCClientSurface.instance().glasses.get().isEmpty()) return;
        if(OCClientSurface.instance().getHosts().size() == 0) return;
        if(!event.getSide().isClient()) return;
        if(!event.getHand().equals(EnumHand.MAIN_HAND)) return;

        for(OpenGlassesHostClient host : OCClientSurface.instance().getHosts())
            if(host.data().sendWorldEvents)
                NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.getUniqueId(), type, event.getPos(), event.getFace()));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(OCClientSurface.instance().glasses.get().isEmpty())
            return;

        if(interactGUIKey.isPressed()) {
            if(Minecraft.getMinecraft().player.isSneaking()){
                Minecraft.getMinecraft().displayGuiScreen(new GlassesGui(false));
            }
            else {
                OCClientSurface.instance().glasses.conditions.setOverlay(true);
                Minecraft.getMinecraft().displayGuiScreen(new InteractGui());
                for(OpenGlassesHostClient host : OCClientSurface.instance().getHosts())
                    if(host.data().sendOverlayEvents)
                        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.getUniqueId(), EventType.ACTIVATE_OVERLAY));
            }

            return;
        }

        for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
            upgrade.onKeyInput();

    }

    private void unEquiped(){
        for(OpenGlassesHostClient host : OCClientSurface.instance().getHosts())
            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.getUniqueId(), EventType.UNEQUIPED_GLASSES));

        OCClientSurface.instance().initLocalGlasses(ItemStack.EMPTY);
    }

    private void equiped(ItemStack glassesStack){
        OCClientSurface.instance().initLocalGlasses(glassesStack);

        for(OpenGlassesHostClient host : OCClientSurface.instance().getHosts())
            NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(host.getUniqueId(), EventType.EQUIPED_GLASSES));
    }
}
