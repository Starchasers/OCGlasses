package com.bymarcin.openglasses.integration.opencomputers;

import com.bymarcin.openglasses.OpenGlasses;
import li.cil.oc.api.IMC;
import li.cil.oc.api.fs.FileSystem;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.Optional;

import java.util.concurrent.Callable;

public class ocProgramDisks {
    public static void register(){
        //register loot disks
        li.cil.oc.api.Items.registerFloppy("openGlasses Demo", EnumDyeColor.GREEN, new OCLootDiskFileSystem("openglasses-demo"), true);
        IMC.registerProgramDiskLabel("openglasses-demo", "openglasses-demo", "Lua 5.2", "Lua 5.3", "LuaJ");

        li.cil.oc.api.Items.registerFloppy("wavefront objects", EnumDyeColor.BLUE, new OCLootDiskFileSystem("wavefrontObjects"), true);
        IMC.registerProgramDiskLabel("wavefrontObjects", "wavefrontObjects", "Lua 5.2", "Lua 5.3", "LuaJ");
    }

    private static class OCLootDiskFileSystem implements Callable<FileSystem> {
        private final String name;
        OCLootDiskFileSystem(String name) {
            this.name = name;
        }

        @Override
        @Optional.Method(modid = "opencomputers")
        public FileSystem call() throws Exception {
            return li.cil.oc.api.FileSystem.asReadOnly(li.cil.oc.api.FileSystem.fromClass(OpenGlasses.class, OpenGlasses.MODID, "loot/" + this.name));
        }
    }
}
