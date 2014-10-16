package com.bymarcin.ocglasses.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EVENTTEST {

	@SubscribeEvent
	public void onTerminalRegister(OCGlassesRegisterEvent evt) {
		System.out.println("EVENTUUID: "+evt.terminalId);
	}
}
