package com.bymarcin.ocglasses.surface.widgets;

import io.netty.buffer.ByteBuf;

import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.Widgets;

public class SquareWidget implements IWidget{

	private float x;
	private float y;
	private float z;
	private float r;
	private float g;
	private float b;
	
	public SquareWidget() {
	}
	
	public SquareWidget(float x, float y, float z, float r, float g, float b) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public SquareWidget(double x, double y, double z, double r, double g, double b){
		this((float)x,(float)y,(float)z,(float)r,(float)g,(float)b);
	}
	
	
	@Override
	public void write(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		z = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
	}

	@Override
	public void read(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(z);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
	}
	
	@Override
	public Widgets getType() {
		return Widgets.QUAD;
	}
	
}
