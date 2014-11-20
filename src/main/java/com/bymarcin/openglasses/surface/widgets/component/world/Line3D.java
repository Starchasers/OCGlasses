package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Line3D extends Widget implements IAlpha{
	float x[];
	float y[];
	float z[];
	float alpha = 0.5F;
	float size = 8.F;
	
	public Line3D() {
		x = new float[2];
		y = new float[2];
		z = new float[2];
	}
	
	public Line3D(float x[], float y[], float z[]) {
		this.x = x;
		this.y = y;
		this.z = z;
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
			//System.out.printf("%f;%f,%f\n",x[0],y[0],z[0]);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glLineWidth(size);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glColor4f(0.0f,1.0f,0.0f,0.5f);
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glVertex3f(x[1], y[1], z[1]);
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
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
}
