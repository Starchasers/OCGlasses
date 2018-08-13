package com.bymarcin.openglasses.surface.widgets.core;

import com.bymarcin.openglasses.lib.penner.easing.*;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

public class Easing{
    public static enum EasingType {
        BACK, BOUNCE, CIRC, CUBIC, ELASTIC, EXPO, LINEAR, QUAD, QUART, QUINT, SINE
    };

    public static enum EasingTypeIO {
        IN, OUT, INOUT
    };
    
    public static enum EasingTypeMode {
        DEFAULT, LOOP, REPEAT  
    };

    public static float applyEasing(ArrayList easingList, float value){
        if(easingList.size() == 0) return value;

        float timeNow = System.nanoTime() / 1000000F;

        if((float) easingList.get(6) == 0) //save the time for first frame
            easingList.set(6, timeNow);

        float time = timeNow - (float) easingList.get(6);

        float duration = (float) easingList.get(2);

        Easing.EasingType type = (Easing.EasingType) easingList.get(0);
        Easing.EasingTypeIO typeIO = (Easing.EasingTypeIO) easingList.get(1);
        Easing.EasingTypeMode mode = (Easing.EasingTypeMode) easingList.get(5);

        value = Easing.ease(time, duration, type, typeIO, (float) easingList.get(3), (float) easingList.get(4), mode);

        return value;
    }

    public static ArrayList setEasing(Easing.EasingType type, Easing.EasingTypeIO typeIO, float duration, float min, float max, Easing.EasingTypeMode mode){
        ArrayList newList = new ArrayList();
        newList.add(type);
        newList.add(typeIO);
        newList.add(duration);
        newList.add(min);
        newList.add(max);
        newList.add(mode);
        newList.add(0F);

        return newList;
    }

    public static float ease(float currentTime, float totalTime, EasingType easingType, EasingTypeIO easingTypeIO, float min, float max, Easing.EasingTypeMode mode) {

        switch(mode){
            case LOOP:
                float loopTime = totalTime * 2;
                if (currentTime > loopTime)
                    currentTime = (currentTime % loopTime);

                if (currentTime > totalTime)
                    currentTime = totalTime - (currentTime - totalTime);
                break;
            case REPEAT:
                if(currentTime > totalTime)
                    currentTime = (currentTime % totalTime);
                break;
            case DEFAULT:
            default:
                if (currentTime > totalTime)
                    currentTime = totalTime;
                break;
        }

        switch(easingTypeIO){
            case IN:
                return easeIn(currentTime, totalTime, min, max, easingType);
            case OUT:
                return easeOut(currentTime, totalTime, min, max, easingType);
            case INOUT:
                return easeInOut(currentTime, totalTime, min, max, easingType);
            default:
                return 0;
        }
    }
        
    public static float easeOut(float currentTime, float totalTime, float min, float max, EasingType easingType){
        switch(easingType) {
            case BACK:
                return Back.easeOut(currentTime, min, max, totalTime);
            case BOUNCE:
                return Bounce.easeOut(currentTime, min, max, totalTime);
            case CIRC:
                return Circ.easeOut(currentTime, min, max, totalTime);
            case CUBIC:
                return Cubic.easeOut(currentTime, min, max, totalTime);
            case ELASTIC:
                return Elastic.easeOut(currentTime, min, max, totalTime);
            case EXPO:
                return Expo.easeOut(currentTime, min, max, totalTime);
            case LINEAR:
                return Linear.easeOut(currentTime, min, max, totalTime);
            case QUAD:
                return Quad.easeOut(currentTime, min, max, totalTime);
            case QUART:
                return Quart.easeOut(currentTime, min, max, totalTime);
            case QUINT:
                return Quint.easeOut(currentTime, min, max, totalTime);
            case SINE:
                return Sine.easeOut(currentTime, min, max, totalTime);
            default:
                return 0;
        }
    }

    public static float easeIn(float currentTime, float totalTime, float min, float max, EasingType easingType){
        switch(easingType) {
            case BACK:
                return Back.easeIn(currentTime, min, max, totalTime);
            case BOUNCE:
                return Bounce.easeIn(currentTime, min, max, totalTime);
            case CIRC:
                return Circ.easeIn(currentTime, min, max, totalTime);
            case CUBIC:
                return Cubic.easeIn(currentTime, min, max, totalTime);
            case ELASTIC:
                return Elastic.easeIn(currentTime, min, max, totalTime);
            case EXPO:
                return Expo.easeIn(currentTime, min, max, totalTime);
            case LINEAR:
                return Linear.easeIn(currentTime, min, max, totalTime);
            case QUAD:
                return Quad.easeIn(currentTime, min, max, totalTime);
            case QUART:
                return Quart.easeIn(currentTime, min, max, totalTime);
            case QUINT:
                return Quint.easeIn(currentTime, min, max, totalTime);
            case SINE:
                return Sine.easeIn(currentTime, min, max, totalTime);
            default:
                return 0;
        }
    }

    public static float easeInOut(float currentTime, float totalTime, float min, float max, EasingType easingType){
        switch(easingType) {
            case BACK:
                return Back.easeInOut(currentTime, min, max, totalTime);
            case BOUNCE:
                return Bounce.easeInOut(currentTime, min, max, totalTime);
            case CIRC:
                return Circ.easeInOut(currentTime, min, max, totalTime);
            case CUBIC:
                return Cubic.easeInOut(currentTime, min, max, totalTime);
            case ELASTIC:
                return Elastic.easeInOut(currentTime, min, max, totalTime);
            case EXPO:
                return Expo.easeInOut(currentTime, min, max, totalTime);
            case LINEAR:
                return Linear.easeInOut(currentTime, min, max, totalTime);
            case QUAD:
                return Quad.easeInOut(currentTime, min, max, totalTime);
            case QUART:
                return Quart.easeInOut(currentTime, min, max, totalTime);
            case QUINT:
                return Quint.easeInOut(currentTime, min, max, totalTime);
            case SINE:
                return Sine.easeInOut(currentTime, min, max, totalTime);
            default:
                return 0;
        }
    }

    public static ArrayList readEasing(ByteBuf buff){
        ArrayList newList = new ArrayList();

        if(buff.readInt() > 0)
            newList = Easing.setEasing(Easing.EasingType.values()[buff.readInt()], Easing.EasingTypeIO.values()[buff.readInt()], buff.readFloat(), buff.readFloat(), buff.readFloat(), Easing.EasingTypeMode.values()[buff.readInt()]);

        return newList;
    }

    public static void writeEasing(ByteBuf buff, ArrayList list){
        buff.writeInt(list.size());
        if(list.size() == 0) return;
        
        buff.writeInt(((Easing.EasingType) list.get(0)).ordinal());
        buff.writeInt(((Easing.EasingTypeIO) list.get(1)).ordinal());
        buff.writeFloat((float) list.get(2));
        buff.writeFloat((float) list.get(3));
        buff.writeFloat((float) list.get(4));
        buff.writeInt(((Easing.EasingTypeMode) list.get(5)).ordinal());
    }
}
