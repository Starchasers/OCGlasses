package com.bymarcin.openglasses.tileentity;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket.TerminalStatus;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.face.Dot;
import com.bymarcin.openglasses.surface.widgets.component.face.Quad;
import com.bymarcin.openglasses.surface.widgets.component.face.SquareWidget;
import com.bymarcin.openglasses.surface.widgets.component.face.Text;
import com.bymarcin.openglasses.surface.widgets.component.face.TriangleWidget;
import com.bymarcin.openglasses.surface.widgets.component.world.Cube3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Dot3D;
import com.bymarcin.openglasses.surface.widgets.component.world.FloatingText;
import com.bymarcin.openglasses.surface.widgets.component.world.Line3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Quad3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Triangle3D;
import com.bymarcin.openglasses.utils.Location;

import li.cil.oc.api.API;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment {
	
	public HashMap<Integer,Widget> widgetList = new HashMap<Integer,Widget>();
	int currID=0;
	Location loc;
	boolean isPowered;
	
	public OpenGlassesTerminalTileEntity() {
		node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenGlasses.energyBuffer).create();
	}

	public void sendInteractEvent(String eventType, String name, double x, double y, double z, double lx, double ly, double lz, double eyeh){
		if(node!=null){
			node.sendToReachable("computer.signal", eventType.toLowerCase(), name, x - getPos().getX(), y  - getPos().getY(), z - getPos().getZ(), lx, ly, lz, eyeh);
		}
	}

	public void sendInteractEvent(String eventType, String name, double button, double x, double y, double width, double height){
		if(node!=null){
			node.sendToReachable("computer.signal", eventType.toLowerCase(), name, button, x, y, width, height);
		}
	}
	
	public String getComponentName() {
		return "glasses";
	}
	
	public Location getTerminalUUID(){
		if(loc!=null){
			return loc;
		}
		return loc = new Location(getPos(), worldObj.provider.getDimension(), UUID.randomUUID().getMostSignificantBits());
	}
	
	public void onGlassesPutOn(String user){
		if(node!=null){
			node.sendToReachable("computer.signal","glasses_on",user);
		}
	}
	
	public void onGlassesPutOff(String user){
		if(node!=null){
			node.sendToReachable("computer.signal","glasses_off",user);
		}
	}

//	@Callback
//    @Optional.Method(modid = "OpenComputers")
//    public Object[] greet(Context context, Arguments args) {
//		return new Object[]{String.format("Hello, %s!", args.checkString(0))};
//    }
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] getBindPlayers(Context context, Arguments args) {
		return ServerSurface.instance.getActivePlayers(getTerminalUUID());
	}
//	
//	@Callback
//	@Optional.Method(modid = "OpenComputers")
//	public Object[] getObjectLimit(Context context, Arguments args){
//		
//		return new Object[]{};
//	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] getObjectCount(Context context, Arguments args){
		return new Object[]{widgetList.size()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeObject(Context context, Arguments args){
		int id = args.checkInteger(0);
		return new Object[]{removeWidget(id)};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeAll(Context context, Arguments args){
		currID = 0;
		widgetList.clear();
		ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), getTerminalUUID());
		return new Object[]{};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
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
	@Optional.Method(modid = "OpenComputers")
	public Object[] addRect(Context context, Arguments args){
		Widget w = new SquareWidget();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addDot(Context context, Arguments args){
		Widget w = new Dot();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addCube3D(Context context, Arguments args){
		Widget w = new Cube3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addFloatingText(Context context, Arguments args){
		Widget w = new FloatingText();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addTriangle(Context context, Arguments args){
		Widget w = new TriangleWidget();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addDot3D(Context context, Arguments args){
		Widget w = new Dot3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addTextLabel(Context context, Arguments args){
		Widget w = new Text();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addLine3D(Context context, Arguments args){
		Widget w = new Line3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addTriangle3D(Context context, Arguments args){
		Widget w = new Triangle3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addQuad3D(Context context, Arguments args){
		Widget w = new Quad3D();
		return addWidget(w);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addQuad(Context context, Arguments args){
		Widget w = new Quad();
		return addWidget(w);
	}

	/* User interaction */
	
	/**
	 * @return Position relative to terminal position
	 */
//	@Callback
//	@Optional.Method(modid = "OpenComputers")
//	public Object[] getUserPosition(Context context, Arguments args){
//		return new Object[]{};
//	}
//	
//	@Callback
//	@Optional.Method(modid = "OpenComputers")
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
		if(w!=null)
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
        
		if(worldObj.isRemote) return;
		boolean lastStatus = isPowered;
		if((node()!=null) && ((Connector)node()).tryChangeBuffer(-widgetList.size()/10f*OpenGlasses.energyMultiplier) ){
			isPowered = true;
		}else{
			isPowered = false;
		}
		
		if(lastStatus != isPowered){
			ServerSurface.instance.sendPowerInfo(getTerminalUUID(), isPowered?TerminalStatus.HavePower:TerminalStatus.NoPower);
		}
		
	}

	
	public boolean isPowered() {
		return isPowered;
	}

}
