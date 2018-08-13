package com.bymarcin.openglasses.surface.widgets.component.world;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.common.CustomShape;

public class Custom3D extends CustomShape {
    //for real this is needed!
    @Override
    public WidgetType getType() {
        return WidgetType.CUSTOM3D;
    }
}
