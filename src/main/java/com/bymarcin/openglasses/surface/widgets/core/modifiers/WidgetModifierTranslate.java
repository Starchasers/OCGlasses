package com.bymarcin.openglasses.surface.widgets.core.modifiers;

import com.bymarcin.openglasses.surface.WidgetModifier;
import com.bymarcin.openglasses.surface.widgets.core.Easing;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IEasing;
import org.lwjgl.opengl.GL11;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class WidgetModifierTranslate extends WidgetModifier implements IEasing {
	public float x, y, z;
	public float renderX, renderY, renderZ;

	private ArrayList<ArrayList> easingListX, easingListY, easingListZ;

	@Override
	public void addEasing(String type, String typeIO, float duration, String list, float min, float max, String mode){
		switch(list.toLowerCase()) {
			case "x":
				this.easingListX = Easing.setEasing(Easing.EasingType.valueOf(type.toUpperCase()), Easing.EasingTypeIO.valueOf(typeIO.toUpperCase()), duration, min, max, Easing.EasingTypeMode.valueOf(mode.toUpperCase()));
				break;
			case "y":
				this.easingListY = Easing.setEasing(Easing.EasingType.valueOf(type.toUpperCase()), Easing.EasingTypeIO.valueOf(typeIO.toUpperCase()), duration, min, max, Easing.EasingTypeMode.valueOf(mode.toUpperCase()));
				break;
			case "z":
				this.easingListZ = Easing.setEasing(Easing.EasingType.valueOf(type.toUpperCase()), Easing.EasingTypeIO.valueOf(typeIO.toUpperCase()), duration, min, max, Easing.EasingTypeMode.valueOf(mode.toUpperCase()));
				break;
		}
	}

	@Override
	public void removeEasing(String list){
		switch(list.toLowerCase()) {
			case "x":
				this.easingListX = new ArrayList<ArrayList>();
				break;
			case "y":
				this.easingListY = new ArrayList<ArrayList>();
				break;
			case "z":
				this.easingListZ = new ArrayList<ArrayList>();
				break;
		}
	}

	public WidgetModifierTranslate(float x, float y, float z){
		this.easingListX = new ArrayList<ArrayList>();
		this.easingListY = new ArrayList<ArrayList>();
		this.easingListZ = new ArrayList<ArrayList>();

		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void update(float[] values){
		if(values.length < 3)
			return;

		this.x = values[0];
		this.y = values[1];
		this.z = values[2];

		this.applyEasings();
	}
		
	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;

		this.applyEasings();

		GL11.glTranslatef(renderX, renderY, renderZ);
	}

	public void applyEasings(){
		this.renderX = Easing.applyEasing(this.easingListX, this.x);
		this.renderY = Easing.applyEasing(this.easingListY, this.y);
		this.renderZ = Easing.applyEasing(this.easingListZ, this.z);
	}

	public void revoke(long conditionStates) {
		if (!shouldApplyModifier(conditionStates)) return;
		GL11.glTranslatef(-renderX, -renderY, -renderZ);
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(this.x);
		buff.writeFloat(this.y);
		buff.writeFloat(this.z);
		Easing.writeEasing(buff, this.easingListX);
		Easing.writeEasing(buff, this.easingListY);
		Easing.writeEasing(buff, this.easingListZ);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.x = buff.readFloat();
		this.y = buff.readFloat();
		this.z = buff.readFloat();
		this.easingListX = Easing.readEasing(buff);
		this.easingListY = Easing.readEasing(buff);
		this.easingListZ = Easing.readEasing(buff);
	}	
	
	public WidgetModifierType getType(){
		return WidgetModifierType.TRANSLATE;
	}
	
	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.renderX, this.renderY, this.renderZ};
	}
}
