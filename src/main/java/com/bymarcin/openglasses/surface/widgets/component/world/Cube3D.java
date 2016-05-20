package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ILookable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IViewDistance;
import com.bymarcin.openglasses.utils.OGUtils;


public class Cube3D extends Widget implements I3DPositionable, IAlpha, IThroughVisibility, IColorizable, IViewDistance, ILookable, IScalable{

	float x;
	float y;
	float z;
	float scale = 1f;
	
	boolean isThroughVisibility;
	boolean isLookingAtEnable;
	
	int lookAtX;
	int lookAtY;
	int lookAtZ;
	
	float r;
	float g;
	float b;
	
	int distance = 100;
	float alpha = 0.5f;
	
	public Cube3D() {}
	
	@Override
	public void writeData(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(z);
		buff.writeFloat(alpha);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeBoolean(isThroughVisibility);
		buff.writeInt(distance);
		buff.writeInt(lookAtX);
		buff.writeInt(lookAtY);
		buff.writeInt(lookAtZ);
		buff.writeBoolean(isLookingAtEnable);
		buff.writeFloat(scale);
	}

	@Override
	public void readData(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		z = buff.readFloat();
		alpha = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		isThroughVisibility = buff.readBoolean();
		distance = buff.readInt();
		lookAtX = buff.readInt();
		lookAtY = buff.readInt();
		lookAtZ = buff.readInt();
		isLookingAtEnable = buff.readBoolean();
		scale = buff.readFloat();
	}

	@Override
	public WidgetType getType() {
		return WidgetType.CUBE3D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderCube3D();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderCube3D implements IRenderableWidget{
		float tr = (1-scale)/2f;
		
		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			if(OGUtils.inRange(playerX, playerY, playerZ, x, y, z, distance)){
				RayTraceResult pos = ClientSurface.getBlockCoordsLookingAt(player);
				if(isLookingAtEnable && (pos == null || pos.getBlockPos().getX() != lookAtX || pos.getBlockPos().getY() != lookAtY || pos.getBlockPos().getZ() != lookAtZ) )
						return;
				drawQuad(x, y, z, alpha);	
				
			}
		}
		
		public void drawQuad(float posX, float posY, float PosZ, float alpha){
			GL11.glTranslated(posX, posY, PosZ);
			if(isThroughVisibility){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}else{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			GL11.glPushMatrix();
			GL11.glTranslatef(tr,tr,tr);
			GL11.glScalef(scale, scale, scale);
			GL11.glBegin(GL11.GL_QUADS);        // Draw The Cube Using quads
			 	GL11.glColor4f(r,g,b,alpha);    // Color Blue
			 	GL11.glVertex3f( 1.0f, 1.0f,0f);    // Top Right Of The Quad (Top)
			 	GL11.glVertex3f(0f, 1.0f,0f);    // Top Left Of The Quad (Top)
			 	GL11.glVertex3f(0f, 1.0f, 1.0f);    // Bottom Left Of The Quad (Top)
			 	GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Bottom Right Of The Quad (Top)

			 	GL11.glVertex3f( 1.0f,0f, 1.0f);    // Top Right Of The Quad (Bottom)
			 	GL11.glVertex3f(0f,0f, 1.0f);    // Top Left Of The Quad (Bottom)
			 	GL11.glVertex3f(0f,0f,0f);    // Bottom Left Of The Quad (Bottom)
			 	GL11.glVertex3f( 1.0f,0f,0f);    // Bottom Right Of The Quad (Bottom)
  
			 	GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Top Right Of The Quad (Front)
			 	GL11.glVertex3f(0f, 1.0f, 1.0f);    // Top Left Of The Quad (Front)
			 	GL11.glVertex3f(0f,0f, 1.0f);    // Bottom Left Of The Quad (Front)
			 	GL11.glVertex3f( 1.0f,0f, 1.0f);    // Bottom Right Of The Quad (Front)

			    GL11.glVertex3f( 1.0f,0f,0f);    // Top Right Of The Quad (Back)
			    GL11.glVertex3f(0f,0f,0f);    // Top Left Of The Quad (Back)
			    GL11.glVertex3f(0f, 1.0f,0f);    // Bottom Left Of The Quad (Back)
			    GL11.glVertex3f( 1.0f, 1.0f,0f);    // Bottom Right Of The Quad (Back)

			    GL11.glVertex3f(0f, 1.0f, 1.0f);    // Top Right Of The Quad (Left)
			    GL11.glVertex3f(0f, 1.0f,0f);    // Top Left Of The Quad (Left)
			    GL11.glVertex3f(0f,0f,0f);    // Bottom Left Of The Quad (Left)
			    GL11.glVertex3f(0f,0f, 1.0f);    // Bottom Right Of The Quad (Left)

			    GL11.glVertex3f( 1.0f, 1.0f,0f);    // Top Right Of The Quad (Right)
			    GL11.glVertex3f( 1.0f, 1.0f, 1.0f);    // Top Left Of The Quad (Right)
			    GL11.glVertex3f( 1.0f,0f, 1.0f);    // Bottom Left Of The Quad (Right)
			    GL11.glVertex3f( 1.0f,0f,0f);    // Bottom Right Of The Quad (Right)
		    GL11.glEnd();            // End Drawing The Cube
		    GL11.glPopMatrix();
		    GL11.glEnable(GL11.GL_TEXTURE_2D);
		    GL11.glTranslated(-posX, -posY, -PosZ);
		    
		    GL11.glEnable(GL11.GL_DEPTH_TEST);
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
	public int getDistanceView() {
		return distance;
	}

	@Override
	public void setDistanceView(int distance) {
		this.distance = distance;
	}

	@Override
	public void setLookingAt(int x, int y, int z) {
		lookAtX = x;
		lookAtY = y;
		lookAtZ = z;
	}

	@Override
	public boolean isLookingAtEnable() {
		return isLookingAtEnable;
	}

	@Override
	public void setLookingAtEnable(boolean enable) {
		isLookingAtEnable = enable;
	}

	@Override
	public int getLookingAtX() {
		return lookAtX;
	}

	@Override
	public int getLookingAtY() {
		return lookAtY;
	}

	@Override
	public int getLookingAtZ() {
		return lookAtZ;
	}

	@Override
	public void setScale(double scale) {
		this.scale = (float) scale;	
	}

	@Override
	public double getScale() {
		return scale;
	}

}
