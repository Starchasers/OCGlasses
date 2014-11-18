package com.bymarcin.ocglasses.surface;

import com.bymarcin.ocglasses.surface.widgets.component.SquareWidget;
import com.bymarcin.ocglasses.surface.widgets.component.TriangleWidget;

public enum Widgets {
	QUAD(SquareWidget.class),
	TRIANGLE(TriangleWidget.class)
	;
	
	Class<? extends Widget> clazz;
	private Widgets(Class<? extends Widget> cl) {
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
