package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.WidgetGLWorld;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.surface.WidgetType; 
import com.bymarcin.openglasses.utils.Location;
import net.minecraft.client.renderer.GlStateManager;

public class Text3D extends WidgetGLWorld implements ITextable{
	String text ="";
	
	public Text3D() {
		faceWidgetToPlayer = true;
	}
	
	@Override
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		ByteBufUtils.writeUTF8String(buff, text);		
	}

	@Override
	public void readData(ByteBuf buff) {
		super.readData(buff);
		text = ByteBufUtils.readUTF8String(buff);
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
		double offsetX, offsetY;
		
		@Override
		public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
			if(text.length() < 1) return;
			
			FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
			offsetY = fontRender.FONT_HEIGHT/2D;
			offsetX = fontRender.getStringWidth(text)/2D;
						
			int currentColor = this.preRender(conditionStates);
			this.applyModifiers(conditionStates);

			// center text on current block position
			GL11.glTranslatef(0.5F, 0.5F, 0.5F); 
			
			GL11.glScalef(0.1F, 0.1F, 0.1F);
			
			//align and rotate text facing the player
			GL11.glTranslated(offsetX, offsetY, 0.0D);
			GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
			GL11.glTranslated(offsetX, offsetY, 0.0D);
			
			this.addPlayerRotation(player);
			
			GL11.glTranslated(-offsetX, -offsetY, 0.0D);
			
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
		return text;
	}
}
