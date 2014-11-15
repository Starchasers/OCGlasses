package com.bymarcin.ocglasses.surface;

import io.netty.buffer.ByteBuf;

public interface IWidget {
	public void write(ByteBuf buff);
	public void read(ByteBuf buff);
	public Widgets getType();
}
