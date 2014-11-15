package com.bymarcin.ocglasses.surface.widgets;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;

import com.bymarcin.ocglasses.surface.IRenderableWidget;
import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.Widgets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SquareWidget implements IWidget{

	private float x;
	private float y;
	private float r;
	private float g;
	private float b;
	
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
	}

	@Override
	public void read(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
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
			tessellator.setColorRGBA_I(0/* col */, 128);
			tessellator.addVertex(x, y, 0);
			tessellator.addVertex(x, y+32, 0);
			tessellator.addVertex(x+32, y+32, 0);
			tessellator.addVertex(x+32, y+0, 0);
			tessellator.draw();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
}
