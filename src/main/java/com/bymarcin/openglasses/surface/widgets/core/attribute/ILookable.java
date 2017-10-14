package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface ILookable extends IAttribute{
	public void setLookingAt(double x, double y, double z);
	public boolean isLookingAtEnable();
	public void setLookingAtEnable(boolean enable);
	public double getLookingAtX();
	public double getLookingAtY();
	public double getLookingAtZ();
}
