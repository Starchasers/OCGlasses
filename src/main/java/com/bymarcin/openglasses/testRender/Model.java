package com.bymarcin.openglasses.testRender;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Model implements ISendable<Model> {
	ArrayList<Command> commandList = new ArrayList<Command>();
	Matrix modelTransformation = Matrix.generateIdentityMatrix(4);
	boolean isVisible = true;
	int id;

	public Model(int id) {
		this.id = id;
	}

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
		commandList.add(new Command(Command.ROTATE, angle, x, y, z));
	}

	public void pushMatrix() {
		commandList.add(new Command(Command.PUSHMATRIX));
	}

	public void popMatrix() {
		commandList.add(new Command(Command.POPMATRIX));
	}

	public void translateModel(float x, float y, float z) {
		modelTransformation = Matrix.multiply(modelTransformation, Transformation.getTrasnslateMatrix(x, y, z));
	}

	public void scaleModel(float x, float y, float z) {
		modelTransformation = Matrix.multiply(modelTransformation, Transformation.getScaleMatrix(x, y, z));
	}

	public void rotateModel(float angle, float x, float y, float z) {
		if (x != 0) {
			modelTransformation = Matrix.multiply(modelTransformation, Transformation.getRoatateXMatrix(angle));
		}

		if (y != 0) {
			modelTransformation = Matrix.multiply(modelTransformation, Transformation.getRoatateYMatrix(angle));
		}

		if (z != 0) {
			modelTransformation = Matrix.multiply(modelTransformation, Transformation.getRoatateZMatrix(angle));
		}
	}

	public void resetModelTransformation() {
		modelTransformation = Matrix.generateIdentityMatrix(4);
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	public float[] generateBuffer() {
		Shape shape = new Shape(modelTransformation);
		if (isVisible) {
			for (Command cmd : commandList) {
				switch (cmd.getCommand()) {
				case Command.COLOR:
					shape.setColor(cmd.getArgs()[0], cmd.getArgs()[1], cmd.getArgs()[2], cmd.getArgs()[3]);
					break;
				case Command.POPMATRIX:
					shape.popMatrix();
					break;
				case Command.PUSHMATRIX:
					shape.pushMatrix();
					break;
				case Command.ROTATE:
					shape.rotate(cmd.getArgs()[0], cmd.getArgs()[1], cmd.getArgs()[2], cmd.getArgs()[3]);
					break;
				case Command.SCALE:
					shape.scale(cmd.getArgs()[0], cmd.getArgs()[1], cmd.getArgs()[2]);
					break;
				case Command.TRANSLATE:
					shape.translate(cmd.getArgs()[0], cmd.getArgs()[1], cmd.getArgs()[2]);
					break;
				case Command.VERTEX:
					shape.addVertex(cmd.getArgs()[0], cmd.getArgs()[1], cmd.getArgs()[2]);
					break;
				}
			}
		} else {
			shape.setColor(0, 0, 0, 0);
			for (Command cmd : commandList) {
				if (cmd.getCommand() == Command.VERTEX) {
					shape.addVertex(31000000, 31000000, 31000000);
				}
			}
		}

		return shape.getBuffer();
	}

	@Override
	public ByteBuf toPacket() {
		ByteBuf b = Unpooled.buffer();
		b.writeInt(id);
		b.writeBoolean(isVisible);
		b.writeInt(commandList.size());
		for (Command cmd : commandList) {
			b.writeBytes(cmd.toPacket());
		}
		b.writeBytes(modelTransformation.toPacket());

		return null;
	}

	@Override
	public Model fromPacket(ByteBuf buf) {
		id = buf.readInt();
		isVisible = buf.readBoolean();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {

		}
		modelTransformation = new Matrix(0, 0).fromPacket(buf);
		return this;
	}
}
