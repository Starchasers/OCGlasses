package com.bymarcin.openglasses.surface.widgets.core.attribute;

import com.bymarcin.openglasses.surface.widgets.component.world.EntityTracker3D;

import java.util.UUID;

public interface ITracker extends IAttribute {
    void setupTracking(EntityTracker3D.EntityType trackingType, int range);
    void setupTrackingFilter(String type, int metaindex);
    void setupTrackingEntity(UUID uuid);
}

