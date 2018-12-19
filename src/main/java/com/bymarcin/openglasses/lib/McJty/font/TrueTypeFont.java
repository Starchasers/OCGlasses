package com.bymarcin.openglasses.lib.McJty.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

// code grabbed from McJtyLib:
// https://github.com/McJtyMods/McJtyLib/blob/1.12/src/main/java/mcjty/lib/font/TrueTypeFont.java

/**
 * TrueTyper: Open Source TTF implementation for Minecraft.
 * Modified from Slick2D - under BSD Licensing -  http://slick.ninjacave.com/license/
 * <p/>
 * Copyright (c) 2013 - Slick2D
 * <p/>
 * All rights reserved.
 */

public class TrueTypeFont {
    public int usageCount = 0;


    /**
     * Array that holds necessary information about the font characters
     */
    private FloatObject[] charArray = new FloatObject[256];

    /**
     * Map of user defined font characters (Character <-> IntObject)
     */
    private Map<Character, FloatObject> customChars = new HashMap<>();

    /**
     * Boolean flag on whether AntiAliasing is enabled or not
     */
    protected boolean antiAlias;

    /**
     * Font's size
     */
    private float fontSize = 0;

    /**
     * Font's height
     */
    private float fontHeight = 0;

    /**
     * Texture used to cache the font 0-255 characters
     */
    private int fontTextureID;

    /**
     * Default font texture width
     */
    private int textureWidth = 1024;

    /**
     * Default font texture height
     */
    private int textureHeight = 1024;

    /**
     * A reference to Java's AWT Font that we create our font texture from
     */
    protected Font font;

    /**
     * The font metrics for our Java AWT font
     */
    private FontMetrics fontMetrics;


    private int correctL = 9, correctR = 8;

    private class FloatObject {
        /**
         * Character's width
         */
        public float width;

        /**
         * Character's height
         */
        public float height;

        /**
         * Character's stored x position
         */
        public float storedX;

        /**
         * Character's stored y position
         */
        public float storedY;
    }

    public Font getFont(){
        return this.font;
    }

    public boolean getAntialias(){
        return this.antiAlias;
    }

    public float getFontSize(){
        return this.fontSize;
    }

    public String getFontName(){
        return this.font.getName();
    }


    public TrueTypeFont(Font font, boolean antiAlias, char[] additionalChars) {
        this.font = font;
        this.fontSize = font.getSize();
        this.antiAlias = antiAlias;

        createSet(additionalChars);
        System.out.println("TrueTypeFont loaded: " + font + " - AntiAlias = " + antiAlias);
        fontHeight -= 1;
        if (fontHeight <= 0) fontHeight = 1;
    }

    public TrueTypeFont(Font font, boolean antiAlias) {
        this(font, antiAlias, null);
    }


    private BufferedImage getFontImage(char ch) {
        // Create a temporary image to extract the character's size
        BufferedImage tempfontImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        fontMetrics = g.getFontMetrics();

        float charwidth = fontMetrics.charWidth(ch) + 8;
        if (charwidth <= 0) {
            charwidth = 7;
        }

        float charheight = fontMetrics.getHeight() + 3;
        if (charheight <= 0) {
            charheight = fontSize;
        }

        // Create another image holding the character we are creating
        BufferedImage fontImage = new BufferedImage((int) charwidth, (int) charheight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D gt = (Graphics2D) fontImage.getGraphics();
        if (antiAlias) gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gt.setFont(font);
        gt.setColor(Color.WHITE);

        int charX = 3, charY = 1;
        gt.drawString(String.valueOf(ch), charX, (charY + fontMetrics.getAscent()));

        return fontImage;
    }

    private void createSet(char[] customCharsArray) {
        // If there are custom chars then I expand the font texture twice
        if (customCharsArray != null && customCharsArray.length > 0) {
            textureWidth *= 2;
        }

        // In any case this should be done in other way. Texture with size 512x512
        // can maintain only 256 characters with resolution of 32x32. The texture
        // size should be calculated dynamicaly by looking at character sizes.

        try {
            BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) imgTemp.getGraphics();

            g.setColor(new Color(0, 0, 0, 1));
            g.fillRect(0, 0, textureWidth, textureHeight);

            float rowHeight = 0;
            float positionX = 0, positionY = 0;

            int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0;


            BufferedImage fontImage;
            FloatObject newIntObject;
            char ch;
            for (int i = 0; i < 256 + customCharsLength; i++) {

                // get 0-255 characters and then custom characters
                ch = (i < 256) ? (char) i : customCharsArray[i - 256];

                fontImage = getFontImage(ch);

                newIntObject = new FloatObject();

                newIntObject.width = fontImage.getWidth();
                newIntObject.height = fontImage.getHeight();

                if (positionX + newIntObject.width >= textureWidth) {
                    positionX = 0;
                    positionY += rowHeight;
                    rowHeight = 0;
                }

                newIntObject.storedX = positionX;
                newIntObject.storedY = positionY;

                if (newIntObject.height > fontHeight) {
                    fontHeight = newIntObject.height;
                }

                if (newIntObject.height > rowHeight) {
                    rowHeight = newIntObject.height;
                }

                // Draw it here
                g.drawImage(fontImage, (int) positionX, (int) positionY, null);

                positionX += newIntObject.width;

                if (i < 256) { // standard characters
                    charArray[i] = newIntObject;
                } else { // custom characters
                    customChars.put(new Character(ch), newIntObject);
                }
            }

            fontTextureID = loadImage(imgTemp);

        } catch (RuntimeException e) {
            System.err.println("Failed to create font.");
            e.printStackTrace();
        }
    }

    private void drawQuad(float drawX, float drawY, float drawX2, float drawY2,
                          float srcX, float srcY, float srcX2, float srcY2) {
        float DrawWidth = drawX2 - drawX;
        float DrawHeight = drawY2 - drawY;
        float TextureSrcX = srcX / textureWidth;
        float TextureSrcY = srcY / textureHeight;
        float SrcWidth = srcX2 - srcX;
        float SrcHeight = srcY2 - srcY;
        float RenderWidth = (SrcWidth / textureWidth);
        float RenderHeight = (SrcHeight / textureHeight);

        GL11.glTexCoord2f(TextureSrcX, TextureSrcY);
        GL11.glVertex2f(drawX, drawY);

        GL11.glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
        GL11.glVertex2f(drawX, drawY + DrawHeight);

        GL11.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight);
        GL11.glVertex2f(drawX + DrawWidth, drawY + DrawHeight);

        GL11.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
        GL11.glVertex2f(drawX + DrawWidth, drawY);
    }

    public float getWidth(String whatchars) {
        float totalwidth = 0;
        int currentChar = 0;
        float lastWidth = -10f;
        for (int i = 0; i < whatchars.length(); i++) {
            currentChar = whatchars.charAt(i);
            FloatObject floatObject;
            if (currentChar < 256) {
                floatObject = charArray[currentChar];
            } else {
                floatObject = customChars.get(new Character((char) currentChar));
            }

            if (floatObject != null) {
                totalwidth += floatObject.width / 2;
                lastWidth = floatObject.width;
            }
        }
        //System.out.println("Size: "+totalwidth);
        return this.fontMetrics.stringWidth(whatchars);
        //return (totalwidth);
    }

    public float getHeight() {
        return fontHeight;
    }


    public float getHeight(String HeightString) {
        return fontHeight;
    }

    public float getLineHeight() {
        return fontHeight;
    }

    public String trimStringToWidth(String text, int width) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            int i1 = (int) this.getWidth("" + c0);

            if (flag) {
                flag = false;

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;

                if (flag1) {
                    ++i;
                }
            }

            if (i > width) {
                break;
            }

            stringbuilder.append(c0);
        }

        return stringbuilder.toString();
    }

    @SideOnly(Side.CLIENT)
    public void drawString(String text, float x, float y, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, getLineHeight(), 0);

        GlStateManager.scale(-1, -1, 1.0f);
        GlStateManager.rotate(180, 0, 1, 0);

        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color & 255) / 255.0F;
        float blue = (float)(color >> 8 & 255) / 255.0F;
        float alpha = (float)(color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, blue, green, alpha);

        GlStateManager.bindTexture(fontTextureID);
        GlStateManager.glBegin(GL11.GL_QUADS);
        drawTextInternal(text);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    private void drawTextInternal(String whatchars) {
        int charCurrent;
        float totalwidth = 0;

        for (int i=0; i < whatchars.length(); i++) {
            charCurrent = whatchars.charAt(i);
            FloatObject floatObject;
            if (charCurrent < 256) {
                floatObject = charArray[charCurrent];
            } else {
                floatObject = customChars.get(new Character((char) charCurrent));
            }

            if (floatObject == null) continue;

            drawQuad((totalwidth + floatObject.width),
                    0,
                    totalwidth,
                    floatObject.height,
                    floatObject.storedX + floatObject.width,
                    floatObject.storedY + floatObject.height,
                    floatObject.storedX,
                    floatObject.storedY);

            totalwidth += floatObject.width - correctL;
        }
    }

    public static int loadImage(BufferedImage bufferedImage) {
        try {
            short width = (short) bufferedImage.getWidth();
            short height = (short) bufferedImage.getHeight();
            //textureLoader.bpp = bufferedImage.getColorModel().hasAlpha() ? (byte)32 : (byte)24;
            int bpp = (byte) bufferedImage.getColorModel().getPixelSize();
            ByteBuffer byteBuffer;
            DataBuffer db = bufferedImage.getData().getDataBuffer();
            if (db instanceof DataBufferInt) {
                int intI[] = ((DataBufferInt) (bufferedImage.getData().getDataBuffer())).getData();
                byte newI[] = new byte[intI.length * 4];
                for (int i = 0; i < intI.length; i++) {
                    byte b[] = intToByteArray(intI[i]);
                    int newIndex = i * 4;

                    newI[newIndex] = b[1];
                    newI[newIndex + 1] = b[2];
                    newI[newIndex + 2] = b[3];
                    newI[newIndex + 3] = b[0];
                }

                byteBuffer = ByteBuffer.allocateDirect(
                        width * height * (bpp / 8))
                        .order(ByteOrder.nativeOrder())
                        .put(newI);
            } else {
                byteBuffer = ByteBuffer.allocateDirect(
                        width * height * (bpp / 8))
                        .order(ByteOrder.nativeOrder())
                        .put(((DataBufferByte) (bufferedImage.getData().getDataBuffer())).getData());
            }
            byteBuffer.flip();


            int internalFormat = GL11.GL_RGBA8,
                    format = GL11.GL_RGBA;
            IntBuffer textureId = BufferUtils.createIntBuffer(1);

            GL11.glGenTextures(textureId);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId.get(0));

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);


            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

            GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, internalFormat, width, height, format, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            return textureId.get(0);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean isSupported(String fontname) {
        Font font[] = getFonts();
        for (int i = font.length - 1; i >= 0; i--) {
            if (font[i].getName().equalsIgnoreCase(fontname))
                return true;
        }
        return false;
    }

    public static Font[] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

    public void destroy() {
        IntBuffer scratch = BufferUtils.createIntBuffer(1);
        scratch.put(0, fontTextureID);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDeleteTextures(scratch);
    }
}