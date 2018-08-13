package com.bymarcin.openglasses.surface.widgets.core.modifiers;

import com.bymarcin.openglasses.surface.ClientSurface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WidgetModifierAutoTranslate extends WidgetModifierTranslate {
    public WidgetModifierAutoTranslate(float x, float y){
        super(x, y, 0);
    }

    public WidgetModifierType getType(){
        return WidgetModifierType.AUTOTRANSLATE;
    }

    @Override
    public void update(float[] values){
        if(values.length < 2)
            return;

        this.x = values[0];
        this.y = values[1];

        this.applyEasings();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void applyEasings(){
        super.applyEasings();

        if(ClientSurface.instances != null){
            renderX*=((double) ClientSurface.resolution.getScaledWidth() / 100D);
            renderY*=((double) ClientSurface.resolution.getScaledHeight() / 100D);
        }
    }

    @Override
    public Object[] getValues(){
        this.applyEasings();
        return new Object[]{ this.x, this.y };
    }
}
