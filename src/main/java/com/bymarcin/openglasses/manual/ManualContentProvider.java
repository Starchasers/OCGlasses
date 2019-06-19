package com.bymarcin.openglasses.manual;

import com.google.common.base.Charsets;
import li.cil.oc.api.manual.ContentProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManualContentProvider implements ContentProvider {
    @Override
    public Iterable<String> getContent(String path) {
        final ArrayList<String> lines = new ArrayList<>();

        if(path.contains("#")) //remove jumpmarks from uri
            path = path.substring(0, path.indexOf("#"));

        boolean inCodeBlock = false;

        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(path + ".md");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                //filter out lines with string screenshot in it... dirty hack to remove them from the "index/_Sidebar" page :>
                if(line.toLowerCase().contains("screenshot"))
                    continue;

                if(line.contains("```")) { // if this line would start/stop a codeblock we change our inCodeBlock var so that code tags are added to each line
                    line = line.replaceAll("```lua", "").replaceAll("```", ""); // strip of codeblock tags, as they aren't supported
                    inCodeBlock = !inCodeBlock;
                }

                if(inCodeBlock)
                    line = "`" + line + "`";

                line = line.replaceAll("!\\[[^]]*]\\([^)]*\\)", ""); // strip of markdown image tags to avoid logspamming of not resolvable image uris

                lines.add(line);
            }
        } catch (Throwable ignored) {
            return null;
        } finally {
            if (is != null)
                try { is.close(); } catch (IOException ignored) {}
        }

        return lines;
    }
}
