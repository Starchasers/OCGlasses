package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IVertex;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Line3D extends Widget implements IAlpha, IColorizable, IVertex, IScalable, IThroughVisibility{
	float x[];
	float y[];
	float z[];
	float alpha = 0.5F;
	float size = 8.F;
	float r;
	float g;
	float b;
	boolean isThroughVisibility = true;
	
	public Line3D() {
		x = new float[2];
		y = new float[2];
		z = new float[2];
	}
	
	@Override
	public void write(ByteBuf buff) {
		buff.writeFloat(x[0]);
		buff.writeFloat(x[1]);
		buff.writeFloat(y[0]);
		buff.writeFloat(y[1]);
		buff.writeFloat(z[0]);
		buff.writeFloat(z[1]);
		buff.writeFloat(alpha);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeBoolean(isThroughVisibility);
		buff.writeFloat(size);
	}

	@Override
	public void read(ByteBuf buff) {
		x[0] = buff.readFloat();
		x[1] = buff.readFloat();
		y[0] = buff.readFloat();
		y[1] = buff.readFloat();
		z[0] = buff.readFloat();
		z[1] = buff.readFloat();
		alpha = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		isThroughVisibility = buff.readBoolean();
		size = buff.readFloat();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x0", x[0]);
		nbt.setFloat("x1", x[1]);
		nbt.setFloat("y0", y[0]);
		nbt.setFloat("y1", y[1]);
		nbt.setFloat("z0", z[0]);
		nbt.setFloat("z1", z[1]);
		nbt.setFloat("alpha", alpha);
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);
		nbt.setBoolean("isThroughVisibility", isThroughVisibility);
		nbt.setFloat("size", size);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x[0] = nbt.getFloat("x0");
		x[1] = nbt.getFloat("x1");
		y[0] = nbt.getFloat("y0");
		y[1] = nbt.getFloat("y1");
		z[0] = nbt.getFloat("z0");
		z[1] = nbt.getFloat("z1");
		alpha = nbt.getFloat("alpha");
		r = nbt.getFloat("r");
		g = nbt.getFloat("g");
		b = nbt.getFloat("b");
		isThroughVisibility = nbt.getBoolean("isThroughVisibility");
		size = nbt.getFloat("size");
	}

	@Override
	public WidgetType getType() {
		return WidgetType.LINE3D;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderLine3D();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderLine3D implements IRenderableWidget{

		@Override
		public void render() {
			GL11.glPushMatrix();
			if(isThroughVisibility){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}else{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glLineWidth(size);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glColor4f(0.0f,1.0f,0.0f,0.5f);
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glVertex3f(x[1], y[1], z[1]);
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.WorldLocated;
		}
		
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
	public void setColor(double d, double e, double f) {
		r = (float) d;
		g = (float) e;
		b = (float) f;
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
	public boolean isVisibleThroughObjects() {
		return isThroughVisibility;
	}

	@Override
	public void setVisibleThroughObjects(boolean visible) {
		isThroughVisibility = visible;
	}

	@Override
	public int getVertexCount() {
		return x.length;
	}

	@Override
	public void setVertex(int n, double x, double y, double z) {
		this.x[n] = (float) x;
		this.y[n] = (float) y;
		this.z[n] = (float) z;
	}

	@Override
	public void setScale(double scale) {
		size = (float) scale;	
	}

	@Override
	public double getScale() {
		return size;
	}
}
