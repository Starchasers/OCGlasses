package com.bymarcin.openglasses.testRender;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.common.primitives.Floats;

public class Shape {
	private ArrayList<Float> buffer = new ArrayList<Float>();
	private LinkedList<Matrix> transformation = new LinkedList<Matrix>();
	private Matrix color = new Matrix(1, 4);
	private int vertexCount;
	
	public Shape() {
		transformation.add(Matrix.generateIdentityMatrix(4));
	}

	public Shape(Matrix t) {
		transformation.add(t);
	}

	public void setColor(float r, float g, float b, float alpha){
		color.set(0, 0, r);
		color.set(0, 1, g);
		color.set(0, 2, b);
		color.set(0, 3, alpha);
	}
	
	public void addVertex(float x, float y, float z) {
		Matrix vertex = new Matrix(1, 4);
		vertex.set(0, 0, x);
		vertex.set(0, 1, y);
		vertex.set(0, 2, z);
		vertex.set(0, 3, 1);
		addVertex(Matrix.multiply(transformation.peek(), vertex));
	}
	
	private void addVertex(Matrix vertex){
		for(int i=0;i<vertex.getHeight()-1; i++){
			buffer.add(vertex.get(0, i));
		}
		
		for(int i=0;i<color.getHeight(); i++){
			buffer.add(color.get(0, i));
		}
		vertexCount++;
	}

	public void translate(float x, float y, float z) {
		Matrix translate = Transformation.getTrasnslateMatrix(x, y, z);
		transformation.add(Matrix.multiply(transformation.poll(), translate));
	}

	public void scale(float x, float y, float z) {
		Matrix scale = Transformation.getScaleMatrix(x, y, z);
		transformation.add(Matrix.multiply(transformation.poll(), scale));
	}

	public void rotate(float angle, float x, float y, float z) {
		if (x != 0) {
			Matrix rotate = Transformation.getRoatateXMatrix(angle);
			transformation.add(Matrix.multiply(transformation.poll(), rotate));
		}

		if (y != 0) {
			Matrix rotate = Transformation.getRoatateYMatrix(angle);
			transformation.add(Matrix.multiply(transformation.poll(), rotate));
		}

		if (z != 0) {
			Matrix rotate = Transformation.getRoatateZMatrix(angle);
			transformation.add(Matrix.multiply(transformation.poll(), rotate));
		}
	}

	public void pushMatrix() {
		transformation.add(transformation.peek());
	}

	public void popMatrix() {
		transformation.poll();
	}
	
	public boolean canCreateShape(){
		return vertexCount%3==0;
	}
	
	public float[] getBuffer(){
		return Floats.toArray(buffer);
	}
	
}
