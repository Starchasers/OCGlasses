package com.bymarcin.openglasses.drivers;

import com.bymarcin.openglasses.component.OpenGlassesHostItem;
import com.bymarcin.openglasses.item.OpenGlassesHostCard;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.item.ItemStack;

public class DriverHostCard extends DriverItem {
    public static DriverHostCard driver = new DriverHostCard();

    public DriverHostCard() {
        super(OpenGlassesHostCard.DEFAULTSTACK);
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost container) {
        return new OpenGlassesHostItem(container);
    }

    @Override
    public String slot(ItemStack stack) {
        return Slot.Card;
    }
}
