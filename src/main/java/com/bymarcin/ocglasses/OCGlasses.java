package com.bymarcin.ocglasses;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bymarcin.ocglasses.network.NetworkRegistry;
import com.bymarcin.ocglasses.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = OCGlasses.MODID, version = OCGlasses.VERSION)
public class OCGlasses
{
	public static final String MODID = "ocglasses";
	public static final String VERSION = "0.1";

	public Configuration config;
	public static Logger logger = LogManager.getLogger(OCGlasses.MODID);

	@SidedProxy(clientSide = "com.bymarcin.ocglasses.proxy.ClientProxy", serverSide = "com.bymarcin.ocglasses.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(value = OCGlasses.MODID)
	public static OCGlasses instance;

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

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}
}
