package com.bymarcin.openglasses.surface.widgets.component.world;

import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Cube3d extends Widget implements I3DPositionable, IAlpha{

	float x;
	float y;
	float z;
	float alpha = 0.5f;
	
	public Cube3d() {}
	
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x", x);
		nbt.setFloat("y", y);
		nbt.setFloat("z", z);
		nbt.setFloat("alpha", alpha);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x = nbt.getFloat("x");
		y = nbt.getFloat("y");
		z = nbt.getFloat("z");
		alpha = nbt.getFloat("alpha");
	}

	@Override
	public WidgetType getType() {
		return WidgetType.CUBE3D;
	}

	@Override
	public IRenderableWidget getRenderable() {
		return new RenderCube3d();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderCube3d implements IRenderableWidget{

		@Override
		public void render() {
			drawQuad(x, y, z, alpha);	
		}
		
		public void drawQuad(float posX, float posY, float PosZ, float alpha){
			GL11.glTranslated(posX, posY, PosZ);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);        // Draw The Cube Using quads
			 	GL11.glColor4f(0.0f,1.0f,0.0f,alpha);    // Color Blue
			 	GL11.glVertex3f( 1.0f, 1.0f,0f);    // Top Right Of The Quad (Top)
			 	GL11.glVertex3f(0f, 1.0f,0f);    // Top Left Of The Quad (Top)
			 	GL11.glVertex3f(0f, 1.0f, 1.0f);    // Bottom Left Of The Quad (Top)
			 	GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Bottom Right Of The Quad (Top)
			 	GL11.glColor4f(1.0f,0.5f,0.0f,alpha);    // Color Orange
			 	GL11.glVertex3f( 1.0f,0f, 1.0f);    // Top Right Of The Quad (Bottom)
			 	GL11.glVertex3f(0f,0f, 1.0f);    // Top Left Of The Quad (Bottom)
			 	GL11.glVertex3f(0f,0f,0f);    // Bottom Left Of The Quad (Bottom)
			 	GL11.glVertex3f( 1.0f,0f,0f);    // Bottom Right Of The Quad (Bottom)
			 	GL11.glColor4f(1.0f,0.0f,0.0f,alpha);    // Color Red    
			 	GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Top Right Of The Quad (Front)
			 	GL11.glVertex3f(0f, 1.0f, 1.0f);    // Top Left Of The Quad (Front)
			 	GL11.glVertex3f(0f,0f, 1.0f);    // Bottom Left Of The Quad (Front)
			 	GL11.glVertex3f( 1.0f,0f, 1.0f);    // Bottom Right Of The Quad (Front)
			 	GL11.glColor4f(1.0f,1.0f,0.0f,alpha);    // Color Yellow
			    GL11.glVertex3f( 1.0f,0f,0f);    // Top Right Of The Quad (Back)
			    GL11.glVertex3f(0f,0f,0f);    // Top Left Of The Quad (Back)
			    GL11.glVertex3f(0f, 1.0f,0f);    // Bottom Left Of The Quad (Back)
			    GL11.glVertex3f( 1.0f, 1.0f,0f);    // Bottom Right Of The Quad (Back)
			    GL11.glColor4f(0.0f,0.0f,1.0f,alpha);    // Color Blue
			    GL11.glVertex3f(0f, 1.0f, 1.0f);    // Top Right Of The Quad (Left)
			    GL11.glVertex3f(0f, 1.0f,0f);    // Top Left Of The Quad (Left)
			    GL11.glVertex3f(0f,0f,0f);    // Bottom Left Of The Quad (Left)
			    GL11.glVertex3f(0f,0f, 1.0f);    // Bottom Right Of The Quad (Left)
			    GL11.glColor4f(1.0f,0.0f,1.0f,alpha);    // Color Violet
			    GL11.glVertex3f( 1.0f, 1.0f,0f);    // Top Right Of The Quad (Right)
			    GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Top Left Of The Quad (Right)
			    GL11.glVertex3f( 1.0f,0f, 1.0f);    // Bottom Left Of The Quad (Right)
			    GL11.glVertex3f( 1.0f,0f,0f);    // Bottom Right Of The Quad (Right)
		    GL11.glEnd();            // End Drawing The Cube
		    GL11.glEnable(GL11.GL_TEXTURE_2D);
		    GL11.glTranslated(-posX, -posY, -PosZ);
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
