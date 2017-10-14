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

import java.util.HashMap;
import java.util.List;

public class Face
{
    /** Name of this {@link Face}. */
    protected String name;
    /** List of {@link Vertex Vertexes} of this {@link Face}. */
    protected Vertex[] vertexes;


    /**
     * Instantiates a new {@link Face}.
     *
     * @param vertexes the vertexes
     */
    public Face(Vertex[] vertexes)
    {
        this.vertexes = vertexes;
        this.setName(null);
    }


    /**
     * Instantiates a new {@link Face}.
     *
     * @param vertexes the vertexes
     */
    public Face(List<Vertex> vertexes){
        this(vertexes.toArray(new Vertex[0]));
    }


    /**
     * Sets the base name for this {@link Face}. If the name specified is null, it is automatically determined based on the {@link Vertex}
     * positions.
     *
     * @param name the base name
     */
    public void setName(String name)
    {
        if (name == null)
        {
            name = "";
            HashMap<String, Integer> map = new HashMap<>();
            String[] dirs = new String[] { "North", "South", "East", "West", "Top", "Bottom" };
            for (String dir : dirs)
            {
                map.put(dir, 0);
                for (Vertex v : vertexes)
                {
                    if (v.name().contains(dir))
                        map.put(dir, map.get(dir) + 1);
                }
                if (map.get(dir) == 4)
                    name = dir;
            }
        }

        this.name = name;
    }

    /**
     * Gets the base name of this {@link Face}.
     *
     * @return the base name
     */
    public String name()
    {
        return name;
    }

    /**
     * Gets the {@link Vertex vertexes} of this {@link Face}.
     *
     * @return the vertexes
     */
    public Vertex[] getVertexes()
    {
        return vertexes;
    }

    @Override
    public String toString()
    {
        String s = name() + " {";
        for (Vertex v : vertexes)
            s += v.name() + ", ";
        return s + "}";
    }


}
