package com.bymarcin.openglasses.surface.widgets.component.face;

import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.common.ItemIcon;

public class Item2D extends ItemIcon {
	public Item2D(){
		this.rendertype = RenderType.GameOverlayLocated;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.ITEM2D;
	}

}

