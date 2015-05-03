package com.bymarcin.openglasses.network;

import java.io.IOException;

import com.bymarcin.openglasses.vbo.ClientLayer;
import com.bymarcin.openglasses.vbo.model.Model;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class WidgetPacket extends Packet<WidgetPacket, IMessage> {
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
	private boolean visible;

	public WidgetPacket() {

	}

	public WidgetPacket(int action) {
		type = action;
	}

	public WidgetPacket(int action, String id) {
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
		type = readInt();
		switch (type) {
		case ADD:
			widgetId = readString();
			data = new Model("").fromPacket(read);
			break;
		case RESET:
		case REMOVE:
			widgetId = readString();
			break;
		case VISIBLE:
		case INVISIBLE:
			widgetId = readString();
			visible = readBoolean();
			break;
		case ROTATE:
		case SCALE:
		case TRANSLATE:
			widgetId = readString();
			int size = readInt();
			args = new float[size];
			for(int i=0; i<size;i++){
				args[i] = readFloat();
			}
			break;
		case REMOVEALL: break;
		}

	}

	@Override
	protected void write() throws IOException {
		writeInt(type);
		switch (type) {
		case ADD:
			writeString(widgetId);
			writeByteArray(data.toPacket().array());
			break;
		case RESET:
		case REMOVE:
			writeString(widgetId);
			break;
		case VISIBLE:
		case INVISIBLE:
			writeString(widgetId);
			writeBoolean(visible);
			break;
		case ROTATE:
		case SCALE:
		case TRANSLATE:
			writeString(widgetId);
			writeInt(args.length);
			for(int i=0; i<args.length;i++){
				writeFloat(args[i]);
			}
			break;
		case REMOVEALL:
			break;
		}
	}

	@Override
	protected IMessage executeOnClient() {
		switch (type) {
		case ADD:
			ClientLayer.getInstance().addModel(data);
			break;
		case REMOVE:
			ClientLayer.getInstance().removeModel(widgetId);
			break;
		case VISIBLE:
		case INVISIBLE:
		case ROTATE:
		case SCALE:
		case TRANSLATE:
		case RESET:
			ClientLayer.getInstance().updateModel(type, widgetId, args);
			break;
		case REMOVEALL:
			ClientLayer.getInstance().clear();
			break;
		}
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}
