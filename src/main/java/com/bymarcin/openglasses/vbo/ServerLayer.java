package com.bymarcin.openglasses.vbo;

import java.util.HashMap;
import java.util.LinkedList;
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
		Location location = players.get(player);
		if (location == null) {
			if (players.put(player, location) != null) {
				HashMap<String, Model> models = widgetList.get(loc);
				if (models != null) {
					for (Model m : models.values()) {
						NetworkRegistry.pm.sendTo(new WidgetPacket(m), player);
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean unsubscribePlayer(EntityPlayerMP player) {
		Location location = players.get(player);
		if (location != null) {
			if(players.remove(player) != null){
				NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVEALL), player);
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
		if (!widgetList.containsValue(m.getId())) {
			buffor.put(m.getId(), m);
			for (Entry<EntityPlayerMP, Location> e : players.entrySet()) {
				if(e.getValue().equals(loc)){
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

}
