package com.bymarcin.openglasses.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import com.bymarcin.openglasses.item.OpenGlassesBaubleItem;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import net.minecraftforge.client.ForgeHooksClient; 
import baubles.api.render.IRenderBauble;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface="baubles.api.render.IRenderBauble",modid="Baubles")
public class BaublesRenderLayer implements IRenderBauble, LayerRenderer<EntityLivingBase> {
	static ModelBiped model;
	static ResourceLocation texture;
	
	public BaublesRenderLayer(){
		this.model = ForgeHooksClient.getArmorModel(Minecraft.getMinecraft().player, new ItemStack(new OpenGlassesItem()), EntityEquipmentSlot.HEAD, new ModelBiped());
		this.texture = new ResourceLocation("openglasses:textures/models/glasses.png");
	}
	
	@Override
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks){
		return;
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase living, float limbSwing, float prevLimbSwing, float partialTicks, float rotation, float yaw, float pitch, float scale){
		ItemStack glasses = getGlassesStackBaubles((EntityPlayer) living);
		if(glasses == null) return;
		boolean sneaky = living.isSneaking();
		
		if(sneaky){
            this.model.bipedBody.rotateAngleX = 0.5F;
            this.model.bipedHead.rotationPointY = 1.0F;
            this.model.bipedHead.offsetY+=0.2F;
        }
        else{
            this.model.bipedBody.rotateAngleX = 0.0F;
            this.model.bipedHead.rotationPointY = 0.0F;
        }
				
		this.model.bipedHead.rotateAngleY = (float) Math.toRadians(yaw);
		this.model.bipedHead.rotateAngleX = (float) Math.toRadians(pitch);
						
		GlStateManager.pushMatrix();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		this.model.bipedHead.render(scale * 1.001F); 
		GlStateManager.popMatrix();
		
		if(sneaky) this.model.bipedHead.offsetY-=0.2F;		
	}	
	
	public static ItemStack getGlassesStackBaubles(EntityPlayer player){
		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
		
		if(handler != null){
			ItemStack glassesStack = handler.getStackInSlot(4);
			if(glassesStack != null && (glassesStack.getItem() instanceof OpenGlassesBaubleItem))
				return glassesStack;
		}
		
		return null;		
	}
	
    @Override
	public boolean shouldCombineTextures() {
		return true;
	}	
}

