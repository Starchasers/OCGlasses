package com.bymarcin.openglasses.component;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class WidgetList {
    HashMap<Integer, Widget> list = new HashMap<>();

    public int getNextWidgetID(){
        int maxID = 0;
        for(int widgetID : list.keySet())
            maxID = Math.max(maxID, widgetID);

        return maxID + 1;
    }

    public int add(Widget widget){
        int id = getNextWidgetID();
        list.put(id, widget);
        return id;
    }

    public Widget get(int widgetID){
        return list.get(widgetID);
    }

    public boolean removeWidget(int id){
        return list.containsKey(id) && list.remove(id) != null;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        int size = list.size();
        nbt.setInteger("listSize", size);
        int i=0;
        for (Map.Entry<Integer, Widget> e: list.entrySet()) {
            NBTTagCompound widget = new NBTTagCompound();
            widget.setInteger("widgetType", e.getValue().getType().ordinal());
            widget.setInteger("ID", e.getKey());
            NBTTagCompound wNBT = new NBTTagCompound();
            e.getValue().writeToNBT(wNBT);
            widget.setTag("widget", wNBT);
            tag.setTag(String.valueOf(i), widget);
            i++;
        }
        nbt.setTag("widgetList", tag);

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        list.clear();
        if(nbt.hasKey("widgetList") && nbt.hasKey("listSize")){
            NBTTagCompound nbtList = (NBTTagCompound) nbt.getTag("widgetList");
            for(int i=0; i < nbt.getInteger("listSize"); i++){
                if(nbtList.hasKey(String.valueOf(i))){
                    NBTTagCompound wiget = (NBTTagCompound) nbtList.getTag(String.valueOf(i));
                    if(wiget.hasKey("widgetType") && wiget.hasKey("widget")&& wiget.hasKey("ID")){
                        Widget newWidget = Widget.create(wiget.getInteger("widgetType"));
                        newWidget.readFromNBT((NBTTagCompound) wiget.getTag("widget"));
                        list.put(wiget.getInteger("ID"), newWidget);
                    }
                }
            }
        }


    }
}
