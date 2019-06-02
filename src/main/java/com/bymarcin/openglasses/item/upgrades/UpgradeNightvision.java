package com.bymarcin.openglasses.item.upgrades;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.surface.OCServerSurface;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UpgradeNightvision extends UpgradeItem {
    public static Potion potionNightvision = Potion.getPotionFromResourceLocation("night_vision");
    public static KeyBinding nightvisionModeKey = new KeyBinding("key.nightvision", Keyboard.KEY_N, "key.categories." + OpenGlasses.MODID.toLowerCase());
    public enum nightVisionModes { OFF, AUTO, ON }

    @Override
    public ItemStack install(ItemStack stack){
        NBTTagCompound tag = stack.getTagCompound();

        if(!tag.getBoolean("nightvision")) {
            tag.setBoolean("nightvision", true);
            tag.setInteger("upkeepCost", tag.getInteger("upkeepCost") + getEnergyUsage()); //increase power usage by 1
        }

        return stack;
    }

    @Override
    public int getUpgradeExperienceCost(){
        return 20;
    }

    @Override
    public int getEnergyUsage(){
        return 5;
    }

    public int getEnergyUsageCurrent(ItemStack stack){
        return stack.getTagCompound().getBoolean("nightVisionActive") ? getEnergyUsage() : 1;
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
            tooltip.add("nightvision: installed (mode: " + nightVisionModes.values()[stack.getTagCompound().getInteger("nightvisionMode")].name() + ")");
        }
		else {
            tooltip.add("nightvision not installed");
            tooltip.add("(install on anvil with any potion of nightvision)");
        }

        return tooltip;
    }

    private static int toggle(ItemStack glassesStack){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return -1;

        int mode = glassesStack.getTagCompound().getInteger("nightvisionMode") + 1;

        if(mode >= nightVisionModes.values().length)
            mode = 0;

        setMode(glassesStack, mode);

        return mode;
    }

    private static void setMode(ItemStack glassesStack, int mode){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return;

        glassesStack.getTagCompound().setInteger("nightvisionMode", mode);

        OpenGlassesItem.upgradeUpkeepCost(glassesStack);
    }

    private static boolean shouldEnableNightvision(EntityPlayer player, ItemStack glassesStack){
        if(!hasUpgrade(glassesStack))
            return false;

        if(OpenGlassesItem.getEnergyStored(glassesStack) == 0)
            return false;

        switch(getMode(glassesStack)){
            case ON:
                return true;
            case AUTO:
                return (player.getBrightness() < 0.3F);
            case OFF:
            default:
                return false;
        }
    }

    private static nightVisionModes getMode(ItemStack glassesStack){
        return nightVisionModes.values()[glassesStack.getTagCompound().getInteger("nightvisionMode")];
    }

    public static void toggleNightvisionMode(EntityPlayer player){
        ItemStack stack = OpenGlasses.getGlassesStack(player);
        if(OpenGlasses.isGlassesStack(stack)) {
            int mode = UpgradeNightvision.toggle(stack);
            player.sendStatusMessage(new TextComponentString("nightvision mode: " + nightVisionModes.values()[mode].name()), true);
        }
    }

    @Override
    public void update(EntityPlayer player, ItemStack glassesStack){
        if(!OpenGlasses.isGlassesStack(glassesStack))
            return;

        PlayerStatsOC stats = (PlayerStatsOC) OCServerSurface.instance().playerStats.get(player.getUniqueID());

        if(stats == null)
            return;

        if (shouldEnableNightvision(player, glassesStack)){
            player.addPotionEffect(new PotionEffect(potionNightvision, 500, 0, false, false));
            glassesStack.getTagCompound().setBoolean("nightVisionActive", true);
            stats.nightVisionActive = true;
        }
        else if(stats.nightVisionActive){
            player.removePotionEffect(potionNightvision);
            glassesStack.getTagCompound().setBoolean("nightVisionActive", false);
            stats.nightVisionActive = false;
        }
    }

    @Override
    public void onKeyInput(){
        if(!nightvisionModeKey.isPressed())
            return;

        if(!hasUpgrade(OCClientSurface.instance().glassesStack))
            return;

        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(null, GlassesEventPacket.EventType.TOGGLE_NIGHTVISION));
    }
}
