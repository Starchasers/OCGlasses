package com.bymarcin.openglasses.item.upgrades;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class UpgradeItem {
    public abstract boolean isUpgradeItem(@Nonnull ItemStack stack);

    public void handleAnvilEvent(AnvilUpdateEvent evt) {
        if (isUpgradeItem(evt.getRight())) {
            // add upgrade to the input item
            ItemStack output = install(evt.getLeft().copy());
            // update energy usage
            OpenGlassesItem.upgradeUpkeepCost(output);
            evt.setOutput(output);
            // set anvil experience cost
            evt.setCost(getUpgradeExperienceCost());
        }
    }

    public abstract int getUpgradeExperienceCost();

    // installs the upgrade on a itemstacks NBT
    public abstract ItemStack install(ItemStack stack);

    // this method is used to determine the maximum/default power usage
    public int getEnergyUsage(){
        return 0;
    }

    // this method is used to determine the current power usage
    public int getEnergyUsageCurrent(ItemStack stack){
        return getEnergyUsage();
    }

    public abstract boolean isInstalled(ItemStack stack);

    public List<String> getTooltip(ItemStack stack){
        return new ArrayList<>();
    }

    public void updateServer(EntityPlayer player, ItemStack stack){}

    public void updateClient(EntityPlayer player, ItemStack stack){}

    // handles keyboard inputs
    public void onKeyInput(){}
}
