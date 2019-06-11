package com.bymarcin.openglasses.event.glasses;

import java.util.ArrayList;

public class GlassesNotifications {
    public static ArrayList<GlassesNotification> notifications = new ArrayList<>();

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
