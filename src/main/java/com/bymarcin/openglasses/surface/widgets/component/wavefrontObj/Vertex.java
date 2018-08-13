package com.bymarcin.openglasses.surface.widgets.component.wavefrontObj;
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;

public class Vertex
{
    public static final int BRIGHTNESS_MAX = (240 << 16) | 240; //sky << 16 | block

    /** Base name of this {@link Vertex}, set when first instanciated and kept after transformation for easy access. */
    private String baseName;
    /** X coordinate of this {@link Vertex} **/
    private double x = 0;
    /** Y coordinate of this {@link Vertex} **/
    private double y = 0;
    /** Z coordinate of this {@link Vertex} **/
    private double z = 0;
    /** Brightness of this {@link Vertex} **/
    private int brightness = 0;
    /** Color of this {@link Vertex} **/
    private int color = 0xFFFFFF;
    private int alpha = 255;
    private int normal = 0;
    private double u = 0.0F;
    private double v = 0.0F;

    private Vertex initialState;

    public Vertex(double x, double y, double z, int rgba, int brightness, double u, double v, int normal, boolean isInitialState)
    {
        set(x, y, z);
        setRGBA(rgba);
        setBrightness(brightness);
        setUV(u, v);
        this.x = x;
        this.y = y;
        this.z = z;

        this.u = u;
        this.v = v;
        this.normal = normal;
        this.baseName();

        if (!isInitialState)
            initialState = new Vertex(x, y, z, rgba, brightness, u, v, normal, true);
    }

    public Vertex(double x, double y, double z)
    {
        this(x, y, z, 0xFFFFFFFF, BRIGHTNESS_MAX, 0, 0, 0, false);
    }

    public Vertex(Vertex vertex)
    {
        this(vertex.x, vertex.y, vertex.z, vertex.color << 8 | vertex.alpha, vertex.getBrightness(), vertex.u, vertex.v, 0, false);
        baseName = vertex.baseName;
    }

    //#region Getters/Setters
    public double getX()
    {
        return x;
    }

    public Vertex setX(double x)
    {
        this.x = x;
        return this;
    }

    public double getY()
    {
        return y;
    }

    public Vertex setY(double y)
    {
        this.y = y;
        return this;
    }

    public double getZ()
    {
        return z;
    }

    public Vertex setZ(double z)
    {
        this.z = z;
        return this;
    }

    public void set(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getColor()
    {
        return this.color;
    }

    public Vertex setColor(int color)
    {
        this.color = color & 0xFFFFFF;
        return this;
    }

    public int getAlpha()
    {
        return this.alpha;
    }

    public Vertex setAlpha(int alpha)
    {
        this.alpha = alpha & 255;
        return this;
    }

    public int getRGBA()
    {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        return alpha << 24 | b << 16 | g << 8 | r;
    }

    public Vertex setRGBA(int rgba)
    {
        this.color = (rgba >>> 8) & 0xFFFFFF;
        this.alpha = rgba & 255;
        return this;
    }

    public int getBlockBrightness()
    {
        return brightness & 240;
    }

    public int getSkyBrightness()
    {
        return (brightness >> 16) & 240;
    }

    public int getBrightness()
    {
        return brightness;
    }

    public Vertex setBrightness(int brightness)
    {
        this.brightness = brightness;
        return this;
    }

    public int getNormal()
    {
        return normal;
    }



    public double getU()
    {
        return this.u;
    }

    public double getV()
    {
        return this.v;
    }

    public void setUV(double u, double v)
    {
        this.u = u;
        this.v = v;
    }

    //#end Getters/Setters

    public boolean isCorner()
    {
        return (x == 1 || x == 0) && (y == 1 || y == 0) && (z == 1 || z == 0);
    }


    public String baseName()
    {
        if (baseName == null)
        {
            baseName = "";
            if (isCorner())
                baseName = (y == 1 ? "Top" : "Bottom") + (z == 1 ? "South" : "North") + (x == 1 ? "East" : "West");
        }
        return baseName;
    }

    public String name()
    {
        return baseName() + " [" + x + ", " + y + ", " + z + "|" + u + ", " + v + "]";
    }

    @Override
    public String toString()
    {
        return name() + " 0x" + Integer.toHexString(color) + " (a:" + alpha + ", bb:" + getBlockBrightness() + ", sb:" + getSkyBrightness()
                + ")";
    }


    /**
     * Gets the vertex data for this {@link Vertex}.
     *
     * @param vertexFormat the vertex format
     * @param offset the offset
     * @return the vertex data
     */
    public int[] getVertexData(VertexFormat vertexFormat, Vec3d offset)
    {
        float x = (float) getX();
        float y = (float) getY();
        float z = (float) getZ();

        if (offset != null)
        {
            x += offset.x;
            y += offset.y;
            z += offset.z;
        }

        int[] data = new int[vertexFormat.getIntegerSize()];
        int index = 0;
        //private
        //if(vertexFormat.hasPosition())
        {
            data[index++] = Float.floatToRawIntBits(x);
            data[index++] = Float.floatToRawIntBits(y);
            data[index++] = Float.floatToRawIntBits(z);
        }
        if (vertexFormat.hasColor())
            data[index++] = getRGBA();
        if (vertexFormat.hasUvOffset(0)) //normal UVs
        {
            data[index++] = Float.floatToRawIntBits((float) getU());
            data[index++] = Float.floatToRawIntBits((float) getV());
        }
        if (vertexFormat.hasUvOffset(1)) //brightness UVs
            data[index++] = getBrightness();
        if (vertexFormat.hasNormal())
            data[index++] = getNormal();

        return data;
    }

}