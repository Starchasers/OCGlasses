package com.bymarcin.openglasses.item.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class UpgradeBatteryTier1 extends UpgradeItem {
    static int energyBufferTotalLimit = 5000000; //limit upgrades to max 5M FE

    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);

        tag.setInteger("EnergyCapacity", Math.min(storage.getMaxEnergyStored() + getCapacity(), energyBufferTotalLimit));

        return stack;
    }

    public int getCapacity(){
        return 100000;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 10;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opencomputers", "upgrade"))
                && stack.getMetadata() == 1;
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return true;
    }
}
