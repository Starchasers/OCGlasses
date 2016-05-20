package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DVertex;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;

public class Triangle3D extends Widget implements IAlpha, IColorizable, IThroughVisibility, I3DVertex {
	float x[];
	float y[];
	float z[];
	float alpha = 0.5f;
	float r;
	float g;
	float b;
	boolean isThroughVisibility = true;
	
	public Triangle3D() {
		x = new float[3];
		y = new float[3];
		z = new float[3];
	}
	
	@Override
	public void writeData(ByteBuf buff) {
		buff.writeFloat(x[0]);
		buff.writeFloat(x[1]);
		buff.writeFloat(x[2]);
		buff.writeFloat(y[0]);
		buff.writeFloat(y[1]);
		buff.writeFloat(y[2]);
		buff.writeFloat(z[0]);
		buff.writeFloat(z[1]);
		buff.writeFloat(z[2]);
		buff.writeFloat(alpha);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeBoolean(isThroughVisibility);
	}

	@Override
	public void readData(ByteBuf buff) {
		x[0] = buff.readFloat();
		x[1] = buff.readFloat();
		x[2] = buff.readFloat();
		y[0] = buff.readFloat();
		y[1] = buff.readFloat();
		y[2] = buff.readFloat();
		z[0] = buff.readFloat();
		z[1] = buff.readFloat();
		z[2] = buff.readFloat();
		alpha = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		isThroughVisibility = buff.readBoolean();
	}

	@Override
	public WidgetType getType() {
		return WidgetType.TRIANGLE3D;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderTriangle3D();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderTriangle3D implements IRenderableWidget{

		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			GL11.glPushMatrix();
			if(isThroughVisibility){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}else{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glColor4f(r, g, b, alpha);
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glVertex3f(x[1], y[1], z[1]);
			GL11.glVertex3f(x[2], y[2], z[2]);
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.WorldLocated;
		}
		
		@Override
		public boolean shouldWidgetBeRendered() {
			return isVisible();
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
}
