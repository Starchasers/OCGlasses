package com.bymarcin.openglasses.testRender.vbo.computer;

import com.bymarcin.openglasses.testRender.vbo.model.Model;

import io.netty.buffer.Unpooled;

import net.minecraft.nbt.NBTTagCompound;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;

public class BasicModelObject implements Value{
	Model model;
	
	@Override
	public void load(NBTTagCompound nbt) {
		if(nbt.hasKey("model")){
			byte[] modelBytes = nbt.getByteArray("model");
			model = new Model("").fromPacket(Unpooled.wrappedBuffer(modelBytes));
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		if(model!=null){
			nbt.setByteArray("model", model.toPacket().array());
		}
	}
	
	@Override
	public void dispose(Context context) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object apply(Context context, Arguments arguments) {
		return null;
	}

	@Override
	public void unapply(Context context, Arguments arguments) {
		
	}

	@Override
	public Object[] call(Context context, Arguments arguments) {
		return null;
	}
}
