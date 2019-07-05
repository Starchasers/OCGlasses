package com.bymarcin.openglasses.proxy;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.minecraft.client.ClientEventHandler;
import com.bymarcin.openglasses.event.minecraft.client.ClientKeyboardEvents;
import com.bymarcin.openglasses.event.minecraft.client.ClientRenderEvents;
import com.bymarcin.openglasses.event.minecraft.client.ClientWorldInteractionEvents;
import com.bymarcin.openglasses.render.BaublesRenderLayer;

import com.bymarcin.openglasses.render.InfraredRenderLayer;
import com.bymarcin.openglasses.render.ThermalEntityRender;
import com.bymarcin.openglasses.utils.VazkiiShaderHelper;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	public static void registermodel(Item item, int meta){
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@Override
	public void init() {
		// 'dynamic' because it has to preInit the keybinds
		MinecraftForge.EVENT_BUS.register(new ClientKeyboardEvents());

		// static event classes
		MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
		MinecraftForge.EVENT_BUS.register(ClientRenderEvents.class);
		MinecraftForge.EVENT_BUS.register(ClientWorldInteractionEvents.class);

	}
	
	@Override
	public void postInit() {
		if(OpenGlasses.baubles){
			Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
			RenderPlayer render;
			render = skinMap.get("default");
			render.addLayer(new BaublesRenderLayer());
			render = skinMap.get("slim");
			render.addLayer(new BaublesRenderLayer());
		}

		//MinecraftForge.EVENT_BUS.register(new RenderEventHandler());

		VazkiiShaderHelper.initShaders();

		MinecraftForge.EVENT_BUS.register(ThermalEntityRender.class);
	}


	public static HashMap<Class<? extends EntityLivingBase>, ModelBase> entityClassModels = new HashMap<>();

	public static class RenderEventHandler{
		@SubscribeEvent
		public void onJoin(EntityJoinWorldEvent event) {
			if(event.getWorld().isRemote && event.getEntity() instanceof EntityLiving){
				Class<? extends EntityLivingBase> entityClass = ((EntityLivingBase) event.getEntity()).getClass();
				if(entityClassModels.containsKey(entityClass))
					return;

				RenderLivingBase<?> rm = (RenderLivingBase<?>) Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(entityClass);

				rm.addLayer(new InfraredRenderLayer());

				entityClassModels.put(entityClass, rm.getMainModel());
			}
		}
	}

	@Override
	public World getWorld(int dimensionId) {
		if (Minecraft.getMinecraft().world.provider.getDimension() != dimensionId) {
			return null;
		} else
			return Minecraft.getMinecraft().world;
	}

	@Override
	public boolean isCallingFromMinecraftThread() {
		return Minecraft.getMinecraft().isCallingFromMinecraftThread();
	}

	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
		return Minecraft.getMinecraft().addScheduledTask(runnable);
	}
}
