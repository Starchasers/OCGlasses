package com.bymarcin.ocglasses.event;

import com.bymarcin.ocglasses.event.GlassesEventPacket.EventType;
import com.bymarcin.ocglasses.item.OCGlassesItem;
import com.bymarcin.ocglasses.network.NetworkRegistry;
import com.bymarcin.ocglasses.utils.Vec3;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientEventHandler {
	private boolean haveGlasses = false;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e){
		ItemStack glassesStack= e.player.inventory.armorInventory[3];
		Item glasses = glassesStack!=null?glassesStack.getItem():null;
		if(glasses instanceof OCGlassesItem){
			Vec3 uuid  = OCGlassesItem.getUUID(glassesStack);
			if(uuid!=null && haveGlasses==false){
				NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, uuid, e.player));
				haveGlasses = true;
			}else if(haveGlasses == true && uuid ==null){
				haveGlasses = false;
				NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES,null, e.player));
			}
		}else if(haveGlasses == true){
			haveGlasses = false;
			NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES,null, e.player));
		}
	}

}
