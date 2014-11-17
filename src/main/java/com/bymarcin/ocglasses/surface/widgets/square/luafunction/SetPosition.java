package com.bymarcin.ocglasses.surface.widgets.square.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.lua.LuaObject;
import com.bymarcin.ocglasses.surface.widgets.square.SquareWidget;
import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;
import com.bymarcin.ocglasses.utils.Location;

public class SetPosition extends LuaFunction{
	LuaObject ref;
	Location loc;
	
	public SetPosition(LuaObject obj, Location loc) {
		this.ref=obj;
		this.loc = loc;
	}
	
	public SetPosition() {

	}

	@Override
	public Object[] call(Context context, Arguments arguments) {
		World w  = MinecraftServer.getServer().worldServerForDimension(loc.dimID);
		if(w==null) return null;
		TileEntity t = w.getTileEntity(loc.x, loc.y, loc.z);
		if(t instanceof OCGlassesTerminalTileEntity){
			SquareWidget o = (SquareWidget) ((OCGlassesTerminalTileEntity)t).widgetList.get(ref.content.get("id"));
			if(o!=null){
				SquareWidget n = new SquareWidget(arguments.checkDouble(0), arguments.checkDouble(1), o.r, o.g, o.b);
				((OCGlassesTerminalTileEntity) t).removeWidget((Integer) ref.content.get("id"));
				ref.content.put("id", ((OCGlassesTerminalTileEntity) t).addWidget(n));
				return null;	
			}
		}
		return null;	
	}
}
