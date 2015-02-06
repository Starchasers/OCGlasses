package com.bymarcin.openglasses.testRender;

import java.util.ArrayList;

public class Model {
	Shape shape;
	ArrayList<Command> commandList = new ArrayList<Command>();
	Matrix modelTransformation = Matrix.generateIdentityMatrix(4);
	
	
	public void setColor(float r, float g, float b, float alpha) {
		commandList.add(new Command(Command.COLOR, r, g, b, alpha));
	}

	public void addVertex(float x, float y, float z) {
		commandList.add(new Command(Command.VERTEX, x, y, z));
	}

	public void translate(float x, float y, float z) {
		commandList.add(new Command(Command.TRANSLATE, x, y, z));
	}

	public void scale(float x, float y, float z) {
		commandList.add(new Command(Command.SCALE, x, y, z));
	}

	public void rotate(float angle, float x, float y, float z) {
		commandList.add(new Command(Command.ROTATE, x, y, z));
	}

	public void pushMatrix() {
		commandList.add(new Command(Command.PUSHMATRIX));
	}

	public void popMatrix() {
		commandList.add(new Command(Command.POPMATRIX));
	}

	
	public void translateModel(float x, float y, float z) {
		
	}

	public void scaleModel(float x, float y, float z) {

	}
	
	public void rotateModel(float angle, float x, float y, float z){
		
	}
	
	public void setVisible(boolean isVisible){
		
	}

}
