package com.bymarcin.ocglasses.surface;

import com.bymarcin.ocglasses.surface.widgets.component.SquareWidget;

public enum Widgets {
	//TRIANGLE("Triangle", 6),
	QUAD(SquareWidget.class);
	
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
