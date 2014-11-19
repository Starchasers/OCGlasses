package com.bymarcin.ocglasses.surface;

import com.bymarcin.ocglasses.surface.widgets.component.Cube3d;
import com.bymarcin.ocglasses.surface.widgets.component.SquareWidget;
import com.bymarcin.ocglasses.surface.widgets.component.TriangleWidget;

public enum WidgetType {
	QUAD(SquareWidget.class),
	TRIANGLE(TriangleWidget.class),
	CUBE3D(Cube3d.class)
	;
	
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
