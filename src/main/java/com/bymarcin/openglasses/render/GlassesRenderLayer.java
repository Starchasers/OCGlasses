package com.bymarcin.openglasses.render;

import com.bymarcin.openglasses.OpenGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import net.minecraftforge.client.ForgeHooksClient; 
import baubles.api.render.IRenderBauble;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface="baubles.api.render.IRenderBauble",modid="baubles")
public class BaublesRenderLayer implements IRenderBauble, LayerRenderer<EntityLivingBase> {
	static ModelBiped model;
	static ResourceLocation texture;
	
	public BaublesRenderLayer(){
		model = ForgeHooksClient.getArmorModel(Minecraft.getMinecraft().player, new ItemStack(new OpenGlassesItem()), EntityEquipmentSlot.HEAD, new ModelBiped());
		texture = new ResourceLocation("openglasses:textures/models/glasses.png");
	}
	
	@Override
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks){}
	
	@Override
	public void doRenderLayer(EntityLivingBase living, float limbSwing, float prevLimbSwing, float partialTicks, float rotation, float yaw, float pitch, float scale){

		ItemStack glasses = OpenGlasses.getGlassesStackBaubles((EntityPlayer) living);

		if(glasses.isEmpty() && OpenGlasses.techguns)
			glasses = OpenGlasses.getGlassesStackTechguns((EntityPlayer) living);

		if(glasses.isEmpty())
			return;


		boolean sneaky = living.isSneaking();
		
		if(sneaky){
            model.bipedBody.rotateAngleX = 0.5F;
            model.bipedHead.rotationPointY = 1.0F;
            model.bipedHead.offsetY+=0.2F;
        }
        else{
            model.bipedBody.rotateAngleX = 0.0F;
            model.bipedHead.rotationPointY = 0.0F;
        }
				
		model.bipedHead.rotateAngleY = (float) Math.toRadians(yaw);
		model.bipedHead.rotateAngleX = (float) Math.toRadians(pitch);
						
		GlStateManager.pushMatrix();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		model.bipedHead.render(scale * 1.001F);
		GlStateManager.popMatrix();
		
		if(sneaky) model.bipedHead.offsetY-=0.2F;
	}


    @Override
	public boolean shouldCombineTextures() {
		return true;
	}	
}

