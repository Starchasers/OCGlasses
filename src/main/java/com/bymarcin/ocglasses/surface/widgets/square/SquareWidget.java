package com.bymarcin.ocglasses.surface.widgets.square;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;

import com.bymarcin.ocglasses.lua.LuaObjectBuilder;
import com.bymarcin.ocglasses.surface.IRenderableWidget;
import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.Widgets;
import com.bymarcin.ocglasses.surface.widgets.atribute.IAlpha;
import com.bymarcin.ocglasses.surface.widgets.atribute.IColorizable;
import com.bymarcin.ocglasses.surface.widgets.atribute.IPositionable;
import com.bymarcin.ocglasses.surface.widgets.atribute.IResizable;
import com.bymarcin.ocglasses.surface.widgets.luafunction.GetAlpha;
import com.bymarcin.ocglasses.surface.widgets.luafunction.GetColor;
import com.bymarcin.ocglasses.surface.widgets.luafunction.GetPosition;
import com.bymarcin.ocglasses.surface.widgets.luafunction.GetSize;
import com.bymarcin.ocglasses.surface.widgets.luafunction.SetAlpha;
import com.bymarcin.ocglasses.surface.widgets.luafunction.SetColor;
import com.bymarcin.ocglasses.surface.widgets.luafunction.SetPosition;
import com.bymarcin.ocglasses.surface.widgets.luafunction.SetSize;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SquareWidget implements IWidget,IPositionable,IResizable,IColorizable,IAlpha{

	float x;
	float y;
	float width;
	float height;
	float alpha;
	float r;
	float g;
	float b;
	
	public SquareWidget() {
	}
	
	public SquareWidget(float x, float y, float r, float g, float b) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public SquareWidget(double x, double y, double r, double g, double b){
		this((float)x,(float)y,(float)r,(float)g,(float)b);
	}
	
	
	@Override
	public void write(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeFloat(height);
		buff.writeFloat(width);
		buff.writeFloat(alpha);
	}

	@Override
	public void read(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		height = buff.readFloat();
		width = buff.readFloat();
		alpha = buff.readFloat();
	}
	
	@Override
	public Widgets getType() {
		return Widgets.QUAD;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return this.new RenderableSquareWidget();
	}
	
	@SideOnly(Side.CLIENT)
	public class RenderableSquareWidget implements IRenderableWidget{
		@Override
		public void render() {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(r, g, b, alpha);
			tessellator.addVertex(x, y, 0);
			tessellator.addVertex(x, y+width, 0);
			tessellator.addVertex(x+height, y+width, 0);
			tessellator.addVertex(x+height, y+0, 0);
			tessellator.draw();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("x", x);
		nbt.setFloat("y", y);
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);	
		nbt.setFloat("width", width);
		nbt.setFloat("height", height);
		nbt.setFloat("alpha", alpha);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		x = nbt.getFloat("x");
		y = nbt.getFloat("y");
		r = nbt.getFloat("r");
		g = nbt.getFloat("g");
		b = nbt.getFloat("b");
		width = nbt.getFloat("width");
		height = nbt.getFloat("height");
		alpha = nbt.getFloat("alpha");
	}

	@Override
	public Object[] getLuaObject(LuaObjectBuilder builder) {
		builder.addFunction("setPosition", new SetPosition());
		builder.addFunction("getPosition", new GetPosition());
		builder.addFunction("setSize", new SetSize());
		builder.addFunction("getSize", new GetSize());
		builder.addFunction("setAlpha", new SetAlpha());
		builder.addFunction("getAlpha", new GetAlpha());
		builder.addFunction("setColor", new SetColor());
		builder.addFunction("getColor", new GetColor());
		return builder.createLuaObject();
	}

	@Override
	public double getPosX() {
		return x;
	}

	@Override
	public double getPosY() {
		return y;
	}

	@Override
	public void setPos(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	@Override
	public void setSize(double width, double height) {
		this.height = (float) height;
		this.width = (float) width;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void setColor(double r, double g, double b) {
		this.r = (float) r;
		this.g = (float) g;
		this.b = (float) b;
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
	public float getAlpha() {
		return alpha;
	}

	@Override
	public void setAlpha(double alpha) {
		this.alpha = (float) alpha;
	}
	
}
