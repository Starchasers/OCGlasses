package com.bymarcin.openglasses.network;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketManager extends Thread{
	ConcurrentLinkedQueue<PacketInfo> packetQueue = new ConcurrentLinkedQueue<PacketInfo>();
	boolean stop = false;
	public PacketManager() {
		setName("OpenGlassesPacketSender");
	}
	
	public void sendToAll(IMessage message){
		packetQueue.add(new PacketInfo(message, null));
	}
	
	public void sendTo(IMessage message, EntityPlayerMP player){
		packetQueue.add(new PacketInfo(message, player));
	}
	
	public void finish(){
		stop = true;
	}
	
	
	@Override
	public void run() {
		while(!stop){
			try {
				packetQueue.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		while(packetQueue.size()>0){
			PacketInfo pi = packetQueue.poll();
			if(pi!=null){
			 if(pi.player!=null){
				 NetworkRegistry.packetHandler.sendTo(pi.message, pi.player);
			 }else{
				 NetworkRegistry.packetHandler.sendToAll(pi.message);
			 }
			}
		}
		}
		
	}
	
	private class PacketInfo{
		IMessage message;
		EntityPlayerMP player;
		public PacketInfo(IMessage message, EntityPlayerMP player) {
			this.message = message;
			this.player = player;
		}
	}

}
