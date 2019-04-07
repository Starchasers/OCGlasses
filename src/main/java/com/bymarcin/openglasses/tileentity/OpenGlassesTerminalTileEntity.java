package com.bymarcin.openglasses.tileentity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import ben_mkiv.commons0815.utils.Location;
import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.world.*;
import ben_mkiv.rendertoolkit.common.widgets.component.face.*;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAttribute;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import com.bymarcin.openglasses.lib.McJty.font.TrueTypeFont;
import com.bymarcin.openglasses.lua.AttributeRegistry;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.surface.OCServerSurface;

import com.bymarcin.openglasses.utils.PlayerStatsOC;
import com.bymarcin.openglasses.utils.TerminalLocation;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment implements ITickable {
	
	public HashMap<Integer, Widget> widgetList = new HashMap<Integer,Widget>();
	int currID=0;
	TerminalLocation loc;
	boolean addedToNetwork;

	public OpenGlassesTerminalTileEntity() {
		node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector().create();
	}

	public void sendInteractEventWorldBlock(String eventType, String name, double x, double y, double z, double lx, double ly, double lz, double eyeh, BlockPos pos, EnumFacing face){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), name, x - getPos().getX(), y  - getPos().getY(), z - getPos().getZ(), lx, ly, lz, eyeh, pos.getX() - getPos().getX(), pos.getY() - getPos().getY(), pos.getZ() - getPos().getZ(), face.getName());
	}

	public void sendInteractEventWorld(String eventType, String name, double x, double y, double z, double lx, double ly, double lz, double eyeh){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), name, x - getPos().getX(), y  - getPos().getY(), z - getPos().getZ(), lx, ly, lz, eyeh);
	}

	public void sendInteractEventOverlay(String eventType, String name, double button, double x, double y){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), name, x, y, button);
	}

	public void sendChangeSizeEvent(String eventType, String player, int width, int height, double scaleFactor){
		if(node == null) return;
		node.sendToReachable("computer.signal", eventType.toLowerCase(), player, width, height, scaleFactor);
	}


	public String getComponentName() {
		return "glasses";
	}

	public TerminalLocation getTerminalUUID(){
		if(this.loc == null)
			this.loc = new TerminalLocation(getPos(), world.provider.getDimension(), UUID.randomUUID());

		return this.loc;
	}

	public void onGlassesPutOn(String user){
		if(node == null) return;
		node.sendToReachable("computer.signal","glasses_on",user);
	}
	
	public void onGlassesPutOff(String user){
		if(node == null) return;
		node.sendToReachable("computer.signal","glasses_off",user);
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getConnectedPlayers(Context context, Arguments args) {
		Object[] ret = new Object[OCServerSurface.instance.playerStats.size()];
		int i = 0;
		for(Entry<UUID, PlayerStats> e : OCServerSurface.instance.playerStats.entrySet()){
			PlayerStatsOC s = (PlayerStatsOC) e.getValue();
			ret[i] = new Object[]{ s.name, s.uuid, s.screenWidth, s.screenHeight, s.guiScale };
			i++;
		}

		return new Object[]{ ret };
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] requestResolutionEvents(Context context, Arguments args) {
		String user = args.optString(0, "").toLowerCase();
		int i=0;
		for(Entry<EntityPlayer, Location> e: OCServerSurface.instance.players.entrySet()){
			if(user.length() == 0
					|| user.equals(e.getKey().getDisplayNameString().toLowerCase()))
				if(e.getValue().equals(getTerminalUUID())) {
					OCServerSurface.instance.requestResolutionEvent((EntityPlayerMP) e.getKey());
					i++;

			}
		}
		return new Object[]{ i };
	}

	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] setRenderResolution(Context context, Arguments args) {
		TerminalStatusPacket packet = new TerminalStatusPacket(TerminalStatusPacket.TerminalEvent.SET_RENDER_RESOLUTION);
		String user = "";

		if(args.count() == 3) {
			user = args.checkString(0).toLowerCase();
			packet.x = (float) args.checkDouble(1);
			packet.y = (float) args.checkDouble(2);
		}
		else if(args.count() == 2) {
			packet.x = (float) args.checkDouble(0);
			packet.y = (float) args.checkDouble(1);
		}
		else{
			if(args.count() < 2)
				return new Object[]{ false, "not enough arguments specified" };
			else
				return new Object[]{ false, "to many arguments specified" };
		}

		int i=0;
		for(Entry<EntityPlayer, Location> e: OCServerSurface.instance.players.entrySet()){
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
		if(args.count() < 1 || !args.isInteger(0))
			return new Object[]{ false, "argument widget id missing" };

		int id = args.checkInteger(0);
		return new Object[]{removeWidget(id)};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] removeAll(Context context, Arguments args){
		currID = 0;
		widgetList.clear();
		OCServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), getTerminalUUID());
		return new Object[]{};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] newUniqueKey(Context context, Arguments args){
		String [] players = OCServerSurface.instance.getActivePlayers(loc);
		for(String p: players){
			OCServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), loc);
			OCServerSurface.instance.unsubscribePlayer(p);
		}
		loc.uniqueKey = UUID.randomUUID();
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
	public Object[] getFonts(Context context, Arguments args){
		ArrayList<String> fonts = new ArrayList<>();

		fonts.add("THIS IS A CLIENT LIST");

		for(Font font : TrueTypeFont.getFonts())
			fonts.add(font.getFontName());

		return new Object[]{ fonts.toArray() };
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
			OCServerSurface.instance.sendToUUID(new WidgetUpdatePacket(id), getTerminalUUID());
			return true;
		}
		return false;
	}
	
	public Object[] addWidget(Widget w){
		widgetList.put(currID,w);
		OCServerSurface.instance.sendToUUID(new WidgetUpdatePacket(currID, w), getTerminalUUID());
		int t = currID;
		currID++;
		return getLuaObject(w, new LuaReference(t, getTerminalUUID()));
	}

	public Object[] getLuaObject(Widget widget, LuaReference ref) {
		HashMap<String, Object> luaObject = new HashMap<String, Object>();
		Class<?> current = widget.getClass();
		do {
			for (Class<?> a : current.getInterfaces()) {
				if (IAttribute.class.isAssignableFrom(a)) {
					luaObject.putAll(AttributeRegistry.getFunctions(a.asSubclass(IAttribute.class), ref));
				}
			}
			current = current.getSuperclass();
		} while (!current.equals(Object.class));

		return new Object[] { luaObject };
	}
	
	public void updateWidget(int id){
		Widget w = widgetList.get(id);
		if(w == null) return;
		OCServerSurface.instance.sendToUUID(new WidgetUpdatePacket(id, w), getTerminalUUID());
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
			widget.setInteger("widgetType", e.getValue().getType().ordinal());
			widget.setInteger("ID", e.getKey());
			NBTTagCompound wNBT = new NBTTagCompound();
			e.getValue().writeToNBT(wNBT);
			widget.setTag("widget", wNBT);
			tag.setTag(String.valueOf(i), widget);
			i++;
		}
		nbt.setTag("widgetList", tag);
		
		if (getTerminalUUID() != null) {
			nbt.setTag("location", getTerminalUUID().writeToNBT(new NBTTagCompound()));
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
			for(int i=0; i < nbt.getInteger("listSize"); i++){
				if(list.hasKey(String.valueOf(i))){
					NBTTagCompound wiget = (NBTTagCompound) list.getTag(String.valueOf(i));
					if(wiget.hasKey("widgetType") && wiget.hasKey("widget")&& wiget.hasKey("ID")){
						Widget newWidget = Widget.create(wiget.getInteger("widgetType"));
						newWidget.readFromNBT((NBTTagCompound) wiget.getTag("widget"));
						widgetList.put(wiget.getInteger("ID"), newWidget);
					}
				}
			}
		}
		if(nbt.hasKey("location")){
			loc = (TerminalLocation) new TerminalLocation().readFromNBT(nbt.getCompoundTag("location"));
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
