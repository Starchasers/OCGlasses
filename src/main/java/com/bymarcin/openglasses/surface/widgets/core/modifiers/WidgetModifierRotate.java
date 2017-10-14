package com.bymarcin.openglasses.surface.widgets.core.modifiers;

import com.bymarcin.openglasses.surface.WidgetModifier;
import com.bymarcin.openglasses.surface.widgets.core.Easing;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IEasing;
import org.lwjgl.opengl.GL11;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class WidgetModifierRotate extends WidgetModifier implements IEasing {
	private float deg, x, y, z;
	private float DEG, X, Y, Z;

	private ArrayList<ArrayList> easingListX, easingListY, easingListZ, easingListDeg;

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
			case "deg":
				this.easingListDeg = Easing.setEasing(Easing.EasingType.valueOf(type.toUpperCase()), Easing.EasingTypeIO.valueOf(typeIO.toUpperCase()), duration, min, max, Easing.EasingTypeMode.valueOf(mode.toUpperCase()));
				break;
		}
	}

	@Override
	public void update(float[] values){
		if(values.length < 4)
			return;

		this.deg = values[0];
		this.x = values[1];
		this.y = values[2];
		this.z = values[3];

		this.applyEasings();
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
			case "deg":
				this.easingListDeg = new ArrayList<ArrayList>();
				break;
		}
	}


	public WidgetModifierRotate(float deg, float x, float y, float z){
		this.easingListX = new ArrayList<ArrayList>();
		this.easingListY = new ArrayList<ArrayList>();
		this.easingListZ = new ArrayList<ArrayList>();
		this.easingListDeg = new ArrayList<ArrayList>();

		this.deg = deg;
		this.x = x;
		this.y = y;
		this.z = z;
	}
		
	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;

		this.applyEasings();


		GL11.glRotatef(DEG, X, Y, Z);
	}

	public void revoke(long conditionStates){
		if(!shouldApplyModifier(conditionStates)) return;
		GL11.glRotatef(-DEG, X, Y, Z);
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(this.deg);
		buff.writeFloat(this.x);
		buff.writeFloat(this.y);
		buff.writeFloat(this.z);
		Easing.writeEasing(buff, this.easingListX);
		Easing.writeEasing(buff, this.easingListY);
		Easing.writeEasing(buff, this.easingListZ);
		Easing.writeEasing(buff, this.easingListDeg);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.deg = buff.readFloat();
		this.x = buff.readFloat();
		this.y = buff.readFloat();
		this.z = buff.readFloat();
		this.easingListX = Easing.readEasing(buff);
		this.easingListY = Easing.readEasing(buff);
		this.easingListZ = Easing.readEasing(buff);
		this.easingListDeg = Easing.readEasing(buff);
	}
	
	public WidgetModifierType getType(){
		return WidgetModifierType.ROTATE;
	}	

	private void applyEasings(){
		this.X = Easing.applyEasing(this.easingListX, this.x);
		this.Y = Easing.applyEasing(this.easingListY, this.y);
		this.Z = Easing.applyEasing(this.easingListZ, this.z);
		this.DEG = Easing.applyEasing(this.easingListDeg, this.deg);
	}

	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.deg, this.x, this.y, this.z };
	}
}
