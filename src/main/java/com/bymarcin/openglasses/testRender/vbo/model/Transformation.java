package com.bymarcin.openglasses.testRender.vbo.model;


public class Transformation {

	public static Matrix getTrasnslateMatrix(float x, float y, float z) {
		Matrix translate = Matrix.generateIdentityMatrix(4);
		translate.set(3, 0, x);
		translate.set(3, 1, y);
		translate.set(3, 2, z);
		return translate;
	}

	public static Matrix getScaleMatrix(float x, float y, float z) {
		Matrix scale = Matrix.generateIdentityMatrix(4);
		scale.set(0, 0, x);
		scale.set(1, 1, y);
		scale.set(2, 2, z);
		return scale;
	}

	public static Matrix getRoatateXMatrix(float angle) {
		float cosA = (float) Math.cos(Math.toRadians(angle));
		float sinA = (float) Math.sin(Math.toRadians(angle));
		Matrix rotate = Matrix.generateIdentityMatrix(4);
		rotate.set(1, 1, cosA);
		rotate.set(2, 1, -sinA);
		rotate.set(1, 2, sinA);
		rotate.set(2, 2, cosA);
		return rotate;
	}

	public static Matrix getRoatateYMatrix(float angle) {
		float cosA = (float) Math.cos(Math.toRadians(angle));
		float sinA = (float) Math.sin(Math.toRadians(angle));
		Matrix rotate = Matrix.generateIdentityMatrix(4);
		rotate.set(0, 0, cosA);
		rotate.set(2, 0, sinA);
		rotate.set(0, 2, -sinA);
		rotate.set(2, 2, cosA);
		return rotate;
	}

	public static Matrix getRoatateZMatrix(float angle) {
		float cosA = (float) Math.cos(Math.toRadians(angle));
		float sinA = (float) Math.sin(Math.toRadians(angle));
		Matrix rotate = Matrix.generateIdentityMatrix(4);
		rotate.set(0, 0, cosA);
		rotate.set(0, 1, sinA);
		rotate.set(1, 0, -sinA);
		rotate.set(1, 1, cosA);
		return rotate;
	}
}
