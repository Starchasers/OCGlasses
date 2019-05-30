package com.bymarcin.openglasses;

import com.bymarcin.openglasses.block.OpenGlassesTerminalBlock;
import com.bymarcin.openglasses.config.Config;
import com.bymarcin.openglasses.event.AnvilEvent;
import com.bymarcin.openglasses.integration.opencomputers.ocProgramDisks;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.OpenGlassesBaubleItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.HostInfoPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.proxy.CommonProxy;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
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

@Mod(modid = OpenGlasses.MODID, version = OpenGlasses.VERSION, dependencies = "required-after:opencomputers@[1.7.1,);required-after:guitoolkit@1.0.0;required-after:rendertoolkit@1.0.3;after:baubles;")
public class OpenGlasses
{
	public static final String MODID = "openglasses";
	public static final String VERSION = "@VERSION@";

	@SidedProxy(clientSide = "com.bymarcin.openglasses.proxy.ClientProxy", serverSide = "com.bymarcin.openglasses.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Item openGlasses;
	public static Item openTerminalItem;

	public static boolean baubles = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		if(Loader.isModLoaded("baubles")) OpenGlasses.baubles = true;

		//Config.preInit();

		NetworkRegistry.initialize();
		MinecraftForge.EVENT_BUS.register(this);

		OpenGlassesTerminalBlock.DEFAULT_BLOCK = new OpenGlassesTerminalBlock();
		openTerminalItem = new ItemBlock(OpenGlassesTerminalBlock.DEFAULT_BLOCK).setRegistryName(OpenGlassesTerminalBlock.DEFAULT_BLOCK.getRegistryName());

		GameRegistry.registerTileEntity(OpenGlassesTerminalTileEntity.class, "openglassesterminalte");

		openGlasses = getOGCObject();
		OpenGlassesItem.DEFAULT_STACK = new ItemStack(openGlasses);
		OpenGlassesItem.initGlassesStack(OpenGlassesItem.DEFAULT_STACK);

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
		return stack != null && stack.getItem() instanceof OpenGlassesItem;
	}

	public static ItemStack getGlassesStack(EntityPlayer e){
		ItemStack glassesStack = e.inventory.armorInventory.get(3); //armor helmet slot

		if(isGlassesStack(glassesStack))
			return glassesStack;
		else if(OpenGlasses.baubles)
			return getGlassesStackBaubles(e);

		return null;
	}

	public static ItemStack getGlassesStackBaubles(EntityPlayer e){
		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(e);
		if (handler == null) return null;

		ItemStack baublesStack = handler.getStackInSlot(4);

		return isGlassesStack(baublesStack) ? baublesStack : null;
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		NetworkRegistry.registerPacket(0, GlassesEventPacket.class, Side.SERVER);
		NetworkRegistry.registerPacket(1, TerminalStatusPacket.class, Side.CLIENT);
		NetworkRegistry.registerPacket(2, HostInfoPacket.class, Side.CLIENT);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(openTerminalItem);
		event.getRegistry().register(openGlasses);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(OpenGlassesTerminalBlock.DEFAULT_BLOCK);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		ocProgramDisks.register();
		//ocAssembler.register();

		MinecraftForge.EVENT_BUS.register(AnvilEvent.instances);  //register anvil event

		proxy.postInit();
	}

    public static CreativeTabs creativeTab = new CreativeTabs("openglasses"){
        @Override
        public ItemStack getTabIconItem(){
            return new ItemStack(OpenGlasses.openTerminalItem);
        }
    };

}
