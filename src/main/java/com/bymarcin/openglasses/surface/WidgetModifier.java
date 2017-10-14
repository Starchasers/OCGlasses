package com.bymarcin.openglasses.surface;

import io.netty.buffer.ByteBuf;

public abstract class WidgetModifier{
	public enum WidgetModifierType {
		TRANSLATE, COLOR, SCALE, ROTATE, TEXTURE
	}

	public long conditions = 0;

	public boolean shouldApplyModifier(long conditionStates){
		if((this.conditions & conditionStates) == this.conditions) 
			return true;
			
		return false;
	}
		
	public Object[] getConditions(){
		Object[] foo = new Object[64];
				
		for(short i=0, s=0; i < 64; i++){
			if(((conditions >>> i) & (long) 1) != 0){
				foo[s] = WidgetModifierConditionType.getName(i);
				s++;
		} }
					
		return foo;
	}	
	
	public void writeData(ByteBuf buff){
		buff.writeLong(this.conditions);		
	}
	
	public void readData(ByteBuf buff){
		this.conditions = buff.readLong();		
	}
	
	public void configureCondition(short type, boolean state){		
		if(state == true) 
			this.conditions |= ((long) 1 << type); 
		else
			this.conditions &= ~((long) 1 << type); 
	}	
	
	//this stuff should be overwritten by childs
	public void apply(long conditionStates){}
	public void revoke(long conditionStates){}
	public void update(float[] values){}
	public WidgetModifierType getType(){ return null; }
	public Object[] getValues(){ return null; }
}
