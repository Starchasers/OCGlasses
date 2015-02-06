package com.bymarcin.openglasses.testRender;

import java.util.ArrayList;

public class SubModel {
	ArrayList<Matrix> vertexBuffer = new ArrayList<Matrix>();
	Matrix transformation;
	
	public SubModel() {
		transformation = Matrix.generateIdentityMatrix(4);
	}
	
	public SubModel(Matrix t){
		transformation = t;
	}

	public void addVertex(float x, float y, float z) {
		Matrix vertex = new Matrix(1, 4);
		vertex.set(0, 0, x);
		vertex.set(0, 1, y);
		vertex.set(0, 2, z);
		vertex.set(0, 3, 1);
		vertexBuffer.add(Matrix.multiply(transformation, vertex));
	}

	public void translate(float x, float y, float z) {
		Matrix translate = Matrix.generateIdentityMatrix(4);
		translate.set(3, 0, x);
		translate.set(3, 1, y);
		translate.set(3, 2, z);
		transformation = Matrix.multiply(transformation, translate);
	}

	public void scale(float x, float y, float z) {
		Matrix scale = Matrix.generateIdentityMatrix(4);
		scale.set(0, 0, x);
		scale.set(1, 1, y);
		scale.set(2, 2, z);
		transformation = Matrix.multiply(transformation, scale);
	}

	public void rotate(float angle, float x, float y, float z) {
		float cosA = (float) Math.toDegrees(Math.cos(Math.toRadians(angle)));
		float sinA = (float) Math.toDegrees(Math.sin(Math.toRadians(angle)));
		if (x != 0) {
			Matrix rotate = Matrix.generateIdentityMatrix(4);
			rotate.set(1, 1, cosA);
			rotate.set(2, 1, -sinA);
			rotate.set(1, 2, sinA);
			rotate.set(2, 2, cosA);
			transformation = Matrix.multiply(transformation, rotate);
		}

		if (y != 0) {
			Matrix rotate = Matrix.generateIdentityMatrix(4);
			rotate.set(0, 0, cosA);
			rotate.set(2, 0, sinA);
			rotate.set(0, 2, -sinA);
			rotate.set(2, 2, cosA);
			transformation = Matrix.multiply(transformation, rotate);
		}

		if (z != 0) {
			Matrix rotate = Matrix.generateIdentityMatrix(4);
			rotate.set(0, 0, cosA);
			rotate.set(0, 1, sinA);
			rotate.set(1, 0, -sinA);
			rotate.set(1, 1, cosA);
			transformation = Matrix.multiply(transformation, rotate);
		}
	}

	public static void main(String[] args) {
		SubModel model = new SubModel();
		model.addVertex(2, 2, 2);
		model.scale(0.5f, 2f, 4f);
		model.translate(2, 2, 2);
		model.addVertex(2, 2, 2);

		for (Matrix m : model.vertexBuffer) {
			System.out.println(m.toString());
		}
	}
}
