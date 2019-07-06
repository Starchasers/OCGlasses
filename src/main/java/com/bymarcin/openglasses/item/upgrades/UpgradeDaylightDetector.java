package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeDaylightDetector  extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("daylightDetector")) {
            tag.setBoolean("daylightDetector", true);
        }

        return stack;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 20;
    }

    @Override
    public int getEnergyUsage(){
        return 1;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().equals(Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR));
    }

    public static boolean hasUpgrade(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("daylightDetector");
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return hasUpgrade(stack);
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        List<String> tooltip = new ArrayList<>();
        if(hasUpgrade(stack)) {
            tooltip.add("lightsensor: installed");
        }
		else {
		    tooltip.add("lightsensor: not installed");
            tooltip.add("§8requires minecraft daylight sensor§7");
        }

        return tooltip;
    }

}
