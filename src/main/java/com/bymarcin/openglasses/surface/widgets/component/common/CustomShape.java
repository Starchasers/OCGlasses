package com.bymarcin.openglasses.surface.widgets.component.common;

import com.bymarcin.openglasses.surface.WidgetGLWorld;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ICustomShape;
import io.netty.buffer.ByteBuf;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.utils.Location;

import java.util.ArrayList;

public abstract class CustomShape extends WidgetGLWorld implements ICustomShape {
    public ArrayList<ArrayList> vectors;
    public boolean gl_strips;
    public boolean smooth_shading;

    public CustomShape() {
        vectors = new ArrayList<ArrayList>();
        gl_strips = false;
        smooth_shading = false;
    }

    public int getVertexCount(){
        return this.vectors.size();
    }

    public void setVertex(int n, float nx, float ny, float nz){
        if(getVertexCount() <= n) {
            vectors.get(n).set(0, nx);
            vectors.get(n).set(1, ny);
            vectors.get(n).set(2, nz);
        }
        else addVertex(nx, ny, nz);
    }

    public void addVertex(float nx, float ny, float nz){
        ArrayList<Float> vector = new ArrayList<Float>();
        vector.add(nx);
        vector.add(ny);
        vector.add(nz);
        this.vectors.add(vector);
    }

    public void removeVertex(int n){
        if(getVertexCount() <= n)
            this.vectors.remove(n);
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        buff.writeBoolean(gl_strips);

        buff.writeInt(vectors.size());
        for(int i = 0; i < vectors.size(); i++) {
            buff.writeFloat((float) vectors.get(i).get(0));
            buff.writeFloat((float) vectors.get(i).get(1));
            buff.writeFloat((float) vectors.get(i).get(2));
        }
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        this.gl_strips = buff.readBoolean();

        vectors = new ArrayList<ArrayList>();
        for(int i = 0, vectorCount = buff.readInt(); i < vectorCount; i++) {
            this.addVertex(buff.readFloat(), buff.readFloat(), buff.readFloat());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableCustom();
    }

    @SideOnly(Side.CLIENT)
    public class RenderableCustom extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
            if(vectors.size() <3) return;

            this.preRender(conditionStates);
            this.applyModifiers(conditionStates);
            if(smooth_shading)
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            else
                GlStateManager.shadeModel(GL11.GL_FLAT);

            if(gl_strips) {
                GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);
                for(int i=0; i < vectors.size(); i++)
                    GlStateManager.glVertex3f((float) vectors.get(i).get(0), (float) vectors.get(i).get(1), (float) vectors.get(i).get(2));
            }
            else{
                GL11.glBegin(GL11.GL_TRIANGLES);
                for(int i=3; i <= vectors.size(); i+=3) {
                    GlStateManager.glVertex3f((float) vectors.get(i-3).get(0), (float) vectors.get(i-3).get(1), (float) vectors.get(i-3).get(2));
                    GlStateManager.glVertex3f((float) vectors.get(i-2).get(0), (float) vectors.get(i-2).get(1), (float) vectors.get(i-2).get(2));
                    GlStateManager.glVertex3f((float) vectors.get(i-1).get(0), (float) vectors.get(i-1).get(1), (float) vectors.get(i-1).get(2));
                }
            }

            GlStateManager.glEnd();

            this.postRender();
        }
    }
}
