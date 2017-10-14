package com.bymarcin.openglasses.tileentity;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.face.*;
import com.bymarcin.openglasses.surface.widgets.component.world.*;
import com.bymarcin.openglasses.utils.Location;

import li.cil.oc.api.API;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment implements ITickable {
	
	public HashMap<Integer,Widget> widgetList = new HashMap<Integer,Widget>();
	int currID=0;
	Location loc;
	boolean addedToNetwork;

	public OpenGlassesTerminalTileEntity() {
		node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector().create();
	}

	public void sendInteractEventWorld(String eventType, String name, double x, double y, double z, double lx, double ly, double lz, double eyeh){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), name, x - getPos().getX(), y  - getPos().getY(), z - getPos().getZ(), lx, ly, lz, eyeh);
	}

	public void sendInteractEventOverlay(String eventType, String name, double button, double x, double y){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), name, button, x, y);
	}

	public void sendChangeSizeEvent(String eventType, String player, int width, int height, int scaleFactor){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), player, width, height, scaleFactor);
	}


	public String getComponentName() {
		return "glasses";
	}

	public Location getTerminalUUID(){
		if(this.loc == null)
			this.loc = new Location(getPos(), world.provider.getDimension(), UUID.randomUUID().getMostSignificantBits());

		return this.loc;
	}

	public void onGlassesPutOn(String user){
		if(node == null) return;
		node.sendToReachable("computer.signal","glasses_on",user);
	}
	
	public void onGlassesPutOff(String user){
		if(node!=null){
			node.sendToReachable("computer.signal","glasses_off",user);
		}
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getConnectedPlayers(Context context, Arguments args) {
		Object[] ret = ServerSurface.instance.getActivePlayers(getTerminalUUID());
		return ret;
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] requestResolutionEvents(Context context, Arguments args) {
		TerminalStatusPacket packet = new TerminalStatusPacket(TerminalStatusPacket.TerminalEvent.ASYNC_SCREEN_SIZES);
		String user = args.checkString(0).toLowerCase();
		int i=0;
		for(Entry<EntityPlayer, Location> e: ServerSurface.instance.players.entrySet()){
			if(user.length() == 0
					|| user.equals(e.getKey().getDisplayNameString().toLowerCase()))
				if(e.getValue().equals(getTerminalUUID())){
				NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) e.getKey());
				i++;
			}
		}
		return new Object[]{ i };
	}


	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] setRenderResolution(Context context, Arguments args) {
		TerminalStatusPacket packet = new TerminalStatusPacket(TerminalStatusPacket.TerminalEvent.SET_RENDER_RESOLUTION);
		String user = args.checkString(0).toLowerCase();
		packet.x = (float) args.checkDouble(1);
		packet.y = (float) args.checkDouble(2);
		int i=0;
		for(Entry<EntityPlayer, Location> e: ServerSurface.instance.players.entrySet()){
			if(user.length() == 0
					|| user.equals(e.getKey().getDisplayNameString().toLowerCase()))
				if(e.getValue().equals(getTerminalUUID())){
					NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) e.getKey());
					i++;
			}
		}
		return new Object[]{ i };
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getWidgetCount(Context context, Arguments args){
		return new Object[]{widgetList.size()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] removeWidget(Context context, Arguments args){
		int id = args.checkInteger(0);
		return new Object[]{removeWidget(id)};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] removeAll(Context context, Arguments args){
		currID = 0;
		widgetList.clear();
		ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), getTerminalUUID());
		return new Object[]{};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] newUniqueKey(Context context, Arguments args){
		String [] players = ServerSurface.instance.getActivePlayers(loc);
		for(String p: players){
			ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), loc);
			ServerSurface.instance.unsubscribePlayer(p);
		}
		loc.uniqueKey = UUID.randomUUID().getMostSignificantBits();
		return new Object[]{loc.uniqueKey};
	}
	
	/* Object manipulation */
	

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addCube3D(Context context, Arguments args){
		Widget w = new Cube3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addText3D(Context context, Arguments args){
		Widget w = new Text3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addText2D(Context context, Arguments args){
		Widget w = new Text2D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addItem2D(Context context, Arguments args){
		Widget w = new Item2D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addItem3D(Context context, Arguments args){
		Widget w = new Item3D();
		return addWidget(w);
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addCustom2D(Context context, Arguments args){
		Widget w = new Custom2D();
		return addWidget(w);
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addOBJModel3D(Context context, Arguments args){
		Widget w = new OBJModel3D();
		return addWidget(w);
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addOBJModel2D(Context context, Arguments args){
		Widget w = new OBJModel2D();
		return addWidget(w);
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addCustom3D(Context context, Arguments args){
		Widget w = new Custom3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addBox2D(Context context, Arguments args){
		Widget w = new Box2D();
		return addWidget(w);
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] addEntityTracker3D(Context context, Arguments args){
		Widget w = new EntityTracker3D();
		return addWidget(w);
	}
	/* User interaction */
	
	/**
	 * @return Position relative to terminal position
	 * (make this available through upgrading)
	 */
//	@Callback
//	@Optional.Method(modid = "opencomputers")
//	public Object[] getUserPosition(Context context, Arguments args){
//		return new Object[]{};
//	}
//	
//	@Callback
//	@Optional.Method(modid = "opencomputers")
//	public Object[] getUserLookingAt(Context context, Arguments args){
//		return new Object[]{};
//	}


	public boolean removeWidget(int id){
		if(widgetList.containsKey(id) && widgetList.remove(id)!=null){
			ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(id), getTerminalUUID());
			return true;
		}
		return false;
	}
	
	public Object[] addWidget(Widget w){
		widgetList.put(currID,w);
		ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(currID, w), getTerminalUUID());
		int t = currID;
		currID++;
		return w.getLuaObject(new LuaReference(t, getTerminalUUID()));
	}
	
	public void updateWidget(int id){
		Widget w = widgetList.get(id);
		if(w == null) return;
		ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(id, w), getTerminalUUID());
	}
	
	public Widget getWidget(int id){
		return widgetList.get(id);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("currID",currID);
		NBTTagCompound tag = new NBTTagCompound();
		int size = widgetList.size();
		nbt.setInteger("listSize", size);
		int i=0;
		for (Entry<Integer, Widget> e: widgetList.entrySet()) {
			NBTTagCompound widget = new NBTTagCompound();
			widget.setString("widgetType", e.getValue().getType().name());
			widget.setInteger("ID", e.getKey());
			NBTTagCompound wNBT = new NBTTagCompound();
			e.getValue().writeToNBT(wNBT);
			widget.setTag("widget", wNBT);
			tag.setTag(String.valueOf(i), widget);
			i++;
		}
		nbt.setTag("widgetList", tag);
		
		NBTTagCompound tagLoc = new NBTTagCompound();
		if (loc != null) {
			loc.writeToNBT(tagLoc);
			nbt.setTag("uniqueKey", tagLoc);
		}
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		widgetList.clear();
		if(nbt.hasKey("currID")){
			currID = nbt.getInteger("currID");
		}
		
		if(nbt.hasKey("widgetList") && nbt.hasKey("listSize")){
			NBTTagCompound list = (NBTTagCompound) nbt.getTag("widgetList");
			int size = nbt.getInteger("listSize");
			for(int i=0;i<size;i++){
				if(list.hasKey(String.valueOf(i))){
					NBTTagCompound wiget = (NBTTagCompound) list.getTag(String.valueOf(i));
					if(wiget.hasKey("widgetType") && wiget.hasKey("widget")&& wiget.hasKey("ID")){
						WidgetType type = WidgetType.valueOf(wiget.getString(("widgetType")));
						Widget w = type.getNewInstance();
						w.readFromNBT((NBTTagCompound) wiget.getTag("widget"));
					    widgetList.put(wiget.getInteger("ID"),w);
					}
				}
			}
		}
		if(nbt.hasKey("uniqueKey")){
			loc = new Location().readFromNBT((NBTTagCompound) nbt.getTag("uniqueKey"));
		}
	}

	@Override
	public void update() {
		if (!addedToNetwork) {
			addedToNetwork = true;
			Network.joinOrCreateNetwork(this);
		}
	}
}
