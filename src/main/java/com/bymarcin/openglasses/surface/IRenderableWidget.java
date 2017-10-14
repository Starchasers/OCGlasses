package com.bymarcin.openglasses.surface;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import com.bymarcin.openglasses.utils.Location;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public interface IRenderableWidget {
	public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates);
	public RenderType getRenderType();
	public boolean shouldWidgetBeRendered(EntityPlayer player);
	public UUID getWidgetOwner();
	public boolean isWidgetOwner(String uuid);
}
