package com.bymarcin.openglasses.item.upgrades;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.minecraft.client.ClientKeyboardEvents;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeOpenSecurity extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("opensecurity")){
            tag.setBoolean("opensecurity", true);
        }
        return stack;
    }

    @Override
    public int getEnergyUsage(){
        return 1;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 20;
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("opensecurity", "nanodna"));
    }

    public static boolean hasUpgrade(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("opensecurity");
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return hasUpgrade(stack);
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        if(!OpenGlasses.opensecurity)
            return new ArrayList<String>();

        List<String> tooltip = new ArrayList<>();
        if(hasUpgrade(stack))
            tooltip.add("opensecurity: installed");
        else {
            tooltip.add("opensecurity: not installed");
            tooltip.add("§8requires OpenSecurity NanoDNA§7");
        }

        return tooltip;
    }

    private static void setMode(ItemStack glassesStack, boolean active){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return;

        glassesStack.getTagCompound().setBoolean("osActive", active);
    }

    public static boolean getMode(ItemStack glassesStack){
        return glassesStack.getTagCompound().getBoolean("osActive");
    }

    public static void toggleMode(EntityPlayer player){
        ItemStack stack = OpenGlasses.getGlassesStack(player);
        if(OpenGlasses.isGlassesStack(stack)) {
            setMode(stack, !getMode(stack));
            player.sendStatusMessage(new TextComponentString("opensecurity overlay active: " + getMode(stack)), true);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onKeyInput(){
        if(!OpenGlasses.opensecurity || !ClientKeyboardEvents.openSecurityModeKey.isPressed())
            return;

        if(!hasUpgrade(OCClientSurface.glasses.get()))
            return;

        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(null, GlassesEventPacket.EventType.TOGGLE_OPENSECURITY));
    }

}