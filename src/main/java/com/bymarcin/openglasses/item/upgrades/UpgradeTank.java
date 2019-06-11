package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeTank extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("tankUpgrade")) {
            tag.setBoolean("tankUpgrade", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost") + getEnergyUsage()); //increase power usage by 1
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
        return !stack.isEmpty()
                && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))
                && stack.getMetadata() == 23;
    }

    public static boolean hasUpgrade(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("tankUpgrade");
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        List<String> tooltip = new ArrayList<>();

        if(hasUpgrade(stack))
            tooltip.add("rainsensor: installed");
        else {
            tooltip.add("rainsensor: not installed");
            tooltip.add("§8requires opencomputers tank upgrade§7");
        }

        return tooltip;
    }
}
