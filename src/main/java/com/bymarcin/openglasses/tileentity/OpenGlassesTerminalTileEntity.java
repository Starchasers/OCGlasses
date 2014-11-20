package com.bymarcin.openglasses.tileentity;

import java.util.HashMap;
import java.util.Map.Entry;

import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.face.Dot;
import com.bymarcin.openglasses.surface.widgets.component.face.SquareWidget;
import com.bymarcin.openglasses.surface.widgets.component.face.TriangleWidget;
import com.bymarcin.openglasses.surface.widgets.component.world.Cube3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Dot3D;
import com.bymarcin.openglasses.surface.widgets.component.world.FloatingText;
import com.bymarcin.openglasses.surface.widgets.component.world.Line3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Quad3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Triangle3D;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment{
	
	public HashMap<Integer,Widget> widgetList = new HashMap<Integer,Widget>();
	int currID=0;
	Location loc;
	
	public OpenGlassesTerminalTileEntity() {
		node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).create();
	}
	
	public String getComponentName() {
		return "glasses";
	}
	
	public Location getTerminalUUID(){
		if(loc!=null){
			return loc;
		}
		return loc = new Location(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
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
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getActivePlayers(Context context, Arguments args) {
		return ServerSurface.instance.getActivePlayers(getTerminalUUID());
	}
//	
//	@Callback
//	@Optional.Method(modid = "OpenComputers")
//	public Object[] getObjectLimit(Context context, Arguments args){
//		
//		return new Object[]{};
//	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getObjectCount(Context context, Arguments args){
		return new Object[]{widgetList.size()};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeObject(Context context, Arguments args){
		int id = args.checkInteger(0);
		return new Object[]{removeWidget(id)};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeAll(Context context, Arguments args){
		currID = 0;
		widgetList.clear();
		ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), getTerminalUUID());
		return new Object[]{};
	}

	
	/* Object manipulation */
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addBox(Context context, Arguments args){
		Widget w = new SquareWidget(args.checkDouble(0),args.checkDouble(1),args.checkDouble(2),args.checkDouble(3),args.checkDouble(4));
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addDot(Context context, Arguments args){
		Widget w = new Dot(args.checkDouble(0),args.checkDouble(1),args.checkDouble(2),args.checkDouble(3),args.checkDouble(4));
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addCube3D(Context context, Arguments args){
		Widget w = new Cube3D();
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addFloatingText(Context context, Arguments args){
		Widget w = new FloatingText();
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addTriangle(Context context, Arguments args){
		Widget w = new TriangleWidget(args.checkDouble(0),args.checkDouble(1),args.checkDouble(2),args.checkDouble(3),args.checkDouble(4), args.checkDouble(5), args.checkDouble(6), args.checkDouble(7), args.checkDouble(8));
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addDot3D(Context context, Arguments args){
		Widget w = new Dot3D((float)args.checkDouble(0),(float)args.checkDouble(1),(float)args.checkDouble(2));
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addLine3D(Context context, Arguments args){
		Widget w = new Line3D(
				new float[]	{(float)args.checkDouble(0),(float)args.checkDouble(1)},
				new float[] {(float)args.checkDouble(2),(float)args.checkDouble(3)},
				new float[] {(float)args.checkDouble(4),(float)args.checkDouble(5)});
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addTriangle3D(Context context, Arguments args){
		Widget w = new Triangle3D(
				new float[]	{(float)args.checkDouble(0),(float)args.checkDouble(1),(float)args.checkDouble(2)},
				new float[] {(float)args.checkDouble(3),(float)args.checkDouble(4),(float)args.checkDouble(5)},
				new float[] {(float)args.checkDouble(6),(float)args.checkDouble(7),(float)args.checkDouble(8)});
		return addWidget(w);
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addQuad3D(Context context, Arguments args){
		Widget w = new Quad3D(
				new float[]	{(float)args.checkDouble(0),(float)args.checkDouble(1),(float)args.checkDouble(2),(float)args.checkDouble(3)},
				new float[] {(float)args.checkDouble(4),(float)args.checkDouble(5),(float)args.checkDouble(6),(float)args.checkDouble(7)},
				new float[] {(float)args.checkDouble(8),(float)args.checkDouble(9),(float)args.checkDouble(10),(float)args.checkDouble(11)});
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
	public void writeToNBT(NBTTagCompound nbt) {
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
	}
	
}
