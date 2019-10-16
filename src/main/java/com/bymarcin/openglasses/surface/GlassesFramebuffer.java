package com.bymarcin.openglasses.surface;

import ben_mkiv.rendertoolkit.client.OptifineHelper;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;

class GlassesFramebuffer extends Framebuffer {
    private static GlassesFramebuffer fb;

    static boolean isOptifineSpecialCase = false, isOptifineFastRender = false;

    private GlassesFramebuffer(int width, int height){
        super(width, height, true);
    }

    static void init(){
        Minecraft mc = Minecraft.getMinecraft();

        if(fb != null) {
            // check if the resolution did change
            if(fb.framebufferTextureWidth == mc.getFramebuffer().framebufferTextureWidth
                    && fb.framebufferTextureHeight == mc.getFramebuffer().framebufferTextureHeight)
                return;
        }

        fb = new GlassesFramebuffer(mc.displayWidth, mc.displayHeight);
        fb.setFramebufferColor(0, 0, 0, 0);
    }

    static void bindFramebufferVanilla(float partialTicks){
        if(fb == null)
            init();

        fb.bindFramebuffer(false);

        bindDepthBuffer();
    }

    static void bindDepthBuffer(){
        if(isOptifineFastRender)
            return;
        if(isOptifineSpecialCase)
            OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, OptifineHelper.getOptifineDepthBufferLocation());
        else
            OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, Minecraft.getMinecraft().getFramebuffer().depthBuffer);
    }

    static void bindFramebuffer(float partialTicks){
        isOptifineFastRender = renderToolkit.Optifine && OptifineHelper.isFastRenderEnabled();
        isOptifineSpecialCase = !isOptifineFastRender && renderToolkit.Optifine && OptifineHelper.isShaderActive();

        if(isOptifineSpecialCase)
            bindFramebufferOptifine(partialTicks);
        else
            bindFramebufferVanilla(partialTicks);
    }

    static void releaseFramebuffer(){
        if(isOptifineSpecialCase)
            releaseFramebufferOptifine();
        else
            releaseFramebufferVanilla();
    }

    static void bindFramebufferOptifine(float partialTicks) {
        if(fb == null)
            init();

        OptifineHelper.releaseShaderProgram();
        fb.bindFramebuffer(false);
        OptifineHelper.bindOptifineDepthBuffer();
    }

    static void releaseFramebufferVanilla() {

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
    }


    static void releaseFramebufferOptifine(){
        OptifineHelper.bindOptifineFramebuffer();
    }

    static void renderWorldFramebuffer(float partialTicks){
        if(fb == null)
            init();

        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        //framebuffer.bindFramebufferTexture();
        fb.render(mc.displayWidth, mc.displayHeight);

        if(isOptifineFastRender){
            GlStateManager.clearColor(fb.framebufferColor[0], fb.framebufferColor[1], fb.framebufferColor[2], fb.framebufferColor[3]);
            if (fb.useDepth){
                GlStateManager.clearDepth(1.0D);
            }
        }
        else
            fb.framebufferClear();

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        // reinit overlay renderer
        releaseFramebufferVanilla();
    }

    public void render(int width, int height)
    {
        if (!OpenGlHelper.isFramebufferEnabled())
            return;


        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();

        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)width, (double)height, 0.0D, 1500.0D, 2500.0D);

        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2500.0F);
        GlStateManager.viewport(0, 0, width, height);


        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        float f = (float)width;
        float f1 = (float)height;
        float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
        float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;

        bindFramebufferTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double)f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos((double)f, (double)f1, 0.0D).tex((double)f2, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos((double)f, 0.0D, 0.0D).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        unbindFramebufferTexture();
    }

}
