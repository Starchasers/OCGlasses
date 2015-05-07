package com.bymarcin.openglasses.vbo.computer;

import com.bymarcin.openglasses.network.WidgetPacket;
import com.bymarcin.openglasses.vbo.ServerLayer;
import com.bymarcin.openglasses.vbo.model.Model;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class ModelLua extends BasicModelObject{
	Model model;
	
	public ModelLua(Model m) {
		model = m;
	}
	
	public ModelLua() {

	}
	
	@Callback(direct = true)
	public Object[] resetModelTransformation(Context context, Arguments arguments) {
		if(arguments.count() == 0){
			model.resetModelTransformation();
			ServerLayer.instance().sendModelUpdate(WidgetPacket.RESET, model.getId());
		}
		return null;
	}
	
	@Callback(direct = true)
	public Object[] rotate(Context context, Arguments arguments) {
		if(arguments.count() == 4){
			float angle = (float) arguments.checkDouble(0);
			int x = arguments.checkInteger(1);
			int y = arguments.checkInteger(2);
			int z = arguments.checkInteger(3);
			model.rotateModel(angle, x, y, z);
			ServerLayer.instance().sendModelUpdate(WidgetPacket.ROTATE, model.getId(), angle, x, y, z);
		}
		return null;
	}
	
	@Callback(direct = true)
	public Object[] translate(Context context, Arguments arguments) {
		if(arguments.count() == 3){
			float x = (float) arguments.checkDouble(0);
			float y = (float) arguments.checkDouble(1);
			float z = (float) arguments.checkDouble(2);
			model.translateModel(x, y, z);
			ServerLayer.instance().sendModelUpdate(WidgetPacket.TRANSLATE, model.getId(), x, y, z);
		}
		return null;
	}
	
	@Callback(direct = true)
	public Object[] scale(Context context, Arguments arguments) {
		if(arguments.count() == 3){
			float x = (float) arguments.checkDouble(0);
			float y = (float) arguments.checkDouble(1);
			float z = (float) arguments.checkDouble(2);
			model.scaleModel(x, y, z);
			ServerLayer.instance().sendModelUpdate(WidgetPacket.SCALE, model.getId(), x, y, z);
		}
		return null;
	}
	
	public Model getModel() {
		return model;
	}

}
