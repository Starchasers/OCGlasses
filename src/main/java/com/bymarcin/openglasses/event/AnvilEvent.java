package com.bymarcin.openglasses.event;

import com.bymarcin.openglasses.OpenGlasses;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilEvent {
    public static AnvilEvent instances = new AnvilEvent();

    @SubscribeEvent
    public void handleAnvilEvent(AnvilUpdateEvent evt) {
        if(evt.getLeft() == null || evt.getRight() == null) return;

        if(!OpenGlasses.isGlassesStack(evt.getLeft())) return;

        if(evt.getRight().getCount() > 1) return; //no support for stacked stuff

        ItemStack anvilOutputGlassesStack = evt.getLeft().copy();

        NBTTagCompound tag = anvilOutputGlassesStack.getTagCompound();

        Item itm = evt.getRight().getItem();

        evt.setCost(0);

        if(itm == Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR)) {

            if(!tag.getBoolean("daylightDetector"))
                evt.setCost(20);
            tag.setBoolean("daylightDetector", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost")+1); //increase power usage by 1
        }
        else if(itm == Items.POTIONITEM
                && (evt.getRight().getTagCompound().getString("Potion").equals("minecraft:night_vision")
                    || evt.getRight().getTagCompound().getString("Potion").equals("minecraft:long_night_vision"))) {

            if(!tag.getBoolean("nightvision"))
                evt.setCost(20);
            tag.setBoolean("nightvision", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost")+1); //increase power usage by 1
        }
        else if(itm.getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))
                && evt.getRight().getMetadata() == 23) { //oc tankUpgrade

            if(!tag.getBoolean("tankUpgrade"))
                evt.setCost(20);
            tag.setBoolean("tankUpgrade", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost")+1); //increase power usage by 1

        }
        else if(itm.getRegistryName().equals(new ResourceLocation("opencomputers", "motionSensor"))) {
            if(!tag.getBoolean("motionsensor"))
                evt.setCost(20);
            tag.setBoolean("motionsensor", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost")+1); //increase power usage by 1
        }
        else if(itm.getRegistryName().equals(new ResourceLocation("opencomputers", "geolyzer"))) {
            if(!tag.getBoolean("geolyzer") || tag.getInteger("radarRange") < 128)
                evt.setCost(20);

            tag.setBoolean("geolyzer", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost")+1); //increase power usage by 1
            tag.setInteger("radarRange", tag.getInteger("radarRange")+16); //increase radar range by 16 blocks

            if(tag.getInteger("radarRange") > 128)
                tag.setInteger("radarRange", 128);
        }

		/* battery and database upgrades */
        else if(itm.getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))) {
            switch (evt.getRight().getMetadata()) {
                case 1:	case 2:	case 3: //battery upgrades
                    IEnergyStorage storage = anvilOutputGlassesStack.getCapability(CapabilityEnergy.ENERGY, null);
                    int newEnergyBufferSize = storage.getMaxEnergyStored();
                    int energyBufferTotalLimit = 5000000; //limit upgrades to max 5M FE
                    if (newEnergyBufferSize > energyBufferTotalLimit) break; //cancel upgrade when the buffer is allready at it's limit
                    switch (evt.getRight().getMetadata()) {
                        case 1: //battery upgrade T1
                            newEnergyBufferSize += 100000;
                            evt.setCost(10);
                            break;
                        case 2: //battery upgrade T2
                            newEnergyBufferSize += 250000;
                            evt.setCost(20);
                            break;
                        case 3: //battery upgrade T3
                            newEnergyBufferSize += 1000000;
                            evt.setCost(34);
                            break;
                    }
                    if (newEnergyBufferSize > energyBufferTotalLimit) newEnergyBufferSize = energyBufferTotalLimit;
                    tag.setInteger("EnergyCapacity", newEnergyBufferSize);
                    break;

                case 12: case 13: case 14:  //database upgrades
                    int newWidgetLimit = tag.getInteger("widgetLimit");
                    int widgetsTotalLimit = 255;
                    if (newWidgetLimit >= widgetsTotalLimit) break;
                    switch (evt.getRight().getMetadata()) {
                        case 12: //database upgrade T1
                            newWidgetLimit += 9;
                            evt.setCost(9);
                            break;
                        case 13: //database upgrade T2
                            newWidgetLimit += 25;
                            evt.setCost(20);
                            break;
                        case 14: //database upgrade T3
                            newWidgetLimit += 81;
                            evt.setCost(34);
                            break;
                    }
                    if (newWidgetLimit > widgetsTotalLimit) newWidgetLimit = widgetsTotalLimit;
                    tag.setInteger("widgetLimit", newWidgetLimit);
                    break;
            }
        }

        if(evt.getCost() > 0)
            evt.setOutput(anvilOutputGlassesStack);

        return;
    }
}
