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
        return getTileEntity(this);
    }

    public OpenGlassesTerminalTileEntity getTerminal(){
        return getTerminal(this);
    }

    public static TerminalLocation getGlassesTerminalUUID(ItemStack itemStack){
        if(itemStack == null || !itemStack.hasTagCompound())
            return null;

        NBTTagCompound tag = itemStack.getTagCompound();
        if (!tag.hasKey("location")) return null;
        return (TerminalLocation) new TerminalLocation().readFromNBT(tag.getCompoundTag("location"));
    }

    private static TileEntity getTileEntity(Location uuid){
        if(uuid == null) return null;
        World world  = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(uuid.dimID);
        return world != null ? world.getTileEntity(uuid.pos) : null;
    }

    public static OpenGlassesTerminalTileEntity getTerminal(Location uuid){
        TileEntity te = getTileEntity(uuid);
        return te instanceof OpenGlassesTerminalTileEntity ? (OpenGlassesTerminalTileEntity) te : null;
    }

    public static OpenGlassesTerminalTileEntity getTerminal(ItemStack stack){
        TerminalLocation uuid = getGlassesTerminalUUID(stack);
        TileEntity te = getTileEntity(uuid);

        return te instanceof OpenGlassesTerminalTileEntity ? (OpenGlassesTerminalTileEntity) te : null;
    }
}
