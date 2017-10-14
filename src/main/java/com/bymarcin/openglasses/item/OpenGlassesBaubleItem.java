package com.bymarcin.openglasses.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import baubles.api.BaubleType;
import baubles.api.IBauble;

import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface="baubles.api.IBauble",modid="Baubles")
public class OpenGlassesBaubleItem extends OpenGlassesItem implements IBauble {	
	@Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.HEAD;
    }
    
    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) { return true; }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player){ this.consumeEnergy(itemstack); }

}
