package com.bymarcin.openglasses.surface.widgets.component.face;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.utils.OGUtils;

public class Text extends Dot implements ITextable{
	String text="";
	
	public Text() {}

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
		return WidgetType.TEXT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderText();
	}
	
	class RenderText implements IRenderableWidget{
		int color = OGUtils.getIntFromColor(r, g, b, alpha);
		FontRenderer fontRender = Minecraft.getMinecraft().fontRendererObj;
		
		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			GL11.glPushMatrix();
			GL11.glScaled(size, size, 0);
			fontRender.drawString(text, (int) x, (int) y, color);
			GL11.glPopMatrix();
			
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.GameOverlayLocated;
		}

		@Override
		public boolean shouldWidgetBeRendered() {
			return isVisible();
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
