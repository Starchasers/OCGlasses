package com.bymarcin.ocglasses.surface;

import com.bymarcin.ocglasses.surface.widgets.component.SquareWidget;
import com.bymarcin.ocglasses.surface.widgets.component.TriangleWidget;

public enum Widgets {
	QUAD(SquareWidget.class),
	TRIANGLE(TriangleWidget.class)
	;
	
	Class<? extends IWidget> clazz;
	private Widgets(Class<? extends IWidget> cl) {
		clazz = cl;
	}
	
	public IWidget getNewInstance(){
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
