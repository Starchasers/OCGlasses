package com.bymarcin.openglasses.render;

import com.bymarcin.openglasses.utils.VazkiiShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;

import static com.bymarcin.openglasses.proxy.ClientProxy.entityClassModels;

@SideOnly(Side.CLIENT)
public class InfraredRenderLayer implements LayerRenderer<EntityLiving>  {
    private static double currentDistance = 0;
    private static boolean wasGlowing;

    public static final VazkiiShaderHelper.ShaderCallback callback = shader -> {
        // Frag Uniforms
        setShaderColor(shader, "red", 1.0f / (float) (currentDistance/16f));
        setShaderColor(shader, "green", 1f - (float) (16f/currentDistance));
        setShaderColor(shader, "blue", 0f);
        setShaderColor(shader, "alpha", 0.6f);
    };

    private static void setShaderColor(int shader, String name, float val){
        ARBShaderObjects.glUniform1fARB(OpenGlHelper.glGetUniformLocation(shader, name), val);
    }

    public void doRenderLayer(EntityLiving entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){

        currentDistance = entity.getPositionVector().distanceTo(Minecraft.getMinecraft().player.getPositionVector());

        Minecraft.getMinecraft().renderGlobal.entityOutlineFramebuffer.bindFramebuffer(true);
        VazkiiShaderHelper.useShader(VazkiiShaderHelper.thermalColorShader, callback);
        //GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        renderEntity(entity, partialTicks);
        GlStateManager.depthMask(true);
        VazkiiShaderHelper.releaseShader();

        //Minecraft.getMinecraft().renderGlobal.entityOutlineFramebuffer.framebufferClear();
        Minecraft.getMinecraft().renderGlobal.entityOutlineFramebuffer.bindFramebuffer(false);

        entity.setGlowing(true);
    }

    private static void renderEntity(EntityLivingBase entity, float partialTicks){
        //GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();

        //GlStateManager.scale(50, 50, 50);

        RenderHelper.disableStandardItemLighting();

        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setRenderShadow(false);

        /*
        if (entity.ticksExisted == 0)
        {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
        }

        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;

        if (entity.isBurning()){}
        */

        GlStateManager.disableTexture2D();
        GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
        EntityPlayer player = Minecraft.getMinecraft().player;
        //GlStateManager.translate(-player.getPositionVector().x, -player.getPositionVector().y, -player.getPositionVector().z);
        //GlStateManager.translate(-entity.getPositionVector().x, -entity.getPositionVector().y, -entity.getPositionVector().z);
        GlStateManager.scale(1, -1, 1);

        entityClassModels.get(entity.getClass()).render(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, entity.getRotationYawHead(), entity.rotationPitch, 1f/16);

        rendermanager.setRenderShadow(true);
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
        //RenderHelper.disableStandardItemLighting();
        //GlStateManager.disableRescaleNormal();
        //GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        //GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        //Minecraft.getMinecraft().entityRenderer.disableLightmap();
    }

    public boolean shouldCombineTextures(){ return false; }


}
