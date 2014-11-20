package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.utils.OGUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FloatingText extends Widget implements I3DPositionable, ITextable, IColorizable, IScalable, IAlpha{
	float x;
	float y;
	float z;
	
	float r;
	float g;
	float b;
	float alpha = 1f;
	
	float scale = 0.05f;
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
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeFloat(scale);
		buff.writeFloat(alpha);
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
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		scale = buff.readFloat();	
		alpha = buff.readFloat();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x", x);
		nbt.setFloat("y", y);
		nbt.setFloat("z", z);
		nbt.setString("text", text);
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);
		nbt.setFloat("scale", scale);
		nbt.setFloat("alpha", alpha);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x = nbt.getFloat("x");
		y = nbt.getFloat("y");
		z = nbt.getFloat("z");
		text = nbt.getString("text");
		r = nbt.getFloat("r");
		g = nbt.getFloat("g");
		b = nbt.getFloat("b");
		scale = nbt.getFloat("scale");
		alpha = nbt.getFloat("alpha");
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
		final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		int color = OGUtils.getIntFromColor(r, g, b, alpha);
		
		@Override
		public void render() {
			GL11.glPushMatrix();
			GL11.glTranslated(x, y , z);
			GL11.glScaled(scale, scale, scale);
			GL11.glTranslated(offsetX, offsetY,0);
			GL11.glPushMatrix();
			GL11.glRotated(180, 0, 0, 1);
			
			GL11.glTranslated(offsetX, offsetY , 0);
			
			GL11.glRotated(player.rotationYaw,0,1,0);
			GL11.glRotated(-player.rotationPitch,1,0,0);
			
			GL11.glTranslated(-offsetX, -fontRender.FONT_HEIGHT/2D , 0);
			
			fontRender.drawString(text, 0, 0, color);
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
	@Override
	public void setColor(double d, double e, double f) {
		r = (float) d;
		g = (float) e;
		b = (float) f;
	}
	
	@Override
	public float getColorR() {
		return r;
	}
	
	@Override
	public float getColorG() {
		return g;
	}
	
	@Override
	public float getColorB() {
		return b;
	}
	
	@Override
	public void setScale(double scale) {
		this.scale = (float) scale;
	}
	
	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

	@Override
	public void setAlpha(double alpha) {
		this.alpha = (float) alpha;
	}
	
}
