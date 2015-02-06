package com.bymarcin.openglasses.testRender;

import net.minecraft.nbt.NBTTagCompound;

public class Command {
	public static final int ROTATE = 0;
	public static final int TRANSLATE = 1;
	public static final int VERTEX = 2;
	public static final int COLOR = 3;
	public static final int SCALE = 4;
	public static final int PUSHMATRIX = 5;
	public static final int POPMATRIX = 6;

	private int cmd;
	private float[] args;

	public Command(int command) {
		cmd = command;
	}

	public Command(int command, float... args) {
		cmd = command;
		this.args = args;
	}

	public float[] getArgs() {
		return args;
	}

	public int getCommand() {
		return cmd;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("cmd", cmd);
		if (args != null && args.length != 0) {
			tag.setInteger("argsCount", args.length);
			for (Integer i = 0; i < args.length; i++) {
				tag.setFloat(i.toString(), args[i]);
			}
		}
	}

	public void readFromNBT(NBTTagCompound tag) {
		cmd = tag.getInteger("cmd");

		if (tag.hasKey("argsCount")) {
			int argsCount = tag.getInteger("argsCount");
			args = new float[argsCount];

			for (Integer i = 0; i < argsCount; i++) {
				args[i] = tag.getFloat(i.toString());
			}
		}
	}

}
