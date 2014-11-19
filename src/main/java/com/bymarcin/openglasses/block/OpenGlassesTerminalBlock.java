package com.bymarcin.openglasses.block;

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

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

public class OpenGlassesTerminalBlock extends BlockContainer {

	public OpenGlassesTerminalBlock() {
		super(Material.iron);
		setCreativeTab(OpenGlasses.creativeTab);
		setBlockName("openglassesterminal");
		setHardness(3.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new OpenGlassesTerminalTileEntity();
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(OpenGlasses.MODID + ":terminal");
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
		OpenGlassesTerminalTileEntity te = getTileEntity(world, x, y, z, OpenGlassesTerminalTileEntity.class);
		if (te == null)
			return false;
		ItemStack glassesStack = player.getHeldItem();
		if (glassesStack != null) {
			Item item = glassesStack.getItem();
			if (item instanceof OpenGlassesItem) {
				((OpenGlassesItem) item).bindToTerminal(glassesStack, new Location(x,y,z,world.provider.dimensionId));
				return true;
			}
		}
		return false;
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int m) {
		ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), new Location(x, y, z, world.provider.dimensionId));
	}
	
}
