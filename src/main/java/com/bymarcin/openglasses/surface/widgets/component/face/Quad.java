package com.bymarcin.openglasses.surface.widgets.component.face;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;


public class Quad extends TriangleWidget {

	public Quad() {
		x = new float[4];
		y = new float[4];
	}

	@Override
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(x[3]);
		buff.writeFloat(y[3]);
	}
	
	@Override
	public void readData(ByteBuf buff) {
		super.readData(buff);
		x[3] = buff.readFloat();
		y[3] = buff.readFloat();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderQuad();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderQuad implements IRenderableWidget{

		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(r, g, b, alpha);
			GL11.glVertex3f(x[0], y[0], 0);
			GL11.glVertex3f(x[1], y[1], 0);
			GL11.glVertex3f(x[2], y[2], 0);
			GL11.glVertex3f(x[3], y[3], 0);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.GameOverlayLocated;
		}

		@Override
		public boolean shouldWidgetBeRendered() {
			return isVisible();
		}
		
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.QUAD;
	}
}
