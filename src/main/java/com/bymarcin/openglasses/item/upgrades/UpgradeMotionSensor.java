package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeMotionSensor extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("motionsensor")){
            tag.setBoolean("motionsensor", true);
        }
        return stack;
    }

    @Override
    public int getEnergyUsage(){
        return 1;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 20;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "motionSensor"));
    }

    public static boolean hasUpgrade(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("motionsensor");
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return hasUpgrade(stack);
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        List<String> tooltip = new ArrayList<>();
        if(hasUpgrade(stack))
            tooltip.add("sneak detection: installed");
        else {
            tooltip.add("sneak detection: not installed");
            tooltip.add("§8requires opencomputers motionsensor§7");
        }

        return tooltip;
    }
}
