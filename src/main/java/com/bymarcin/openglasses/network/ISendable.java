package com.bymarcin.openglasses.network;

import io.netty.buffer.ByteBuf;

public interface ISendable<E> {
	public ByteBuf toPacket();
	public E fromPacket(ByteBuf buf);
}
