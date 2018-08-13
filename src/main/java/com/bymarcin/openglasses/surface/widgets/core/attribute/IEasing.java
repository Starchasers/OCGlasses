package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface IEasing {
    void addEasing(String type, String typeIO, float duration, String list, float min, float max, String mode);
    void removeEasing(String list);
}
