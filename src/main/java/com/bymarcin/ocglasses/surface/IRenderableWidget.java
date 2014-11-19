package com.bymarcin.ocglasses.surface;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderableWidget {
	public void render();
	public RenderType getRenderType();
}
