package com.bymarcin.openglasses.surface.widgets.component.world;

import com.bymarcin.openglasses.surface.widgets.component.common.TextWidget;
import com.bymarcin.openglasses.utils.utilsClient;
import net.minecraft.client.Minecraft;
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
		double offsetX, offsetY;

		@Override
		public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
			if(getText().length() < 1) return;

			if(getFontName().length() == 0) {
				offsetY = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2D;
				offsetX = Minecraft.getMinecraft().fontRenderer.getStringWidth(getText()) / 2D;
			}
			else {
				offsetY = getFont(getFontName()).getHeight() / 2D;
				offsetX = getFont(getFontName()).getWidth(getText()) / 2D;
			}

			int currentColor = this.preRender(conditionStates);
			this.applyModifiers(conditionStates);

			// center text on current block position
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			GL11.glScalef(scale, scale, scale);

			//align and rotate text facing the player
			GL11.glTranslated(offsetX, offsetY, 0.0D);
			GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
			GL11.glTranslated(offsetX, offsetY, 0.0D);

			this.addPlayerRotation(player);

			GL11.glTranslated(-offsetX, -offsetY, 0.0D);

			if(getFontName().equals(""))
				utilsClient.fontRenderer().drawString(getText(), 0, 0, currentColor);
			else
				getFont(getFontName()).drawString(0, 0, getText(), 1, 1, currentColor);

			GlStateManager.disableAlpha();
			this.postRender();
		}


	}


}
