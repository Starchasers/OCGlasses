package com.bymarcin.ocglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.ocglasses.surface.IRenderableWidget;
import com.bymarcin.ocglasses.surface.RenderType;
import com.bymarcin.ocglasses.surface.Widget;
import com.bymarcin.ocglasses.surface.WidgetType;
import com.bymarcin.ocglasses.surface.widgets.core.attribute.IAlpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Triangle3d extends Widget implements IAlpha {
	float x[];
	float y[];
	float z[];
	float alpha = 0.5f;
	
	public Triangle3d() {
		x = new float[3];
		y = new float[3];
		z = new float[3];
	}
	
	public Triangle3d(float x[], float y[], float z[]) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void write(ByteBuf buff) {
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
	}

	@Override
	public void read(ByteBuf buff) {
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x1", x[0]);
		nbt.setFloat("x2", x[1]);
		nbt.setFloat("x3", x[2]);
		nbt.setFloat("y1", x[0]);
		nbt.setFloat("y2", x[1]);
		nbt.setFloat("y3", x[2]);
		nbt.setFloat("y1", x[0]);
		nbt.setFloat("y2", x[1]);
		nbt.setFloat("y3", x[2]);
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
		z[0] = nbt.getFloat("z0");
		z[1] = nbt.getFloat("z1");
		z[2] = nbt.getFloat("z2");
		alpha = nbt.getFloat("alpha");
	}

	@Override
	public WidgetType getType() {
		return WidgetType.TRIANGLE3D;
	}

	@Override
	public IRenderableWidget getRenderable() {
		return new RenderTriangle3d();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderTriangle3d implements IRenderableWidget{

		@Override
		public void render() {
			//System.out.printf("%f;%f,%f\n",x[0],y[0],z[0]);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glColor4f(0.0f,1.0f,0.0f,0.5f);
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glVertex3f(x[1], y[1], z[1]);
			GL11.glVertex3f(x[2], y[2], z[2]);
			
			//GL11.glVertex3f(x[2], y[2], z[2]);
			//GL11.glVertex3f(x[1], y[1], z[1]);
			//GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
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
