package com.bymarcin.openglasses.item;

import java.util.List;
import java.util.UUID;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.bymarcin.openglasses.manual.IItemWithDocumentation;
import com.bymarcin.openglasses.surface.OCClientSurface;
import com.bymarcin.openglasses.utils.nightvision;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bymarcin.openglasses.OpenGlasses;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraft.util.EnumFacing;

import net.minecraft.world.World;

import javax.annotation.Nullable;

@Optional.Interface(iface="baubles.api.IBauble",modid="baubles")
public class OpenGlassesItem extends ItemArmor implements IItemWithDocumentation, IBauble {
	final static int nightvisionCostFE = 5;
	public static ItemStack DEFAULT_STACK;

    public OpenGlassesItem() {
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.HEAD);
		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(OpenGlasses.creativeTab);
		setUnlocalizedName("openglasses");
		setRegistryName("openglasses");
	}

	public static void initGlassesStack(ItemStack glassesStack){
		glassesStack.setTagCompound(new NBTTagCompound());

		NBTTagCompound glassesTag = glassesStack.getTagCompound();
		glassesTag.setInteger("widgetLimit", 9); //default to max 9 Widgets
		glassesTag.setInteger("upkeepCost", 1);  //default to upkeep cost of 1FE / tick
		glassesTag.setInteger("radarRange", 0);
		glassesTag.setInteger("Energy", 0);
		glassesTag.setInteger("EnergyCapacity", 50000); //set the default EnergyBuffer to 50k FE
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if(!this.isInCreativeTab(tab)) return;

		//configure creative glasses
		ItemStack creativeGlasses = DEFAULT_STACK.copy();
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
		creativeTag.setBoolean("nightvision", true);

		subItems.add(DEFAULT_STACK);
		subItems.add(creativeGlasses);
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
		if(stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());

		if(nbt != null) {
			NBTTagCompound stackNBT = stack.getTagCompound();
			stackNBT.merge(nbt);
		}

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

		if(tag.hasUniqueId("host")){
			UUID host = tag.getUniqueId("host");

			//if(!FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules().getBoolean("reducedDebugInfo"))
			//	tooltip.add("linked to: X: " + location.pos.getX() + ", Y: " + location.pos.getY() + ", Z: " + location.pos.getZ() + " (DIM: " + location.dimID +")");
			tooltip.add("host: " + host.toString());
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

		if(tag.getBoolean("nightvision"))
			tooltip.add("nightvision: installed (mode: "+ nightvision.nightVisionModes.values()[tag.getInteger("nightvisionMode")].name()+")");
		else {
			tooltip.add("nightvision not installed");
			tooltip.add("(install on anvil with any potion of nightvision)");
		}

		if(tag.getBoolean("geolyzer")) {
			tooltip.add("geolyzer: installed");
			tooltip.add("radar Range: " + tag.getInteger("radarRange"));
		}
		else {
			tooltip.add("geolyzer: not installed");
			tooltip.add("(install on anvil with opencomputers geolyzer to enable swimming detection)");
		}

		int widgetCount = OCClientSurface.instances.getWidgetCount();
		tooltip.add("using " + widgetCount + "/" + tag.getInteger("widgetLimit") + " widgets");

		int energyUsage = tag.getInteger("upkeepCost");
		if(tag.getBoolean("nightVisionActive"))
			energyUsage+=nightvisionCostFE;

		IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		tooltip.add(String.format("%s/%s FE", storage.getEnergyStored(), storage.getMaxEnergyStored()));
		tooltip.add("usage " + energyUsage + " FE/tick");
	}

	public String getDocumentationName(ItemStack stack){
		return "Glasses";
	}

	public static void link(ItemStack glassesStack, UUID hostUUID, EntityPlayer player) {
		if(player.world.isRemote)
		    return;

	    NBTTagCompound tag = glassesStack.getTagCompound();

		tag.setUniqueId("host", hostUUID);
		tag.setString("userUUID", player.getGameProfile().getId().toString());
		tag.setString("user", player.getGameProfile().getName());
	}

	public static void unlink(ItemStack glassesStack, EntityPlayer player) {
		if(player.world.isRemote)
			return;

		NBTTagCompound tag = glassesStack.getTagCompound();

		tag.removeTag("hostMost");
		tag.removeTag("hostLeast");
		tag.removeTag("userUUID");
		tag.removeTag("user");
	}


	// Forge Energy
	@Override
	public void onUpdate(ItemStack glassesStack, World world, Entity entity, int slot, boolean isCurrentItem) {
		if(world.isRemote) return;
		if (!(entity instanceof EntityPlayer)) return;

		ItemStack glasses = OpenGlasses.getGlassesStack((EntityPlayer) entity);
		if(glasses.isEmpty()) return;

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
		int consumed = 0;
		IEnergyStorage storage = glassesStack.getCapability(CapabilityEnergy.ENERGY, null);
		consumed+= storage.extractEnergy(glassesStack.getTagCompound().getInteger("upkeepCost"), false);
		if(glassesStack.getTagCompound().getBoolean("nightVisionActive")){
			consumed+=storage.extractEnergy(nightvisionCostFE, false); //consume 5FE/tick for active nightvision
		}
		return consumed;
	}

	public static double getEnergyStored(ItemStack glassesStack){
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

	public static UUID getHostUUID(EntityPlayer player){
		return getHostUUID(OpenGlasses.getGlassesStack(player));
	}

	public static UUID getHostUUID(ItemStack stack){
		NBTTagCompound nbt = stack.getTagCompound();
    	return nbt.hasUniqueId("host") ? nbt.getUniqueId("host") : null;
	}


	/* Baubles integration */
	@Override
	@Optional.Method(modid="baubles")
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.HEAD;
	}

	@Override
	@Optional.Method(modid="baubles")
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	@Optional.Method(modid="baubles")
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	@Optional.Method(modid="baubles")
	public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) { return true; }

	@Override
	@SideOnly(Side.SERVER)
	@Optional.Method(modid="baubles")
	public void onWornTick(ItemStack itemstack, EntityLivingBase player){ this.consumeEnergy(itemstack); }

}