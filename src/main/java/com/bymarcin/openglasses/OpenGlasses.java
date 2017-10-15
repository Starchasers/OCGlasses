package com.bymarcin.openglasses;

import com.bymarcin.openglasses.block.OpenGlassesTerminalBlock;
import com.bymarcin.openglasses.event.AnvilEvent;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.OpenGlassesBaubleItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.proxy.CommonProxy;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import li.cil.oc.api.fs.FileSystem;
import li.cil.oc.api.IMC;
import li.cil.oc.api.Items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.concurrent.Callable;

@Mod(modid = OpenGlasses.MODID, version = OpenGlasses.VERSION, dependencies = "required-after:opencomputers@[1.7.0,);after:baubles;")
public class OpenGlasses
{
	public static final String MODID = "openglasses";
	public static final String VERSION = "@VERSION@";

	@SidedProxy(clientSide = "com.bymarcin.openglasses.proxy.ClientProxy", serverSide = "com.bymarcin.openglasses.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs creativeTab = CreativeTabs.REDSTONE;

	public static Item openGlasses;
	public static OpenGlassesTerminalBlock openTerminal;
	public static Item openTerminalItem;

	public static boolean baubles = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		if(Loader.isModLoaded("baubles")) OpenGlasses.baubles = true;

		NetworkRegistry.initialize();
		MinecraftForge.EVENT_BUS.register(this);

		openTerminal = new OpenGlassesTerminalBlock();
		openTerminalItem = new ItemBlock(openTerminal).setRegistryName(openTerminal.getRegistryName());

		GameRegistry.registerTileEntity(OpenGlassesTerminalTileEntity.class, "openglassesterminalte");

		openGlasses = getOGCObject();


		proxy.init();
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		proxy.registermodel(openTerminalItem, 0);
		proxy.registermodel(openGlasses, 0);
	}

	public Item getOGCObject(){
		if(OpenGlasses.baubles)
			return new OpenGlassesBaubleItem();
		else
			return new OpenGlassesItem();
	}

	public static Item getGlasses(EntityPlayer e){
		ItemStack glassesStack = getGlassesStack(e);
		if(!isGlassesStack(glassesStack)) return null;

		return glassesStack.getItem();
	}

	public static boolean isGlassesStack(ItemStack stack){
		Item glasses = stack!=null?stack.getItem():null;

		if(glasses instanceof OpenGlassesItem)
			return true;
		else
			return false;
	}

	public static ItemStack getGlassesStack(EntityPlayer e){
		ItemStack glassesStack = e.inventory.armorInventory.get(3); //armor helmet slot

		if(isGlassesStack(glassesStack))
			return glassesStack;
		else
			return getGlassesStackBaubles(e);
	}

	public static ItemStack getGlassesStackBaubles(EntityPlayer e){
		if(!Loader.isModLoaded("baubles")) return null;

		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(e);
		if (handler == null) return null;

		ItemStack glassesStack = handler.getStackInSlot(4);
		if(!isGlassesStack(glassesStack)) return null;

		return glassesStack;
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		NetworkRegistry.registerPacket(0, GlassesEventPacket.class, Side.SERVER);
		NetworkRegistry.registerPacket(1, WidgetUpdatePacket.class, Side.CLIENT);
		NetworkRegistry.registerPacket(2, TerminalStatusPacket.class, Side.CLIENT);
	}


	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(openTerminalItem);
		event.getRegistry().register(openGlasses);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(openTerminal);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		//register loot disks
		li.cil.oc.api.Items.registerFloppy("openGlasses Demo", EnumDyeColor.GREEN, new OCLootDiskFileSystem("ocglasses"), true);
		IMC.registerProgramDiskLabel("ocglasses", "ocglasses", "Lua 5.2", "Lua 5.3", "LuaJ");

		li.cil.oc.api.Items.registerFloppy("wavefront objects", EnumDyeColor.BLUE, new OCLootDiskFileSystem("wavefrontObjects"), true);
		IMC.registerProgramDiskLabel("wavefrontObjects", "wavefrontObjects", "Lua 5.2", "Lua 5.3", "LuaJ");

		MinecraftForge.EVENT_BUS.register(AnvilEvent.instances);  //register anvil event

		proxy.postInit();
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


	private static class OCLootDiskFileSystem implements Callable<FileSystem> {
		private final String name;
		OCLootDiskFileSystem(String name) {
			this.name = name;
		}

		@Override
		@Optional.Method(modid = "opencomputers")
		public FileSystem call() throws Exception {
			return li.cil.oc.api.FileSystem.asReadOnly(li.cil.oc.api.FileSystem.fromClass(OpenGlasses.class, MODID, "loot/" + this.name));
		}
	}

}
