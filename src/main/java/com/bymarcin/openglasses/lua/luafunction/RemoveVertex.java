package com.bymarcin.openglasses.lua.luafunction;

import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ICustomShape;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

public class RemoveVertex extends LuaFunction{

    @Override
    @Callback(direct = true)
    public Object[] call(Context context, Arguments arguments) {
        super.call(context, arguments);
        Widget widget = getSelf().getWidget();
        if(widget instanceof ICustomShape){
            if(arguments.checkInteger(0)>0 && arguments.checkInteger(0)<=((ICustomShape) widget).getVertexCount()){
                ((ICustomShape) widget).removeVertex(arguments.checkInteger(0)-1);
                getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
                return null;
            }
            throw new RuntimeException("Vertex not exist!");
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "removeVertex";
    }

}
