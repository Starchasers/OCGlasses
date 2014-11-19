package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Dot3d extends Widget implements IAlpha, IScalable, IColorizable, I3DPositionable {
	float x;
	float y;
	float z;
	
	float r;
	float g;
	float b;
	
	float size = 0.2F;
	float alpha = 0.5F;
	
	public Dot3d() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Dot3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void write(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(z);
		buff.writeFloat(alpha);
	}

	@Override
	public void read(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		z = buff.readFloat();
		alpha = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x0", x);
		nbt.setFloat("y0", y);
		nbt.setFloat("z0", z);
		nbt.setFloat("alpha", alpha);
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x = nbt.getFloat("x0");
		y = nbt.getFloat("y0");
		z = nbt.getFloat("z0");
		alpha = nbt.getFloat("alpha");
		r = nbt.getFloat("r0");
		g = nbt.getFloat("g0");
		b = nbt.getFloat("b0");
		
	}

	@Override
	public WidgetType getType() {
		return WidgetType.DOT3D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderDot3d();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderDot3d implements IRenderableWidget{
		final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		@Override
		public void render() {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslated(x, y, z);
			
			GL11.glRotated(-player.rotationYaw,0,1,0);
			GL11.glRotated(player.rotationPitch,1,0,0);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(r,g,b,alpha);
			
			GL11.glVertex3f(size/2, size/2, 0);
			GL11.glVertex3f(size/2, -size/2, 0);
			GL11.glVertex3f(-size/2, -size/2, 0);
			
			GL11.glVertex3f(-size/2, size/2, 0);

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

	@Override
	public void setScale(double scale) {
		size = (float) scale;
		
	}

	@Override
	public double getScale() {
		return size;
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
	public double getPosX() {
		return x;
	}

	@Override
	public double getPosY() {
		return y;
	}

	@Override
	public double getPosZ() {
		return z;
	}

	@Override
	public void setPos(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;	
	}
}
