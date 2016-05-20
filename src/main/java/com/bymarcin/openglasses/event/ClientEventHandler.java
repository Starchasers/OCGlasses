package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.utils.Location;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientEventHandler {
	
	int tick = 0;
	
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
