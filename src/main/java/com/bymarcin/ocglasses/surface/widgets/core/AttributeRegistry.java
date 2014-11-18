package com.bymarcin.ocglasses.surface.widgets.core;

import java.util.HashMap;
import java.util.LinkedList;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.lua.LuaReference;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IAttribute;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IPositionable;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IResizable;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.GetAlpha;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.GetColor;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.GetPosition;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.GetSize;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.SetAlpha;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.SetColor;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.SetPosition;
import com.bymarcin.ocglasses.surface.widgets.core.luafunction.SetSize;

public class AttributeRegistry {
	static{
		attributes = new HashMap<Class<? extends IAttribute>, LinkedList<Class<? extends LuaFunction>>>();
		
		addAtribute(IAlpha.class, GetAlpha.class);
		addAtribute(IAlpha.class, SetAlpha.class);
		
		addAtribute(IColorizable.class, SetColor.class);
		addAtribute(IColorizable.class, GetColor.class);
		
		addAtribute(IPositionable.class, SetPosition.class);
		addAtribute(IPositionable.class, GetPosition.class);
		
		addAtribute(IResizable.class, GetSize.class);
		addAtribute(IResizable.class, SetSize.class);
		
	}
	
	static HashMap<Class<? extends IAttribute>, LinkedList<Class<? extends LuaFunction>>> attributes;
	
	public static void addAtribute(Class<? extends IAttribute> atribute, Class<? extends LuaFunction> luaFunction){
		LinkedList<Class<? extends LuaFunction>> a = attributes.get(atribute);
		if(a!=null){
			a.push(luaFunction);
		}else{
			LinkedList<Class<? extends LuaFunction>> f = new LinkedList<Class<? extends LuaFunction>>();
			f.add(luaFunction);
			attributes.put(atribute, f);
		}	
	}
	
	public static HashMap<String, Object> getFunctions(Class<? extends IAttribute> atribute, LuaReference ref){
		LinkedList<Class<? extends LuaFunction>> functions = attributes.get(atribute);
		HashMap<String, Object> luaObject = new HashMap<String, Object>();
		for(Class<? extends LuaFunction> f: functions){
			try {
				LuaFunction lf = f.newInstance();
				lf.setRef(ref);
				luaObject.put(lf.getName(), lf);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return luaObject;
	}
	
}
