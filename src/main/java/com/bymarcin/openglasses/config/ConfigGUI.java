package com.bymarcin.openglasses.config;

// @author ben_mkiv, based on MinecraftByExample Templates
import com.bymarcin.openglasses.OpenGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ConfigGUI implements IModGuiFactory {
    //this class is accessed when Forge needs a GUI made relating to your mod (e.g. config GUI)

    @Override
    public void initialize(Minecraft minecraftInstance){}

    // called when your GUI needs to be created
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new OpenGlassesConfigGUI(parentScreen);
    }

    public boolean hasConfigGui() {
        return true;
    }

    // This function is needed for implementation only, the config gui does not require them
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }


    //This class inherits from GuiConfig, a specialized GuiScreen designed to display your
    // configuration categories
    public static class OpenGlassesConfigGUI extends GuiConfig {

        public OpenGlassesConfigGUI(GuiScreen parentScreen){
            //I18n function basically "translates" or localizes the given key using the appropriate .lang file
            super(parentScreen, getConfigElements(), OpenGlasses.MODID,
                    false, false, I18n.format("gui.openglasses.config.title"));
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<>();
            list.add(new DummyConfigElement.DummyCategoryElement("general", "gui.openglasses.config.general", CategoryEntryCustom.class));
            return list;
        }
    }


    public static class CategoryEntryCustom extends GuiConfigEntries.CategoryEntry {

        public CategoryEntryCustom(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop){
            super(owningScreen, owningEntryList, prop);
        }

        protected GuiScreen buildChildScreen(String categoryName){
            ConfigElement category = new ConfigElement(Config.getConfig().getCategory(categoryName));

            return new GuiConfig(this.owningScreen, category.getChildElements(),
                    this.owningScreen.modID,
                    categoryName,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    I18n.format(category.getLanguageKey()));
        }

        @Override
        protected GuiScreen buildChildScreen(){
            return buildChildScreen(getName());
        }

    }


}