package com.bymarcin.ocglasses.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bymarcin.ocglasses.OCGlasses;
import com.bymarcin.ocglasses.item.OCGlassesItem;
import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;

public class OCGlassesTerminalBlock extends BlockContainer {

	public OCGlassesTerminalBlock() {
		super(Material.iron);
		setCreativeTab(OCGlasses.creativeTab);
		setBlockName("ocglassesterminal");
		setHardness(3.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new OCGlassesTerminalTileEntity();
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(OCGlasses.MODID + ":terminal");
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return blockIcon;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getTileEntity(IBlockAccess world, int x, int y, int z, Class<T> T) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && T.isAssignableFrom(te.getClass())) {
			return (T) te;
		}
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking() || world.isRemote)
			return false;
		OCGlassesTerminalTileEntity te = getTileEntity(world, x, y, z, OCGlassesTerminalTileEntity.class);
		if (te == null)
			return false;
		ItemStack glassesStack = player.getHeldItem();
		if (glassesStack != null) {
			Item item = glassesStack.getItem();
			if (item instanceof OCGlassesItem) {
				((OCGlassesItem) item).bindToTerminal(glassesStack, te.getTerminalUUID());
				return true;
			}
		}
		return false;
	}
}
