package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface I3DPositionable extends IAttribute{
	public double getPosX();
	public double getPosY();
	public double getPosZ();
	public void setPos(double x, double y, double z);
}
