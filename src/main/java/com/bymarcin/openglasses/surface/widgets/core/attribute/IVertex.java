package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface IVertex extends IAttribute{
	public int getVertexCount();
	public void setVertex(int n, double x, double y, double z);
}
