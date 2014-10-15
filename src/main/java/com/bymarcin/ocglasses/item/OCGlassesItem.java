package com.bymarcin.ocglasses.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bymarcin.ocglasses.OCGlasses;

public class OCGlassesItem extends ItemArmor {

	public OCGlassesItem() {
		super(ArmorMaterial.CHAIN, 0, 0);
		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(OCGlasses.creativeTab);
		setUnlocalizedName("ocglasses");
	}

	@Override
	public void registerIcons(IIconRegister register) {
		itemIcon = register.registerIcon(OCGlasses.MODID + ":glasses");
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

	}

}
