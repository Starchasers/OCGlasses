package com.bymarcin.ocglasses.surface;

import java.io.IOException;
import java.util.ArrayList;

import com.bymarcin.ocglasses.network.Packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WidgetUpdatePacket extends Packet<WidgetUpdatePacket, IMessage>{
	ArrayList<IWidget> wigetList;
	Action type;
	
	public WidgetUpdatePacket() {
	}
	
	public WidgetUpdatePacket(ArrayList<IWidget> wigetList, Action type) {
		this.wigetList = wigetList;
		this.type = type;
	}
	
	public WidgetUpdatePacket(IWidget wiget, Action type) {
		this.wigetList = new ArrayList<IWidget>();
		wigetList.add(wiget);
		this.type = type;
	}
	
	@Override
	protected void read() throws IOException {
		wigetList = new ArrayList<IWidget>();
		type = Action.values()[readInt()];
		int size =  readInt();
		for(int i=0; i<size ;i++){
			Widgets wigetType = Widgets.values()[readInt()];
			IWidget w = wigetType.getNewInstance();
			w.read(read);
			wigetList.add(w);
		}
	}

	@Override
	protected void write() throws IOException {
		writeInt(type.ordinal());
		writeInt(wigetList.size());
		for(IWidget w : wigetList){
			writeInt(w.getType().ordinal());
			w.write(write);
		}	
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected IMessage executeOnClient() {
		switch(type){
		case AddWigets: ClientSurface.instances.addWiget(wigetList);
		default:
			break;
		}
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		// TODO Auto-generated method stuba
		return null;
	}

	public enum Action{
		AddWigets,RemoveWigets;
	}
	
}
