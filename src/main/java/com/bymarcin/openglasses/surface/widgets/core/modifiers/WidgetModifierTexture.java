package com.bymarcin.openglasses.surface.widgets.core.modifiers;

import com.bymarcin.openglasses.surface.WidgetModifier;
import io.netty.buffer.ByteBuf;

import net.minecraft.util.ResourceLocation;

import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.common.network.ByteBufUtils;


public class WidgetModifierTexture extends WidgetModifier {
	private ResourceLocation textureLocation;
	private String textureName = "";
	private TextureAtlasSprite tex;
	public WidgetModifierTexture(String texloc){
		setupTexture(texloc);	
	}
	
	
	public void setupTexture(String texloc){
		if(texloc == null) return;
		
		//this.textureName = texloc;
		//this.textureLocation = ResourceUtil.getModelTexture(texloc);
		//this.tex = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(this.textureName);		
		//this.textureLocation = Minecraft.getMinecraft().getTextureManager().getResource(texloc);	
	}
		
	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;
		
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(imgFile));
		
		Minecraft mc = Minecraft.getMinecraft();
		TextureAtlasSprite tex = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("minecraft:blocks/stone");		
		//Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(tex.getIconName()));
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//mc.getTextureManager().bindTexture(textureLocation);		
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		ByteBufUtils.writeUTF8String(buff, this.textureName);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		setupTexture(ByteBufUtils.readUTF8String(buff));
	}
	
	public WidgetModifierType getType(){
		return WidgetModifierType.TEXTURE;
	}
	
	public Object[] getValues(){
		return new Object[]{ this.textureName };
	}
}
