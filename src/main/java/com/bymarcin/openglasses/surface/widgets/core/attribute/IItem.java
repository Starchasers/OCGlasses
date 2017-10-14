package com.bymarcin.openglasses.surface.widgets.core.attribute;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItem extends IAttribute{
	public void setItem(ItemStack itm);
	public void setItem(String itm, int meta);
	public void setItem(String itm);
	public Item getItem();
}
