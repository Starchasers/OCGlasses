package com.bymarcin.openglasses.surface.widgets.component.world;

import com.bymarcin.openglasses.surface.widgets.component.common.TextWidget;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.utils.Location;
import net.minecraft.client.renderer.GlStateManager;

public class Text3D extends TextWidget {
	public Text3D() {
		super();
		faceWidgetToPlayer = true;
		valign = VAlignment.MIDDLE;
		halign = HAlignment.CENTER;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.TEXT3D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderableFloatingText();
	}

	@SideOnly(Side.CLIENT)
	class RenderableFloatingText extends RenderableGLWidget{
		final float scale = 0.1F;


		@Override
		public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
			if(getText().length() < 1) return;

			updateStringDimensions();
			updateAlignments();
			int currentColor = this.preRender(conditionStates);

			// center text on current block position
			GlStateManager.translate(0.5F, 0.5F, 0.5F);
			this.applyModifiers(conditionStates);


			if(faceWidgetToPlayer) {
				GL11.glRotated(-player.rotationYaw, 0.0D, 1.0D, 0.0D);
				GL11.glRotated(player.rotationPitch, 1.0D, 0.0D, 0.0D);
			}

			GlStateManager.scale(scale, scale, scale);

			GlStateManager.translate(-offsetX, -offsetY, 0.0D);

			GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
			drawString(currentColor);

			this.postRender();
		}
	}


}
