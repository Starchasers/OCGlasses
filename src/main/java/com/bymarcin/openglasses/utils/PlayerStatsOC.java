package com.bymarcin.openglasses.utils;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerStatsOC extends ben_mkiv.commons0815.utils.PlayerStats {
    public boolean nightVisionActive = false; //this is used to keep track of nv state for the unsubscribe event where we cant edit the NBT of the glasses anymore

    public Conditions conditions;

    public PlayerStatsOC(EntityPlayer player){
        super(player);
        conditions = new Conditions();
    }

}