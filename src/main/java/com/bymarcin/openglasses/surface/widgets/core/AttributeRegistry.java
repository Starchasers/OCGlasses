package com.bymarcin.openglasses.surface.widgets.core;

import java.util.HashMap;
import java.util.LinkedList;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I2DVertex;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAttribute;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IViewDistance;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ILookable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IResizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DVertex;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetAlpha;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetColor;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetViewDistance;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetID;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetLookingAt;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetPosition;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetPosition3D;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetScale;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetSize;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetText;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.GetVertexCount;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.IsVisible;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.IsVisibleThroughObjects;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetAlpha;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetColor;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetViewDistance;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetLookingAt;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetPosition;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetPosition3D;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetScale;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetSize;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetText;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetVertex;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetVisible;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.SetVisibleThroughObjects;

public class AttributeRegistry {
	static{
		attributes = new HashMap<Class<? extends IAttribute>, LinkedList<Class<? extends LuaFunction>>>();
		
		addAtribute(IAttribute.class, GetID.class);
		addAtribute(IAttribute.class, IsVisible.class);
		addAtribute(IAttribute.class, SetVisible.class);
		
		addAtribute(IAlpha.class, GetAlpha.class);
		addAtribute(IAlpha.class, SetAlpha.class);
		
		addAtribute(IColorizable.class, SetColor.class);
		addAtribute(IColorizable.class, GetColor.class);
		
		addAtribute(IPositionable.class, SetPosition.class);
		addAtribute(IPositionable.class, GetPosition.class);
		
		addAtribute(IResizable.class, GetSize.class);
		addAtribute(IResizable.class, SetSize.class);
		
		addAtribute(I3DPositionable.class, SetPosition3D.class);
		addAtribute(I3DPositionable.class, GetPosition3D.class);
		
		addAtribute(ITextable.class, SetText.class);
		addAtribute(ITextable.class, GetText.class);
		
		addAtribute(IScalable.class, SetScale.class);
		addAtribute(IScalable.class, GetScale.class);
		
		addAtribute(IThroughVisibility.class, SetVisibleThroughObjects.class);
		addAtribute(IThroughVisibility.class, IsVisibleThroughObjects.class);
		
		addAtribute(I3DVertex.class, SetVertex.class);
		addAtribute(I3DVertex.class, GetVertexCount.class);
		
		addAtribute(I2DVertex.class, SetVertex.class);
		addAtribute(I2DVertex.class, GetVertexCount.class);
		
		addAtribute(IViewDistance.class, SetViewDistance.class);
		addAtribute(IViewDistance.class, GetViewDistance.class);
		
		addAtribute(ILookable.class, SetLookingAt.class);
		addAtribute(ILookable.class, GetLookingAt.class);
		
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
