package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.surface.widgets.component.face.*;
import com.bymarcin.openglasses.surface.widgets.component.world.*;

public enum WidgetType {
	CUBE3D(Cube3D.class),
	BOX2D(Box2D.class),
	TEXT2D(Text2D.class),
	TEXT3D(Text3D.class),
	CUSTOM2D(Custom2D.class),
	CUSTOM3D(Custom3D.class),
	ITEM2D(Item2D.class),
	ITEM3D(Item3D.class),
	OBJMODEL2D(OBJModel2D.class),
	OBJMODEL3D(OBJModel3D.class),
	ENTITYTRACKER3D(EntityTracker3D.class);

	Class<? extends Widget> clazz;
	private WidgetType(Class<? extends Widget> cl) {
		clazz = cl;
	}
	
	public Widget getNewInstance(){
		try {
			return this.clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
