package com.bymarcin.openglasses.utils;

import ben_mkiv.commons0815.utils.utilsCommon;
import ben_mkiv.rendertoolkit.common.widgets.WidgetModifierConditionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Conditions {
    private boolean hasMotionSensor, hasGeolyzer, hasLightSensor, hasRainSensor;
    private boolean overlayActive = false;

    private long lastExtendedConditionCheck = 0;
    private long conditionStates = 0L;

    public void bufferSensors(ItemStack glassesStack){
        NBTTagCompound tag = glassesStack.getTagCompound();
        if(tag == null) return;

        this.hasMotionSensor = tag.getBoolean("motionsensor");
        this.hasGeolyzer = tag.getBoolean("geolyzer");
        this.hasRainSensor = tag.getBoolean("tankUpgrade");
        this.hasLightSensor = tag.getBoolean("daylightDetector");
    }

    public long getConditionStates(ItemStack glassesStack, EntityPlayer player){
        long curConditionStates = 0;
        long checkConditions = ~0;
        NBTTagCompound tag = glassesStack.getTagCompound();

        if(overlayActive)
            curConditionStates |= ((long) 1 << WidgetModifierConditionType.OVERLAY_ACTIVE);
        else
            curConditionStates |= ((long) 1 << WidgetModifierConditionType.OVERLAY_INACTIVE);

        if(hasMotionSensor && (((checkConditions >>> WidgetModifierConditionType.IS_SNEAKING) & 1) != 0 || ((checkConditions >>> WidgetModifierConditionType.IS_NOT_SNEAKING) & 1) != 0)){
            if(player.isSneaking())
                curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_SNEAKING);
            else
                curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_NOT_SNEAKING);
        }

        //bs
        if(player.world.getWorldTime() - lastExtendedConditionCheck < 20){
            long States = conditionStates;
            States &= ~((long) 1 << WidgetModifierConditionType.OVERLAY_ACTIVE);
            States &= ~((long) 1 << WidgetModifierConditionType.OVERLAY_INACTIVE);
            States &= ~((long) 1 << WidgetModifierConditionType.IS_SNEAKING);
            States &= ~((long) 1 << WidgetModifierConditionType.IS_NOT_SNEAKING);
            return set(curConditionStates | States );
        }

        lastExtendedConditionCheck = player.world.getWorldTime();

        if(hasRainSensor && (((checkConditions >>> WidgetModifierConditionType.IS_WEATHER_RAIN) & 1) != 0 || ((checkConditions >>> WidgetModifierConditionType.IS_WEATHER_CLEAR) & 1) != 0)){
            if(player.world.isRaining())
                curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_WEATHER_RAIN);
            else
                curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_WEATHER_CLEAR);
        }

        if(hasGeolyzer && (((checkConditions >>> WidgetModifierConditionType.IS_SWIMMING) & 1) != 0 || ((checkConditions >>> WidgetModifierConditionType.IS_NOT_SWIMMING) & 1) != 0)){
            if(utilsCommon.isPlayerSwimming(player))
                curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_SWIMMING);
            else
                curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_NOT_SWIMMING);
        }

        if(hasLightSensor) {
            int lightLevel = utilsCommon.getLightLevelPlayer(player);

            for (int i = WidgetModifierConditionType.IS_LIGHTLEVEL_MIN_0, l = 0; i < WidgetModifierConditionType.IS_LIGHTLEVEL_MIN_15; i++, l++)
                if (((checkConditions >>> i) & 1) != 0 && lightLevel >= l)
                    curConditionStates |= ((long) 1 << i);

            for (int i = WidgetModifierConditionType.IS_LIGHTLEVEL_MAX_0, l = 0; i < WidgetModifierConditionType.IS_LIGHTLEVEL_MAX_15; i++, l++)
                if (((checkConditions >>> i) & 1) != 0 && lightLevel <= l)
                    curConditionStates |= ((long) 1 << i);
        }

        return set(curConditionStates);
    }

    public void setOverlay(boolean state){
        overlayActive = state;
    }

    public long get(){
        return this.conditionStates;
    }

    private long set(long newStates){
        conditionStates = newStates;
        return get();
    }

}
