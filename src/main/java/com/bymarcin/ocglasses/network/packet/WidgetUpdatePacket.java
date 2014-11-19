package com.bymarcin.ocglasses.network.packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.bymarcin.ocglasses.network.Packet;
import com.bymarcin.ocglasses.surface.ClientSurface;
import com.bymarcin.ocglasses.surface.Widget;
import com.bymarcin.ocglasses.surface.WidgetType;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WidgetUpdatePacket extends Packet<WidgetUpdatePacket, IMessage>{
	HashMap<Integer, Widget> widgetList;
	List<Integer> ids;
	Action type;
	
	public WidgetUpdatePacket() {
		type = Action.RemoveAllWidgets;
	}
	
	public WidgetUpdatePacket(HashMap<Integer, Widget> widgetList) {
		this.widgetList = widgetList;
		type = Action.AddWigets;
	}
	
	public WidgetUpdatePacket(List<Integer> l){
		ids = l;
		type = Action.RemoveWidgets;
	}
	
	public WidgetUpdatePacket(int id){
		ids = new ArrayList<Integer>();
		ids.add(id);
		type = Action.RemoveWidgets;
	}
	
	public WidgetUpdatePacket(int id, Widget widget) {
		this.widgetList = new HashMap<Integer,Widget>();
		widgetList.put(id,widget);
		this.type = Action.AddWigets;
	}
	
	@Override
	protected void read() throws IOException {
		type = Action.values()[readInt()];
		switch (type) {
		case AddWigets: readOnAddAction();
			break;
		case RemoveWidgets: readOnRemoveAction();
			break;
		case RemoveAllWidgets: ;
			break;
		default:
			break;

		}
	}
	
	@Override
	protected void write() throws IOException {
		writeInt(type.ordinal());
		switch (type) {
		case AddWigets: writeOnAddAction();
			break;
		case RemoveWidgets: writeOnRemoveAction();
			break;
		case RemoveAllWidgets: ;
			break;
		default:
			break;

		}
	}

	private void readOnAddAction() throws IOException{
		widgetList = new HashMap<Integer,Widget>();
		int size =  readInt();
		for(int i=0; i<size ;i++){
			WidgetType wigetType = WidgetType.values()[readInt()];
			Widget w = wigetType.getNewInstance();
			w.read(read);
			widgetList.put(readInt(),w);
		}
	}
	
	private void readOnRemoveAction() throws IOException{
		ids = new ArrayList<Integer>();
		int size = readInt();
		for(int i = 0; i<size; i++){
			ids.add(readInt());
		}
	}

	private void writeOnAddAction() throws IOException{
		writeInt(widgetList.size());
		for(Entry<Integer, Widget> w : widgetList.entrySet()){
			writeInt(w.getValue().getType().ordinal());
			w.getValue().write(write);
			writeInt(w.getKey());
		}
	}
	
	private void writeOnRemoveAction() throws IOException{
		writeInt(ids.size());
		for(Integer i: ids){
			writeInt(i);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	protected IMessage executeOnClient() {
		switch(type){
		case AddWigets: ClientSurface.instances.updateWigets(widgetList.entrySet());
			break;
		case RemoveWidgets: ClientSurface.instances.removeWidgets(ids);
			break;
		case RemoveAllWidgets: ClientSurface.instances.removeAllWidgets();
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

	public enum Action{
		AddWigets,RemoveWidgets,RemoveAllWidgets;
	}
	
}
