package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeGeolyzer extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if (!tag.getBoolean("geolyzer") || tag.getInteger("radarRange") < 128) {
            tag.setBoolean("geolyzer", true);
            tag.setInteger("radarRange", Math.min(tag.getInteger("radarRange") + 16, 128)); //increase radar range by 16 blocks
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
    public int getEnergyUsageCurrent(ItemStack stack){
        return stack.getTagCompound().getInteger("radarRange")/16 * getEnergyUsage();
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return hasUpgrade(stack);
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "geolyzer"));
    }

    public static boolean hasUpgrade(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("geolyzer");
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        List<String> tooltip = new ArrayList<>();
        if(hasUpgrade(stack)) {
            tooltip.add("geolyzer: installed");
            tooltip.add("radar Range: " + stack.getTagCompound().getInteger("radarRange"));
        }
        else {
            tooltip.add("geolyzer: not installed");
            tooltip.add("§8requires opencomputers geolyzer§7");
        }

        return tooltip;
    }

}
