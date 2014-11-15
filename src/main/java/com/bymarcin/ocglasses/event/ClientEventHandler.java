package com.bymarcin.ocglasses.event;

import com.bymarcin.ocglasses.item.OCGlassesItem;
import com.bymarcin.ocglasses.network.NetworkRegistry;
import com.bymarcin.ocglasses.network.packet.GlassesEventPacket;
import com.bymarcin.ocglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.ocglasses.surface.ClientSurface;
import com.bymarcin.ocglasses.utils.Location;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientEventHandler {
	private boolean haveGlasses = false;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e){
		ItemStack glassesStack= e.player.inventory.armorInventory[3];
		Item glasses = glassesStack!=null?glassesStack.getItem():null;
		if(glasses instanceof OCGlassesItem){
			Location uuid  = OCGlassesItem.getUUID(glassesStack);
			if(uuid!=null && haveGlasses==false){
				equiped(e, uuid);
			}else if(haveGlasses == true && uuid ==null){
				unEquiped(e);
			}
		}else if(haveGlasses == true){
			unEquiped(e);
		}
	}
	
	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent e){
		if (((e.entity instanceof EntityPlayer)) && (e.world.isRemote)){
			ClientSurface.instances.removeAllWidgets();
			haveGlasses = false;
		}
	}
	
	private void unEquiped(PlayerTickEvent e){
		haveGlasses = false;
		ClientSurface.instances.removeAllWidgets();
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES,null, e.player));
	}
	
	private void equiped(PlayerTickEvent e, Location uuid){
		NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, uuid, e.player));
		haveGlasses = true;
	}
}
