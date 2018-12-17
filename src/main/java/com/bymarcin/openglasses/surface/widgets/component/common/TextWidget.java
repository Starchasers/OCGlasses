package com.bymarcin.openglasses.surface.widgets.component.common;

import com.bymarcin.openglasses.lib.McJty.font.FontLoader;
import com.bymarcin.openglasses.lib.McJty.font.TrueTypeFont;
import com.bymarcin.openglasses.surface.WidgetGLWorld;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlignable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.utils.utilsClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public abstract class TextWidget extends WidgetGLWorld implements ITextable, IAlignable {
    String text, fontName;
    boolean antialias;
    float fontSize;
    static ArrayList<TrueTypeFont> fontRenderer = new ArrayList<>();

    public TextWidget(){
        text = "";
        fontName = "";
        fontSize = 12;
        antialias = false;
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);
        ByteBufUtils.writeUTF8String(buff, text);
        ByteBufUtils.writeUTF8String(buff, fontName);
        buff.writeFloat(fontSize);
        buff.writeBoolean(antialias);
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);
        setText(ByteBufUtils.readUTF8String(buff));
        setFont(ByteBufUtils.readUTF8String(buff));
        setFontSize(buff.readFloat());
        setAntialias(buff.readBoolean());

        setSize(0D, 0D);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setSize(double w, double h){
        FontRenderer fontRender = utilsClient.fontRenderer();
        super.setSize(fontRender.getStringWidth(this.getText()), fontRender.FONT_HEIGHT);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public TrueTypeFont getFont(String fontName){
        for(TrueTypeFont ttf : fontRenderer) {
            if(ttf.getFontName().equalsIgnoreCase(fontName)
                    && ttf.getFontSize() == fontSize
                    && ttf.getAntialias() == antialias)
                return ttf;
        }

        fontRenderer.add(FontLoader.loadSystemFont(fontName, fontSize, antialias));

        return getFont(fontName);
    }

    @Override
    public void setFont(String fontName) {
        this.fontName = fontName;
    }

    @Override
    public void setFontSize(float size) {
        this.fontSize = size;
    }

    public String getFontName(){
        return this.fontName;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setAntialias(boolean state){
        this.antialias = state;
    }

}
