package com.bymarcin.openglasses.integration.opensecurity;

import ben_mkiv.rendertoolkit.client.ModelCube;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.lwjgl.opengl.GL11;
import pcl.opensecurity.common.protection.Protection;
import pcl.opensecurity.common.protection.ProtectionAreaChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProtectionRenderer {
    public static void renderOpenSecurityProtections(float partialTicks){
        if(OCClientSurface.glasses.energyStored == 0)
            return;

        GlStateManager.pushMatrix();
        OCClientSurface.preRender(RenderType.WorldLocated, partialTicks);
        Protection handler = Protection.get(Minecraft.getMinecraft().player.world);

        //GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableCull();
        GlStateManager.enableBlend();

        EntityPlayer player = Minecraft.getMinecraft().player;
        HashMap<BlockPos, AxisAlignedBB> areas = new HashMap<>();

        // merge chunk areas which belong to the same controller
        for(Map.Entry<ChunkPos, ArrayList<ProtectionAreaChunk>> e : handler.getAllAreas().entrySet()){
            for(ProtectionAreaChunk area : e.getValue()) {
                double distance = area.getControllerPosition().distanceSq(player.getPosition());

                if(distance/distance > 64)
                    continue;

                if(!areas.containsKey(area.getControllerPosition()))
                    areas.put(area.getControllerPosition(), area.getArea());
                else {
                    AxisAlignedBB newAABB = areas.get(area.getControllerPosition()).union(area.getArea());
                    areas.replace(area.getControllerPosition(), newAABB);
                }
            }
        }




        for(Map.Entry<BlockPos, AxisAlignedBB> e : areas.entrySet()){
            ModelCube controller = new ModelCube(new AxisAlignedBB(e.getKey(), e.getKey().add(1, 1, 1)), 0.01f);
            ModelCube area = new ModelCube(e.getValue(), 0.01f);


            GlStateManager.glLineWidth(5);

            GlStateManager.disableDepth();
            GlStateManager.color(1, 0, 1, 0.2f);
            controller.drawCube();
            GlStateManager.color(1, 0, 1, 0.5f);
            controller.drawCube(GL11.GL_LINE_LOOP);
            GlStateManager.enableDepth();

            GlStateManager.color(1, 0, 0, 0.2f);
            area.drawCube();
            GlStateManager.color(1, 0, 0, 0.5f);
            area.drawCube(GL11.GL_LINE_LOOP);
        }

        GlStateManager.enableCull();

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
        OCClientSurface.postRender(RenderType.WorldLocated);

        GlStateManager.enableDepth();
    }

}
