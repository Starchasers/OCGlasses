package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.OpenGlasses;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IPrivate;
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
	public Object[] call(Context context, Arguments arguments) {
		super.call(context, arguments);
		if(getSelf().getWidget() instanceof IPrivate){
			String name = "";
			if(arguments.count() >= 1)
				name = arguments.checkString(0);

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
