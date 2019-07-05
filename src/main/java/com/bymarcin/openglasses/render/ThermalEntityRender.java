package com.bymarcin.openglasses.render;

import com.bymarcin.openglasses.utils.VazkiiShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

//todo: check if we can load the shader as resource pack data and pass a boolean to decide in the shader which one has to be used
//Barteks2x depending on how it's implemented, resourcepacks that replace that shader on their own may override that
public class ThermalEntityRender {
    private static double currentDistance = 0;
    private static boolean wasGlowing;

    public static final VazkiiShaderHelper.ShaderCallback callback = shader -> {
        // Frag Uniforms
        setShaderColor(shader, "red", 1.0f / (float) (currentDistance/8f));
        setShaderColor(shader, "green", 1f - (float) (8f/currentDistance));
        setShaderColor(shader, "blue", 0f);
        setShaderColor(shader, "alpha", 1f);
    };

    private static void setShaderColor(int shader, String name, float val){
        ARBShaderObjects.glUniform1fARB(OpenGlHelper.glGetUniformLocation(shader, name), val);
    }

    @SubscribeEvent
    static void preRender(RenderLivingEvent.Pre<EntityLivingBase> event){
        // handle glowing state
        wasGlowing = event.getEntity().isGlowing();
        event.getEntity().setGlowing(true);

        // get distance to player
        currentDistance = event.getEntity().getPositionVector().distanceTo(Minecraft.getMinecraft().player.getPositionVector());

        // reenable depth testing (which gets disabled in outline renderer)
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // set thermal rendershader
        VazkiiShaderHelper.useShader(VazkiiShaderHelper.thermalColorShader, callback);
    }

    @SubscribeEvent
    static void postRender(RenderLivingEvent.Post<EntityLivingBase> event){
        VazkiiShaderHelper.releaseShader();

        // do this somewhere else
        //event.getEntity().setGlowing(wasGlowing);
    }

}
