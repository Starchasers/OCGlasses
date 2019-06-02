package com.bymarcin.openglasses.component;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import com.bymarcin.openglasses.surface.OCServerSurface;

public class WidgetServer extends WidgetList {
    private OpenGlassesHostComponent host;

    public WidgetServer(OpenGlassesHostComponent hostComponent){
        host = hostComponent;
    }

    public int add(Widget widget){
        int id = super.add(widget);
        OCServerSurface.instances.sendToUUID(new WidgetUpdatePacket(host.getUUID(), id, widget), host.getUUID());
        return id;
    }

    public boolean remove(int id){
        if(super.removeWidget(id)){
            OCServerSurface.instances.sendToUUID(new WidgetUpdatePacket(host.getUUID(), id), host.getUUID());
            return true;
        }
        return false;
    }

    public void update(int widgetID){
        Widget w = list.get(widgetID);
        if(w == null) return;
        OCServerSurface.instances.sendToUUID(new WidgetUpdatePacket(host.getUUID(), widgetID, w), host.getUUID());
    }

    public void clear(){
        list.clear();
        OCServerSurface.instances.sendToUUID(new WidgetUpdatePacket(host.getUUID()), host.getUUID());
    }

    public int size(){
        return list.size();
    }


}
