package com.bymarcin.openglasses.manual;

import com.bymarcin.openglasses.OpenGlasses;
import li.cil.oc.api.Manual;
import li.cil.oc.api.manual.PathProvider;
import li.cil.oc.api.prefab.TextureTabIconRenderer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Vexatos, ben-mkiv
 */
public class ManualPathProvider implements PathProvider {

    public static void initialize() {
        Manual.addProvider(new ManualPathProvider());
        Manual.addProvider(new ManualContentProvider());
        Manual.addTab(new TextureTabIconRenderer(new ResourceLocation(OpenGlasses.MODID, "textures/blocks/glasses_side.png")),
                "openGlasses", "_Sidebar");
    }

    @Override
    public String pathFor(ItemStack stack) {
        if(stack == null) return null;

        if(stack.getItem() instanceof IItemWithDocumentation) {
            return ((IItemWithDocumentation) stack.getItem()).getDocumentationName(stack);
        }
        if(stack.getItem() instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(stack.getItem());
            if(block instanceof IBlockWithDocumentation) {
                return ((IBlockWithDocumentation) block).getDocumentationName(stack);
            }
        }
        return null;
    }

    @Override
    public String pathFor(World world, BlockPos pos) {
        if(world == null) return null;

        Block block = world.getBlockState(pos).getBlock();
        if(block instanceof IBlockWithDocumentation) {
            return ((IBlockWithDocumentation) block).getDocumentationName(world, pos);
        }
        return null;
    }

}