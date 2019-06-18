package com.bymarcin.openglasses.event.minecraft.server;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import com.bymarcin.openglasses.surface.OCServerSurface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerEventHandler {
    private static int playerIndex = 0;

    @SubscribeEvent
    public void onWorldJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(!(event.player instanceof EntityPlayerMP))
            return;

        ItemStack glassesStack = OpenGlasses.getGlassesStack(event.player);

        if(!glassesStack.isEmpty())
            OCServerSurface.equipmentChanged((EntityPlayerMP) event.player, glassesStack);
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.player instanceof EntityPlayerMP))
            return;

        OCServerSurface.instance().unsubscribePlayer(event.player.getUniqueID());
    }

    @SubscribeEvent
    public void equipmentChanged(LivingEquipmentChangeEvent event){
        if(event.getEntityLiving() instanceof EntityPlayerMP && event.getSlot().equals(EntityEquipmentSlot.HEAD))
            OCServerSurface.equipmentChanged((EntityPlayerMP) event.getEntityLiving(), event.getTo());
    }

    @SubscribeEvent
    public void tickStart(TickEvent.WorldTickEvent event) {
        int i=0;
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if(i == playerIndex) {
                updatePlayer(player);
                break;
            }
            i++;
        }

        playerIndex++;

        if (playerIndex >= OCServerSurface.instance().players.size()) playerIndex = 0;
    }

    private static void updatePlayer(EntityPlayer player){
        ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

        if(OpenGlasses.isGlassesStack(glassesStack))
            for(UpgradeItem upgrade : OpenGlassesItem.upgrades)
                upgrade.update(player, glassesStack);
    }
}
