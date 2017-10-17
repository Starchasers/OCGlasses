package com.bymarcin.openglasses.surface.widgets.component.face;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.WidgetGLOverlay;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.utils.utilsClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

import com.bymarcin.openglasses.utils.Location;
public class Text2D extends WidgetGLOverlay implements ITextable{
	String text;

	public Text2D() {
		text = "";
	}

	@Override
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		ByteBufUtils.writeUTF8String(buff, this.text);
	}

	@Override
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.setText(ByteBufUtils.readUTF8String(buff));

		setSize(0D, 0D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSize(double w, double h){
		FontRenderer fontRender = utilsClient.fontRenderer();
		super.setSize(fontRender.getStringWidth(this.getText()), fontRender.FONT_HEIGHT);
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
			FontRenderer fontRender = utilsClient.fontRenderer();

			int currentColor = this.preRender(conditionStates);
			this.applyModifiers(conditionStates);
			this.applyAlignments();

			fontRender.drawString(text, 0, 0, currentColor);
			GlStateManager.disableAlpha();
			this.postRender();
		}

		public void applyAlignments(){
			switch(this.getHorizontalAlign()) {
				case CENTER:
					GL11.glTranslatef((-width/2F), 0F, 0F);
					break;
				case RIGHT:
					GL11.glTranslatef(-width, 0F, 0F);
					break;
			}

			switch(this.getVerticalAlign()) {
				case MIDDLE:
					GL11.glTranslatef(0F, (-height/2F), 0F);
					break;
				case BOTTOM:
					GL11.glTranslatef(0F, -height, 0F);
					break;
			}
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
