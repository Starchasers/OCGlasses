package com.bymarcin.openglasses.surface.widgets.core.modifiers;

import com.bymarcin.openglasses.surface.WidgetModifier;
import com.bymarcin.openglasses.surface.widgets.core.Easing;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IEasing;
import org.lwjgl.opengl.GL11;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class WidgetModifierColor extends WidgetModifier implements IEasing {
	private float r, g, b, alpha;
	private float red, green, blue, Alpha;

	private ArrayList<ArrayList> easingListRed, easingListGreen, easingListBlue, easingListAlpha;

	@Override
	public void addEasing(String type, String typeIO, float duration, String list, float min, float max, String mode){
		ArrayList easing = Easing.setEasing(Easing.EasingType.valueOf(type.toUpperCase()), Easing.EasingTypeIO.valueOf(typeIO.toUpperCase()), duration, min, max, Easing.EasingTypeMode.valueOf(mode.toUpperCase()));

		switch(list.toLowerCase()) {
			case "red":
				this.easingListRed = easing;
				break;
			case "green":
				this.easingListGreen = easing;
				break;
			case "blue":
				this.easingListBlue = easing;
				break;
			case "alpha":
				this.easingListAlpha = easing;
				break;
		}
	}

	@Override
	public void update(float[] values){
		if(values.length < 3)
			return;

		this.r = values[0];
		this.g = values[1];
		this.b = values[2];

		if(values.length >= 4)
			this.alpha = values[3];
		else
			this.alpha = 1;

		this.applyEasings();
	}

	@Override
	public void removeEasing(String list){
		switch(list.toLowerCase()) {
			case "red":
				this.easingListRed = new ArrayList<ArrayList>();
				break;
			case "green":
				this.easingListGreen = new ArrayList<ArrayList>();
				break;
			case "blue":
				this.easingListBlue = new ArrayList<ArrayList>();
				break;
			case "alpha":
				this.easingListAlpha = new ArrayList<ArrayList>();
				break;
		}
	}

	public WidgetModifierColor(float r, float g, float b, float alpha){
		this.easingListRed = new ArrayList<ArrayList>();
		this.easingListGreen = new ArrayList<ArrayList>();
		this.easingListBlue = new ArrayList<ArrayList>();
		this.easingListAlpha = new ArrayList<ArrayList>();
		this.setColor(r, g, b, alpha);
	}

	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;

		this.applyEasings();

		if(this.Alpha < 1)
			GL11.glColor4f(this.red, this.green, this.blue, this.Alpha);
		else
			GL11.glColor3f(this.red, this.green, this.blue);
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(this.r);
		buff.writeFloat(this.g);
		buff.writeFloat(this.b);
		buff.writeFloat(this.alpha);
		Easing.writeEasing(buff, this.easingListRed);
		Easing.writeEasing(buff, this.easingListGreen);
		Easing.writeEasing(buff, this.easingListBlue);
		Easing.writeEasing(buff, this.easingListAlpha);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.setColor(buff.readFloat(), buff.readFloat(), buff.readFloat(), buff.readFloat());

		this.easingListRed = Easing.readEasing(buff);
		this.easingListGreen = Easing.readEasing(buff);
		this.easingListBlue = Easing.readEasing(buff);
		this.easingListAlpha = Easing.readEasing(buff);
	}

	private void setColor(float r, float g, float b, float alpha){
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}
	
	public WidgetModifierType getType(){
		return WidgetModifierType.COLOR;
	}

	private void applyEasings(){
		this.red   = Easing.applyEasing(this.easingListRed, this.r);
		this.green = Easing.applyEasing(this.easingListGreen, this.g);
		this.blue  = Easing.applyEasing(this.easingListBlue, this.b);
		this.Alpha = Easing.applyEasing(this.easingListAlpha, this.alpha);
	}

	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.red, this.green, this.blue, this.Alpha };
	}
}
