package com.bymarcin.openglasses;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bymarcin.openglasses.block.OpenGlassesTerminalBlock;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.proxy.CommonProxy;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = OpenGlasses.MODID, version = OpenGlasses.VERSION)
public class OpenGlasses
{
	public static final String MODID = "openglasses";
	public static final String VERSION = "@VERSION@";

	public Configuration config;
	public static Logger logger = LogManager.getLogger(OpenGlasses.MODID);

	@SidedProxy(clientSide = "com.bymarcin.openglasses.proxy.ClientProxy", serverSide = "com.bymarcin.openglasses.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(value = OpenGlasses.MODID)
	public static OpenGlasses instance;

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
		
		GameRegistry.registerBlock(new OpenGlassesTerminalBlock(), "openglassesterminal");
		GameRegistry.registerTileEntity(OpenGlassesTerminalTileEntity.class, "openglassesterminal");
		GameRegistry.registerItem(new OpenGlassesItem(), "openglasses");
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		config.save();
	}
}
