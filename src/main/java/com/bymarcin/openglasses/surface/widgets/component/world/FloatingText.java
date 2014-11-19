package com.bymarcin.openglasses.surface.widgets.component.world;

import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FloatingText extends Widget implements I3DPositionable, ITextable{
	float x;
	float y;
	float z;
	String text ="";
	
	
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
	public void write(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(z);
		buff.writeInt(text.length());
		for(char s: text.toCharArray()){
			buff.writeChar(s);
		}
	}

	@Override
	public void read(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		z = buff.readFloat();
		StringBuilder sb = new StringBuilder();
		int size = buff.readInt();
		for(int i=0;i<size;i++){
			sb.append(buff.readChar());
		}
		
		text = sb.toString();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x", x);
		nbt.setFloat("y", y);
		nbt.setFloat("z", z);
		nbt.setString("text", text);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x = nbt.getFloat("x");
		y = nbt.getFloat("y");
		z = nbt.getFloat("z");
		text = nbt.getString("text");
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.FLOATINGTEXT;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderableFloatingText();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderableFloatingText implements IRenderableWidget{
		FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
		double offsetX = fontRender.getStringWidth(text)/2D;
		double offsetY = fontRender.FONT_HEIGHT/2D;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		@Override
		public void render() {
			GL11.glPushMatrix();
			GL11.glTranslated(x, y , z);
			GL11.glScaled(0.05, 0.05, 0.05);
			GL11.glTranslated(offsetX, offsetY,0);
			GL11.glPushMatrix();
			GL11.glRotated(180, 0, 0, 1);
			
			GL11.glTranslated(offsetX, offsetY , 0);
			
			GL11.glRotated(player.rotationYaw,0,1,0);
			GL11.glRotated(-player.rotationPitch,1,0,0);
			
			GL11.glTranslated(-offsetX, -fontRender.FONT_HEIGHT/2D , 0);
			
			fontRender.drawString(text, 0, 0, 0xFF0000);
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.WorldLocated;
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
