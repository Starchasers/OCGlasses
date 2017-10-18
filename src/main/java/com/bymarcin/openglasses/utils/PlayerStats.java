package com.bymarcin.openglasses.utils;

import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PlayerStats {
    public int screenWidth, screenHeight;
    public double guiScale;

    public UUID uuid;
    public String name;

    public PlayerStats(EntityPlayer player){
        this.uuid = player.getUniqueID();
        this.name = player.getGameProfile().getName();
        this.setScreen(0, 0, 0);
    }

    public void setScreen(int w, int h, double s){
        this.screenWidth = w;
        this.screenHeight = h;
        this.guiScale = s;
    }

}
