package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Quad3D extends Triangle3D{
	
	public Quad3D() {
		x = new float[4];
		y = new float[4];
		z = new float[4];
	}

	@Override
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(x[3]);
		buff.writeFloat(y[3]);
		buff.writeFloat(z[3]);
	}

	@Override
	public void readData(ByteBuf buff) {
		super.readData(buff);
		x[3] = buff.readFloat();
		y[3] = buff.readFloat();
		z[3] = buff.readFloat();
	}

	@Override
	public WidgetType getType() {
		return WidgetType.QUAD3D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderQuad3D();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderQuad3D implements IRenderableWidget{

		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			GL11.glPushMatrix();
			
			if(isThroughVisibility){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}else{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUAD_STRIP);
			GL11.glColor4f(r,g,b,alpha);
			
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glVertex3f(x[1], y[1], z[1]);
			GL11.glVertex3f(x[2], y[2], z[2]);
			GL11.glVertex3f(x[3], y[3], z[3]);			
			GL11.glVertex3f(x[0], y[0], z[0]);
			GL11.glVertex3f(x[1], y[1], z[1]);
			
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

}
