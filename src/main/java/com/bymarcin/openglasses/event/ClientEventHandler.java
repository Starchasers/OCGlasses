package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.utils.Location;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientEventHandler {
	
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e){
		ItemStack glassesStack= e.player.inventory.armorInventory[3];
		Item glasses = glassesStack!=null?glassesStack.getItem():null;
		if(glasses instanceof OpenGlassesItem){
			Location uuid  = OpenGlassesItem.getUUID(glassesStack);
			if(uuid!=null && ClientSurface.instances.haveGlasses==false){
				equiped(e, uuid);
			}else if(ClientSurface.instances.haveGlasses == true && uuid ==null){
				unEquiped(e);
			}
		}else if(ClientSurface.instances.haveGlasses == true){
			unEquiped(e);
		}
	}
	
	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent e){
		if (((e.entity instanceof EntityPlayer)) && (e.world.isRemote)){
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
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, uuid, e.player));
		ClientSurface.instances.haveGlasses = true;
	}
}
