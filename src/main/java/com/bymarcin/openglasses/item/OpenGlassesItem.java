package com.bymarcin.openglasses.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.utils.Location;


public class OpenGlassesItem extends ItemArmor {

	public OpenGlassesItem() {
		super(ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.HEAD);
		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(OpenGlasses.creativeTab);
		setUnlocalizedName("openglasses");
		setRegistryName("openglasses");
	}
	
//	@Override
//	public void registerIcons(IIconRegister register) {
//		itemIcon = register.registerIcon(OpenGlasses.MODID + ":glasses");
//	}
//
//	@Override
//	public IIcon getIcon(ItemStack stack, int pass) {
//		return itemIcon;
//	}
//	
//	@Override
//	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
//		return OpenGlasses.MODID + ":textures/models/glasses.png";
//	}

	public static Location getUUID(ItemStack itemStack){
		NBTTagCompound tag = getItemTag(itemStack);
		if (!tag.hasKey("X") || !tag.hasKey("Y") || ! tag.hasKey("Z") || ! tag.hasKey("uniqueKey")) return null;
		return new Location(new BlockPos(tag.getInteger("X"),tag.getInteger("Y"),tag.getInteger("Z")),tag.getInteger("DIM"), tag.getLong("uniqueKey"));
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
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound();
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
