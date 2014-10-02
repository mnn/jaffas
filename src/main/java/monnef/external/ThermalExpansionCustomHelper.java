package monnef.external;

import cpw.mods.fml.common.registry.GameRegistry;
import monnef.core.utils.GameObjectsDumper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static monnef.external.ThermalExpansionHelper.addSmelterRecipe;

public class ThermalExpansionCustomHelper {
    private static final String ThermalExpansionModId = "ThermalExpansion";

    private static ItemStack sawdust;
    private static ItemStack slag;
    private static ItemStack slagRich;
    private static ItemStack blockSand;

    private ThermalExpansionCustomHelper() {
    }

    public static void init() {
        slag = createTEStack("slag");
        slagRich = createTEStack("slagRich");
        sawdust = createTEStack("dustWood");
        blockSand = new ItemStack(Blocks.sand);
    }

    private static ItemStack createTEStack(String itemName) {
        ItemStack stack = GameRegistry.findItemStack(ThermalExpansionModId, itemName, 1);
        if (stack == null) {
            GameObjectsDumper.dump("gameObjectsFromCrash.csv");
            throw new RuntimeException("Cannot find TE item \"" + itemName + "\".");
        }
        return stack;
    }

    public static void addSmelterDustToIngotsRecipe(ItemStack inputDust, ItemStack outputIngots) {
        if (inputDust == null) throw new RuntimeException("Dust cannot be null");
        if (outputIngots == null) throw new RuntimeException("Ingot cannot be null");
        if (blockSand == null) throw new RuntimeException("Sand block cannot be null");

        ItemStack dust = inputDust.copy();
        dust.stackSize = 2;

        ItemStack ingots = outputIngots.copy();
        ingots.stackSize = 2;

        addSmelterRecipe(80, dust, blockSand, ingots, slag, 25);
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack inputOre, ItemStack outputIngots) {
        if (inputOre == null) throw new RuntimeException("Ore cannot be null");
        if (outputIngots == null) throw new RuntimeException("Ingot cannot be null");
        if (blockSand == null) throw new RuntimeException("Sand block cannot be null");
        if (slag == null) throw new RuntimeException("Slag cannot be null");
        if (slagRich == null) throw new RuntimeException("Rich slag cannot be null");

        ItemStack ore = inputOre.copy();
        ore.stackSize = 1;

        ItemStack ingots2 = outputIngots.copy();
        ingots2.stackSize = 2;

        ItemStack ingots3 = outputIngots.copy();
        ingots3.stackSize = 3;

        addSmelterRecipe(320, ore, blockSand, ingots2, slagRich, 5);
        addSmelterRecipe(400, ore, slagRich, ingots3, slag, 75);
    }

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

    // wrapped

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
        ThermalExpansionHelper.addPulverizerRecipe(energy, input, primaryOutput);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
        ThermalExpansionHelper.addPulverizerRecipe(energy, input, primaryOutput, secondaryOutput);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
        ThermalExpansionHelper.addPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, secondaryChance);
    }
}
