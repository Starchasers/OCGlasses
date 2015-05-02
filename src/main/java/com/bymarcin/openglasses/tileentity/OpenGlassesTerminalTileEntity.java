package com.bymarcin.openglasses.tileentity;

import java.util.UUID;

import com.bymarcin.openglasses.network.packet.TerminalStatusPacket.TerminalStatus;
import com.bymarcin.openglasses.utils.Location;
import com.bymarcin.openglasses.utils.OGUtils;
import com.bymarcin.openglasses.vbo.ServerLayer;
import com.bymarcin.openglasses.vbo.computer.ModelBuilder;
import com.bymarcin.openglasses.vbo.computer.ModelLua;

import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment{

	private Location loc;
	boolean isPowered;
	boolean isRegistered = false;
	
	public OpenGlassesTerminalTileEntity() {
		node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(100).create();
		
	}
	
	
	
	public String getComponentName() {
		return "glasses";
	}
	
	public Location getTerminalLocation(){
		if(loc!=null){
			return loc;
		}
		return loc = new Location(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, UUID.randomUUID().getMostSignificantBits());
	}
	
	public void onGlassesPutOn(String user){
		if(node!=null){
			node.sendToReachable("computer.signal", "glasses_on", user);
		}
	}
	
	public void onGlassesPutOff(String user){
		if(node!=null){
			node.sendToReachable("computer.signal", "glasses_off", user);
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if(worldObj.isRemote) return;
		ServerLayer.instance().unregisterLocation(getTerminalLocation());
		isRegistered = false;
	}

//	@Callback
//    @Optional.Method(modid = "OpenComputers")
//    public Object[] greet(Context context, Arguments args) {
//		return new Object[]{String.format("Hello, %s!", args.checkString(0))};
//    }
	
//	@Callback(direct = true)
//	@Optional.Method(modid = "OpenComputers")
//	public Object[] getBindPlayers(Context context, Arguments args) {
//		return ServerSurface.instance.getActivePlayers(getTerminalUUID());
//	}

	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] getModelCount(Context context, Arguments args){
		return new Object[]{ServerLayer.instance().getModelCount(getTerminalLocation())};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeModel(Context context, Arguments args){
		ModelLua model = (ModelLua) args.checkAny(0);
		if(ServerLayer.instance().removeModel(getTerminalLocation(), model.getModel())){
			return new Object[]{true};
		}else{
			return new Object[]{false, OGUtils.getLocalization("og.object.error.notfound")};
		}
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] removeAll(Context context, Arguments args){
		ServerLayer.instance().removeAllModels(getTerminalLocation());
		return null;
	}
	
//	@Callback(direct = true)
//	@Optional.Method(modid = "OpenComputers")
//	public Object[] newUniqueKey(Context context, Arguments args){
//		String [] players = ServerSurface.instance.getActivePlayers(loc);
//		for(String p: players){
//			ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), loc);
//			ServerSurface.instance.unsubscribePlayer(p);
//		}
//		loc.uniqueKey = UUID.randomUUID().getMostSignificantBits();
//		return new Object[]{loc.uniqueKey};
//	}

	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] newModel(Context context, Arguments args){
		return new Object[]{new ModelBuilder(UUID.randomUUID().toString())};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] addModel(Context context, Arguments args){
		ModelLua model = (ModelLua) args.checkAny(0);
		if(ServerLayer.instance().addModel(getTerminalLocation(), model.getModel())){
			return new Object[]{true};
		}else{
			return new Object[]{false, OGUtils.getLocalization("og.object.error.exists")};
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound tagLoc = new NBTTagCompound();
		loc.writeToNBT(tagLoc);
		nbt.setTag("uniqueKey", tagLoc);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("uniqueKey")){
			loc = new Location().readFromNBT((NBTTagCompound) nbt.getTag("uniqueKey"));
		}
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.isRemote) return;
		if(!isRegistered){
			ServerLayer.instance().registerLocation(getTerminalLocation());
			isRegistered = true;
		}
		
		boolean lastStatus = isPowered;
		if((node()!=null) && ((Connector)node()).tryChangeBuffer(-10f) ){
			isPowered = true;
		}else{
			isPowered = false;
		}
		
		if(lastStatus != isPowered){
		//	ServerSurface.instance.sendPowerInfo(getTerminalLocation(), isPowered?TerminalStatus.HavePower:TerminalStatus.NoPower);
		}
		
	}
	
	public boolean isPowered() {
		return isPowered;
	}
}
