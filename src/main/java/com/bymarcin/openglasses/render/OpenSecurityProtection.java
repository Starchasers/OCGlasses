package com.bymarcin.openglasses.render;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import pcl.opensecurity.client.models.ModelCube;
import pcl.opensecurity.common.protection.Protection;
import pcl.opensecurity.common.protection.ProtectionAreaChunk;

import java.util.ArrayList;
import java.util.Map;

public class OpenSecurityProtection {
    public static void renderOpenSecurityProtections(float partialTicks){
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        OCClientSurface.preRender(RenderType.WorldLocated, partialTicks);
        Protection handler = Protection.get(Minecraft.getMinecraft().player.world);

        //GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        for(Map.Entry<ChunkPos, ArrayList<ProtectionAreaChunk>> e : handler.getAllAreas().entrySet()){
            for(ProtectionAreaChunk area : e.getValue()){
                AxisAlignedBB aabb = area.getArea();
                BlockPos controller = area.getControllerPosition();

                GlStateManager.color(1, 0, 1, 0.2f);
                ModelCube.drawCube(controller.getX()-0.001f, controller.getY()-0.001f, controller.getZ()-0.001f, controller.getX() + 1.001f, controller.getY() + 1.001f, controller.getZ() + 1.001f);
                GlStateManager.color(1, 0, 0, 0.5f);
                ModelCube.drawCube((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
            }
        }

        OCClientSurface.postRender(RenderType.WorldLocated);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
