package com.bymarcin.openglasses.lua;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import com.bymarcin.openglasses.component.OpenGlassesHostComponent;
import com.bymarcin.openglasses.surface.OCServerSurface;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class LuaReference {
	private int widgetRef;
	private UUID hostUUID;
	
	public LuaReference(int id, UUID uuid) {
		hostUUID = uuid;
		widgetRef = id;
	}

	public int getWidgetRef() {
		return widgetRef;
	}
	
	public OpenGlassesHostComponent getHost(){
		return OCServerSurface.getHost(hostUUID);
	}
	
	public Widget getWidget(){
		OpenGlassesHostComponent host = getHost();
		if(host != null){
			return host.getWidget(widgetRef);
		}
		return null;
	}
	
	public LuaReference() {}
	
	public LuaReference readFromNBT(NBTTagCompound nbt) {
		hostUUID = nbt.getUniqueId("host");
		widgetRef = nbt.getInteger("id");
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setUniqueId("host", hostUUID);
		nbt.setInteger("id", widgetRef);
		return nbt;
	}
}
