package com.bymarcin.ocglasses.surface;

import com.bymarcin.ocglasses.surface.widgets.component.SquareWidget;

public enum Widgets {
	//TRIANGLE("Triangle", 6),
	QUAD(SquareWidget.class);
	
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
