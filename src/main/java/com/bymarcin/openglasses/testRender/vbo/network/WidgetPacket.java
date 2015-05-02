package com.bymarcin.openglasses.testRender.vbo.network;

import java.io.IOException;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.testRender.vbo.model.Model;

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
	public static final int REMOVEALL = 8;
	
	private int type;
	private String widgetId;
	private Model data;
	private float[] args;
	
	public WidgetPacket() {

	}
	
	public WidgetPacket(int action){
		type = action;
	}
	
	public WidgetPacket(int action, String id){
		this(action);
		widgetId = id;
	}
	
	public WidgetPacket(Model m) {
		type = ADD;
		data = m;
		widgetId = m.getId();
	}
	
	public WidgetPacket(String id, int action, float... args) {
		this(action, id);
		this.args = args;
	}
	
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
