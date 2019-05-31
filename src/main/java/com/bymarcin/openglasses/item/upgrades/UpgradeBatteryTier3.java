package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class UpgradeBatteryTier3 extends UpgradeBatteryTier1 {
    @Override
    public int getCapacity(){
        return 1000000;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 34;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))
                && stack.getMetadata() == 3;
    }
}
