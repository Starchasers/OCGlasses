package com.bymarcin.openglasses.surface;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.component.face.Text2D;
import net.minecraft.util.text.TextComponentTranslation;

public class StaticWidgets {
    static IRenderableWidget noPowerRender = getNoPowerRender();
    static IRenderableWidget noLinkRender = getNoLinkRender();
    static IRenderableWidget widgetLimitRender = getWidgetLimitRender();
    static IRenderableWidget navigationUpgradeRequired = getWidgetUpgradeRequired("navigation");

    private static IRenderableWidget getNoPowerRender(){
        Text2D t = new Text2D();
        t.setText(new TextComponentTranslation("openglasses.infotext.noenergy").getUnformattedText());
        t.WidgetModifierList.addColor(1F, 0F, 0F, 0.5F);
        t.WidgetModifierList.addTranslate(5, 5, 0);
        return t.getRenderable();
    }

    private static IRenderableWidget getNoLinkRender(){
        Text2D t = new Text2D();
        t.setText(new TextComponentTranslation("openglasses.infotext.nolink").getUnformattedText());
        t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
        t.WidgetModifierList.addTranslate(5, 5, 0);
        return t.getRenderable();
    }

    private static IRenderableWidget getWidgetLimitRender(){
        Text2D t = new Text2D();
        t.setText(new TextComponentTranslation("openglasses.infotext.widgetlimitexhausted").getUnformattedText());
        t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
        t.WidgetModifierList.addTranslate(5, 5, 0);
        return t.getRenderable();
    }

    private static IRenderableWidget getWidgetUpgradeRequired(String name){
        Text2D t = new Text2D();
        t.setText(new TextComponentTranslation("openglasses.infotext.upgraderequired", name).getUnformattedText());
        t.WidgetModifierList.addColor(1F, 1F, 1F, 0.7F);
        t.WidgetModifierList.addTranslate(5, 5, 0);
        return t.getRenderable();
    }
}
