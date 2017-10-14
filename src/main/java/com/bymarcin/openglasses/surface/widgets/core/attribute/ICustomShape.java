package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface ICustomShape extends IAttribute{
    public int getVertexCount();
    public void setVertex(int n, float nx, float ny, float nz);
    public void addVertex(float nx, float ny, float nz);
    public void removeVertex(int n);
}
