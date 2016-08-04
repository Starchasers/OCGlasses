package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.InteractGui;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.utils.Location;

import li.cil.oc.client.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.discovery.JarDiscoverer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;

public class ClientEventHandler {
	public static KeyBinding interactGUIKey = new KeyBinding("key.interact", Keyboard.KEY_C, "key.categories." + OpenGlasses.MODID.toLowerCase());
	int tick = 0;

	public ClientEventHandler() {
		ClientRegistry.registerKeyBinding(interactGUIKey);
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e){
		if(e.player != Minecraft.getMinecraft().thePlayer) return;
		tick ++;
		if(tick%20 != 0){ 
			return;
		}
		tick = 0;
		
		ItemStack glassesStack= e.player.inventory.armorInventory[3];
		Item glasses = glassesStack!=null?glassesStack.getItem():null;
		
		if(glasses instanceof OpenGlassesItem){
			Location uuid  = OpenGlassesItem.getUUID(glassesStack);
			if(uuid!=null && ClientSurface.instances.haveGlasses==false){
				equiped(e, uuid);
			}else if(ClientSurface.instances.haveGlasses == true && (uuid ==null || !uuid.equals(ClientSurface.instances.lastBind) ) ) {
				unEquiped(e);
			}
		}else if(ClientSurface.instances.haveGlasses == true){
			unEquiped(e);
		}
	}
	
	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent e){
		if ((e.getEntity() == Minecraft.getMinecraft().thePlayer) && (e.getWorld().isRemote)){
			ClientSurface.instances.removeAllWidgets();
			ClientSurface.instances.haveGlasses = false;
		}
	}

	@SubscribeEvent
	public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
		onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
	}

	@SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
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
		onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
	}

	private void onInteractEvent(EventType type, PlayerInteractEvent event){
		if(event.getSide().isClient() && event.getHand() == EnumHand.MAIN_HAND && ClientSurface.instances.haveGlasses) {
			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.INTERACT_WORLD_RIGHT, ClientSurface.instances.lastBind, event.getEntityPlayer()));
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(interactGUIKey.isPressed()){
			Minecraft.getMinecraft().displayGuiScreen(new InteractGui());
		}

	}

	private void unEquiped(PlayerTickEvent e){
		ClientSurface.instances.haveGlasses = false;
		ClientSurface.instances.removeAllWidgets();
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES,null, e.player));
	}
	
	private void equiped(PlayerTickEvent e, Location uuid){
		ClientSurface.instances.lastBind = uuid;
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, uuid, e.player));
		ClientSurface.instances.haveGlasses = true;
	}


}
