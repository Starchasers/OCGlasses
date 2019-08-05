package com.bymarcin.openglasses.item.upgrades;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.minecraft.client.ClientKeyboardEvents;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.surface.OCServerSurface;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeThermalVision extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("thermalvision")) {
            tag.setBoolean("thermalvision", true);
        }

        return stack;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 420;
    }

    @Override
    public int getEnergyUsage(){
        return 10;
    }

    public int getEnergyUsageCurrent(ItemStack stack){
        return getMode(stack) ? getEnergyUsage() : 0;
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return hasUpgrade(stack);
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return false; // no upgrade item, this upgrade depends on other upgrades
    }

    public static boolean hasUpgrade(ItemStack stack){
        return UpgradeGeolyzer.hasUpgrade(stack)
            && UpgradeNightvision.hasUpgrade(stack)
            && UpgradeDaylightDetector.hasUpgrade(stack);
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        List<String> tooltip = new ArrayList<>();
        if(hasUpgrade(stack)) {
            tooltip.add("thermalvision: available (active: " + getMode(stack) + ")");
        }
        else {
            tooltip.add("thermalvision not available");
            if(!UpgradeGeolyzer.hasUpgrade(stack))
                tooltip.add("§8requires geolyzer§7");
            if(!UpgradeNightvision.hasUpgrade(stack))
                tooltip.add("§8requires nightvision§7");
            if(!UpgradeDaylightDetector.hasUpgrade(stack))
                tooltip.add("§8requires lightsensor§7");
        }

        return tooltip;
    }

    private static boolean toggle(ItemStack glassesStack){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return false;

        boolean mode = !getMode(glassesStack);

        setMode(glassesStack, mode);

        return mode;
    }

    private static void setMode(ItemStack glassesStack, boolean active){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return;

        glassesStack.getTagCompound().setBoolean("thermalActive", active);
    }

    private static boolean shouldEnableInfrared(EntityPlayer player, ItemStack glassesStack){
        if(!hasUpgrade(glassesStack))
            return false;

        if(OpenGlassesItem.getEnergyStored(glassesStack) == 0)
            return false;

        return getMode(glassesStack);
    }

    private static boolean getMode(ItemStack glassesStack){
        return glassesStack.getTagCompound().getBoolean("thermalActive");
    }

    public static void toggleInfraredMode(EntityPlayer player){
        ItemStack stack = OpenGlasses.getGlassesStack(player);
        if(OpenGlasses.isGlassesStack(stack)) {
            setMode(stack, !getMode(stack));
            player.sendStatusMessage(new TextComponentString("thermal vision active: " + getMode(stack)), true);
        }
    }

    @Override
    public void updateServer(EntityPlayer player, ItemStack glassesStack){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return;

        PlayerStatsOC stats = OCServerSurface.getStats((EntityPlayerMP) player);

        boolean wasActive = stats.thermalActive;

        if (shouldEnableInfrared(player, glassesStack)){
            setMode(glassesStack, true);

            if(!wasActive) {
                OpenGlassesItem.upgradeUpkeepCost(glassesStack);
                GlassesNBT.syncStackNBT(glassesStack, (EntityPlayerMP) player);
            }

            stats.thermalActive = true;
        }
        else if(stats.thermalActive){
            setMode(glassesStack, false);

            if(wasActive) {
                OpenGlassesItem.upgradeUpkeepCost(glassesStack);
                GlassesNBT.syncStackNBT(glassesStack, (EntityPlayerMP) player);
            }

            stats.thermalActive = false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onKeyInput(){
        if(!ClientKeyboardEvents.thermalvisionModeKey.isPressed())
            return;

        if(!hasUpgrade(OCClientSurface.glasses.get()))
            return;

        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(null, GlassesEventPacket.EventType.TOGGLE_INFRARED));
    }

}
