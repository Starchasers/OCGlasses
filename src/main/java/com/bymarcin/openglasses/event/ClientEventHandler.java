package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.openglasses.utils.Location;
import com.bymarcin.openglasses.vbo.ClientLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

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
			if(uuid!=null && ClientLayer.getInstance().haveGlasses==false){
				equiped(e, uuid);
			}else if(ClientLayer.getInstance().haveGlasses == true && (uuid ==null || !uuid.equals(ClientLayer.getInstance().lastBind) ) ) {
				unEquiped(e);
			}
		}else if(ClientLayer.getInstance().haveGlasses == true){
			unEquiped(e);
		}
	}
	
	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent e){
		if ((e.entity == Minecraft.getMinecraft().thePlayer) && (e.world.isRemote)){
			ClientLayer.getInstance().clear();
			ClientLayer.getInstance().haveGlasses = false;
		}
	}
	
	private void unEquiped(PlayerTickEvent e){
		ClientLayer.getInstance().haveGlasses = false;
		ClientLayer.getInstance().clear();
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES,null, e.player));
	}
	
	private void equiped(PlayerTickEvent e, Location uuid){
		ClientLayer.getInstance().lastBind = uuid;
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, uuid, e.player));
		ClientLayer.getInstance().haveGlasses = true;
	}
}
