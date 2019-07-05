package com.bymarcin.openglasses.utils;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ThermalShaderGroup extends ShaderGroup {
    public ThermalShaderGroup(TextureManager p_i1050_1_, IResourceManager resourceManagerIn, Framebuffer mainFramebufferIn, ResourceLocation p_i1050_4_) throws JsonException, IOException, JsonSyntaxException    {
        super(p_i1050_1_, resourceManagerIn, mainFramebufferIn, p_i1050_4_);
    }

    @Override
    public void addFramebuffer(String name, int width, int height)
    {
        Framebuffer framebuffer = new ThermalFramebuffer(width, height, true);
        framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);

        this.mapFramebuffers.put(name, framebuffer);

        if (width == this.mainFramebufferWidth && height == this.mainFramebufferHeight)
        {
            this.listFramebuffers.add(framebuffer);
        }
    }
}
