package com.bymarcin.openglasses.surface.widgets.component.common;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetGLWorld;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IItem;
import com.bymarcin.openglasses.utils.Location;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class ItemIcon extends WidgetGLWorld implements IItem {
    public ItemStack itmStack = null;
    public IBakedModel ibakedmodel = null;
    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        int itemid = 0;
        if(itmStack != null)
            itemid = Item.getIdFromItem(itmStack.getItem());

        buff.writeInt(itemid);
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        int itemid = buff.readInt();
        if(itemid > 0)
            setItem(new ItemStack(new Item().getItemById(itemid)));
    }

    @Override
    public void setItem(ItemStack newItem) {
        if(newItem == null)	return;
        this.itmStack = newItem;
        if(Minecraft.getMinecraft().world.isRemote) {
            this.ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(this.itmStack);
        }
    }

    @Override
    public void setItem(String newItem, int meta) {
        setItem(new ItemStack(new Item().getByNameOrId(newItem), 1, meta));
    }

    @Override
    public void setItem(String newItem) {
        setItem(new ItemStack(new Item().getByNameOrId(newItem)));
    }

    @Override
    public Item getItem() {
        return this.itmStack.getItem();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableItemIcon();
    }

    private class RenderableItemIcon extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
            if(ibakedmodel == null) return;

            Minecraft mc = Minecraft.getMinecraft();
            TextureManager tm = mc.getTextureManager();

            tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tm.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

            int alphaColor = this.preRender(conditionStates);
            this.applyModifiers(conditionStates);

            if(rendertype == RenderType.WorldLocated) {
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
                this.addPlayerRotation(player);
                GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            }

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
            EnumFacing[] var6 = EnumFacing.values();

            for (int var8 = 0, var7 = var6.length; var8 < var7; ++var8)
                renderQuads(vertexbuffer, ibakedmodel.getQuads(null, var6[var8], 0L), alphaColor);

            renderQuads(vertexbuffer, ibakedmodel.getQuads(null, null, 0L), alphaColor);
            tessellator.draw();
            this.postRender();
        }

        private  void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color) {
            for (int j = quads.size(), i = 0; i < j; ++i)
                LightUtil.renderQuadColor(renderer, quads.get(i), color);
        }
    }

}


