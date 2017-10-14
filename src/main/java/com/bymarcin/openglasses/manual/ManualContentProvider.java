package com.bymarcin.openglasses.manual;

import com.bymarcin.openglasses.OpenGlasses;
import com.google.common.base.Charsets;
import li.cil.oc.api.manual.ContentProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@SuppressWarnings("UnusedDeclaration")
public class ManualContentProvider implements ContentProvider {
    private final String resourceDomain;
    private final String basePath;

    public ManualContentProvider(String resourceDomain, String basePath) {
        this.resourceDomain = resourceDomain;
        this.basePath = basePath;
    }

    public ManualContentProvider(String resourceDomain) {
        this(OpenGlasses.MODID, "");
    }

    @Override
    public Iterable<String> getContent(String path) {

        if(path.contains("#"))
            path = path.substring(0, path.indexOf("#"));


        path+=".md";

        final ResourceLocation location = new ResourceLocation(resourceDomain, basePath + (path.startsWith("/") ? path.substring(1) : path));
        InputStream is = null;
        try {
            is = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            final ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                //filter out lines with string screenshot in it... dirty hack to remove them from the "index/_Sidebar" page :>
                if(!line.toLowerCase().contains("screenshot"))
                    lines.add(line);
            }
            return lines;
        } catch (Throwable ignored) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
