package com.bymarcin.openglasses.surface.rendering;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector4f;

public class AnimationFrame {
	public static final AnimationFrame EMPTY_FRAME = new AnimationFrame();
	
	private ReadableVector4f startScale = new Vector4f(1, 1, 1, 1);
	private ReadableVector4f startTranslate = new Vector4f(0, 0, 0, 1);
	private ReadableVector4f startRotation = Quaternion.setIdentity(new Quaternion());
	private ReadableVector4f startColor = new Vector4f(1, 1, 1, 1);
	
	private ReadableVector4f stopScale = new Vector4f(1, 1, 1, 1);
	private ReadableVector4f stopTranslate = new Vector4f(0, 0, 0, 1);
	private ReadableVector4f stopRotation = Quaternion.setIdentity(new Quaternion());
	private ReadableVector4f stopColor = new Vector4f(1, 1, 1, 1);
	
	private long duration;
	private long endtime;
	
	
	public void setStartScale(ReadableVector4f startScale) {
		this.startScale = startScale;
	}
	
	public void setStartTranslate(ReadableVector4f startTranslate) {
		this.startTranslate = startTranslate;
	}
	
	public void setStartRotation(ReadableVector4f startRotation) {
		this.startRotation = startRotation;
	}
	
	public void setStartColor(ReadableVector4f startColor) {
		this.startColor = startColor;
	}
	
	public void setStopScale(ReadableVector4f stopScale) {
		this.stopScale = stopScale;
	}
	
	public void setStopTranslate(ReadableVector4f stopTranslate) {
		this.stopTranslate = stopTranslate;
	}
	
	public void setStopRotation(ReadableVector4f stopRotation) {
		this.stopRotation = stopRotation;
	}
	
	public void setStopColor(ReadableVector4f stopColor) {
		this.stopColor = stopColor;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	
	public ReadableVector4f getStartScale() {
		return startScale;
	}
	
	public ReadableVector4f getStartTranslate() {
		return startTranslate;
	}
	
	public ReadableVector4f getStartRotation() {
		return startRotation;
	}
	
	public ReadableVector4f getStartColor() {
		return startColor;
	}
	
	public ReadableVector4f getStopScale() {
		return stopScale;
	}
	
	public ReadableVector4f getStopTranslate() {
		return stopTranslate;
	}
	
	public ReadableVector4f getStopRotation() {
		return stopRotation;
	}
	
	public ReadableVector4f getStopColor() {
		return stopColor;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public long getEndtime() {
		return endtime;
	}
	
	public void store(FloatBuffer floatBuffer) {
		startTranslate.store(floatBuffer);
		stopTranslate.store(floatBuffer);
		
		startRotation.store(floatBuffer);
		stopRotation.store(floatBuffer);
		
		startScale.store(floatBuffer);
		stopScale.store(floatBuffer);
		
		startColor.store(floatBuffer);
		stopColor.store(floatBuffer);
	}
	
}