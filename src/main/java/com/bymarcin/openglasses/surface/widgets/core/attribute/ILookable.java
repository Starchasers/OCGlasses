package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface ILookable extends IAttribute{
	public void setLookingAt(int x, int y, int z);
	public boolean isLookingAtEnable();
	public void setLookingAtEnable(boolean enable);
	public int getLookingAtX();
	public int getLookingAtY();
	public int getLookingAtZ();
}
