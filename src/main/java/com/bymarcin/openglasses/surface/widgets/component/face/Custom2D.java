package com.bymarcin.openglasses.surface.widgets.component.face;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.common.CustomShape;

public class Custom2D extends CustomShape {
    //for real this is needed!

    public Custom2D(){
        super();
        this.rendertype = RenderType.GameOverlayLocated;
    }

    @Override
    public WidgetType getType() {
        return WidgetType.CUSTOM2D;
    }
}
