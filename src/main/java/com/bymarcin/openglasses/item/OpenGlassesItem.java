package com.bymarcin.openglasses.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OpenGlassesItem extends ItemArmor {

	public OpenGlassesItem() {
		super(ArmorMaterial.CHAIN, 0, 0);
		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(OpenGlasses.creativeTab);
		setUnlocalizedName("openglasses");
	}
	
	@Override
	public void registerIcons(IIconRegister register) {
		itemIcon = register.registerIcon(OpenGlasses.MODID + ":glasses");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}

	public static Location getUUID(ItemStack itemStack){
		NBTTagCompound tag = getItemTag(itemStack);
		if (!tag.hasKey("X") || !tag.hasKey("Y") || ! tag.hasKey("Z") || ! tag.hasKey("uniqueKey")) return null;
		return new Location(tag.getInteger("X"),tag.getInteger("Y"),tag.getInteger("Z"),tag.getInteger("DIM"), tag.getLong("uniqueKey"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(itemStack, player, list, par4);
		Location uuid = getUUID(itemStack);
		if (uuid != null){
			list.add("Link to:");
			for(String s : uuid.toArrayString()){
				list.add(s);
			}
		}
	}

	public static NBTTagCompound getItemTag(ItemStack stack) {
		if (stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound;
	}

	public void bindToTerminal(ItemStack glass, Location uuid) {
		NBTTagCompound tag = getItemTag(glass);
		tag.setInteger("X", uuid.x);
		tag.setInteger("Y", uuid.y);
		tag.setInteger("Z", uuid.z);
		tag.setInteger("DIM", uuid.dimID);
		tag.setLong("uniqueKey", uuid.uniqueKey);
	}
}
