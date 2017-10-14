package com.bymarcin.openglasses.surface.widgets.component.face;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.WidgetGLOverlay;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

import com.bymarcin.openglasses.utils.Location;
public class Text2D extends WidgetGLOverlay implements ITextable{
	String text="";

	public Text2D() {}

	@Override
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		ByteBufUtils.writeUTF8String(buff, this.text);
	}

	@Override
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.text = ByteBufUtils.readUTF8String(buff);
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
			FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;

			int currentColor = this.preRender(conditionStates);
			this.applyModifiers(conditionStates);

			float offsetX = 0, offsetY = 0;
			switch(this.getHorizontalAlign()) {
				case CENTER:
					offsetX = (float) -fontRender.getStringWidth(text);
					offsetX/= 2F;
					break;

				case RIGHT:
					offsetX = (float) -fontRender.getStringWidth(text);
					break;
			}

			switch(this.getVerticalAlign()) {
				case MIDDLE:
					offsetY = (float) -fontRender.FONT_HEIGHT;
					offsetY/= 2F;
					break;
				case BOTTOM:
					offsetY = (float) -fontRender.FONT_HEIGHT;
					break;
			}

			GL11.glTranslatef(offsetX, offsetY, 0F);

			fontRender.drawString(text, 0, 0, currentColor);
			GlStateManager.disableAlpha();
			this.postRender();
		}
	}

	@Override
	public void setText(String text) {
		this.text = text;	
	}

	@Override
	public String getText() {
		return this.text;
	}
}
