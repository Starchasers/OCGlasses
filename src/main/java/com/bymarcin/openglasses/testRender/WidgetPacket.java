package com.bymarcin.openglasses.testRender;

import java.io.IOException;

import com.bymarcin.openglasses.network.Packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class WidgetPacket extends Packet<WidgetPacket, IMessage>{
	public static final int ADD = 0;
	public static final int REMOVE = 1;
	public static final int VISIBLE = 2;
	public static final int INVISIBLE = 3;
	public static final int ROTATE = 4;
	public static final int SCALE = 5;
	public static final int TRANSLATE = 6;
	public static final int RESET = 7;
	
	@Override
	protected void read() throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void write() throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected IMessage executeOnClient() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected IMessage executeOnServer() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
