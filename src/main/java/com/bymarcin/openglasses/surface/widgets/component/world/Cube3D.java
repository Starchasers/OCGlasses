package com.bymarcin.openglasses.surface.widgets.component.world;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.WidgetGLWorld;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.utils.Location;

public class Cube3D extends WidgetGLWorld {
	public Cube3D() {}
	
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
	class RenderCube3D extends RenderableGLWidget{
		@Override
		public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
			this.preRender(conditionStates);

			this.applyModifiers(conditionStates);
						
			GlStateManager.glBegin(GL11.GL_QUADS);    // Draw The Cube Using quads			    
			GlStateManager.glVertex3f(1.0f,1.0f,0.0f);    // Top Right Of The Quad (Top)
			GlStateManager.glVertex3f(0.0f,1.0f,0.0f);    // Top Left Of The Quad (Top)
			GlStateManager.glVertex3f(0.0f,1.0f,1.0f);    // Bottom Left Of The Quad (Top)
			GlStateManager.glVertex3f(1.0f,1.0f,1.0f);    // Bottom Right Of The Quad (Top)

			GlStateManager.glVertex3f(1.0f,0.0f,1.0f);    // Top Right Of The Quad (Bottom)
			GlStateManager.glVertex3f(0.0f,0.0f,1.0f);    // Top Left Of The Quad (Bottom)
			GlStateManager.glVertex3f(0.0f,0.0f,0.0f);    // Bottom Left Of The Quad (Bottom)
			GlStateManager.glVertex3f(1.0f,0.0f,0.0f);    // Bottom Right Of The Quad (Bottom)
  
			GlStateManager.glVertex3f(1.0f,1.0f,1.0f);    // Top Right Of The Quad (Front)
			GlStateManager.glVertex3f(0.0f,1.0f,1.0f);    // Top Left Of The Quad (Front)
			GlStateManager.glVertex3f(0.0f,0.0f,1.0f);    // Bottom Left Of The Quad (Front)
			GlStateManager.glVertex3f(1.0f,0.0f,1.0f);    // Bottom Right Of The Quad (Front)

			GlStateManager.glVertex3f(1.0f,0.0f,0.0f);    // Top Right Of The Quad (Back)
			GlStateManager.glVertex3f(0.0f,0.0f,0.0f);    // Top Left Of The Quad (Back)
			GlStateManager.glVertex3f(0.0f,1.0f,0.0f);    // Bottom Left Of The Quad (Back)
			GlStateManager.glVertex3f(1.0f,1.0f,0.0f);    // Bottom Right Of The Quad (Back)

			GlStateManager.glVertex3f(0.0f,1.0f,1.0f);    // Top Right Of The Quad (Left)
			GlStateManager.glVertex3f(0.0f,1.0f,0.0f);    // Top Left Of The Quad (Left)
			GlStateManager.glVertex3f(0.0f,0.0f,0.0f);    // Bottom Left Of The Quad (Left)
			GlStateManager.glVertex3f(0.0f,0.0f,1.0f);    // Bottom Right Of The Quad (Left)

			GlStateManager.glVertex3f(1.0f,1.0f,0.0f);    // Top Right Of The Quad (Right)
			GlStateManager.glVertex3f(1.0f,1.0f,1.0f);    // Top Left Of The Quad (Right)
			GlStateManager.glVertex3f(1.0f,0.0f,1.0f);    // Bottom Left Of The Quad (Right)
			GlStateManager.glVertex3f(1.0f,0.0f,0.0f);    // Bottom Right Of The Quad (Right)		
			GlStateManager.glEnd();            // End Drawing The Cube
						
		    this.postRender();
		}
	}
}
