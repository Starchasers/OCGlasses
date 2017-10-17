package com.bymarcin.openglasses.item;

import java.util.List;

import com.bymarcin.openglasses.manual.IItemWithDocumentation;
import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.surface.WidgetModifierConditionType;
import com.bymarcin.openglasses.utils.utilsCommon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.utils.Location;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraft.util.EnumFacing;

import net.minecraft.world.World;

import javax.annotation.Nullable;

public class OpenGlassesItem extends ItemArmor implements IItemWithDocumentation {
	public OpenGlassesItem() {
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.HEAD);
		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(OpenGlasses.creativeTab);
		setUnlocalizedName("openglasses");
		setRegistryName("openglasses");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		//configure creative glasses
		ItemStack creativeGlasses = OpenGlasses.glassesStack.copy();
		NBTTagCompound creativeTag = creativeGlasses.getTagCompound();
		creativeTag.setInteger("Energy", 5000000);
		creativeTag.setInteger("EnergyCapacity", 5000000);
		creativeTag.setInteger("widgetLimit", 255);
		creativeTag.setInteger("upkeepCost", 0);
		creativeTag.setInteger("radarRange", 128); //set the maximum radar range to 128
		creativeTag.setBoolean("daylightDetector", true);
		creativeTag.setBoolean("tankUpgrade", true);
		creativeTag.setBoolean("motionsensor", true);
		creativeTag.setBoolean("geolyzer", true);

		subItems.add(OpenGlasses.glassesStack);
		subItems.add(creativeGlasses);
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
		if(nbt != null)
			stack.setTagCompound(nbt);

		return new EnergyCapabilityProvider(stack);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return OpenGlasses.MODID + ":textures/models/glasses.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		NBTTagCompound tag = stack.getTagCompound();

		if(tag.getLong("uniqueKey") > 0L){
			tooltip.add("linked to: X: " + tag.getInteger("X") + ", Y: " + tag.getInteger("Y") + ", Z: " + tag.getInteger("Z") + " (DIM: " + tag.getInteger("DIM") +")");
			tooltip.add("terminal: " + tag.getLong("uniqueKey"));
			tooltip.add("user: " + tag.getString("user"));
		}
		else
			tooltip.add("use at glassesterminal to link glasses");


		if(tag.getBoolean("daylightDetector"))
			tooltip.add("lightsensor: installed");
		else {
			tooltip.add("lightsensor: not installed");
			tooltip.add("(install on anvil with minecraft daylight sensor)");
		}
		if(tag.getBoolean("tankUpgrade"))
			tooltip.add("rainsensor: installed");
		else {
			tooltip.add("rainsensor: not installed");
			tooltip.add("(install on anvil with opencomputers tank upgrade)");
		}
		if(tag.getBoolean("motionsensor"))
			tooltip.add("sneak detection: installed");
		else {
			tooltip.add("sneak detection: not installed");
			tooltip.add("(install on anvil with opencomputers motionsensor)");
		}
		if(tag.getBoolean("geolyzer")) {
			tooltip.add("geolyzer: installed");
			tooltip.add("radar Range: " + tag.getInteger("radarRange"));
		}
		else {
			tooltip.add("geolyzer: not installed");
			tooltip.add("(install on anvil with opencomputers geolyzer to enable swimming detection)");
		}


		int widgetCount = ClientSurface.instances.getWidgetCount();
		tooltip.add("using " + widgetCount + "/" + tag.getInteger("widgetLimit") + " widgets");

		IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		tooltip.add(String.format("%s/%s FE", storage.getEnergyStored(), storage.getMaxEnergyStored()));
		tooltip.add("usage " + tag.getInteger("upkeepCost") + " FE/tick");
	}

	public String getDocumentationName(ItemStack stack){
		return "Glasses";
	}

	public void bindToTerminal(ItemStack glassesStack, Location uuid, EntityPlayer player) {
		NBTTagCompound tag = glassesStack.getTagCompound();
		tag.setInteger("X", uuid.x);
		tag.setInteger("Y", uuid.y);
		tag.setInteger("Z", uuid.z);
		tag.setInteger("DIM", uuid.dimID);
		tag.setLong("uniqueKey", uuid.uniqueKey);

		tag.setString("userUUID", player.getGameProfile().getId().toString());
		tag.setString("user", player.getGameProfile().getName());
	}


	public long getConditionStates(ItemStack glassesStack, EntityPlayer player){
		long curConditionStates = 0;
		long checkConditions = ~0;
		NBTTagCompound tag = glassesStack.getTagCompound();

		if(tag.getBoolean("overlayActive"))
			curConditionStates |= ((long) 1 << WidgetModifierConditionType.OVERLAY_ACTIVE);
		else
			curConditionStates |= ((long) 1 << WidgetModifierConditionType.OVERLAY_INACTIVE);

		if(tag.getBoolean("motionsensor") && (((checkConditions >>> WidgetModifierConditionType.IS_SNEAKING) & 1) != 0 || ((checkConditions >>> WidgetModifierConditionType.IS_NOT_SNEAKING) & 1) != 0)){
			if(player.isSneaking())
				curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_SNEAKING);
			else
				curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_NOT_SNEAKING);
		}

		//bs
		if((player.world.getWorldTime() - tag.getLong("lastExtendedConditionCheck")) < 20){
			long States = tag.getLong("conditionStates");
			States &= ~((long) 1 << WidgetModifierConditionType.OVERLAY_ACTIVE);
			States &= ~((long) 1 << WidgetModifierConditionType.OVERLAY_INACTIVE);
			States &= ~((long) 1 << WidgetModifierConditionType.IS_SNEAKING);
			States &= ~((long) 1 << WidgetModifierConditionType.IS_NOT_SNEAKING);
			return (curConditionStates | States );
		}

		tag.setLong("lastExtendedConditionCheck", player.world.getWorldTime());

		if(tag.getBoolean("tankUpgrade") && (((checkConditions >>> WidgetModifierConditionType.IS_WEATHER_RAIN) & 1) != 0 || ((checkConditions >>> WidgetModifierConditionType.IS_WEATHER_CLEAR) & 1) != 0)){
			if(player.world.isRaining())
				curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_WEATHER_RAIN);
			else
				curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_WEATHER_CLEAR);
		}

		if(tag.getBoolean("geolyzer") && (((checkConditions >>> WidgetModifierConditionType.IS_SWIMMING) & 1) != 0 || ((checkConditions >>> WidgetModifierConditionType.IS_NOT_SWIMMING) & 1) != 0)){
			if(utilsCommon.isPlayerSwimming(player))
				curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_SWIMMING);
			else
				curConditionStates |= ((long) 1 << WidgetModifierConditionType.IS_NOT_SWIMMING);
		}

		if(tag.getBoolean("daylightDetector")) {
			int lightLevel = utilsCommon.getLightLevelPlayer(player);

			for (int i = WidgetModifierConditionType.IS_LIGHTLEVEL_MIN_0, l = 0; i < WidgetModifierConditionType.IS_LIGHTLEVEL_MIN_15; i++, l++)
				if (((checkConditions >>> i) & 1) != 0 && lightLevel >= l)
					curConditionStates |= ((long) 1 << i);

			for (int i = WidgetModifierConditionType.IS_LIGHTLEVEL_MAX_0, l = 0; i < WidgetModifierConditionType.IS_LIGHTLEVEL_MAX_15; i++, l++)
				if (((checkConditions >>> i) & 1) != 0 && lightLevel <= l)
					curConditionStates |= ((long) 1 << i);
		}


		return curConditionStates;
	}

	// Forge Energy

	@Override
	public void onUpdate(ItemStack glassesStack, World world, Entity entity, int slot, boolean isCurrentItem) {
		if(world.isRemote) return;
		if (!(entity instanceof EntityPlayer)) return;

		ItemStack glasses = OpenGlasses.getGlassesStack((EntityPlayer) entity);
		if(glasses == null) return;

		if(glasses.equals(glassesStack))
			this.consumeEnergy(glassesStack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack){
		IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(storage.getEnergyStored() >= storage.getMaxEnergyStored())
			return false;
		else
			return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		return 1 - (((double) 1 / storage.getMaxEnergyStored()) * storage.getEnergyStored());
	}

	public int consumeEnergy(ItemStack glassesStack){
		IEnergyStorage storage = glassesStack.getCapability(CapabilityEnergy.ENERGY, null);
		return storage.extractEnergy(glassesStack.getTagCompound().getInteger("upkeepCost"), false);
	}

	public double getEnergyStored(ItemStack glassesStack){
		IEnergyStorage storage = glassesStack.getCapability(CapabilityEnergy.ENERGY, null);
		if(storage == null) return 0;
		return storage.getEnergyStored();
	}

	private static class EnergyCapabilityProvider implements ICapabilityProvider{
		public final EnergyStorage storage;

		public EnergyCapabilityProvider(final ItemStack stack){
			this.storage = new EnergyStorage(0, 1000, 1000){
				@Override
				public int getEnergyStored(){
					return stack.getTagCompound().getInteger("Energy");
				}

				@Override
				public int getMaxEnergyStored(){
					return stack.getTagCompound().getInteger("EnergyCapacity");
				}

				public void setEnergyStored(int energy){
					stack.getTagCompound().setInteger("Energy", energy);
				}

				@Override
				public int receiveEnergy(int receive, boolean simulate){
					int energy = this.getEnergyStored();

					int energyReceived = Math.min(this.getMaxEnergyStored()-energy, Math.min(this.maxReceive, receive));

					if(!simulate) this.setEnergyStored(energy+energyReceived);

					return energyReceived;
				}

				@Override
				public int extractEnergy(int extract, boolean simulate){
					if(!this.canExtract()) return 0;

					int energy = this.getEnergyStored();

					int energyExtracted = Math.min(energy, Math.min(this.maxExtract, extract));
					if(!simulate) this.setEnergyStored(energy-energyExtracted);

					return energyExtracted;
				}
			};
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing){
			return this.getCapability(capability, facing) != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing){
			if(capability == CapabilityEnergy.ENERGY){
				return (T) this.storage;
			}
			return null;
		}
	}
}