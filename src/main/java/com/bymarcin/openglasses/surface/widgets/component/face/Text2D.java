package com.bymarcin.openglasses.surface.widgets.component.face;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.common.TextWidget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAutoTranslateable;
import com.bymarcin.openglasses.utils.utilsClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

import com.bymarcin.openglasses.utils.Location;
public class Text2D extends TextWidget implements IAutoTranslateable {

	public Text2D() {
		super();
		this.rendertype = RenderType.GameOverlayLocated;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.TEXT2D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderText();
	}
	
	class RenderText extends RenderableGLWidget{
		@Override
		public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
			if(getText().length() < 1) return;

			updateStringDimensions();

			int currentColor = this.preRender(conditionStates);
			this.applyModifiers(conditionStates);
			this.applyAlignments();

			drawString(currentColor);

			GlStateManager.disableAlpha();
			this.postRender();
		}

		public void applyAlignments(){
			switch(this.getHorizontalAlign()) {
				case CENTER:
					GL11.glTranslatef((-stringWidth/2F), 0F, 0F);
					break;
				case RIGHT:
					GL11.glTranslatef(-stringWidth, 0F, 0F);
					break;
			}

			switch(this.getVerticalAlign()) {
				case MIDDLE:
					GL11.glTranslatef(0F, (-stringHeight/2F), 0F);
					break;
				case BOTTOM:
					GL11.glTranslatef(0F, -stringHeight, 0F);
					break;
			}
		}
	}
}
