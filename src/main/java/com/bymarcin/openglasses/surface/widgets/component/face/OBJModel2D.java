package com.bymarcin.openglasses.surface.widgets.component.face;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;

public class OBJModel2D extends com.bymarcin.openglasses.surface.widgets.component.common.OBJModelOC {
    public OBJModel2D(){
        this.rendertype = RenderType.GameOverlayLocated;
    }


    //for real this is needed!
    @Override
    public WidgetType getType() {
        return WidgetType.OBJMODEL2D;
    }
}
