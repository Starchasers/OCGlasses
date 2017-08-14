package com.bymarcin.openglasses;

import java.util.Arrays;

import com.bymarcin.openglasses.block.OpenGlassesTerminalBlock;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.proxy.CommonProxy;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import li.cil.oc.api.Items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = OpenGlasses.MODID, version = OpenGlasses.VERSION, dependencies = "required-after:opencomputers@[1.7.0,)")
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

	public static CreativeTabs creativeTab = CreativeTabs.REDSTONE;
	
	public static Item openGlasses;
	public static OpenGlassesTerminalBlock openTerminal;
	public static Item openTerminalItem;
	
	public static int energyBuffer = 100;
	public static double energyMultiplier  = 1;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		NetworkRegistry.initialize();
		energyBuffer = config.get("Energy", "energyBuffer", 100).getInt(100);
		energyMultiplier = config.get("Energy", "energyMultiplier", 1.0, "PowerDrain= (NumberOfWidgets / 10) * energyMultiplier").getDouble(1.0);
		
		
		openTerminal = new OpenGlassesTerminalBlock();
		openTerminalItem = new ItemBlock(openTerminal).setRegistryName(openTerminal.getRegistryName());
		
		
		
		GameRegistry.registerTileEntity(OpenGlassesTerminalTileEntity.class, "openglassesterminalte");
		
		openGlasses = new OpenGlassesItem();
		
		proxy.init();
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(openTerminal);
	}
	
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		proxy.registermodel(openTerminalItem, 0);
		proxy.registermodel(openGlasses, 0);
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(openTerminalItem);
		event.getRegistry().register(openGlasses);
		
	}
	
	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ItemStack ram = Items.get("ram5").createItemStack(1);
		ItemStack graphics = Items.get("graphicscard3").createItemStack(1);
		ItemStack wlanCard = Items.get("wlancard").createItemStack(1);
		ItemStack server = Items.get("geolyzer").createItemStack(1);
		ItemStack screen = Items.get("screen3").createItemStack(1);
		ItemStack cpu = Items.get("cpu3").createItemStack(1);
		
		ShapedOreRecipe r1 = new ShapedOreRecipe(new ResourceLocation(OpenGlasses.MODID, "openglasses"), new ItemStack(openGlasses), "SCS", " W ", "   ", 'S', screen, 'W', wlanCard, 'C', graphics);
		ShapedOreRecipe r2 = new ShapedOreRecipe(new ResourceLocation(OpenGlasses.MODID, "openterminal"), new ItemStack(openTerminal), "R  ", "S  ", "M  ", 'S', server, 'R', ram, 'M', cpu);
		r1.setRegistryName(OpenGlasses.MODID, "openglasses");
		r2.setRegistryName(OpenGlasses.MODID, "openterminal");
		event.getRegistry().register(r1);
		event.getRegistry().register(r2);
	}
		

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.registerPacket(0, GlassesEventPacket.class, Side.SERVER);
		NetworkRegistry.registerPacket(1, WidgetUpdatePacket.class, Side.CLIENT);
		NetworkRegistry.registerPacket(2, TerminalStatusPacket.class, Side.CLIENT);


	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

		
		config.save();
	}
}
