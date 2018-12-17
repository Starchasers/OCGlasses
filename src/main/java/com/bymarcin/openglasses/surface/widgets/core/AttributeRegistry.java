package com.bymarcin.openglasses.surface.widgets.core;

import java.util.HashMap;
import java.util.LinkedList;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.surface.widgets.core.attribute.*;
import com.bymarcin.openglasses.surface.widgets.core.luafunction.*;


public class AttributeRegistry {
	static{
		attributes = new HashMap<Class<? extends IAttribute>, LinkedList<Class<? extends LuaFunction>>>();
		
		addAtribute(IAttribute.class, GetID.class);
		addAtribute(IAttribute.class, IsVisible.class);
		addAtribute(IAttribute.class, SetVisible.class);
		
		addAtribute(IAttribute.class, RemoveWidget.class);
		
		addAtribute(IAttribute.class, AddColor.class);
		addAtribute(IAttribute.class, AddTranslation.class);
		addAtribute(IAttribute.class, AddRotation.class);
		addAtribute(IAttribute.class, AddScale.class);
		addAtribute(IAttribute.class, RemoveModifier.class);
		addAtribute(IAttribute.class, GetModifiers.class);		
		addAtribute(IAttribute.class, SetCondition.class);
		addAtribute(IAttribute.class, SetEasing.class);
		addAtribute(IAttribute.class, RemoveEasing.class);
		addAtribute(IAttribute.class, GetColor.class);
		addAtribute(IAttribute.class, UpdateModifier.class);
		addAtribute(IAttribute.class, GetRenderPosition.class);

		addAtribute(IAutoTranslateable.class, AddAutoTranslation.class);
		addAtribute(IAlignable.class, SetHorizontalAlign.class);
		addAtribute(IAlignable.class, SetVerticalAlign.class);

		addAtribute(ITracker.class, SetTrackingType.class);
		addAtribute(ITracker.class, SetTrackingFilter.class);
		addAtribute(ITracker.class, SetTrackingEntity.class);

		addAtribute(ICustomShape.class, SetGLMODE.class);
		addAtribute(ICustomShape.class, SetShading.class);
		addAtribute(ICustomShape.class, SetVertex.class);
		addAtribute(ICustomShape.class, AddVertex.class);
		addAtribute(ICustomShape.class, RemoveVertex.class);
		addAtribute(ICustomShape.class, GetVertexCount.class);

		addAtribute(IOBJModel.class, LoadOBJ.class);

		addAtribute(IResizable.class, GetSize.class);
		addAtribute(IResizable.class, SetSize.class);

		addAtribute(ITextable.class, SetFont.class);
		addAtribute(ITextable.class, SetText.class);
		addAtribute(ITextable.class, GetText.class);
		addAtribute(ITextable.class, GetSize.class);
		addAtribute(ITextable.class, SetAntialias.class);
		addAtribute(ITextable.class, SetFontSize.class);

		addAtribute(IItem.class, SetItem.class);
		addAtribute(IItem.class, GetItem.class);

		addAtribute(IThroughVisibility.class, SetVisibleThroughObjects.class);
		addAtribute(IThroughVisibility.class, IsVisibleThroughObjects.class);
		
		addAtribute(IViewDistance.class, SetViewDistance.class);
		addAtribute(IViewDistance.class, GetViewDistance.class);

		addAtribute(IViewDistance.class, SetFaceWidgetToPlayer.class);

		addAtribute(ILookable.class, SetLookingAt.class);
		addAtribute(ILookable.class, GetLookingAt.class);
				
		addAtribute(IPrivate.class, SetOwner.class);
		addAtribute(IPrivate.class, GetOwner.class);
		addAtribute(IPrivate.class, GetOwnerUUID.class);		
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

