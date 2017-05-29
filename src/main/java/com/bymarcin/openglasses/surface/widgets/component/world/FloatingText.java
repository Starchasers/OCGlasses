package com.bymarcin.openglasses.surface.widgets.component.world;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.I3DPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IColorizable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ILookable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IViewDistance;
import com.bymarcin.openglasses.utils.OGUtils;

public class FloatingText extends Widget implements IViewDistance, ILookable, I3DPositionable, ITextable, IColorizable, IScalable, IAlpha, IThroughVisibility{
	float x;
	float y;
	float z;
	
	int lookingAtX;
	int lookingAtY;
	int lookingAtZ;
	boolean isLookingAtEnable;
	int viewDistance = 100;
	
	float r;
	float g;
	float b;
	float alpha = 0.5f;
	
	boolean isThroughVisibility = true;
	float scale = 0.05f;
	String text ="";
	
	public FloatingText() {}
	
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
	public void writeData(ByteBuf buff) {
		buff.writeFloat(x);
		buff.writeFloat(y);
		buff.writeFloat(z);
		ByteBufUtils.writeUTF8String(buff, text);
		buff.writeFloat(r);
		buff.writeFloat(g);
		buff.writeFloat(b);
		buff.writeFloat(scale);
		buff.writeFloat(alpha);
		buff.writeBoolean(isThroughVisibility);
		buff.writeInt(lookingAtX);
		buff.writeInt(lookingAtY);
		buff.writeInt(lookingAtZ);
		buff.writeBoolean(isLookingAtEnable);
		buff.writeInt(viewDistance);
	}

	@Override
	public void readData(ByteBuf buff) {
		x = buff.readFloat();
		y = buff.readFloat();
		z = buff.readFloat();
		text = ByteBufUtils.readUTF8String(buff);
		r = buff.readFloat();
		g = buff.readFloat();
		b = buff.readFloat();
		scale = buff.readFloat();	
		alpha = buff.readFloat();
		isThroughVisibility = buff.readBoolean();
		lookingAtX = buff.readInt();
		lookingAtY = buff.readInt();
		lookingAtZ = buff.readInt();
		isLookingAtEnable = buff.readBoolean();
		viewDistance = buff.readInt();
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
		int color = OGUtils.getIntFromColor(r, g, b, alpha);
		
		@Override
		public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
			if(!OGUtils.inRange(playerX, playerY, playerZ, x, y, z, viewDistance)){
				return;
			}
			if(isLookingAtEnable){
				RayTraceResult pos = ClientSurface.getBlockCoordsLookingAt(player);
				if(pos == null || pos.getBlockPos().getX() != lookingAtX || pos.getBlockPos().getY() != lookingAtY || pos.getBlockPos().getZ() != lookingAtZ)
					return;
			}
			GL11.glPushMatrix();
			if(isThroughVisibility){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}else{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
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
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		@Override
		public RenderType getRenderType() {
			return RenderType.WorldLocated;
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

	@Override
	public boolean isVisibleThroughObjects() {
		return isThroughVisibility;
	}

	@Override
	public void setVisibleThroughObjects(boolean visible) {
		isThroughVisibility = visible;
	}

	@Override
	public void setLookingAt(int x, int y, int z) {
		lookingAtX = x;
		lookingAtY = y;
		lookingAtZ = z;
		
	}

	@Override
	public boolean isLookingAtEnable() {
		return isLookingAtEnable;
	}

	@Override
	public void setLookingAtEnable(boolean enable) {
		isLookingAtEnable = enable;
	}

	@Override
	public int getLookingAtX() {
		return lookingAtX;
	}

	@Override
	public int getLookingAtY() {
		return lookingAtY;
	}

	@Override
	public int getLookingAtZ() {
		return lookingAtZ;
	}

	@Override
	public int getDistanceView() {
		return viewDistance;
	}

	@Override
	public void setDistanceView(int distance) {
		viewDistance = distance;
	}
	
}
