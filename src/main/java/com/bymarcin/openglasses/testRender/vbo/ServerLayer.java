package com.bymarcin.openglasses.testRender.vbo;

import java.util.HashMap;
import java.util.LinkedList;

import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.testRender.vbo.model.Model;
import com.bymarcin.openglasses.testRender.vbo.network.WidgetPacket;
import com.bymarcin.openglasses.utils.Location;

import net.minecraft.entity.player.EntityPlayerMP;

public class ServerLayer {
	private static ServerLayer instance = new ServerLayer();
	private HashMap<Location, HashMap<String, Model>> widgetList = new HashMap<Location, HashMap<String, Model>>();
	private HashMap<Location, LinkedList<EntityPlayerMP>> players = new HashMap<Location, LinkedList<EntityPlayerMP>>();

	private ServerLayer() {

	}

	public static ServerLayer instance() {
		return instance;
	}

	public boolean subscribePlayer(Location loc, EntityPlayerMP player) {
		LinkedList<EntityPlayerMP> pl = players.get(loc);
		if (pl != null) {
			if (pl.add(player)) {
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

	public boolean unsubscribePlayer(Location loc, EntityPlayerMP player) {
		LinkedList<EntityPlayerMP> pl = players.get(loc);
		if (pl != null) {
			if(pl.remove(player)){
				NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVEALL), player);
				return true;
			}
		}
		return false;

	}

	public void registerLocation(Location loc) {
		widgetList.put(loc, new HashMap<String, Model>());
		players.put(loc, new LinkedList<EntityPlayerMP>());
		System.out.println("REGISTER: " + loc.toString());
	}

	public void unregisterLocation(Location loc) {
		widgetList.remove(loc);
		players.remove(loc);
		System.out.println("UNREGISTER: " + loc.toString());
	}

	public boolean addModel(Location loc, Model m) {
		HashMap<String, Model> buffor = widgetList.get(loc);
		if (buffor == null)
			return false;
		if (!widgetList.containsValue(m.getId())) {
			buffor.put(m.getId(), m);
			for (EntityPlayerMP p : players.get(loc)) {
				NetworkRegistry.pm.sendTo(new WidgetPacket(m), p);
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
			for (EntityPlayerMP p : players.get(loc)) {
				NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVE, model.getId()), p);
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
		for (EntityPlayerMP p : players.get(loc)) {
			NetworkRegistry.pm.sendTo(new WidgetPacket(WidgetPacket.REMOVEALL), p);
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
