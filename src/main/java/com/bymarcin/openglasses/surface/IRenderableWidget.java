package com.bymarcin.openglasses.surface;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import com.bymarcin.openglasses.utils.Location;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public interface IRenderableWidget {
	void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates);
	RenderType getRenderType();
	boolean shouldWidgetBeRendered(EntityPlayer player);
	UUID getWidgetOwner();
	boolean isWidgetOwner(String uuid);
}
