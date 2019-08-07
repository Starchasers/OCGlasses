package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.item.upgrades.UpgradeItem;
import com.bymarcin.openglasses.utils.GlassesInstance;
import com.bymarcin.openglasses.utils.OpenGlassesHostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;

import static com.bymarcin.openglasses.item.OpenGlassesItem.upgrades;

public class GlassesInitSequence {
    static ArrayList<String> initSequenceLines = new ArrayList<>();
    static int initSequenceChars = 0;

    static boolean renderInitSequence(ItemStack glasses){
        if(glasses.isEmpty())
            return false;

        long wornTicks = OCClientSurface.instance().wornTicks;

        if(OCClientSurface.glasses.energyStored == 0 || wornTicks > initSequenceChars + 80)
            return false;

        GlStateManager.pushMatrix();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        int i=0;
        int charsPrinted = 0;
        for(String line : initSequenceLines){
            if(wornTicks < charsPrinted+line.length()) {
                int chars = (int) wornTicks - charsPrinted;
                fontRenderer.drawString(line.substring(0, chars) + " #", 10, ++i * 10, 0xCCFFFFFF);
                break;
            }

            fontRenderer.drawString(line, 10, ++i * 10, 0xCCFFFFFF);
            charsPrinted+=line.length();
        }

        GlStateManager.popMatrix();
        return true;
    }

    static void initalize(){
        initSequenceLines.clear();
        initSequenceChars = 0;

        initSequenceLines.add("");
        initSequenceLines.add("# initializing upgrades");
        for (UpgradeItem upgrade : upgrades)
            initSequenceLines.addAll(upgrade.getTooltip(OCClientSurface.glasses.get()));

        initSequenceLines.add("");

        NBTTagCompound tag = OCClientSurface.glasses.get().getTagCompound();
        int energyUsage = tag.getInteger("upkeepCost");
        IEnergyStorage storage = OCClientSurface.glasses.get().getCapability(CapabilityEnergy.ENERGY, null);
        initSequenceLines.add("# Energy Buffer");
        initSequenceLines.add(String.format("%s/%s FE", storage.getEnergyStored(), storage.getMaxEnergyStored()));
        initSequenceLines.add("usage " + energyUsage + " FE/tick");

        initSequenceLines.add("");

        //widgets arent cached when we run this, so nope :(
        //int widgetCount = OCClientSurface.instance().getWidgetCount(null, null);
        //initSequenceLines.add("# Widget Cache " + widgetCount + "/" + tag.getInteger("widgetLimit") + " widgets");
        //initSequenceLines.add("");

        initSequenceLines.add("# initializing "+OCClientSurface.glasses.getHosts().size()+" hosts");
        for (GlassesInstance.HostClient host : OCClientSurface.glasses.getHosts().values()) {
            OpenGlassesHostClient hc = OCClientSurface.instance().getHost(host.uuid);
            if(hc.terminalName.length() > 0)
                initSequenceLines.add(":: "+hc.terminalName);
            initSequenceLines.add(":: uuid "+host.uuid.toString());
            initSequenceLines.add(":: type: "+hc.hostType);
            //initSequenceLines.add("(i) overlay widgets: " + hc.renderables.size());
            //initSequenceLines.add("(i) world widgets: " + hc.renderablesWorld.size());
        }

        for(String line : initSequenceLines)
            initSequenceChars+=line.length();
    }
}
