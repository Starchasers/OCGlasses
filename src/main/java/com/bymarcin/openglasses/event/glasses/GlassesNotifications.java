package com.bymarcin.openglasses.event.glasses;

import java.util.HashSet;

public class GlassesNotifications {
    public static HashSet<GlassesNotification> notifications = new HashSet<>();

    public static void update(){
        for(GlassesNotification notification : notifications)
            notification.update();
    }

    public interface GlassesNotification{
        void update();
        void cancel();
        void submit();
    }
}
