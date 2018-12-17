package com.bymarcin.openglasses.surface.widgets.core.attribute;

public interface ITextable extends IAttribute{
	void setText(String text);
	void setFont(String fontName);
	String getText();
	void setAntialias(boolean state);
	void setFontSize(float size);
}
