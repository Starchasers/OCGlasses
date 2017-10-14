package com.bymarcin.openglasses.surface.widgets.core.attribute;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItem extends IAttribute{
	void setItem(ItemStack itm);
	void setItem(String itm, int meta);
	void setItem(String itm);
	Item getItem();
}
