package com.bymarcin.openglasses.integration.opencomputers;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import li.cil.oc.api.IMC;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.common.Tier;
import li.cil.oc.common.init.Items;
import li.cil.oc.common.template.TabletTemplate;
import li.cil.oc.common.template.Template;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.logging.Logger;


public class ocAssembler {
    public static void register(){
        String   select = "com.bymarcin.openglasses.integration.opencomputers.ocAssembler.select";
        String validate = "com.bymarcin.openglasses.integration.opencomputers.ocAssembler.validate";
        String assemble = "com.bymarcin.openglasses.integration.opencomputers.ocAssembler.assemble";

        int containerTiers[] = new int[]{ Tier.Three() };
        int upgradeTiers[] = new int[]{ Tier.Three() };

        ArrayList<Pair<String, Integer>> componentSlots = new ArrayList<>();

        componentSlots.add(new ImmutablePair<>(Slot.CPU, Tier.Three()));
        componentSlots.add(new ImmutablePair<>(Slot.Memory, Tier.Three()));
        componentSlots.add(new ImmutablePair<>(Slot.HDD, Tier.Three()));
        componentSlots.add(new ImmutablePair<>(Slot.ComponentBus, Tier.Three()));


        //Items.registerItem(OpenGlasses.openGlasses, "openglasses");

        Logger.getLogger(OpenGlasses.MODID).info("registering assembler recipe");
        IMC.registerAssemblerTemplate("openglasses", select, validate, assemble, OpenGlassesItem.class, containerTiers, upgradeTiers, componentSlots);
    }

    public static boolean select(ItemStack stack){
        return true;
    }

    public static Object[] validate(IInventory inventory){
        return new Object[]{ true };
        //return new Object[]{ true, new TextComponentString("toolbar text"), new TextComponentString("tooltip text")};
    }

    public static Object[] assemble(IInventory inventory){
        return new Object[]{ inventory.getStackInSlot(0), 1000D };
    }

    /*
    public static boolean validate(IInventory inventory, int slot, int tier, ItemStack stack){
        return true;
    }
    */



}
