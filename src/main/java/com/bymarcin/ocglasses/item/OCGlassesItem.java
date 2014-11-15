package com.bymarcin.ocglasses.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import com.bymarcin.ocglasses.OCGlasses;
import com.bymarcin.ocglasses.surface.ClientSurface;
import com.bymarcin.ocglasses.utils.Location;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		MovingObjectPosition coords = getBlockCoordsLookingAt(player);
		if(coords!=null){
			ClientSurface.instances.onLookingAt(world, coords.blockX, coords.blockY, coords.blockZ);
		}
	}
	
	@Override
	public void registerIcons(IIconRegister register) {
		itemIcon = register.registerIcon(OCGlasses.MODID + ":glasses");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}

	public static Location getUUID(ItemStack itemStack){
		NBTTagCompound tag = getItemTag(itemStack);
		if (!tag.hasKey("X") || !tag.hasKey("Y") || ! tag.hasKey("Z")) return null;
		return new Location(tag.getInteger("X"),tag.getInteger("Y"),tag.getInteger("Z"),tag.getInteger("DIM"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(itemStack, player, list, par4);
		Location uuid = getUUID(itemStack);
		if (uuid != null)
			list.add("Link to: " + uuid.toString());
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
	}
	
	private MovingObjectPosition getBlockCoordsLookingAt(EntityPlayer player){
		MovingObjectPosition objectMouseOver;
		objectMouseOver = player.rayTrace(200, 1);	
		if(objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
		{
			return objectMouseOver;
		}
		return null;
	}

}
