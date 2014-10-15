package com.bymarcin.ocglasses.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bymarcin.ocglasses.OCGlasses;
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
}
