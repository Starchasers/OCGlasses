package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.gui.InteractGui;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.openglasses.surface.ClientSurface;

import com.bymarcin.openglasses.utils.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
		if(e.player != Minecraft.getMinecraft().player) return;
		tick++;

		if(tick%20 != 0) return;

		tick = 0;		
		checkGlasses(e);		
	}
	
	public boolean checkGlasses(PlayerTickEvent e) {
		ItemStack glassesStack = OpenGlasses.getGlassesStack(e.player);

		if(glassesStack != null){
			if (ClientSurface.instances.glassesStack == null) {
				equiped(e, glassesStack);
				return true;
			}
			else if(glassesStack.equals(ClientSurface.instances.glassesStack))
				return true;
		}
		else if(ClientSurface.instances.glassesStack != null) {
			unEquiped(e);
		}

		return false;
	}
	
	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent e){
		if(!e.getEntity().equals(Minecraft.getMinecraft().player)) return;
		if(!e.getWorld().isRemote) return;
		ClientSurface.instances.resetLocalGlasses();
	}

	@SubscribeEvent
	public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
		onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
	}

	@SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
		if(ClientSurface.instances.glassesStack == null) return;
		if(ClientSurface.instances.glassesStack.getTagCompound().getBoolean("geolyzer"))
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
		if(ClientSurface.instances.glassesStack == null) return;
		if(ClientSurface.instances.glassesStack.getTagCompound().getBoolean("geolyzer"))
			onInteractEvent(EventType.INTERACT_WORLD_BLOCK_RIGHT, e);
		else
			onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
	}

	private void onInteractEvent(EventType type, PlayerInteractEvent event){
		if(ClientSurface.instances.glassesStack == null) return;
		if(ClientSurface.instances.lastBind == null) return;
		if(!event.getSide().isClient()) return;
		if(!event.getHand().equals(EnumHand.MAIN_HAND)) return;

		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(type, ClientSurface.instances.lastBind, event.getEntityPlayer(), event.getPos()));
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(ClientSurface.instances.glassesStack == null) return;
		if(!interactGUIKey.isPressed()) return;

		ClientSurface.instances.glassesStack.getTagCompound().setBoolean("overlayActive", true);
		Minecraft.getMinecraft().displayGuiScreen(new InteractGui());
	}

	private void unEquiped(PlayerTickEvent e){
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES, ClientSurface.instances.lastBind, e.player));
		ClientSurface.instances.resetLocalGlasses();
	}

	private void equiped(PlayerTickEvent e, ItemStack glassesStack){
		ClientSurface.instances.initLocalGlasses(glassesStack);
		if(ClientSurface.instances.lastBind == null) return;
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, ClientSurface.instances.lastBind, e.player));
	}
}
