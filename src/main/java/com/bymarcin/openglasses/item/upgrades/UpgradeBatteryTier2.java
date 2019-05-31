package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class UpgradeBatteryTier2 extends UpgradeBatteryTier1 {
    @Override
    public int getCapacity(){
        return 250000;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 20;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))
                && stack.getMetadata() == 2;
    }
}
