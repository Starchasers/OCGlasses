package com.bymarcin.ocglasses.event;

import java.util.UUID;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

public class OCGlassesRegisterEvent extends Event{
	public final EntityPlayer player;
	public final UUID terminalId;

	public OCGlassesRegisterEvent(EntityPlayer player, UUID terminalId) {
		this.player = player;
		this.terminalId = terminalId;
	}
}
