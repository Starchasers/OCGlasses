package com.bymarcin.ocglasses.item;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.bymarcin.ocglasses.OCGlasses;
import com.bymarcin.ocglasses.event.OCGlassesRegisterEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OCGlassesItem extends ItemArmor {
	public final static String GLASS_TAG = "GLASSUUID";

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
		UUID id = getUUID(itemStack);
		if (id != null) MinecraftForge.EVENT_BUS.post(new OCGlassesRegisterEvent(player, id));
	}

	public static UUID getUUID(ItemStack itemStack){
		NBTTagCompound tag = getItemTag(itemStack);
		if (!tag.hasKey(GLASS_TAG)) return null;
		String uuid = tag.getString(GLASS_TAG);
		return UUID.fromString(uuid);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(itemStack, player, list, par4);
		UUID uuid = getUUID(itemStack);
		if (uuid != null)
			list.add("UUID: " + uuid.toString());
	}

	public static NBTTagCompound getItemTag(ItemStack stack) {
		if (stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound;
	}

	public void bindToTerminal(ItemStack glass, UUID uuid) {
		NBTTagCompound tag = getItemTag(glass);
		tag.setString(GLASS_TAG, uuid.toString());
	}

}
