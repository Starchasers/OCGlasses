package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.OpenGlasses;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ShaderHelper {
    static boolean isThermalShader = false;

    public static void makeEntityOutlineShader(boolean thermalShader){
        if(isThermalShader == thermalShader)
            return;

        if (!OpenGlHelper.shadersSupported)
            return;

        if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
            ShaderLinkHelper.setNewStaticShaderLinkHelper();

        ResourceLocation resourcelocation;

        if(thermalShader)
            resourcelocation = new ResourceLocation(OpenGlasses.MODID, "shaders/post/thermal.json");
        else
            resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");

        Minecraft mc = Minecraft.getMinecraft();
        RenderGlobal rg = Minecraft.getMinecraft().renderGlobal;

        //rg.makeEntityOutlineShader();

        isThermalShader = thermalShader;

        try
        {
            //todo: another case for vanilla glow shader, to not use my framebuffer class
            rg.entityOutlineShader = new ThermalShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourcelocation);
            rg.entityOutlineShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);

            rg.entityOutlineFramebuffer = rg.entityOutlineShader.getFramebufferRaw("final");
            //rg.entityOutlineFramebuffer.setFramebufferColor(1, 1, 1, 0);



            //rg.entityOutlineShader.addShader("thermal_color", rg.entityOutlineFramebuffer, mc.getFramebuffer());

            System.out.println("replaced vanilla outline shader");
        }
        catch (IOException ioexception)
        {
            System.out.println("Failed to load shader");
            rg.entityOutlineShader = null;
            rg.entityOutlineFramebuffer = null;
        }
        catch (JsonSyntaxException jsonsyntaxexception)
        {
            System.out.println("Failed to load shader: {}");
            rg.entityOutlineShader = null;
            rg.entityOutlineFramebuffer = null;
        }
    }
}
