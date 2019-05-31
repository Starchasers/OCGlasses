package com.bymarcin.openglasses.drivers;

import com.bymarcin.openglasses.block.OpenGlassesTerminalBlock;
import com.bymarcin.openglasses.component.OpenGlassesHostItem;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.common.Tier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DriverTerminal extends OpenGlassesTerminalBlock implements DriverItem, EnvironmentProvider, HostAware {
    public static DriverTerminal driver = new DriverTerminal();

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().equals(Item.getItemFromBlock(OpenGlassesTerminalBlock.DEFAULT_BLOCK));
    }

    @Override
    public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
        if(!worksWith(stack))
            return false;

        return true;
    }

    @Override
    public Class<? extends Environment> getEnvironment(ItemStack stack) {
        return worksWith(stack) ? OpenGlassesHostItem.class : null;
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost container) {
        return new OpenGlassesHostItem(container);
    }

    @Override
    public String slot(ItemStack stack){
        return Slot.Upgrade;
    }

    @Override
    public int tier(ItemStack stack) {
        return Tier.Two();
    }

    @Override
    public NBTTagCompound dataTag(ItemStack stack) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        final NBTTagCompound nbt = stack.getTagCompound();
        // This is the suggested key under which to store item component data.
        // You are free to change this as you please.
        if(!nbt.hasKey("oc:data")) {
            nbt.setTag("oc:data", new NBTTagCompound());
        }
        return nbt.getCompoundTag("oc:data");
    }
}
