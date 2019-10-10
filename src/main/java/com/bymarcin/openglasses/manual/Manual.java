package com.bymarcin.openglasses.manual;

import com.bymarcin.openglasses.OpenGlasses;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.HashSet;

public class Manual {
    private static ResourceLocation iconResourceLocation = new ResourceLocation(OpenGlasses.MODID, "textures/blocks/glasses_side.png");
    private static String tooltip = "OpenGlasses";
    private static String homepage = "assets/" + OpenGlasses.MODID + "/doc/_Sidebar";

    public static HashSet<Item> items = new HashSet<>();


    public static void preInit(){
        if(Loader.isModLoaded("rtfm")) {
            new ManualPathProviderRTFM().initialize(iconResourceLocation, tooltip, homepage);
            items.add(ManualPathProviderRTFM.getManualItem().setUnlocalizedName("openglasses_manual").setRegistryName("manual").setCreativeTab(OpenGlasses.creativeTab));
        }

        if(Loader.isModLoaded("opencomputers"))
            new ManualPathProviderOC().initialize(iconResourceLocation, tooltip, homepage);
    }

}
