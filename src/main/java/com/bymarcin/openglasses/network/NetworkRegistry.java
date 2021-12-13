package com.bymarcin.openglasses.network;

import java.util.HashSet;
import java.util.Set;

import com.bymarcin.openglasses.OpenGlasses;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


public class NetworkRegistry {
	public static SimpleNetworkWrapper packetHandler;
	private static Set<RPacket> packetsID = new HashSet<RPacket>();

	public static void initialize() {
		packetHandler = new SimpleNetworkWrapper(OpenGlasses.MODID);
	}

	static int id = 0;

	public static <T extends Packet<T, U>, U extends IMessage> void registerPacket(Class<T> clazz, Side handleOn) {
		packetsID.add(new RPacket(id, handleOn));
		packetHandler.registerMessage(clazz, clazz, id, handleOn);
		id++;
	}

	private static class RPacket {
		@SuppressWarnings("unused")
		final int id;
		@SuppressWarnings("unused")
		final Side side;

		public RPacket(int id, Side side) {
			this.side = side;
			this.id = id;
		}
	}

}
