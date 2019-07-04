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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeInfrared extends UpgradeItem {
    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("infrared")) {
            tag.setBoolean("infrared", true);
        }

        return stack;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 20;
    }

    @Override
    public int getEnergyUsage(){
        return 20;
    }

    public int getEnergyUsageCurrent(ItemStack stack){
        return getMode(stack) ? getEnergyUsage() : 1;
    }

    @Override
    public boolean isInstalled(ItemStack stack){
        return hasUpgrade(stack);
    }

    @Override
    public boolean isUpgradeItem(@Nonnull ItemStack stack){
        return !stack.isEmpty() && stack.getItem().equals(Items.POTIONITEM)
                && (stack.getTagCompound().getString("Potion").equals("minecraft:night_vision")
                || stack.getTagCompound().getString("Potion").equals("minecraft:long_night_vision"));
    }

    public static boolean hasUpgrade(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("nightvision");
    }

    @Override
    public List<String> getTooltip(ItemStack stack){
        List<String> tooltip = new ArrayList<>();
        if(hasUpgrade(stack)) {
            tooltip.add("infrared: installed (active: " + getMode(stack) + ")");
        }
        else {
            tooltip.add("infrared not installed");
            tooltip.add("§8requires infrared§7");
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

        glassesStack.getTagCompound().setBoolean("infraredActive", active);
    }

    private static boolean shouldEnableInfrared(EntityPlayer player, ItemStack glassesStack){
        if(!hasUpgrade(glassesStack))
            return false;

        if(OpenGlassesItem.getEnergyStored(glassesStack) == 0)
            return false;

        return getMode(glassesStack);
    }

    private static boolean getMode(ItemStack glassesStack){
        return glassesStack.getTagCompound().getBoolean("infraredActive");
    }

    public static void toggleInfraredMode(EntityPlayer player){
        ItemStack stack = OpenGlasses.getGlassesStack(player);
        if(OpenGlasses.isGlassesStack(stack)) {
            boolean mode = !getMode(stack);
            player.sendStatusMessage(new TextComponentString("infrared active: " + getMode(stack)), true);
        }
    }

    @Override
    public void update(EntityPlayer player, ItemStack glassesStack){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return;

        PlayerStatsOC stats = OCServerSurface.getStats((EntityPlayerMP) player);

        boolean wasActive = getMode(glassesStack);

        if (shouldEnableInfrared(player, glassesStack)){
            setMode(glassesStack, true);

            if(!wasActive) {
                OpenGlassesItem.upgradeUpkeepCost(glassesStack);
                GlassesNBT.syncStackNBT(glassesStack, (EntityPlayerMP) player);
            }

            stats.infraredActive = true;
        }
        else if(stats.infraredActive){
            setMode(glassesStack, false);

            if(wasActive) {
                OpenGlassesItem.upgradeUpkeepCost(glassesStack);
                GlassesNBT.syncStackNBT(glassesStack, (EntityPlayerMP) player);
            }

            stats.infraredActive = false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onKeyInput(){
        if(!ClientKeyboardEvents.infraredModeKey.isPressed())
            return;

        if(!hasUpgrade(OCClientSurface.glasses.get()))
            return;

        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(null, GlassesEventPacket.EventType.TOGGLE_INFRARED));
    }



}
