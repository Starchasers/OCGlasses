package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.surface.widgets.component.face.Dot;
import com.bymarcin.openglasses.surface.widgets.component.face.SquareWidget;
import com.bymarcin.openglasses.surface.widgets.component.face.Text;
import com.bymarcin.openglasses.surface.widgets.component.face.TriangleWidget;
import com.bymarcin.openglasses.surface.widgets.component.world.Cube3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Dot3D;
import com.bymarcin.openglasses.surface.widgets.component.world.FloatingText;
import com.bymarcin.openglasses.surface.widgets.component.world.Line3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Quad3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Triangle3D;

public enum WidgetType {
	QUAD(SquareWidget.class),
	TRIANGLE(TriangleWidget.class),
	DOT(Dot.class),
	TEXT(Text.class),
	CUBE3D(Cube3D.class),
	FLOATINGTEXT(FloatingText.class),
	TRIANGLE3D(Triangle3D.class),
	QUAD3D(Quad3D.class),
	DOT3D(Dot3D.class),
	LINE3D(Line3D.class)
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
