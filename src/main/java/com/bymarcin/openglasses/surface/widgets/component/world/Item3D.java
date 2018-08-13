package com.bymarcin.openglasses.surface.widgets.component.world;

import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.common.ItemIcon;

public class Item3D extends ItemIcon {
	public Item3D(){}

	@Override
	public WidgetType getType() {
		return WidgetType.ITEM3D;
	}
}

