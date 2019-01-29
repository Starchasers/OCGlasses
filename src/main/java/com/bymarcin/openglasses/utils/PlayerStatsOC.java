package com.bymarcin.openglasses.utils;

import com.bymarcin.openglasses.OpenGlasses;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;

import com.bymarcin.openglasses.utils.nightvision.nightVisionModes;
import static com.bymarcin.openglasses.utils.nightvision.potionNightvision;

public class PlayerStatsOC extends ben_mkiv.commons0815.utils.PlayerStats {
    public boolean nightVisionActive = false; //this is used to keep track of nv state for the unsubscribe event where we cant edit the NBT of the glasses anymore

    public Conditions conditions;

    public PlayerStatsOC(EntityPlayer player){
        super(player);
        conditions = new Conditions();
    }

    public void toggleNightvisionMode(EntityPlayer player){
        ItemStack glassesStack = OpenGlasses.getGlassesStack(player);
        if(glassesStack == null)
            return;

        int mode = glassesStack.getTagCompound().getInteger("nightvisionMode") + 1;

        if(mode >= nightVisionModes.values().length)
            mode = 0;

        glassesStack.getTagCompound().setInteger("nightvisionMode", mode);

        player.sendStatusMessage(new TextComponentString("nightvision mode: " + nightVisionModes.values()[mode].name()), true);
    }

    public void updateNightvision(EntityPlayer player){
        ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

        if(glassesStack == null)
            return;

        if (shouldEnableNightvision(player, glassesStack)){
            player.addPotionEffect(new PotionEffect(potionNightvision, 500, 0, false, false));
            glassesStack.getTagCompound().setBoolean("nightVisionActive", true);
            nightVisionActive = true;
        }
        else if(nightVisionActive){
            player.removePotionEffect(potionNightvision);
            glassesStack.getTagCompound().setBoolean("nightVisionActive", false);
            nightVisionActive = false;
        }
    }

    boolean shouldEnableNightvision(EntityPlayer player, ItemStack glassesStack){
        if(glassesStack == null)
            return false;

        if(!glassesStack.getTagCompound().getBoolean("nightvision"))
            return false;

        if(glassesStack.getTagCompound().getInteger("Energy") == 0)
            return false;

        nightVisionModes mode = nightVisionModes.values()[glassesStack.getTagCompound().getInteger("nightvisionMode")];

        if(mode.equals(nightVisionModes.ON))
            return true;

        if(mode.equals(nightVisionModes.AUTO))
            return (player.getBrightness() < 0.3F);

        return false;
    }



}