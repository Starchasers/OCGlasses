package com.bymarcin.openglasses.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class ThermalFramebuffer extends Framebuffer {
    public ThermalFramebuffer(int width, int height, boolean useDepthIn){
        super(width, height, useDepthIn);
    }

    @Override
    public void framebufferRenderExt(int width, int height, boolean p_178038_3_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {

            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.viewport(0, 0, width, height);
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.enableColorMaterial();

            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            //GlStateManager.glBlendEquation(GL14.GL_BLEND_EQUATION);
            //setFramebufferColor(0, 0, 0, 0);

            //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindFramebufferTexture();
            float f = (float)width;
            float f1 = (float)height;
            float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
            float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0.0D, (double)f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos((double)f, (double)f1, 0.0D).tex((double)f2, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos((double)f, 0.0D, 0.0D).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)f3).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.unbindFramebufferTexture();
            GlStateManager.depthMask(true);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }
}
