package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.OpenGlasses;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.LanguageRegistry;


public class OGUtils {
	
	public static int getIntFromColor(float red, float green, float blue, float alpha){
	    int R = Math.round(255 * red);
	    int G = Math.round(255 * green);
	    int B = Math.round(255 * blue);
	    int A = Math.round(255 * alpha);
	    A = (A << 24) & 0xFF000000;
	    R = (R << 16) & 0x00FF0000;
	    G = (G << 8) & 0x0000FF00;
	    B = B & 0x000000FF;
	    return A | R | G | B;
	}
	
	public static boolean inRange(double x, double y, double z, double sx, double sy, double sz, double r){
		return (((x-sx)*(x-sx)) + ((y-sy)*(y-sy)) + ((z-sz)*(z-sz))) <= (r*r);
	}
	
	public static void writeString(ByteBuf buff, String value){
		ByteBufUtils.writeUTF8String(buff, value);
	}
	
	public static String readString(ByteBuf buff){
		return ByteBufUtils.readUTF8String(buff);
	}
	
	public static String getLocalization(String name){
		return LanguageRegistry.instance().getStringLocalization(OpenGlasses.MODID + name);
	}
	
	
	public static EntityPlayerMP getPlayerMP(String name){
		for(Object p : MinecraftServer.getServer().getConfigurationManager().playerEntityList){
			if(((EntityPlayerMP)p).getGameProfile().getName().equals(name))
				return (EntityPlayerMP) p;
		}
		return null;	
	}
	
	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		World world = OpenGlasses.proxy.getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(x, y, z);
	}

	public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z) {
		World world = MinecraftServer.getServer().worldServerForDimension(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(x, y, z);
	}
}
