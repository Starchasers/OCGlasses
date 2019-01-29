package com.bymarcin.openglasses.utils;

import ben_mkiv.commons0815.utils.Location;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class TerminalLocation extends Location {

    public TerminalLocation(BlockPos pos, int dimID, UUID uniqueKey){
        super(pos, dimID, uniqueKey);
    }

    public TerminalLocation(){
        super();
    }

    public TileEntity getTileEntity(){
        World world  = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimID);
        if(world==null)
            return null;

        return world.getTileEntity(pos);
    }

    public OpenGlassesTerminalTileEntity getTerminal(){
        TileEntity te = getTileEntity();

        if(!(te instanceof OpenGlassesTerminalTileEntity))
            return null;

        return (OpenGlassesTerminalTileEntity) te;
    }

    public static TerminalLocation getGlassesTerminalUUID(ItemStack itemStack){
        NBTTagCompound tag = itemStack.getTagCompound();
        if (!tag.hasKey("location")) return null;
        return (TerminalLocation) new TerminalLocation().readFromNBT(tag.getCompoundTag("location"));
    }
}
