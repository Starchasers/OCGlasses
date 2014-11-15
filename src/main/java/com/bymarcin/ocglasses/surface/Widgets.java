package com.bymarcin.ocglasses.surface;

import com.bymarcin.ocglasses.surface.widgets.SquareWidget;

public enum Widgets {
	//TRIANGLE("Triangle", 6),
	QUAD("Quad", 6, SquareWidget.class);
	
	Class<? extends IWidget> clazz;
	private Widgets(String name, int parameters, Class<? extends IWidget> cl) {
		clazz = cl;
	}
	
	private IWidget getObject(){
		try {
			return this.clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IWidget getNewInstance(){
		switch(this){
			case QUAD:  return getObject();
		}
		return null;
	}
}
