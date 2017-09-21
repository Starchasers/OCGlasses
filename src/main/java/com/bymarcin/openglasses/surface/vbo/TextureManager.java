package com.bymarcin.openglasses.surface.vbo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TextureManager {
	Cache<String, TextureAtlasSprite> graphs;
	
	public TextureManager() {
		graphs = CacheBuilder.newBuilder()
				.maximumSize(ForgeRegistries.ITEMS.getKeys().size())
				.expireAfterAccess(1, TimeUnit.SECONDS).build();
	}
	
	public static class AdvancedResourceLocation {
		ResourceLocation mcLocation = new ResourceLocation("minecraft:air");
		int meta = 0;
		EnumFacing face = EnumFacing.UP;
		
		public AdvancedResourceLocation(String location) {
			String[] parts = location.split(":");
			if (parts.length >= 2) {
				mcLocation = new ResourceLocation(parts[0], parts[1]);
			}
			
			if (parts.length >= 3) {
				try {
					meta = Integer.parseInt(parts[2]);
				} catch (NumberFormatException ignored) {
				}
			}
			
			if (parts.length == 4) {
				EnumFacing newFace = EnumFacing.byName(parts[3]);
				if (newFace != null) {
					face = newFace;
				}
			}
		}
		
		public ResourceLocation getMcLocation() {
			return mcLocation;
		}
		
		public int getMeta() {
			return meta;
		}
		
		public EnumFacing getFace() {
			return face;
		}
	}
	
	
	public TextureAtlasSprite findTexture(String name) {
		
		try {
			return graphs.get(name, ()-> loadTexture(new AdvancedResourceLocation(name)));
		} catch (Exception e) {
			graphs.invalidate(name);
			return getMissingTexture();
		}
	}
	
	private TextureAtlasSprite loadTexture(AdvancedResourceLocation advancedResourceLocation) {
		Item item = ForgeRegistries.ITEMS.getValue(advancedResourceLocation.getMcLocation()); //Find item in registry
		if (item != null) {
			ItemStack stack = new ItemStack(item, 1, advancedResourceLocation.getMeta());
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null); //get model for item
			List<BakedQuad> quads = model.getQuads(null, advancedResourceLocation.getFace(), 0);
			if (!quads.isEmpty()) {
				BakedQuad quad = quads.iterator().next(); // first quad for given facing
				return quad.getSprite(); // texture from first quad
			} else {
				return model.getParticleTexture()!=null?model.getParticleTexture():getMissingTexture(); // fallback
			}
		} else {
			return getMissingTexture();
		}
	}
	
	private TextureAtlasSprite getMissingTexture(){
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureMap.LOCATION_MISSING_TEXTURE.toString());
	}
}
