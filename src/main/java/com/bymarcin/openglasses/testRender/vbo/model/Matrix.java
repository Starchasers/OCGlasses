package com.bymarcin.openglasses.testRender.vbo.model;

import com.bymarcin.openglasses.testRender.vbo.network.ISendable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Matrix implements ISendable<Matrix> {
	private float[][] matrix;
	private int width;
	private int height;

	public Matrix(int x, int y) {
		matrix = new float[x][y];
		width = x;
		height = y;
	}

	public Float get(int x, int y) {
		return matrix[x][y];
	}

	public void set(int x, int y, float data) {
		matrix[x][y] = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------------------------------\n");

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(matrix[x][y] + "\t");
			}
			sb.append("\n");
		}

		sb.append("----------------------------------------------------");
		return sb.toString();
	}

	public static Matrix multiply(Matrix first, Matrix second) {
		if (first.width != second.height) {
			return null;
		}

		Matrix result = new Matrix(second.width, first.height);
		for (int i = 0; i < result.height; i++) {
			for (int j = 0; j < result.width; j++) {
				float sum = 0;
				for (int k = 0; k < second.height; k++) {
					sum += first.get(k, i) * second.get(j, k);
				}
				result.set(j, i, sum);
			}
		}
		return result;
	}

	public static Matrix generateIdentityMatrix(int n) {
		Matrix matrix = new Matrix(n, n);
		for (int i = 0; i < n; i++) {
			matrix.set(i, i, 1);
		}
		return matrix;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public ByteBuf toPacket() {
		ByteBuf b = Unpooled.buffer();
		b.writeInt(width);
		b.writeInt(height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				b.writeFloat(matrix[i][j]);
			}
		}
		return b;
	}

	@Override
	public Matrix fromPacket(ByteBuf buf) {
		width = buf.readInt();
		height = buf.readInt();
		matrix = new float[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				matrix[i][j] = buf.readFloat();
			}
		}
		return this;
	}
}
