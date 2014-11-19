package com.bymarcin.openglasses.surface.widgets.component.face;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriangleWidget extends Widget implements IColorizable,IAlpha{
	float x1, x2, x3;
	float y1, y2, y3;
	float alpha = 1;
	float r;
	float g;
	float b;
	
	public TriangleWidget() {
	}
	
	public TriangleWidget(float x1, float x2, float x3, float y1, float y2, float y3, float r, float g, float b) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public TriangleWidget(double x1, double x2, double x3, double y1, double y2, double y3, double r, double g, double b){
		this((float) x1, (float) x2, (float) x3, (float) y1, (float) y2, (float) y3, (float) r, (float) g, (float) b);
	}
	
	
	@Override
	public void write(ByteBuf buff) {
		buff.writeFloat(x1);
		buff.writeFloat(x2);
		buff.writeFloat(x3);
		buff.writeFloat(y1);
		buff.writeFloat(y2);
		buff.writeFloat(y3);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeFloat(alpha);
	}

	@Override
	public void read(ByteBuf buff) {
		x1 = buff.readFloat();
		x2 = buff.readFloat();
		x3 = buff.readFloat();
		y1 = buff.readFloat();
		y2 = buff.readFloat();
		y3 = buff.readFloat();
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
			tessellator.addVertex(x1, y1, 0);
			tessellator.addVertex(x2, y2, 0);
			tessellator.addVertex(x3, y3, 0);
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
		nbt.setFloat("x1", x1);
		nbt.setFloat("x2", x2);
		nbt.setFloat("x3", x3);
		nbt.setFloat("y1", y1);
		nbt.setFloat("y2", y2);
		nbt.setFloat("y3", y3);
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);	
		nbt.setFloat("alpha", alpha);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x1 = nbt.getFloat("x1");
		x2 = nbt.getFloat("x2");
		x3 = nbt.getFloat("x3");
		y1 = nbt.getFloat("y1");
		y2 = nbt.getFloat("y2");
		y3 = nbt.getFloat("y3");
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
	
}
