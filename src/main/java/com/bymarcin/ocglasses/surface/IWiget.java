package com.bymarcin.ocglasses.surface;

import io.netty.buffer.ByteBuf;

public interface IWiget {
	public void write(ByteBuf buff);
	public void read(ByteBuf buff);
}
