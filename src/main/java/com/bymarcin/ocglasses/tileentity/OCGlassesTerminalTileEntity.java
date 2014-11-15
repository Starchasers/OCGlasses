package com.bymarcin.ocglasses.tileentity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.ServerSurface;
import com.bymarcin.ocglasses.surface.Widgets;
import com.bymarcin.ocglasses.surface.WigetUpdatePacket;
import com.bymarcin.ocglasses.surface.widgets.SquareWidget;
import com.bymarcin.ocglasses.utils.Vec3;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class OCGlassesTerminalTileEntity extends TileEntityEnvironment
	implements SimpleComponent{
	public ArrayList<IWidget> widgetList = new ArrayList<IWidget>();
	
	public OCGlassesTerminalTileEntity() {
		node = Network.newNode(this, Visibility.Network).create();
	}
	
	@Override
	public String getComponentName() {
		return "glasses";
	}
	
	public Vec3 getTerminalUUID(){
		return new Vec3(xCoord, yCoord, zCoord);
	}
	
	public void onGlassesPutOff(String user){
		if(node!=null){
		node.sendToReachable("computer.signal","glasses_off",user);
		}
	}
	
	public void onGlassesPutLinked(String user){
		if(node!=null){
		node.sendToReachable("computer.signal","glasses_linked",user);
		}
	}
	
	
//	@Callback
//    @Optional.Method(modid = "OpenComputers")
//    public Object[] greet(Context context, Arguments args) {
//		return new Object[]{String.format("Hello, %s!", args.checkString(0))};
//    }
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getLinkedGlasses(Context context, Arguments args) {
		return new Object[]{"uu-id-sdds-v-df-dsfg-ds"};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getObjectLimit(Context context, Arguments args){
		
		return new Object[]{};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getObjectCount(Context context, Arguments args){
		return new Object[]{};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getObject(Context context, Arguments args){
		return new Object[]{};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] setCurrentDisplay(Context context, Arguments args){
		return new Object[]{};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeObject(Context context, Arguments args){
		return new Object[]{};
	}
	
	public void onGlassesPutOn(String user){
		if(node!=null){
		node.sendToReachable("computer.signal","glasses_on",user);
		}
	}
	
	/* Object manipulation */
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] addBox(Context context, Arguments args){
		IWidget w = new SquareWidget(args.checkDouble(0),args.checkDouble(1),args.checkDouble(2),args.checkDouble(3),args.checkDouble(4));
		widgetList.add(w);
		ServerSurface.instance.sendToUUID(new WigetUpdatePacket(w, WigetUpdatePacket.Action.AddWigets), getTerminalUUID());
		return new Object[]{widgetList.indexOf(w)};
	}

	
	/* User interaction */
	
	/**
	 * @return Position relative to terminal position
	 */
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getUserPosition(Context context, Arguments args){
		return new Object[]{};
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getUserLookingAt(Context context, Arguments args){
		return new Object[]{};
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound tag = new NBTTagCompound();
		int size = widgetList.size();
		nbt.setInteger("listSize", size);
		
		for (int i=0; i< size; i++) {
			NBTTagCompound widget = new NBTTagCompound();
			widget.setString("widgetType", widgetList.get(i).getType().name());
			
			NBTTagCompound wNBT = new NBTTagCompound();
			widgetList.get(i).writeToNBT(wNBT);
			widget.setTag("widget", wNBT);
			tag.setTag(String.valueOf(i), widget);
		}
		nbt.setTag("widgetList", tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		widgetList.clear();
		if(nbt.hasKey("widgetList") && nbt.hasKey("listSize")){
			NBTTagCompound list = (NBTTagCompound) nbt.getTag("widgetList");
			int size = nbt.getInteger("listSize");
			for(int i=0;i<size;i++){
				if(list.hasKey(String.valueOf(i))){
					NBTTagCompound wiget = (NBTTagCompound) list.getTag(String.valueOf(i));
					if(wiget.hasKey("widgetType") && wiget.hasKey("widget")){
						Widgets type = Widgets.valueOf(wiget.getString(("widgetType")));
						IWidget w = type.getNewInstance();
						w.readFromNBT((NBTTagCompound) wiget.getTag("widget"));
					    widgetList.add(w);
					}
				}
			}
		}
	}
	
}
