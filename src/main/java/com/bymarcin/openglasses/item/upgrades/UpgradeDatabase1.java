package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class UpgradeDatabase1 extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        int newWidgetLimit = tag.getInteger("widgetLimit");
        int widgetsTotalLimit = 255;

        if (newWidgetLimit < widgetsTotalLimit){
            newWidgetLimit += getCapacity();
            tag.setInteger("widgetLimit", Math.min(newWidgetLimit, widgetsTotalLimit));
        }

        return stack;
    }

    public int getCapacity(){
        return 9;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 10;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))
                && stack.getMetadata() == 12;
    }
}