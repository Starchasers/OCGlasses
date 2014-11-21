package com.bymarcin.openglasses.surface.widgets.component.face;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I2DVertex;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriangleWidget extends Widget implements IColorizable, IAlpha, I2DVertex{
	float x[];
	float y[];
	
	float alpha = 1;
	float r;
	float g;
	float b;
	
	public TriangleWidget() {
		x = new float[3];
		y = new float[3];
	}
	
	@Override
	public void write(ByteBuf buff) {
		buff.writeFloat(x[0]);
		buff.writeFloat(x[1]);
		buff.writeFloat(x[2]);
		buff.writeFloat(y[0]);
		buff.writeFloat(y[1]);
		buff.writeFloat(y[2]);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeFloat(alpha);
	}

	@Override
	public void read(ByteBuf buff) {
		x[0] = buff.readFloat();
		x[1] = buff.readFloat();
		x[2] = buff.readFloat();
		y[0] = buff.readFloat();
		y[1] = buff.readFloat();
		y[2] = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		alpha = buff.readFloat();
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.TRIANGLE;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderableSquareWidget();
	}
	
	@SideOnly(Side.CLIENT)
	public class RenderableSquareWidget implements IRenderableWidget{
		@Override
		public void render() {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawing(GL11.GL_TRIANGLES);
			tessellator.setColorRGBA_F(r, g, b, alpha);
			tessellator.addVertex(x[0], y[0], 0);
			tessellator.addVertex(x[1], y[1], 0);
			tessellator.addVertex(x[2], y[2], 0);
			tessellator.draw();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.GameOverlayLocated;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x0", x[0]);
		nbt.setFloat("x1", x[1]);
		nbt.setFloat("x2", x[2]);
		nbt.setFloat("y0", y[0]);
		nbt.setFloat("y1", y[1]);
		nbt.setFloat("y2", y[2]);
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);	
		nbt.setFloat("alpha", alpha);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x[0] = nbt.getFloat("x0");
		x[1] = nbt.getFloat("x1");
		x[2] = nbt.getFloat("x2");
		y[0] = nbt.getFloat("y0");
		y[1] = nbt.getFloat("y1");
		y[2] = nbt.getFloat("y2");
		r = nbt.getFloat("r");
		g = nbt.getFloat("g");
		b = nbt.getFloat("b");
		alpha = nbt.getFloat("alpha");
	}

	@Override
	public void setColor(double r, double g, double b) {
		this.r = (float) r;
		this.g = (float) g;
		this.b = (float) b;
	}

	@Override
	public float getColorR() {
		return r;
	}

	@Override
	public float getColorG() {
		return g;
	}

	@Override
	public float getColorB() {
		return b;
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

	@Override
	public void setAlpha(double alpha) {
		this.alpha = (float) alpha;
	}

	@Override
	public int getVertexCount() {
		return x.length;
	}

	@Override
	public void setVertex(int n, double x, double y) {
		this.x[n] = (float) x;
		this.y[n] = (float) y;
	}
	
}
