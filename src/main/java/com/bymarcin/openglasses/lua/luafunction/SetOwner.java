package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IPrivate;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class SetOwner extends LuaFunction{
	private Object[] setOwner(String name){
		Widget widget = getSelf().getWidget();
		UUID owner = widget.setOwner(name);

		if(name.length() > 0) {
			if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name) != null) {
				getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
				return new Object[]{ true, owner };
			}
			else
				return new Object[]{ false };
		}
		else {
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
			return new Object[]{true, owner};
		}
	}

	@Override
	@Callback(direct = true)
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		if(getSelf().getWidget() instanceof IPrivate){
			String name = arguments.optString(0, "");
			return this.setOwner(name);
		}
		throw new RuntimeException("Component does not exists!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "setOwner";
	}

}
