package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IViewDistance;
import com.bymarcin.openglasses.utils.OGUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Dot3D extends Widget implements IAlpha, IScalable, IColorizable, I3DPositionable, IThroughVisibility, IViewDistance {
	float x;
	float y;
	float z;
	
	int distance = 100;
	float r;
	float g;
	float b;
	boolean isThroughVisibility = true;
	
	float size = 0.2F;
	float alpha = 0.5F;
	
	public Dot3D() {}
	
	@Override
	public void write(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(z);
		buff.writeFloat(alpha);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeBoolean(isThroughVisibility);
		buff.writeInt(distance);
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
		isThroughVisibility = buff.readBoolean();
		distance = buff.readInt();
	}

	@Override
	public WidgetType getType() {
		return WidgetType.DOT3D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderDot3D();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderDot3D implements IRenderableWidget{
		final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			
			if(!OGUtils.inRange(playerX, playerY, playerZ, x, y, z, distance)){
				return;
			}
			
			GL11.glPushMatrix();
			if(isThroughVisibility){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}else{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
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

	@Override
	public boolean isVisibleThroughObjects() {
		return isThroughVisibility;
	}

	@Override
	public void setVisibleThroughObjects(boolean visible) {
		isThroughVisibility = visible;
	}

	@Override
	public int getDistanceView() {
		return distance;
	}

	@Override
	public void setDistanceView(int distance) {
		this.distance = distance;	
	}
}
