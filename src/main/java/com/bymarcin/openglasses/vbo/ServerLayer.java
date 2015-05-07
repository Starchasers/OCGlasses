package com.bymarcin.openglasses.vbo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.WidgetPacket;
import com.bymarcin.openglasses.utils.Location;
import com.bymarcin.openglasses.vbo.model.Model;

import net.minecraft.entity.player.EntityPlayerMP;

public class ServerLayer {
	private static ServerLayer instance = new ServerLayer();
	private HashMap<Location, HashMap<String, Model>> widgetList = new HashMap<Location, HashMap<String, Model>>();
	private HashMap<EntityPlayerMP, Location> players = new HashMap<EntityPlayerMP, Location>();
	
	
	private ServerLayer() {

	}

	public static ServerLayer instance() {
		return instance;
	}

	public boolean subscribePlayer(Location loc, EntityPlayerMP player) {
		System.out.println("SUBSCRIBEEVENT");
		Location location = players.get(player);
		if (location == null) {
				players.put(player, loc);
				HashMap<String, Model> models = widgetList.get(loc);
				if (models != null) {
					for (Model m : models.values()) {
						NetworkRegistry.pm.sendTo(new WidgetPacket(m), player);
					}
				}
				System.out.println("SUBSCRIBE");
				return true;
		}
		return false;
	}

	public boolean unsubscribePlayer(EntityPlayerMP player) {
		Location location = players.get(player);
		if (location != null) {
			if(players.remove(player) != null){
				NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVEALL), player);
				System.out.println("UNSUBSCRIBE");
				return true;
			}
		}
		return false;
	}

	public void registerLocation(Location loc) {
		widgetList.put(loc, new HashMap<String, Model>());
		System.out.println("REGISTER: " + loc.toString());
	}

	public void unregisterLocation(Location loc) {
		widgetList.remove(loc);
		LinkedList<EntityPlayerMP> playersToRemove = new LinkedList<EntityPlayerMP>();
		for(Entry<EntityPlayerMP, Location> e : players.entrySet()){
			if(e.getValue().equals(loc)){
				playersToRemove.add(e.getKey());
			}
		}
		
		for(EntityPlayerMP p: playersToRemove){
			unsubscribePlayer(p);
		}
		System.out.println("UNREGISTER: " + loc.toString());
	}

	public boolean addModel(Location loc, Model m) {
		HashMap<String, Model> buffor = widgetList.get(loc);
		if (buffor == null)
			return false;
		if (!buffor.containsKey(m.getId())) {
			buffor.put(m.getId(), m);
			for (Entry<EntityPlayerMP, Location> e : players.entrySet()) {
				if(e.getValue().equals(loc)){
					System.out.println("SENDING" + e.getKey().getGameProfile().getName());
					NetworkRegistry.pm.sendTo(new WidgetPacket(m),e.getKey());
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean removeModel(Location loc, Model m) {
		HashMap<String, Model> buffor = widgetList.get(loc);
		if (buffor == null)
			return false;
		Model model = buffor.remove(m.getId());
		if (model != null) {
			for (Entry<EntityPlayerMP, Location> e : players.entrySet()) {
				if(e.getValue().equals(loc)){
					NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVE, model.getId()), e.getKey());
				}
			}
			return true;
		} else {
			return false;
		}

	}

	public void removeAllModels(Location loc) {
		HashMap<String, Model> buffor = widgetList.get(loc);
		if (buffor != null) {
			buffor.clear();
		}
		for (Entry<EntityPlayerMP, Location> e : players.entrySet()) {
			if(e.getValue().equals(loc)){
				NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVEALL), e.getKey());
			}
		}
	}

	public int getModelCount(Location l) {
		HashMap<String, Model> buffor = widgetList.get(l);
		if (buffor != null) {
			return buffor.size();
		} else {
			return 0;
		}
	}
	
	public void sendModelUpdate(int action, String id, float... args){
				WidgetPacket p = null;
				switch (action) {
				case WidgetPacket.VISIBLE:
					p = new WidgetPacket(WidgetPacket.VISIBLE, id);
					break;
				case WidgetPacket.INVISIBLE:
					p = new WidgetPacket(WidgetPacket.INVISIBLE, id);
					break;
				case WidgetPacket.RESET:		
					p = new WidgetPacket(WidgetPacket.RESET, id);
					break;
				case WidgetPacket.ROTATE:
					p = new WidgetPacket(id, WidgetPacket.ROTATE, args);
					break;
				case WidgetPacket.SCALE:
					p = new WidgetPacket(id, WidgetPacket.SCALE, args);
					break;
				case WidgetPacket.TRANSLATE:
					p = new WidgetPacket(id, WidgetPacket.TRANSLATE, args);
					break;
				}
				if(p == null) return;
				Location loc = findLocation(id);
				if(loc == null) return;
				List<EntityPlayerMP> recivers = findPlayers(loc);
				for(EntityPlayerMP player : recivers){
					NetworkRegistry.pm.sendTo(p, player);	
				}
		}
	
	private List<EntityPlayerMP> findPlayers(Location loc){
		List<EntityPlayerMP> playersList = new LinkedList<EntityPlayerMP>();
		for(Entry<EntityPlayerMP, Location> entry : players.entrySet()){
			if(entry.getValue().equals(loc)){
				playersList.add(entry.getKey());
			}
		}
		return playersList;	 
	}
	
	private Location findLocation(String id){
		for(Entry<Location, HashMap<String, Model>> e: widgetList.entrySet()){
			HashMap<String, Model> modelList = e.getValue();
			if(modelList!=null && modelList.containsKey(id)){
				return e.getKey();
			}
		}
		return null;
	}

}
