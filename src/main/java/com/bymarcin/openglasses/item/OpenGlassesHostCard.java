package com.bymarcin.openglasses.item;

import com.bymarcin.openglasses.OpenGlasses;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OpenGlassesHostCard extends Item {
    public static final String NAME = "openglasses_card";
    public static ItemStack DEFAULTSTACK;

    public OpenGlassesHostCard() {
        setUnlocalizedName(NAME);
        setRegistryName(OpenGlasses.MODID, NAME);
        setCreativeTab(OpenGlasses.creativeTab);
    }
}
