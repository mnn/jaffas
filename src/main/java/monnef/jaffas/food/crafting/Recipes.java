/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import monnef.core.utils.DyeColor;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.ListConverter;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.block.BlockSwitchgrass;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemPack;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.power.block.TileGrinder;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static monnef.core.utils.DyeHelper.getDye;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.block.TilePie.PieType;
import static monnef.jaffas.food.common.ContentHolder.blockColumn;
import static monnef.jaffas.food.common.ContentHolder.blockJaffaStatue;
import static monnef.jaffas.food.common.ContentHolder.blockPie;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrass;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrassSolid;
import static monnef.jaffas.food.common.ContentHolder.blockTable;
import static monnef.jaffas.food.item.JaffaItem.*;
import static monnef.jaffas.food.item.common.Items.ANY_EGG;
import static monnef.jaffas.food.item.common.Items.JAFFA;
import static monnef.jaffas.food.item.common.Items.LollipopRecord;
import static monnef.jaffas.food.item.common.Items.MALLET;
import static monnef.jaffas.food.item.common.Items.MINCEABLEMEAT;
import static monnef.jaffas.food.item.common.Items.MUSHROOM;
import static monnef.jaffas.food.item.common.Items.lollipops;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class Recipes {
    public static final String WOOD_PLANK = "plankWood";
    public static final String WOOD_LOG = "logWood";
    public static final String WOOD_SLAB = "slabWood";
    public static final String TREE_SAPLING = "treeSapling";
    public static final String TREE_LEAVES = "treeLeaves";
    public static final String WOOD_STICK = "stickWood";
    public static final String STONE = "stone";
    public static final String WHEAT = "cropWheat";

    public static final int JAFFAS_PACK_CONTENT_SIZE = 8;
    public static final int ANY_DMG = OreDictionary.WILDCARD_VALUE;

    public static void postLoadInstallRecipes() {
        TileGrinder.addOreDictRecipe(MINCEABLEMEAT(), getItemStack(mincedMeat, 2), 100);
    }

    public static void installRecipes() {
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                new ItemStack(getItem(pastrySweet)), Items.sugar, ANY_EGG(), getItem(butter), getItem(flour), getItem(flour)
        ));

        GameRegistry.addRecipe(new ShapelessOreRecipe(
                new ItemStack(getItem(pastry)), ANY_EGG(), getItem(butter), getItem(flour), getItem(flour)
        ));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sweetBeans)),
                new ItemStack(getItem(beans)),
                new ItemStack(Items.sugar));

        GameRegistry.addSmelting(getItem(sweetBeans), new ItemStack(getItem(chocolate)), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(apples)),
                new ItemStack(Items.apple),
                new ItemStack(Items.apple),
                new ItemStack(Items.apple),
                new ItemStack(Items.apple));

        GameRegistry.addShapelessRecipe(new ItemStack(Items.apple, 4),
                new ItemStack(getItem(apples)));

        GameRegistry.addSmelting(getItem(apples), new ItemStack(
                getItem(jamR)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(jaffa), 12), "X", "Y", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaR), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamR)), 'Z', new ItemStack(getItem(cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaO), 12),
                "X", "Y", "Z", 'X',
                new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamO)), 'Z',
                new ItemStack(getItem(cake)));


        GameRegistry.addSmelting(getItem(pastrySweet), new ItemStack(
                getItem(cake)), 5F);

        addMalletRecipes();

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadDiamond)), "BIS", "IDI", "SIB",
                'B', new ItemStack(Items.slime_ball), 'I', new ItemStack(Items.iron_ingot),
                'S', new ItemStack(Items.string), 'D', new ItemStack(Blocks.diamond_block));

        addRecipe(new ShapedOreRecipe(new ItemStack(getItem(malletHead)), "SP ", "PWP", " P ",
                'S', new ItemStack(Items.string), 'P', WOOD_PLANK, 'W', WOOD_LOG));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadStone)), "SC ", "COC", " CS",
                'S', new ItemStack(Items.string), 'C', new ItemStack(Blocks.cobblestone),
                'O', new ItemStack(Blocks.stone));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadIron)), "SOS", "OBO", "SOS",
                'S', new ItemStack(Items.string), 'B', new ItemStack(Blocks.iron_block),
                'O', new ItemStack(Blocks.stone));

        PersistentItemsCraftingHandler.AddPersistentItemWhichTakesDamage(mallet);
        PersistentItemsCraftingHandler.AddPersistentItemWhichTakesDamage(malletStone);
        PersistentItemsCraftingHandler.AddPersistentItemWhichTakesDamage(malletIron);
        PersistentItemsCraftingHandler.AddPersistentItemWhichTakesDamage(malletDiamond);

        addMalletShapedRecipe(new ItemStack(getItem(beans)), new ItemStack(Items.dye, 1, 3));
        addMalletShapedRecipe(new ItemStack(getItem(butter)), getItemStack(cream));
        addMalletShapedRecipe(new ItemStack(getItem(cheeseRaw)), getItemStack(milkBoxFull));
        addMalletShapedRecipe(new ItemStack(getItem(cakeTin)), new ItemStack(Items.iron_ingot));

        GameRegistry.addRecipe(new ShapelessOreRecipe(
                new ItemStack(getItem(puffPastry)),
                getItem(butter), getItem(butter), getItem(butter),
                ANY_EGG(), getItem(flour), getItem(flour)
        ));

        GameRegistry.addRecipe(new ItemStack(getItem(browniesInTinRaw)), "P", "T", 'P', new ItemStack(getItem(browniesPastry)), 'T', new ItemStack(getItem(cakeTin)));
        GameRegistry.addSmelting(getItem(browniesInTinRaw), new ItemStack(getItem(browniesInTin)), 1F);
        GameRegistry.addRecipe(new ItemStack(getItem(brownie), 15), "S", "T", 'S', new ItemStack(getItem(knifeKitchen), 1, WILDCARD_VALUE), 'T', getItem(browniesInTin));
        PersistentItemsCraftingHandler.AddPersistentItem(browniesInTin, false, cakeTin);

        addShapelessOreRecipe(new ItemStack(getItem(sweetRollRaw), 10), new ItemStack(getItem(puffPastry)), WOOD_STICK);

        GameRegistry.addSmelting(getItem(sweetRollRaw), new ItemStack(getItem(sweetRoll)), 0.2F);

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(creamSweet), 4),
                ANY_EGG(), getItemStack(duckEgg), Items.sugar, getItem(milkBoxFull)
        ));

        GameRegistry.addRecipe(new ItemStack(getItem(creamRoll)), "RC", 'R', new ItemStack(getItem(sweetRoll)), 'C', new ItemStack(getItem(creamSweet)));

        addShapelessOreRecipe(new ItemStack(getItem(flour), 3), WHEAT, WHEAT, WHEAT, WHEAT, WHEAT, WHEAT, Items.paper);

        GameRegistry.addRecipe(new ItemStack(getItem(sausageRaw), 3), " F ", "PPP", 'F', new ItemStack(getItem(flour)), 'P', getItem(mincedMeat));

        GameRegistry.addRecipe(new ItemStack(getItem(bunRaw), 8), "PP", 'P', new ItemStack(getItem(pastry)));

        GameRegistry.addSmelting(getItem(bunRaw), new ItemStack(getItem(bun)), 0.2F);
        GameRegistry.addSmelting(getItem(sausageRaw), new ItemStack(getItem(sausage)), 0.2F);

        GameRegistry.addRecipe(new ItemStack(getItem(hotdog)), "S", "B", 'S', new ItemStack(getItem(sausage)), 'B', new ItemStack(getItem(bun)));

        GameRegistry.addRecipe(new ItemStack(getItem(chocolateWrapper), 8), "XXX", "XCX", "XXX", 'X', new ItemStack(Items.paper), 'C', new ItemStack(Items.dye, 1, 5));

        GameRegistry.addRecipe(new ItemStack(getItem(chocolateBar), 2), "C", "C", "W", 'C', new ItemStack(getItem(chocolate)), 'W', new ItemStack(getItem(chocolateWrapper)));

        GameRegistry.addRecipe(new ItemStack(getItem(wrapperJaffas), 8), "PPP", "PCP", "PPP", 'P', new ItemStack(Items.paper), 'C', new ItemStack(Items.dye, 1, 12));

        addPackRecipe(jaffa);
        addPackRecipe(jaffaO);
        addPackRecipe(jaffaL);
        addPackRecipe(jaffaP);
        addPackRecipe(jaffaR);
        addPackRecipe(jaffaRaspberry);
        addPackRecipe(jaffaV);
        addPackRecipe(jaffaStrawberry);

        GameRegistry.addRecipe(new ItemStack(ContentHolder.blockJaffaBomb), "JJJ", "RLG", " T ", 'J', new ItemStack(getItem(jaffasPack)),
                'R', Items.redstone, 'L', Items.gold_nugget, 'G', Items.glowstone_dust,
                'T', new ItemStack(Blocks.tnt));

        GameRegistry.addRecipe(new ItemStack(getItem(waferIcecreamRaw), 2), "PP", "PP", 'P', new ItemStack(getItem(pastrySweet)));
        GameRegistry.addRecipe(new ItemStack(getItem(coneRaw), 1), "P P", " P ", 'P', new ItemStack(getItem(pastrySweet)));

        addMalletShapedRecipe(new ItemStack(getItem(vanillaPowder)), new ItemStack(getItem(vanillaBeans)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(vanillaIcecreamRaw), 4), new ItemStack(getItem(creamSweet)), new ItemStack(getItem(vanillaPowder)), new ItemStack(Items.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(chocolateIcecreamRaw), 4), new ItemStack(getItem(creamSweet)), new ItemStack(getItem(beans)), new ItemStack(Items.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(icecreamRaw), 4), new ItemStack(getItem(creamSweet)), new ItemStack(getItem(creamSweet)), new ItemStack(Items.snowball));

        GameRegistry.addRecipe(new ItemStack(getItem(vanillaIcecream), 4), "S", "C", 'S', new ItemStack(getItem(vanillaIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(chocolateIcecream), 4), "S", "C", 'S', new ItemStack(getItem(chocolateIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(russianIcecream), 4), "W", "I", "W", 'W', new ItemStack(getItem(waferIcecream)), 'I', new ItemStack(getItem(icecreamFrozen)));

        RecipesFridge.addRecipe(getItem(icecreamRaw), new ItemStack(getItem(icecreamFrozen)));
        RecipesFridge.addRecipe(getItem(vanillaIcecreamRaw), new ItemStack(getItem(vanillaIcecreamFrozen)));
        RecipesFridge.addRecipe(getItem(chocolateIcecreamRaw), new ItemStack(getItem(chocolateIcecreamFrozen)));

        if (!ModuleManager.isModuleEnabled(ModulesEnum.technic)) {
            GameRegistry.addRecipe(new ItemStack(ContentHolder.blockFridge), "GGG", "IMI", "SRS", 'G', new ItemStack(Items.gold_ingot), 'I', new ItemStack(Blocks.iron_block), 'M', new ItemStack(Blocks.fence), 'S', new ItemStack(Blocks.stone), 'R', new ItemStack(Items.redstone));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(donutRaw)), " P ", "P P", " P ", 'P', new ItemStack(getItem(pastrySweet)));
        GameRegistry.addSmelting(getItem(donutRaw), new ItemStack(getItem(donut)), 0.25F);
        GameRegistry.addRecipe(new ItemStack(getItem(donutChocolate), 8), "C", "D", 'C', new ItemStack(getItem(chocolate)), 'D', new ItemStack(getItem(donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(donutPink), 8), "C", "D", 'C', new ItemStack(getItem(jamR)), 'D', new ItemStack(getItem(donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(donutSugar), 8), "C", "D", 'C', new ItemStack(Items.sugar), 'D', new ItemStack(getItem(donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(donutSprinkled)), "C", "D", 'C', new ItemStack(getItem(sprinkles)), 'D', new ItemStack(getItem(donutChocolate)));

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaL), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y', new ItemStack(getItem(jamL)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaP), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y', new ItemStack(getItem(jamP)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaV), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y', new ItemStack(getItem(jamV)), 'Z', new ItemStack(getItem(cake)));

        GameRegistry.addSmelting(getItem(lemons), new ItemStack(getItem(jamL)), 0.5F);
        GameRegistry.addSmelting(getItem(oranges), new ItemStack(getItem(jamO)), 0.5F);
        GameRegistry.addSmelting(getItem(plums), new ItemStack(getItem(jamP)), 0.5F);


        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(sprinkles), 16),
                Items.sugar, Items.sugar, Items.sugar,
                getItem(jamMix), ANY_EGG()
        ));

        GameRegistry.addRecipe(new ItemStack(getItem(magnifier)), "GG ", "GG ", "  I", 'G', new ItemStack(Blocks.glass), 'I', new ItemStack(Items.iron_ingot));

        GameRegistry.addRecipe(new ItemStack(ContentHolder.itemJaffaPlate), "BBB", " J ", " B ", 'B', new ItemStack(Blocks.wool, 1, 15), 'J', new ItemStack(getItem(jaffa)));

        GameRegistry.addSmelting(getItem(vanillaPowder), new ItemStack(getItem(jamV)), 0.6F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(jamMix), 3),
                new ItemStack(getItem(jamV)), new ItemStack(getItem(jamR)),
                new ItemStack(getItem(jamL)), new ItemStack(getItem(jamO)),
                new ItemStack(getItem(jamP))
        );

        GameRegistry.addSmelting(getItem(raspberries), new ItemStack(
                getItem(jamRaspberry)), 0.5F);
        GameRegistry.addSmelting(getItem(strawberries), new ItemStack(
                getItem(jamStrawberry)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaRaspberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamRaspberry)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaStrawberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamStrawberry)), 'Z', new ItemStack(getItem(cake)));

        addOreRecipe(getItem(kettle), "XS ", " XX", " XX", 'X', new ItemStack(Items.iron_ingot), 'S', WOOD_STICK);
        GameRegistry.addRecipe(new ItemStack(getItem(cupRaw)), "XXX", "XX ", 'X', new ItemStack(Items.clay_ball));
        GameRegistry.addSmelting(getItem(cupRaw), new ItemStack(getItem(cup)), 3);
        addMalletShapedRecipe(new ItemStack(getItem(coffee)), new ItemStack(getItem(coffeeRoasted)));
        GameRegistry.addSmelting(getItem(kettleWaterCold), new ItemStack(getItem(kettleWaterHot)), 0);
        GameRegistry.addRecipe(new ItemStack(getItem(cupCoffee)), "K", "C", "U",
                'K', new ItemStack(getItem(kettleWaterHot), 1, WILDCARD_VALUE), 'C', new ItemStack(getItem(coffee)), 'U', new ItemStack(getItem(cup)));
        PersistentItemsCraftingHandler.AddPersistentItem(kettleWaterHot, true, kettle);

        addOreRecipe(getItem(knifeKitchen), "I  ", " I ", "  S", 'I', new ItemStack(Items.iron_ingot), 'S', WOOD_STICK);

        PersistentItemsCraftingHandler.AddPersistentItemWhichTakesDamage(knifeKitchen);

        RecipesBoard.addRecipeSimple(roll, rollChopped);
        RecipesBoard.addRecipe(new ItemStack(Items.porkchop), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Items.beef), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Items.chicken), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Items.fish), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipeSimple(duckRaw, meatChopped);
        RecipesBoard.addRecipeSimple(wolfMeatRaw, meatChopped);

        GameRegistry.addRecipe(new ItemStack(getItem(ironSkewer)), "  I", " I ", "I  ", 'I', new ItemStack(Items.iron_ingot));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(skewerRaw)), new ItemStack(getItem(ironSkewer)), new ItemStack(getItem(rollChopped)), new ItemStack(getItem(meatChopped)));
        GameRegistry.addSmelting(getItem(skewerRaw), new ItemStack(getItem(skewer)), 2F);

        GameRegistry.addRecipe(new ItemStack(getItem(rollRaw), 8), " P", "P ", 'P', new ItemStack(getItem(pastry)));
        GameRegistry.addSmelting(getItem(rollRaw), new ItemStack(getItem(roll)), 0.5F);

        addOmeletRecipe(tomatoChopped);
        addOmeletRecipe(paprikaChopped);
        GameRegistry.addSmelting(getItem(omeletteRaw), new ItemStack(getItem(omelette)), 1.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(brownPastry)), new ItemStack(getItem(pastrySweet)), new ItemStack(getItem(chocolate)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(gingerbread)), new ItemStack(getItem(pastrySweet)), new ItemStack(getItem(honey)));

        //honey recipe
        if (otherMods.isForestryDetected()) {
            ItemStack i = forestry.api.core.ItemInterface.getItem("honeyDrop");
            GameRegistry.addRecipe(new ItemStack(getItem(honey)), "H", "H", "B", 'H', i, 'B', Items.glass_bottle);
        } else {
            GameRegistry.addRecipe(new ItemStack(getItem(honey)), "SSS", "SYS", " B ", 'B', Items.glass_bottle, 'S', Items.sugar, 'Y', new ItemStack(Items.dye, 1, 11));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(hamburgerBunRaw)), " O ", "OOO", 'O', getItem(pastry));
        GameRegistry.addSmelting(getItem(hamburgerBunRaw), new ItemStack(getItem(hamburgerBun)), 0.5f);
        //addMalletShapedRecipe(new ItemStack(getItem(cheese)), new ItemStack(getItem(butter)));

        RecipesBoard.addRecipe(cheese, 1, cheeseSlice, 4);

        GameRegistry.addSmelting(getItem(coneRaw), new ItemStack(getItem(cone)), 1f);
        GameRegistry.addSmelting(getItem(waferIcecreamRaw), new ItemStack(getItem(waferIcecream)), 1f);

        PersistentItemsCraftingHandler.AddPersistentItem(grater);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(cheeseGrated)), getItem(grater), getItem(cheese));
        RecipesBoard.addRecipe(salami, 1, salamiSliced, 1);

        GameRegistry.addSmelting(getItem(pizzaRaw), new ItemStack(getItem(pizza)), 5f);
        GameRegistry.addRecipe(new ItemStack(getItem(salami)), "M  ", "SM ", "  M", 'M', getItem(mincedMeat), 'S', Items.string);
        GameRegistry.addRecipe(new ItemStack(getItem(pizzaRaw)), "SCK", "PPP", " T ", 'S', getItem(salamiSliced), 'C', getItem(cheeseGrated), 'K', getItem(bottleKetchup), 'P', getItem(pastry), 'T', getItem(cakeTin));

        PersistentItemsCraftingHandler.AddPersistentItem(bottleKetchup, false, bottleEmpty);
        PersistentItemsCraftingHandler.AddPersistentItem(bottleBrownMustard, false, bottleEmpty);
        PersistentItemsCraftingHandler.AddPersistentItem(bottleMustard, false, bottleEmpty);

        GameRegistry.addRecipe(new ItemStack(getItem(wolfHelmet)), " S ", "S S", 'S', getItem(wolfSkin));
        GameRegistry.addRecipe(getItemStack(wolfChest), "S S", "SCS", "CCC", 'S', getItem(wolfSkin), 'C', new ItemStack(Blocks.wool, 1, 14));
        GameRegistry.addRecipe(getItemStack(wolfLeggins), "SCS", "S S", "C C", 'S', getItem(wolfSkin), 'C', Items.leather);
        GameRegistry.addRecipe(getItemStack(wolfBoots), "S S", "S S", 'S', getItem(wolfSkin));

        PersistentItemsCraftingHandler.AddPersistentItem(milkBoxFull, false, crumpledPaper).setSubstituteItemsCount(2);

        GameRegistry.addRecipe(getItemStack(milkBoxEmpty, 3), "PP", "PP", "PP", 'P', Items.paper);
        ItemStack milkStack = getItemStack(milkBoxFull, 2);
        GameRegistry.addShapelessRecipe(milkStack, Items.milk_bucket, getItem(milkBoxEmpty), getItem(milkBoxEmpty));
        LeftoversCraftingHandler.registerLeftovers(milkStack, getItemStack(cream));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.milk_bucket), getItem(milkBoxFull), getItem(milkBoxFull), Items.bucket);

        GameRegistry.addRecipe(getItemStack(breadRaw), "PPP", "PPP", 'P', getItem(pastry));
        GameRegistry.addSmelting(getItem(breadRaw), getItemStack(bread), 0.5f);

        GameRegistry.addRecipe(getItemStack(rawBurger), "PPP", "PPP", 'P', getItem(mincedMeat));
        addFryingPanRecipe(rawBurger, fryingPanBurgerRaw, fryingPanBurger, burger);
        RecipesBoard.addRecipe(bread, 1, breadSlice, 12);
        GameRegistry.addShapelessRecipe(getItemStack(hamburger, 5), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(burger), getItem(onionSliced));
        GameRegistry.addShapelessRecipe(getItemStack(cheeseburger, 6), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(burger), getItem(onionSliced), getItem(cheeseSlice));
        addFryingPanRecipe(Items.egg, fryingPanEggRaw, fryingPanEgg, eggFried);
        addFryingPanRecipe(duckEgg, fryingPanEggRaw, fryingPanEgg, eggFried);
        addFryingPanRecipe(chipsRaw, fryingPanChipsRaw, fryingPanChips, chips);

        addJamBreadSliceRecipe(jamStrawberry);
        addJamBreadSliceRecipe(jamRaspberry);
        addJamBreadSliceRecipe(jamR);
        GameRegistry.addShapelessRecipe(getItemStack(breadSliceButter, 6), getItemStackAnyDamage(knifeKitchen), getItem(butter),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted));
        GameRegistry.addRecipe(getItemStack(breadSliceEgg), "E", "T", 'E', getItem(eggFried), 'T', getItem(breadSliceToasted));

        // 15 ~ white
        GameRegistry.addRecipe(new ItemStack(blockColumn), "SSS", "DSD", "SSS", 'S', Blocks.stone, 'D', new ItemStack(Items.dye, 1, 15));
        addRecipe(new ShapedOreRecipe(blockJaffaStatue, "JIJ", "III", "JIJ", 'J', JAFFA(), 'I', Items.iron_ingot));

        GameRegistry.addRecipe(getItemStack(glassEmpty, 4), "G G", "GGG", 'G', Blocks.glass);
        GameRegistry.addShapelessRecipe(getItemStack(glassMilk, 2), getItem(milkBoxFull), getItem(glassEmpty), getItem(glassEmpty));

        addRecipe(new ShapedOreRecipe(getItemStack(woodenBowl, 2), "W W", " S ", 'W', WOOD_PLANK, 'S', WOOD_SLAB));
        addRecipe(new ShapelessOreRecipe(getItem(cookedMushroomsRaw), getItem(woodenBowl), MUSHROOM(), MUSHROOM(), MUSHROOM()));
        addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(cookedMushroomsRaw), 3), getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl), MUSHROOM(), MUSHROOM(), MUSHROOM(), MUSHROOM(), MUSHROOM(), MUSHROOM()));
        GameRegistry.addSmelting(getItem(cookedMushroomsRaw), getItemStack(cookedMushrooms), 0.3f);

        GameRegistry.addSmelting(getItem(pepperStuffedRaw), getItemStack(pepperStuffed), 0.2f);
        GameRegistry.addSmelting(getItem(peanutsSugar), getItemStack(peanutsCaramelized), 0.2f);

        GameRegistry.addSmelting(getItem(wolfMeatRaw), getItemStack(wolfMeat), 0.3f);
        GameRegistry.addSmelting(getItem(muttonRaw), getItemStack(mutton), 0.3f);
        GameRegistry.addSmelting(getItem(spiderLegRaw), getItemStack(spiderLeg), 0.3f);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(strawberryIcecreamRaw), 4), new ItemStack(getItem(creamSweet)), new ItemStack(getItem(jamStrawberry)), new ItemStack(Items.snowball));
        RecipesFridge.addRecipe(getItem(strawberryIcecreamRaw), getItemStack(strawberryIcecreamFrozen));
        GameRegistry.addRecipe(new ItemStack(getItem(strawberryIcecream), 4), "S", "C", 'S', new ItemStack(getItem(strawberryIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));

        GameRegistry.addRecipe(new ItemStack(getItem(duckHelmet)), " S ", "S S", 'S', getItem(featherDuck));
        GameRegistry.addRecipe(getItemStack(duckChest), "S S", "SCS", "CCC", 'S', getItem(featherDuck), 'C', new ItemStack(Blocks.wool, 1, WILDCARD_VALUE));
        GameRegistry.addRecipe(getItemStack(duckLeggins), "SCS", "S S", "C C", 'S', getItem(featherDuck), 'C', Items.leather);
        GameRegistry.addRecipe(getItemStack(duckBoots), "S S", "S S", 'S', getItem(featherDuck));

        addOreRecipe(new ItemStack(getItem(chocIceStick), 4), "S ", " S", 'S', WOOD_STICK);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(chocIce), 2), getItem(chocIceStick), getItem(chocIceStick), getItem(icecreamFrozen), getItem(chocolate));

        // 4x paper + sweet pastry -> 4x raw muffin
        // raw muffin => unfinished muffin
        // 4x unfinished muffin + chocolate -> 4x muffin
        GameRegistry.addShapelessRecipe(getItemStack(muffinRaw, 4), Items.paper, Items.paper, Items.paper, Items.paper, getItem(pastrySweet));
        GameRegistry.addSmelting(getItem(muffinRaw), getItemStack(muffinUnfinished), 0.3f);
        GameRegistry.addShapelessRecipe(getItemStack(muffin, 4), getItem(muffinUnfinished), getItem(muffinUnfinished), getItem(muffinUnfinished), getItem(muffinUnfinished), getItem(chocolate));

        // bread slice (not toasted)
        // slice of cheese + sliced salami -> sandwich
        // bread slice
        GameRegistry.addRecipe(getItemStack(sandwich1), " B", "CS", " B", 'B', getItem(breadSlice), 'C', getItem(cheeseSlice), 'S', getItem(salamiSliced));
        GameRegistry.addRecipe(getItemStack(sandwich1), " B", "SC", " B", 'B', getItem(breadSlice), 'C', getItem(cheeseSlice), 'S', getItem(salamiSliced));

        GameRegistry.addRecipe(getItemStack(plateRaw, 2), " C ", "CCC", " C ", 'C', Items.clay_ball);
        GameRegistry.addSmelting(getItem(plateRaw), getItemStack(plate), 1f);

        GameRegistry.addSmelting(getItem(duckRaw), getItemStack(duck), 0.5f);

        addTableRecipe(new ItemStack(blockTable, 1, 0), 14);
        addTableRecipe(new ItemStack(blockTable, 1, 0), 6);
        addTableRecipe(new ItemStack(blockTable, 1, 1), 5);
        addTableRecipe(new ItemStack(blockTable, 1, 1), 13);
        addTableRecipe(new ItemStack(blockTable, 1, 2), 3);
        addTableRecipe(new ItemStack(blockTable, 1, 2), 11);

        GameRegistry.addShapelessRecipe(getItemStack(cocoBarWrapper, 15), getDye(DyeColor.L_BLUE), Items.paper, Items.paper, Items.paper, Items.paper, Items.paper);

        GameRegistry.addShapelessRecipe(new ItemStack(Items.paper), getItem(crumpledPaper), getItem(crumpledPaper));
        GameRegistry.addRecipe(getItemStack(cocoBar, 3), "cC ", " Cc", "WWW", 'c', getItem(coconutPowder), 'C', getItem(chocolate), 'W', getItem(cocoBarWrapper));

        addRecipe(new ShapedOreRecipe(getItemStack(cookingPotEggsRaw), "EEE", "EEE", " P ", 'E', ANY_EGG(), 'P', getItem(cookingPotWater)));
        GameRegistry.addSmelting(getItem(cookingPotEggsRaw), getItemStack(cookingPotEggs), 3f);
        PersistentItemsCraftingHandler.AddPersistentItem(cookingPotEggs, false, cookingPot);
        GameRegistry.addShapelessRecipe(getItemStack(eggHardBoiled, 6), getItem(cookingPotEggs));

        GameRegistry.addShapelessRecipe(getItemStack(cookingPotCocoaCold), getItem(milkBoxFull), getItem(milkBoxFull), getItem(sweetBeans), getItem(sweetBeans), getItem(cookingPot));
        GameRegistry.addSmelting(getItem(cookingPotCocoaCold), getItemStack(cookingPotCocoaHot), 3f);
        PersistentItemsCraftingHandler.AddPersistentItem(cookingPotCocoaHot, false, cookingPot);
        GameRegistry.addShapelessRecipe(getItemStack(cupCocoa, 5), getItem(cookingPotCocoaHot), getItem(cup), getItem(cup), getItem(cup), getItem(cup), getItem(cup));

        GameRegistry.addShapelessRecipe(getItemStack(cookingPot), getItem(cookingPotWater));

        RegistryUtils.registerBlockPackingRecipe(ItemHelper.getItemStackAnyDamage(blockSwitchgrass), new ItemStack(blockSwitchgrassSolid));

        addOreRecipe(getItemStack(meatDryer), "SSS", "SIS", "S S", 'S', WOOD_STICK, 'I', Items.string);

        if (ConfigurationManager.vanillaRecipesEnabled) {
            registerVanillaLikeRecipes();
        }

        RecipesBoard.addRecipe(new ItemStack(Items.potato), getItemStack(potatesSliced));
        RecipesBoard.addRecipe(potatesSliced, 1, chipsRaw, 1);
        GameRegistry.addShapedRecipe(getItemStack(potatesSlicedInTinRaw), " P ", "P P", " T ", 'P', getItem(potatesSliced), 'T', getItem(cakeTin));
        GameRegistry.addSmelting(getItem(potatesSlicedInTinRaw), new ItemStack(getItem(potatesSlicedInTin)), 1F);
        GameRegistry.addShapelessRecipe(getItemStack(crisps, 2), getItem(woodenBowl), getItem(woodenBowl), getItem(potatesSlicedInTin));
        PersistentItemsCraftingHandler.AddPersistentItem(potatesSlicedInTin, false, cakeTin);

        GameRegistry.addShapedRecipe(new ItemStack(Items.paper, 2), "xxx", "xxx", 'x', new ItemStack(blockSwitchgrass, 1, BlockSwitchgrass.VALUE_TOP));

        for (LollipopRecord lollipop : ListConverter.toJava(lollipops())) {
            addLollipopRecipe(lollipop.id(), lollipop.jam());
        }

        addFryingPanRecipe(fishStickRaw, fryingPanFishStickRaw, fryingPanFishStick, fishStickCooked);
        addRecipe(new ShapelessOreRecipe(getItemStack(fishStickRaw, 2), ANY_EGG(), getItem(breadCrumbs), getItem(flour), getItem(fishFillet), getItem(fishFillet)));

        addSoupRecipes(fishFillet, soupFishRaw, soupFishCooked, true);

        // vanilla egg recipes
        addOreRecipe(new ItemStack(Items.cake), "MMM", "SES", "WWW", 'W', WHEAT, 'S', Items.sugar, 'E', ANY_EGG(), 'M', Items.milk_bucket);
        addShapelessOreRecipe(Items.pumpkin_pie, Items.sugar, ANY_EGG(), Blocks.pumpkin);

        addOreRecipe(new ItemStack(Blocks.torch, 8), "W", "S", 'S', WOOD_STICK, 'W', ContentHolder.blockSwitchgrassSolid);
    }

    private static void addOmeletRecipe(JaffaItem choppedItem) {
        addShapelessOreRecipe(new ItemStack(getItem(omeletteRaw), 3), ANY_EGG(), ANY_EGG(), ANY_EGG(), getItem(choppedItem));
    }

    public static void addSoupRecipes(JaffaItem inputIngredient, JaffaItem rawSoup, JaffaItem cookedSoup, boolean useCheaperFormat) {
        addSoupRecipes(getItem(inputIngredient), getItem(rawSoup), getItem(cookedSoup), useCheaperFormat);
    }

    public static void addSoupRecipes(Item inputIngredient, JaffaItem rawSoup, JaffaItem cookedSoup, boolean useCheaperFormat) {
        addSoupRecipes(inputIngredient, getItem(rawSoup), getItem(cookedSoup), useCheaperFormat);
    }

    public static void addSoupRecipes(Item inputIngredient, Item rawSoup, Item cookedSoup, boolean useCheaperFormat) {
        if (useCheaperFormat) {
            GameRegistry.addShapelessRecipe(new ItemStack(rawSoup), getItem(woodenBowl), inputIngredient);
        } else {
            GameRegistry.addShapelessRecipe(new ItemStack(rawSoup), getItem(woodenBowl), inputIngredient, inputIngredient, inputIngredient);
        }

        if (useCheaperFormat) {
            GameRegistry.addShapelessRecipe(new ItemStack(rawSoup, 3), getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl), inputIngredient, inputIngredient);
        } else {
            GameRegistry.addShapelessRecipe(new ItemStack(rawSoup, 3),
                    getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl),
                    inputIngredient, inputIngredient, inputIngredient,
                    inputIngredient, inputIngredient, inputIngredient);
        }

        GameRegistry.addSmelting(rawSoup, new ItemStack(cookedSoup), 0.1f);
    }

    public static void addLollipopRecipe(int dmg, JaffaItem jam) {
        addOreRecipe(getItemStack(lollipop, 4, dmg), " JS", " SS", "I  ", 'I', WOOD_STICK, 'S', Items.sugar, 'J', getItemStack(jam));
    }

    public static void addPackRecipe(JaffaItem item) {
        ItemStack output = new ItemStack(getItem(jaffasPack));
        ItemPack.setContent(output, getItem(item), JAFFAS_PACK_CONTENT_SIZE, 0);
        GameRegistry.addShapelessRecipe(output, new ItemStack(getItem(wrapperJaffas)),
                new ItemStack(getItem(item)), new ItemStack(getItem(item)),
                new ItemStack(getItem(item)), new ItemStack(getItem(item)), new ItemStack(getItem(item)),
                new ItemStack(getItem(item)), new ItemStack(getItem(item)), new ItemStack(getItem(item)));
    }


    public static void addTableRecipe(ItemStack output, int color) {
        addRecipe(new ShapedOreRecipe(output, "CWC", "PPP", "PPP", 'C', new ItemStack(Blocks.wool, 1, color), 'W', new ItemStack(Blocks.wool, 1, 0), 'P', WOOD_PLANK));
    }

    public static void addPieRecipe(Item lowCostIngredient, JaffaItem rawPie, PieType type, boolean isSweet, Item highCostIngredient) {
        String ingredientsShape = " I ";

        // vanilla
        if (highCostIngredient == getItem(jamV)) {
            ingredientsShape = "I I";
        }

        Item pastry = getItem(isSweet ? pastrySweet : JaffaItem.pastry);
        if (lowCostIngredient != null) {
            GameRegistry.addRecipe(getItemStack(rawPie), " I ", "III", "PPP", 'I', lowCostIngredient, 'P', pastry);
        }

        if (highCostIngredient != null) {
            GameRegistry.addRecipe(getItemStack(rawPie), ingredientsShape, "PPP", 'I', highCostIngredient, 'P', pastry);
        }

        GameRegistry.addSmelting(getItem(rawPie), new ItemStack(blockPie, 1, type.ordinal()), 5f);
    }

    private static void registerVanillaLikeRecipes() {
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.grass), "GGG", "SSS", "SSS", 'S', Blocks.dirt, 'G', new ItemStack(Blocks.tallgrass, 1, 1));
        addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.dirt),
                TREE_SAPLING, TREE_SAPLING, TREE_SAPLING,
                TREE_SAPLING, TREE_SAPLING, TREE_SAPLING,
                TREE_SAPLING, TREE_SAPLING, TREE_SAPLING));
        addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.dirt),
                TREE_LEAVES, TREE_LEAVES, TREE_LEAVES,
                TREE_LEAVES, TREE_LEAVES, TREE_LEAVES,
                TREE_LEAVES, TREE_LEAVES, TREE_LEAVES));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.gravel, 3), "SCS", "CSC", "SCS", 'S', Blocks.stone, 'C', Blocks.cobblestone);
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.sand, 3), "GGG", "GWG", "GGG", 'W', Items.water_bucket, 'G', Blocks.gravel);
        GameRegistry.addShapedRecipe(new ItemStack(Items.water_bucket), "CCC", "CCC", "B", 'C', Blocks.cactus, 'B', Items.bucket);
        addRecipe(new ShapelessOreRecipe(new ItemStack(Items.book), Items.paper, Items.paper, Items.paper, Items.string, WOOD_PLANK));
    }

    private static void addJamBreadSliceRecipe(JaffaItem jam) {
        GameRegistry.addShapelessRecipe(getItemStack(breadSliceJam, 6), getItemStackAnyDamage(knifeKitchen), getItem(jam),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted));
    }

    private static void addFryingPanRecipe(Item input, JaffaItem coupledRaw, JaffaItem coupled, JaffaItem output) {
        GameRegistry.addRecipe(getItemStack(coupledRaw), "I", "F", 'I', input, 'F', getItem(fryingPan));
        GameRegistry.addSmelting(getItem(coupledRaw), getItemStack(coupled), 0f);
        GameRegistry.addShapelessRecipe(getItemStack(output), getItem(coupled));
        PersistentItemsCraftingHandler.AddPersistentItem(coupled, false, fryingPan);
    }

    public static void addFryingPanRecipe(JaffaItem input, JaffaItem coupledRaw, JaffaItem coupled, JaffaItem output) {
        addFryingPanRecipe(getItem(input), coupledRaw, coupled, output);
    }

    public static ItemStack getItemStackAnyDamage(JaffaItem item) {
        return new ItemStack(getItem(item), 1, WILDCARD_VALUE);
    }

    public static ItemStack getItemStack(JaffaItem item, int size, int dmg) {
        ItemStack stack = getItemStack(item, size);
        stack.setItemDamage(dmg);
        return stack;
    }

    public static ItemStack getItemStack(JaffaItem item, int size) {
        ItemStack stack = getItemStack(item);
        stack.stackSize = size;
        return stack;
    }

    public static ItemStack getItemStack(JaffaItem item) {
        return new ItemStack(getItem(item));
    }

    public static void addRecipe(IRecipe recipe) {
        CraftingManager.getInstance().getRecipeList().add(recipe);
    }

    public static void addOreRecipe(Block result, Object... recipe) {
        addRecipe(new ShapedOreRecipe(result, recipe));
    }

    public static void addOreRecipe(Item result, Object... recipe) {
        addRecipe(new ShapedOreRecipe(result, recipe));
    }

    public static void addOreRecipe(ItemStack result, Object... recipe) {
        addRecipe(new ShapedOreRecipe(result, recipe));
    }

    public static void addShapelessOreRecipe(Block result, Object... recipe) {
        addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addShapelessOreRecipe(Item result, Object... recipe) {
        addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addShapelessOreRecipe(ItemStack result, Object... recipe) {
        addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addMalletShapedRecipe(ItemStack output, ItemStack input) {
        addRecipe(new ShapedOreRecipe(output, "M", "O", 'M', MALLET(), 'O', input));
    }

    public static Item getItem(JaffaItem item) {
        return ContentHolder.getItem(item);
    }

    private static void addMalletRecipes() {
        for (int i = 0; i < ItemManager.mallets.length; i++) {
            addOreRecipe(new ItemStack(getItem(ItemManager.mallets[i])), "H", "S", "S", 'H', new ItemStack(getItem(ItemManager.malletHeads[i])), 'S', WOOD_STICK);
        }
    }
}
