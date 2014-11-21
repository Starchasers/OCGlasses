package com.bymarcin.openglasses.surface.widgets.component.face;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.utils.OGUtils;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Text extends Dot implements ITextable{
	String text;
	
	public Text() {}

	@Override
	public void write(ByteBuf buff) {
		super.write(buff);
		ByteBufUtils.writeUTF8String(buff, text);
	}

	@Override
	public void read(ByteBuf buff) {
		super.read(buff);
		text = ByteBufUtils.readUTF8String(buff);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("text", text);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		text = nbt.getString("text");
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
		FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
		
		@Override
		public void render() {
			GL11.glPushMatrix();
			GL11.glScaled(size, size, 0);
			fontRender.drawString(text, (int) x, (int) y, color);
			GL11.glPopMatrix();
			
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.GameOverlayLocated;
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
