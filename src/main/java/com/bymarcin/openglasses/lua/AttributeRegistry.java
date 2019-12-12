package com.bymarcin.openglasses.lua;

import java.util.HashMap;
import java.util.LinkedList;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.*;
import com.bymarcin.openglasses.lua.luafunction.*;


public class AttributeRegistry {
    private static HashMap<Class<? extends IAttribute>, LinkedList<Class<? extends LuaFunction>>> attributes = new HashMap<>();

	static{
		addAttribute(IAttribute.class, GetID.class);
		addAttribute(IAttribute.class, IsVisible.class);
		addAttribute(IAttribute.class, SetVisible.class);

		addAttribute(IAttribute.class, SetCulling.class);

		addAttribute(IAttribute.class, Modifiers.class);
		
		addAttribute(IAttribute.class, RemoveWidget.class);
		
		addAttribute(IAttribute.class, AddColor.class);
		addAttribute(IAttribute.class, AddTranslation.class);
		addAttribute(IAttribute.class, AddRotation.class);
		addAttribute(IAttribute.class, AddScale.class);
		addAttribute(IAttribute.class, RemoveModifier.class);
		addAttribute(IAttribute.class, GetModifiers.class);
		addAttribute(IAttribute.class, SetCondition.class);
		addAttribute(IAttribute.class, SetEasing.class);
		addAttribute(IAttribute.class, RemoveEasing.class);
		addAttribute(IAttribute.class, GetColor.class);
		addAttribute(IAttribute.class, UpdateModifier.class);
		addAttribute(IAttribute.class, GetRenderPosition.class);

		addAttribute(IAutoTranslateable.class, AddAutoTranslation.class);
		addAttribute(IAlignable.class, SetHorizontalAlign.class);
		addAttribute(IAlignable.class, SetVerticalAlign.class);

		addAttribute(ITracker.class, SetTrackingType.class);
		addAttribute(ITracker.class, SetTrackingFilter.class);
		addAttribute(ITracker.class, SetTrackingEntity.class);

		addAttribute(ICustomShape.class, SetGLMODE.class);
		addAttribute(ICustomShape.class, SetShading.class);
		addAttribute(ICustomShape.class, SetVertex.class);
		addAttribute(ICustomShape.class, AddVertex.class);
		addAttribute(ICustomShape.class, RemoveVertex.class);
		addAttribute(ICustomShape.class, GetVertexCount.class);

		addAttribute(IOBJModel.class, LoadOBJ.class);

		addAttribute(IResizable.class, GetSize.class);
		addAttribute(IResizable.class, SetSize.class);

		addAttribute(ITextable.class, SetFont.class);
		addAttribute(ITextable.class, SetText.class);
		addAttribute(ITextable.class, GetText.class);
		addAttribute(ITextable.class, GetSize.class);
		addAttribute(ITextable.class, SetAntialias.class);
		addAttribute(ITextable.class, SetFontSize.class);

		addAttribute(IItem.class, SetItem.class);
		addAttribute(IItem.class, GetItem.class);

		addAttribute(IThroughVisibility.class, SetVisibleThroughObjects.class);
		addAttribute(IThroughVisibility.class, IsVisibleThroughObjects.class);
		
		addAttribute(IViewDistance.class, SetViewDistance.class);
		addAttribute(IViewDistance.class, GetViewDistance.class);

		addAttribute(IViewDistance.class, SetFaceWidgetToPlayer.class);

		addAttribute(ILookable.class, SetLookingAt.class);
		addAttribute(ILookable.class, GetLookingAt.class);
				
		addAttribute(IPrivate.class, SetOwner.class);
		addAttribute(IPrivate.class, GetOwner.class);
		addAttribute(IPrivate.class, GetOwnerUUID.class);
	}
	
	public static void addAttribute(Class<? extends IAttribute> attribute, Class<? extends LuaFunction> luaFunction){
		LinkedList<Class<? extends LuaFunction>> a = attributes.get(attribute);
		if(a != null){
			a.push(luaFunction);
		}else{
			LinkedList<Class<? extends LuaFunction>> f = new LinkedList<>();
			f.add(luaFunction);
			attributes.put(attribute, f);
		}	
	}
	
	public static HashMap<String, Object> getFunctions(Class<? extends IAttribute> attribute, LuaReference reference){
		HashMap<String, Object> luaObject = new HashMap<>();


		for(Class<? extends LuaFunction> function : attributes.get(attribute)){
			try {
				LuaFunction luaFunction = function.newInstance();
				luaFunction.setRef(reference);
				luaObject.put(luaFunction.getName(), luaFunction);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}


		return luaObject;
	}
	
}

