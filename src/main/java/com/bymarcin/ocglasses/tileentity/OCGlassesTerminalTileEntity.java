package com.bymarcin.ocglasses.tileentity;

import java.util.ArrayList;

import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.ServerSurface;
import com.bymarcin.ocglasses.surface.WigetUpdatePacket;
import com.bymarcin.ocglasses.surface.widgets.SquareWidget;
import com.bymarcin.ocglasses.utils.Vec3;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class OCGlassesTerminalTileEntity extends TileEntityEnvironment
	implements SimpleComponent{
	public ArrayList<IWidget> wigetList = new ArrayList<IWidget>();
	
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
		wigetList.add(w);
		ServerSurface.instance.sendToUUID(new WigetUpdatePacket(w, WigetUpdatePacket.Action.AddWigets), getTerminalUUID());
		return new Object[]{wigetList.indexOf(w)};
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
	
}
