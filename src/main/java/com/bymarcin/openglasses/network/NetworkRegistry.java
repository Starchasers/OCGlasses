package com.bymarcin.openglasses.network;

import java.util.HashSet;
import java.util.Set;

import com.bymarcin.openglasses.OpenGlasses;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;


public class NetworkRegistry {
	public static SimpleNetworkWrapper packetHandler;
	private static Set<RPacket> packetsID = new HashSet<RPacket>();

	public static void initialize() {
		packetHandler = new SimpleNetworkWrapper(OpenGlasses.MODID);
	}

	public static <T extends Packet<T, U>, U extends IMessage> void registerPacket(int id, Class<T> clazz, Side handleOn) {
		if (!packetsID.contains(new RPacket(id, handleOn))) {
			packetsID.add(new RPacket(id, handleOn));
			packetHandler.registerMessage(clazz, clazz, id, handleOn);
		} else {
			LogManager.getLogger(OpenGlasses.MODID).error("Packet ID:" + id + "try overwritting other packet!");
		}
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
