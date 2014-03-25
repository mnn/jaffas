package monnef.external;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author KingLemming
 */
public class ThermalExpansionHelper {
    private static final String ThermalExpansionModId = "ThermalExpansion";

    private static ItemStack sawdust = createTEStack("sawdust");
    private static ItemStack slag = createTEStack("slag");
    private static ItemStack slagRich = createTEStack("slagRich");
    private static ItemStack blockSand = new ItemStack(Block.sand);

    private ThermalExpansionHelper() {
    }

    private static ItemStack createTEStack(String itemName) {
        return new ItemStack(GameRegistry.findItem(ThermalExpansionModId, itemName));
    }

    public static void addFurnaceRecipe(int energy, ItemStack input, ItemStack output) {
        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("input", new NBTTagCompound());
        toSend.setCompoundTag("output", new NBTTagCompound());

        input.writeToNBT(toSend.getCompoundTag("input"));
        output.writeToNBT(toSend.getCompoundTag("output"));
        FMLInterModComms.sendMessage("ThermalExpansion", "FurnaceRecipe", toSend);
    }

    // some methods adopted from old TE API

    public static void addPulverizerOreToDustRecipe(ItemStack inputOre, ItemStack outputDust) {
        ItemStack ore = inputOre.copy();
        ore.stackSize = 1;

        ItemStack primaryDust = outputDust.copy();
        primaryDust.stackSize = 2;

        addPulverizerRecipe(400, ore, primaryDust);
    }

    public static void addPulverizerIngotToDustRecipe(ItemStack inputIngot, ItemStack outputDust) {
        ItemStack ingot = inputIngot.copy();
        ingot.stackSize = 1;

        ItemStack primaryDust = outputDust.copy();
        primaryDust.stackSize = 1;

        addPulverizerRecipe(240, ingot, primaryDust);
    }

    public static void addSmelterDustToIngotsRecipe(ItemStack inputDust, ItemStack outputIngots) {
        ItemStack dust = inputDust.copy();
        dust.stackSize = 2;

        ItemStack ingots = outputIngots.copy();
        ingots.stackSize = 2;

        addSmelterRecipe(80, dust, blockSand, ingots, slag, 25);
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack inputOre, ItemStack outputIngots) {
        ItemStack ore = inputOre.copy();
        ore.stackSize = 1;

        ItemStack ingots2 = outputIngots.copy();
        ingots2.stackSize = 2;

        ItemStack ingots3 = outputIngots.copy();
        ingots3.stackSize = 3;

        addSmelterRecipe(320, ore, blockSand, ingots2, slagRich, 5);
        addSmelterRecipe(400, ore, slagRich, ingots3, slag, 75);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
        addPulverizerRecipe(energy, input, primaryOutput, null, 0);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
        addPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, 100);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("input", new NBTTagCompound());
        toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
        toSend.setCompoundTag("secondaryOutput", new NBTTagCompound());

        input.writeToNBT(toSend.getCompoundTag("input"));
        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
        secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
        toSend.setInteger("secondaryChance", secondaryChance);

        FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", toSend);
    }

    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
        addSawmillRecipe(energy, input, primaryOutput, null, 0);
    }

    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
        addSawmillRecipe(energy, input, primaryOutput, secondaryOutput, 100);
    }

    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("input", new NBTTagCompound());
        toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
        toSend.setCompoundTag("secondaryOutput", new NBTTagCompound());

        input.writeToNBT(toSend.getCompoundTag("input"));
        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
        secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
        toSend.setInteger("secondaryChance", secondaryChance);

        FMLInterModComms.sendMessage("ThermalExpansion", "SawmillRecipe", toSend);
    }

    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput) {

        addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, null, 0);
    }

    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput) {

        addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, 100);
    }

    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput,
                                        int secondaryChance) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("primaryInput", new NBTTagCompound());
        toSend.setCompoundTag("secondaryInput", new NBTTagCompound());
        toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
        toSend.setCompoundTag("secondaryOutput", new NBTTagCompound());

        primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"));
        secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"));
        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
        secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
        toSend.setInteger("secondaryChance", secondaryChance);

        FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", toSend);
    }

    /**
     * Use this to register an Ore TYPE as a "Blast" recipe - it will require Pyrotheum Dust to smelt. Do not add the prefix. This is an opt-in for ores which
     * do NOT have vanilla furnace recipes.
     * <p/>
     * Ex: "Steel" or "ElectrumFlux", not "dustSteel" or "dustElectrumFlux"
     *
     * @param oreType
     */
    public static void addSmelterBlastOre(String oreType) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setString("oreType", oreType);

        FMLInterModComms.sendMessage("ThermalExpansion", "SmelterBlastOreType", toSend);
    }

    public static void addCrucibleRecipe(int energy, ItemStack input, FluidStack output) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("input", new NBTTagCompound());
        toSend.setCompoundTag("output", new NBTTagCompound());

        input.writeToNBT(toSend.getCompoundTag("input"));
        output.writeToNBT(toSend.getCompoundTag("output"));

        FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);
    }

    public static void addTransposerFill(int energy, ItemStack input, ItemStack output, FluidStack fluid, boolean reversible) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("input", new NBTTagCompound());
        toSend.setCompoundTag("output", new NBTTagCompound());
        toSend.setCompoundTag("fluid", new NBTTagCompound());

        input.writeToNBT(toSend.getCompoundTag("input"));
        output.writeToNBT(toSend.getCompoundTag("output"));
        toSend.setBoolean("reversible", reversible);
        fluid.writeToNBT(toSend.getCompoundTag("fluid"));

        FMLInterModComms.sendMessage("ThermalExpansion", "TransposerFillRecipe", toSend);
    }

    public static void addTransposerExtract(int energy, ItemStack input, ItemStack output, FluidStack fluid, int chance, boolean reversible) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setInteger("energy", energy);
        toSend.setCompoundTag("input", new NBTTagCompound());
        toSend.setCompoundTag("output", new NBTTagCompound());
        toSend.setCompoundTag("fluid", new NBTTagCompound());

        input.writeToNBT(toSend.getCompoundTag("input"));
        output.writeToNBT(toSend.getCompoundTag("output"));
        toSend.setBoolean("reversible", reversible);
        toSend.setInteger("chance", chance);
        fluid.writeToNBT(toSend.getCompoundTag("fluid"));

        FMLInterModComms.sendMessage("ThermalExpansion", "TransposerExtractRecipe", toSend);
    }

    public static void addMagmaticFuel(String fluidName, int energy) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setString("fluidName", fluidName);
        toSend.setInteger("energy", energy);

        FMLInterModComms.sendMessage("ThermalExpansion", "MagmaticFuel", toSend);
    }

    public static void addCompressionFuel(String fluidName, int energy) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setString("fluidName", fluidName);
        toSend.setInteger("energy", energy);

        FMLInterModComms.sendMessage("ThermalExpansion", "CompressionFuel", toSend);
    }

    public static void addReactantFuel(String fluidName, int energy) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setString("fluidName", fluidName);
        toSend.setInteger("energy", energy);

        FMLInterModComms.sendMessage("ThermalExpansion", "ReactantFuel", toSend);
    }

    public static void addCoolant(String fluidName, int energy) {

        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setString("fluidName", fluidName);
        toSend.setInteger("energy", energy);

        FMLInterModComms.sendMessage("ThermalExpansion", "Coolant", toSend);
    }

}