package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface IEasing {
    public void addEasing(String type, String typeIO, float duration, String list, float min, float max, String mode);
    public void removeEasing(String list);
}
