package com.bymarcin.openglasses.event.minecraft;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilEvent {
    public static AnvilEvent instances = new AnvilEvent();

    @SubscribeEvent
    public void handleAnvilEvent(AnvilUpdateEvent evt) {
        if(evt.getLeft().isEmpty() || evt.getRight().isEmpty()) return;

        if(!OpenGlasses.isGlassesStack(evt.getLeft())) return;

        if(evt.getRight().getCount() > 1) return; //no support for stacked stuff

        for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
            if(upgrade.isUpgradeItem(evt.getRight())) {
                upgrade.handleAnvilEvent(evt);
                if(ItemStack.areItemStacksEqual(evt.getOutput(), evt.getLeft())) // cancel upgrade if nothing would change
                    evt.setOutput(ItemStack.EMPTY);
                return;
            }
    }
}
