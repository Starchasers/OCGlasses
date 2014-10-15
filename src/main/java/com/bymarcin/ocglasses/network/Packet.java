package com.bymarcin.ocglasses.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.Type;

import net.minecraft.tileentity.TileEntity;

import com.google.gson.Gson;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public abstract class Packet<T extends Packet<T, RES>, RES extends IMessage> implements IMessage, IMessageHandler<T, RES> {
	private final ByteBuf write;
	private ByteBuf read;
	private final Gson gson = new Gson();

	abstract protected void read() throws IOException;

	abstract protected void write() throws IOException;

	abstract protected RES executeOnClient();

	abstract protected RES executeOnServer();

	public Packet() {
		this.write = Unpooled.buffer();
	}

	// Custom read functions

	public TileEntity readClientTileEntity() throws IOException {
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return Utils.getTileEntity(dimensionId, x, y, z);
	}

	public TileEntity readServerTileEntity() throws IOException {
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return Utils.getTileEntityServer(dimensionId, x, y, z);
	}

	public byte[] readByteArray() throws IOException {
		return readByteArrayData(read.readUnsignedShort());
	}

	public byte[] readByteArrayData(int size) throws IOException {
		byte[] data = new byte[size];
		read.readBytes(data, 0, size);
		return data;
	}

	public Object readJSON(Type t) throws IOException {
		return gson.fromJson(ByteBufUtils.readUTF8String(read), t);
	}

	public <U> U readJSON(Class<U> t) throws IOException {
		return gson.fromJson(ByteBufUtils.readUTF8String(read), t);
	}

	// Forwarding existing read functions

	public byte readByte() throws IOException {
		return read.readByte();
	}

	public short readShort() throws IOException {
		return read.readShort();
	}

	public byte readSignedByte() throws IOException {
		return read.readByte();
	}

	public short readSignedShort() throws IOException {
		return read.readShort();
	}

	public int readUnsignedByte() throws IOException {
		return read.readUnsignedByte();
	}

	public int readUnsignedShort() throws IOException {
		return read.readUnsignedShort();
	}

	public int readInt() throws IOException {
		return read.readInt();
	}

	public long readLong() throws IOException {
		return read.readLong();
	}

	public double readDouble() throws IOException {
		return read.readDouble();
	}

	public float readFloat() throws IOException {
		return read.readFloat();
	}

	public String readString() throws IOException {
		return ByteBufUtils.readUTF8String(read);
	}

	public boolean readBoolean() throws IOException {
		return read.readBoolean();
	}

	// Custom write instructions

	public Packet<T, RES> writeTileLocation(TileEntity te) throws IOException, RuntimeException {
		if (te.getWorldObj() == null)
			throw new RuntimeException("World does not exist!");
		if (te.isInvalid())
			throw new RuntimeException("TileEntity is invalid!");
		write.writeInt(te.getWorldObj().provider.dimensionId);
		write.writeInt(te.xCoord);
		write.writeInt(te.yCoord);
		write.writeInt(te.zCoord);
		return this;
	}

	public Packet<T, RES> writeByteArray(byte[] array) throws IOException, RuntimeException {
		if (array.length > 65535)
			throw new RuntimeException("Invalid array size!");
		write.writeShort(array.length);
		write.writeBytes(array);
		return this;
	}

	public Packet<T, RES> writeByteArrayData(byte[] array) throws IOException {
		write.writeBytes(array);
		return this;
	}

	// Forwarding all write instructions I care about

	public Packet<T, RES> writeByte(byte v) throws IOException {
		write.writeByte(v);
		return this;
	}

	public Packet<T, RES> writeBoolean(boolean v) throws IOException {
		write.writeBoolean(v);
		return this;
	}

	public Packet<T, RES> writeString(String s) throws IOException {
		ByteBufUtils.writeUTF8String(this.write, s);
		return this;
	}

	public Packet<T, RES> writeShort(short v) throws IOException {
		write.writeShort(v);
		return this;
	}

	public Packet<T, RES> writeInt(int v) throws IOException {
		write.writeInt(v);
		return this;
	}

	public Packet<T, RES> writeDouble(double v) throws IOException {
		write.writeDouble(v);
		return this;
	}

	public Packet<T, RES> writeFloat(float v) throws IOException {
		write.writeFloat(v);
		return this;
	}

	public Packet<T, RES> writeLong(long v) throws IOException {
		write.writeLong(v);
		return this;
	}

	@Override
	public final void fromBytes(ByteBuf buf) {
		this.read = buf;
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void toBytes(ByteBuf buf) {
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		buf.writeBytes(this.write);
	}

	@Override
	public final RES onMessage(T message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			return message.executeOnServer();
		}
		else {
			return message.executeOnClient();
		}
	}
}