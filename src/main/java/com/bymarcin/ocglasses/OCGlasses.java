package com.bymarcin.ocglasses;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bymarcin.ocglasses.block.OCGlassesTerminalBlock;
import com.bymarcin.ocglasses.item.OCGlassesItem;
import com.bymarcin.ocglasses.network.NetworkRegistry;
import com.bymarcin.ocglasses.network.packet.GlassesEventPacket;
import com.bymarcin.ocglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.ocglasses.proxy.CommonProxy;
import com.bymarcin.ocglasses.tileentity.OCGlassesTerminalTileEntity;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = OCGlasses.MODID, version = OCGlasses.VERSION)
public class OCGlasses
{
	public static final String MODID = "ocglasses";
	public static final String VERSION = "@VERSION@";

	public Configuration config;
	public static Logger logger = LogManager.getLogger(OCGlasses.MODID);

	@SidedProxy(clientSide = "com.bymarcin.ocglasses.proxy.ClientProxy", serverSide = "com.bymarcin.ocglasses.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(value = OCGlasses.MODID)
	public static OCGlasses instance;

	public static CreativeTabs creativeTab = CreativeTabs.tabRedstone;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		NetworkRegistry.initialize();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.registerPacket(0, GlassesEventPacket.class, Side.SERVER);
		NetworkRegistry.registerPacket(1, WidgetUpdatePacket.class, Side.CLIENT);
		
		GameRegistry.registerBlock(new OCGlassesTerminalBlock(), "ocglassesterminal");
		GameRegistry.registerTileEntity(OCGlassesTerminalTileEntity.class, "ocglassesterminal");
		GameRegistry.registerItem(new OCGlassesItem(), "ocglasses");
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		config.save();
	}
}
