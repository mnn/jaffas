/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import monnef.core.utils.DyeColor;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemJaffaPack;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.food.item.common.Items;
import monnef.jaffas.power.block.TileEntityGrinder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static monnef.core.utils.DyeHelper.getDye;
import static monnef.jaffas.food.JaffasFood.blockColumn;
import static monnef.jaffas.food.JaffasFood.blockJaffaStatue;
import static monnef.jaffas.food.JaffasFood.blockPie;
import static monnef.jaffas.food.JaffasFood.blockSwitchgrass;
import static monnef.jaffas.food.JaffasFood.blockSwitchgrassSolid;
import static monnef.jaffas.food.JaffasFood.blockTable;
import static monnef.jaffas.food.JaffasFood.instance;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.block.TileEntityPie.PieType;
import static monnef.jaffas.food.item.JaffaItem.*;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class Recipes {
    public static final String WOOD_PLANK = "plankWood";
    public static final String WOOD_LOG = "logWood";
    public static final String WOOD_SLAB = "slabWood";
    public static final String TREE_SAPLING = "treeSapling";
    public static final String TREE_LEAVES = "treeLeaves";
    public static final int JAFFAS_PACK_CONTENT_SIZE = 8;

    public static void postLoadInstallRecipes() {
        TileEntityGrinder.addOreDictRecipe(Items.MINCEABLEMEAT, getItemStack(mincedMeat, 2), 100);
    }

    public static void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(pastrySweet)), new ItemStack(Item.sugar),
                new ItemStack(Item.egg), new ItemStack(getItem(butter)), new ItemStack(getItem(flour)), new ItemStack(getItem(flour)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(pastry)), new ItemStack(Item.egg), new ItemStack(getItem(butter)), new ItemStack(getItem(flour)), new ItemStack(getItem(flour)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sweetBeans)),
                new ItemStack(getItem(beans)),
                new ItemStack(Item.sugar));

        GameRegistry.addSmelting(getItem(sweetBeans).itemID, new ItemStack(getItem(chocolate)), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(apples)),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed));

        GameRegistry.addShapelessRecipe(new ItemStack(Item.appleRed, 4),
                new ItemStack(getItem(apples)));

        GameRegistry.addSmelting(getItem(apples).itemID, new ItemStack(
                getItem(jamR)), 0.5F);

        /*
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jamO)), "X", "Y", 'X', new ItemStack(Item.dyePowder, 1, 14), 'Y',
                new ItemStack(getItem(JaffaItem.jamR)));
        */

        GameRegistry.addRecipe(new ItemStack(getItem(jaffa), 12), "X", "Y", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaR), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamR)), 'Z', new ItemStack(getItem(cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaO), 12),
                "X", "Y", "Z", 'X',
                new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamO)), 'Z',
                new ItemStack(getItem(cake)));


        GameRegistry.addSmelting(getItem(pastrySweet).itemID, new ItemStack(
                getItem(cake)), 5F);

        addMalletRecipes();

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadDiamond)), "BIS", "IDI", "SIB",
                'B', new ItemStack(Item.slimeBall), 'I', new ItemStack(Item.ingotIron),
                'S', new ItemStack(Item.silk), 'D', new ItemStack(Block.blockDiamond));

        addRecipe(new ShapedOreRecipe(new ItemStack(getItem(malletHead)), "SP ", "PWP", " P ",
                'S', new ItemStack(Item.silk), 'P', WOOD_PLANK, 'W', WOOD_LOG));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadStone)), "SC ", "COC", " CS",
                'S', new ItemStack(Item.silk), 'C', new ItemStack(Block.cobblestone),
                'O', new ItemStack(Block.stone));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadIron)), "SOS", "OBO", "SOS",
                'S', new ItemStack(Item.silk), 'B', new ItemStack(Block.blockIron),
                'O', new ItemStack(Block.stone));

        JaffaCraftingHandler.AddPersistentItem(mallet, true, -1);
        JaffaCraftingHandler.AddPersistentItem(malletStone, true, -1);
        JaffaCraftingHandler.AddPersistentItem(malletIron, true, -1);
        JaffaCraftingHandler.AddPersistentItem(malletDiamond, true, -1);

        addMalletShapedRecipe(new ItemStack(getItem(beans)), new ItemStack(Item.dyePowder, 1, 3));
        addMalletShapedRecipe(new ItemStack(getItem(butter)), getItemStack(milkBoxFull));
        addMalletShapedRecipe(new ItemStack(getItem(cakeTin)), new ItemStack(Item.ingotIron));

        // moved to the trees module because of the peanut
//        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.browniesPastry)), new ItemStack(getItem(JaffaItem.peanut)),
//                new ItemStack(getItem(JaffaItem.pastrySweet)), new ItemStack(getItem(JaffaItem.chocolate)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(puffPastry)), new ItemStack(getItem(butter)),
                new ItemStack(getItem(butter)), new ItemStack(getItem(butter)), new ItemStack(Item.egg),
                new ItemStack(getItem(flour)), new ItemStack(getItem(flour)));

        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.peanut)), "SSS", 'S', new ItemStack(Item.seeds));

        GameRegistry.addRecipe(new ItemStack(getItem(browniesInTinRaw)), "P", "T", 'P', new ItemStack(getItem(browniesPastry)), 'T', new ItemStack(getItem(cakeTin)));
        GameRegistry.addSmelting(getItem(browniesInTinRaw).itemID, new ItemStack(getItem(browniesInTin)), 1F);
        GameRegistry.addRecipe(new ItemStack(getItem(brownie), 15), "S", "T", 'S', new ItemStack(getItem(knifeKitchen), 1, WILDCARD_VALUE), 'T', getItem(browniesInTin));
        JaffaCraftingHandler.AddPersistentItem(browniesInTin, false, cakeTin);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sweetRollRaw), 10), new ItemStack(getItem(puffPastry)), new ItemStack(Item.stick));

        GameRegistry.addSmelting(getItem(sweetRollRaw).itemID, new ItemStack(getItem(sweetRoll)), 0.2F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(cream), 4), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.sugar), getItem(milkBoxFull));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(cream), 4), getItemStack(duckEgg), getItemStack(duckEgg), new ItemStack(Item.sugar), getItem(milkBoxFull));

        GameRegistry.addRecipe(new ItemStack(getItem(creamRoll)), "RC", 'R', new ItemStack(getItem(sweetRoll)), 'C', new ItemStack(getItem(cream)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(flour), 3), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat),
                new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.paper));

        GameRegistry.addRecipe(new ItemStack(getItem(sausageRaw), 3), " F ", "PPP", 'F', new ItemStack(getItem(flour)), 'P', getItem(mincedMeat));

        GameRegistry.addRecipe(new ItemStack(getItem(bunRaw), 8), "PP", 'P', new ItemStack(getItem(pastry)));

        GameRegistry.addSmelting(getItem(bunRaw).itemID, new ItemStack(getItem(bun)), 0.2F);
        GameRegistry.addSmelting(getItem(sausageRaw).itemID, new ItemStack(getItem(sausage)), 0.2F);

        GameRegistry.addRecipe(new ItemStack(getItem(hotdog)), "S", "B", 'S', new ItemStack(getItem(sausage)), 'B', new ItemStack(getItem(bun)));

        GameRegistry.addRecipe(new ItemStack(getItem(chocolateWrapper), 8), "XXX", "XCX", "XXX", 'X', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 5));

        GameRegistry.addRecipe(new ItemStack(getItem(chocolateBar), 2), "C", "C", "W", 'C', new ItemStack(getItem(chocolate)), 'W', new ItemStack(getItem(chocolateWrapper)));

        GameRegistry.addRecipe(new ItemStack(getItem(wrapperJaffas), 8), "PPP", "PCP", "PPP", 'P', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 12));

        addPackRecipe(jaffa);
        addPackRecipe(jaffaO);
        addPackRecipe(jaffaL);
        addPackRecipe(jaffaP);
        addPackRecipe(jaffaR);
        addPackRecipe(jaffaRaspberry);
        addPackRecipe(jaffaV);
        addPackRecipe(jaffaStrawberry);

        GameRegistry.addRecipe(new ItemStack(JaffasFood.blockJaffaBomb), "JJJ", "RLG", " T ", 'J', new ItemStack(getItem(jaffasPack)),
                'R', Item.redstone, 'L', Item.goldNugget, 'G', Item.lightStoneDust,
                'T', new ItemStack(Block.tnt));

        GameRegistry.addRecipe(new ItemStack(getItem(waferIcecreamRaw), 2), "PP", "PP", 'P', new ItemStack(getItem(pastrySweet)));
        GameRegistry.addRecipe(new ItemStack(getItem(coneRaw), 1), "P P", " P ", 'P', new ItemStack(getItem(pastrySweet)));

        addMalletShapedRecipe(new ItemStack(getItem(vanillaPowder)), new ItemStack(getItem(vanillaBeans)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(vanillaIcecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(vanillaPowder)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(chocolateIcecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(beans)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(icecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(cream)), new ItemStack(Item.snowball));

        GameRegistry.addRecipe(new ItemStack(getItem(vanillaIcecream), 4), "S", "C", 'S', new ItemStack(getItem(vanillaIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(chocolateIcecream), 4), "S", "C", 'S', new ItemStack(getItem(chocolateIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(russianIcecream), 4), "W", "I", "W", 'W', new ItemStack(getItem(waferIcecream)), 'I', new ItemStack(getItem(icecreamFrozen)));

        RecipesFridge.AddRecipe(getItem(icecreamRaw).itemID, new ItemStack(getItem(icecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(vanillaIcecreamRaw).itemID, new ItemStack(getItem(vanillaIcecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(chocolateIcecreamRaw).itemID, new ItemStack(getItem(chocolateIcecreamFrozen)));

        if (!ModuleManager.isModuleEnabled(ModulesEnum.technic)) {
            GameRegistry.addRecipe(new ItemStack(instance.blockFridge), "GGG", "IMI", "SRS", 'G', new ItemStack(Item.ingotGold), 'I', new ItemStack(Block.blockIron), 'M', new ItemStack(Block.fenceIron), 'S', new ItemStack(Block.stone), 'R', new ItemStack(Item.redstone));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(donutRaw)), " P ", "P P", " P ", 'P', new ItemStack(getItem(pastrySweet)));
        GameRegistry.addSmelting(getItem(donutRaw).itemID, new ItemStack(getItem(donut)), 0.25F);
        GameRegistry.addRecipe(new ItemStack(getItem(donutChocolate), 8), "C", "D", 'C', new ItemStack(getItem(chocolate)), 'D', new ItemStack(getItem(donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(donutPink), 8), "C", "D", 'C', new ItemStack(getItem(jamR)), 'D', new ItemStack(getItem(donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(donutSugar), 8), "C", "D", 'C', new ItemStack(Item.sugar), 'D', new ItemStack(getItem(donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(donutSprinkled)), "C", "D", 'C', new ItemStack(getItem(sprinkles)), 'D', new ItemStack(getItem(donutChocolate)));

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaL), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamL)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaP), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamP)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaV), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamV)), 'Z', new ItemStack(getItem(cake)));

        GameRegistry.addSmelting(getItem(lemons).itemID, new ItemStack(
                getItem(jamL)), 0.5F);
        GameRegistry.addSmelting(getItem(oranges).itemID, new ItemStack(
                getItem(jamO)), 0.5F);
        GameRegistry.addSmelting(getItem(plums).itemID, new ItemStack(
                getItem(jamP)), 0.5F);


        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sprinkles), 16), new ItemStack(Item.sugar), new ItemStack(Item.sugar), new ItemStack(Item.sugar),
                new ItemStack(getItem(jamMix)), new ItemStack(Item.egg));

        GameRegistry.addRecipe(new ItemStack(getItem(magnifier)), "GG ", "GG ", "  I", 'G', new ItemStack(Block.glass), 'I', new ItemStack(Item.ingotIron));

        GameRegistry.addRecipe(new ItemStack(instance.itemJaffaPlate), "BBB", " J ", " B ", 'B', new ItemStack(Block.cloth, 1, 15), 'J', new ItemStack(getItem(jaffa)));

        GameRegistry.addSmelting(getItem(vanillaPowder).itemID, new ItemStack(getItem(jamV)), 0.6F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(jamMix), 3),
                new ItemStack(getItem(jamV)), new ItemStack(getItem(jamR)),
                new ItemStack(getItem(jamL)), new ItemStack(getItem(jamO)),
                new ItemStack(getItem(jamP))
        );

        GameRegistry.addSmelting(getItem(raspberries).itemID, new ItemStack(
                getItem(jamRaspberry)), 0.5F);
        GameRegistry.addSmelting(getItem(strawberries).itemID, new ItemStack(
                getItem(jamStrawberry)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaRaspberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamRaspberry)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaStrawberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamStrawberry)), 'Z', new ItemStack(getItem(cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(kettle)), "XS ", " XX", " XX", 'X', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        GameRegistry.addRecipe(new ItemStack(getItem(cupRaw)), "XXX", "XX ", 'X', new ItemStack(Item.clay));
        GameRegistry.addSmelting(getItem(cupRaw).itemID, new ItemStack(getItem(cup)), 3);
        addMalletShapedRecipe(new ItemStack(getItem(coffee)), new ItemStack(getItem(coffeeRoasted)));
        //GameRegistry.addRecipe(new ItemStack(getItem(kettleWaterCold)), "W", "K", 'W', new ItemStack(Item.bucketWater), 'K', new ItemStack(getItem(kettle)));
        GameRegistry.addSmelting(getItem(kettleWaterCold).itemID, new ItemStack(getItem(kettleWaterHot)), 0);
        GameRegistry.addRecipe(new ItemStack(getItem(cupCoffee)), "K", "C", "U",
                'K', new ItemStack(getItem(kettleWaterHot), 1, WILDCARD_VALUE), 'C', new ItemStack(getItem(coffee)), 'U', new ItemStack(getItem(cup)));
        JaffaCraftingHandler.AddPersistentItem(kettleWaterHot, true, kettle);

        GameRegistry.addRecipe(new ItemStack(getItem(knifeKitchen)), "I  ", " I ", "  S", 'I', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        JaffaCraftingHandler.AddPersistentItem(knifeKitchen, true, -1);

        RecipesBoard.addRecipeSimple(roll, rollChopped);
        RecipesBoard.addRecipe(new ItemStack(Item.porkRaw), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.beefRaw), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.chickenRaw), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.fishRaw), new ItemStack(getItem(meatChopped)));

        GameRegistry.addRecipe(new ItemStack(getItem(ironSkewer)), "  I", " I ", "I  ", 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(skewerRaw)), new ItemStack(getItem(ironSkewer)), new ItemStack(getItem(rollChopped)), new ItemStack(getItem(meatChopped)));
        GameRegistry.addSmelting(getItem(skewerRaw).itemID, new ItemStack(getItem(skewer)), 2F);

        GameRegistry.addRecipe(new ItemStack(getItem(rollRaw), 8), " P", "P ", 'P', new ItemStack(getItem(pastry)));
        GameRegistry.addSmelting(getItem(rollRaw).itemID, new ItemStack(getItem(roll)), 0.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(tomatoChopped)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(paprikaChopped)));
        GameRegistry.addSmelting(getItem(omeletteRaw).itemID, new ItemStack(getItem(omelette)), 1.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(brownPastry)), new ItemStack(getItem(pastrySweet)), new ItemStack(getItem(chocolate)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(gingerbread)), new ItemStack(getItem(pastrySweet)), new ItemStack(getItem(honey)));

        //honey recipe
        if (otherMods.isForestryDetected()) {
            ItemStack i = forestry.api.core.ItemInterface.getItem("honeyDrop");
            GameRegistry.addRecipe(new ItemStack(getItem(honey)), "H", "H", "B", 'H', i, 'B', Item.glassBottle);
        } else {
            GameRegistry.addRecipe(new ItemStack(getItem(honey)), "SSS", "SYS", " B ", 'B', Item.glassBottle, 'S', Item.sugar, 'Y', new ItemStack(Item.dyePowder, 1, 11));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(hamburgerBunRaw)), " O ", "OOO", 'O', getItem(pastry));
        GameRegistry.addSmelting(getItem(hamburgerBunRaw).itemID, new ItemStack(getItem(hamburgerBun)), 0.5f);
        addMalletShapedRecipe(new ItemStack(getItem(cheese)), new ItemStack(getItem(butter)));

        RecipesBoard.addRecipe(cheese, 1, cheeseSlice, 4);

        GameRegistry.addSmelting(getItem(coneRaw).itemID, new ItemStack(getItem(cone)), 1f);
        GameRegistry.addSmelting(getItem(waferIcecreamRaw).itemID, new ItemStack(getItem(waferIcecream)), 1f);

        JaffaCraftingHandler.AddPersistentItem(grater);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(cheeseGrated)), getItem(grater), getItem(cheese));
        RecipesBoard.addRecipe(salami, 1, salamiSliced, 1);

        GameRegistry.addSmelting(getItem(pizzaRaw).itemID, new ItemStack(getItem(pizza)), 5f);
        GameRegistry.addRecipe(new ItemStack(getItem(salami)), "M  ", "SM ", "  M", 'M', getItem(mincedMeat), 'S', Item.silk);
        GameRegistry.addRecipe(new ItemStack(getItem(pizzaRaw)), "SCK", "PPP", " T ", 'S', getItem(salamiSliced), 'C', getItem(cheeseGrated), 'K', getItem(bottleKetchup), 'P', getItem(pastry), 'T', getItem(cakeTin));

        JaffaCraftingHandler.AddPersistentItem(bottleKetchup, false, bottleEmpty);
        JaffaCraftingHandler.AddPersistentItem(bottleBrownMustard, false, bottleEmpty);
        JaffaCraftingHandler.AddPersistentItem(bottleMustard, false, bottleEmpty);

        GameRegistry.addRecipe(new ItemStack(getItem(wolfHelmet)), " S ", "S S", 'S', getItem(wolfSkin));
        GameRegistry.addRecipe(getItemStack(wolfChest), "S S", "SCS", "CCC", 'S', getItem(wolfSkin), 'C', new ItemStack(Block.cloth, 1, 14));
        GameRegistry.addRecipe(getItemStack(wolfLeggins), "SCS", "S S", "C C", 'S', getItem(wolfSkin), 'C', Item.leather);
        GameRegistry.addRecipe(getItemStack(wolfBoots), "S S", "S S", 'S', getItem(wolfSkin));

        JaffaCraftingHandler.AddPersistentItem(milkBoxFull, false, crumpledPaper).SetSubstituteItemsCount(2);

        GameRegistry.addRecipe(getItemStack(milkBoxEmpty, 3), "PP", "PP", "PP", 'P', Item.paper);
        GameRegistry.addShapelessRecipe(getItemStack(milkBoxFull, 2), Item.bucketMilk, getItem(milkBoxEmpty), getItem(milkBoxEmpty));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.bucketMilk), getItem(milkBoxFull), getItem(milkBoxFull), Item.bucketEmpty);

        GameRegistry.addRecipe(getItemStack(breadRaw), "PPP", "PPP", 'P', getItem(pastry));
        GameRegistry.addSmelting(getItem(breadRaw).itemID, getItemStack(bread), 0.5f);

        GameRegistry.addRecipe(getItemStack(rawBurger), "PPP", "PPP", 'P', getItem(mincedMeat));
        //GameRegistry.addSmelting(getItem(JaffaItem.rawBurger).itemID, getItemStack(JaffaItem.burger), 2f);
        addFryingPanRecipe(rawBurger, fryingPanBurgerRaw, fryingPanBurger, burger);
        RecipesBoard.addRecipe(bread, 1, breadSlice, 12);
        GameRegistry.addShapelessRecipe(getItemStack(hamburger, 5), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(burger), getItem(onionSliced));
        GameRegistry.addShapelessRecipe(getItemStack(cheeseburger, 6), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(burger), getItem(onionSliced), getItem(cheeseSlice));
        addFryingPanRecipe(Item.egg, fryingPanEggRaw, fryingPanEgg, eggFried);
        addFryingPanRecipe(duckEgg, fryingPanEggRaw, fryingPanEgg, eggFried);
        addFryingPanRecipe(chipsRaw, fryingPanChipsRaw, fryingPanChips, chips);

        addJamBreadSliceRecipe(jamStrawberry);
        addJamBreadSliceRecipe(jamRaspberry);
        addJamBreadSliceRecipe(jamR);
        GameRegistry.addShapelessRecipe(getItemStack(breadSliceButter, 6), getItemStackAnyDamage(knifeKitchen), getItem(butter),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted));
        GameRegistry.addRecipe(getItemStack(breadSliceEgg), "E", "T", 'E', getItem(eggFried), 'T', getItem(breadSliceToasted));
        //GameRegistry.addSmelting(getItem(breadSlice).itemID, getItemStack(breadSliceToasted), 0.1f);

        // 15 ~ white
        GameRegistry.addRecipe(new ItemStack(blockColumn), "SSS", "DSD", "SSS", 'S', Block.stone, 'D', new ItemStack(Item.dyePowder, 1, 15));
        addRecipe(new ShapedOreRecipe(blockJaffaStatue, "JIJ", "III", "JIJ", 'J', Items.JAFFA, 'I', Item.ingotIron));

        for (Items.Juice juice : Items.Juice.values()) {
            JaffaCraftingHandler.AddPersistentItem(juice.juiceBottle, false, juiceBottle);
            GameRegistry.addShapelessRecipe(getItemStack(juice.glass, 3), getItem(juice.juiceBottle), getItem(glassEmpty), getItem(glassEmpty), getItem(glassEmpty));
        }
        GameRegistry.addRecipe(getItemStack(glassEmpty, 4), "G G", "GGG", 'G', Block.glass);
        GameRegistry.addShapelessRecipe(getItemStack(glassMilk, 2), getItem(milkBoxFull), getItem(glassEmpty), getItem(glassEmpty));

        addRecipe(new ShapedOreRecipe(getItemStack(woodenBowl, 2), "W W", " S ", 'W', WOOD_PLANK, 'S', WOOD_SLAB));
        addRecipe(new ShapelessOreRecipe(getItem(cookedMushroomsRaw), getItem(woodenBowl), Items.MUSHROOM, Items.MUSHROOM, Items.MUSHROOM));
        addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(cookedMushroomsRaw), 3), getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl), Items.MUSHROOM, Items.MUSHROOM, Items.MUSHROOM, Items.MUSHROOM, Items.MUSHROOM, Items.MUSHROOM));
        GameRegistry.addSmelting(getItem(cookedMushroomsRaw).itemID, getItemStack(cookedMushrooms), 0.3f);

        GameRegistry.addSmelting(getItem(pepperStuffedRaw).itemID, getItemStack(pepperStuffed), 0.2f);
        GameRegistry.addSmelting(getItem(peanutsSugar).itemID, getItemStack(peanutsCaramelized), 0.2f);

        GameRegistry.addSmelting(getItem(wolfMeatRaw).itemID, getItemStack(wolfMeat), 0.3f);
        GameRegistry.addSmelting(getItem(muttonRaw).itemID, getItemStack(mutton), 0.3f);
        GameRegistry.addSmelting(getItem(spiderLegRaw).itemID, getItemStack(spiderLeg), 0.3f);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(strawberryIcecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(jamStrawberry)), new ItemStack(Item.snowball));
        RecipesFridge.AddRecipe(getItem(strawberryIcecreamRaw).itemID, getItemStack(strawberryIcecreamFrozen));
        GameRegistry.addRecipe(new ItemStack(getItem(strawberryIcecream), 4), "S", "C", 'S', new ItemStack(getItem(strawberryIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));

        GameRegistry.addRecipe(new ItemStack(getItem(duckHelmet)), " S ", "S S", 'S', getItem(featherDuck));
        GameRegistry.addRecipe(getItemStack(duckChest), "S S", "SCS", "CCC", 'S', getItem(featherDuck), 'C', new ItemStack(Block.cloth, 1, WILDCARD_VALUE));
        GameRegistry.addRecipe(getItemStack(duckLeggins), "SCS", "S S", "C C", 'S', getItem(featherDuck), 'C', Item.leather);
        GameRegistry.addRecipe(getItemStack(duckBoots), "S S", "S S", 'S', getItem(featherDuck));

        GameRegistry.addRecipe(new ItemStack(getItem(chocIceStick), 4), "S ", " S", 'S', Item.stick);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(chocIce), 2), getItem(chocIceStick), getItem(chocIceStick), getItem(icecreamFrozen), getItem(chocolate));

        // 4x paper + sweet pastry -> 4x raw muffin
        // raw muffin => unfinished muffin
        // 4x unfinished muffin + chocolate -> 4x muffin
        GameRegistry.addShapelessRecipe(getItemStack(muffinRaw, 4), Item.paper, Item.paper, Item.paper, Item.paper, getItem(pastrySweet));
        GameRegistry.addSmelting(getItem(muffinRaw).itemID, getItemStack(muffinUnfinished), 0.3f);
        GameRegistry.addShapelessRecipe(getItemStack(muffin, 4), getItem(muffinUnfinished), getItem(muffinUnfinished), getItem(muffinUnfinished), getItem(muffinUnfinished), getItem(chocolate));

        // bread slice (not toasted)
        // slice of cheese + sliced salami -> sandwich
        // bread slice
        GameRegistry.addRecipe(getItemStack(sandwich1), " B", "CS", " B", 'B', getItem(breadSlice), 'C', getItem(cheeseSlice), 'S', getItem(salamiSliced));
        GameRegistry.addRecipe(getItemStack(sandwich1), " B", "SC", " B", 'B', getItem(breadSlice), 'C', getItem(cheeseSlice), 'S', getItem(salamiSliced));

        GameRegistry.addRecipe(getItemStack(plateRaw, 2), " C ", "CCC", " C ", 'C', Item.clay);
        GameRegistry.addSmelting(getItem(plateRaw).itemID, getItemStack(plate), 1f);

        GameRegistry.addSmelting(getItem(duckRaw).itemID, getItemStack(duck), 0.5f);

        addTableRecipe(new ItemStack(blockTable, 1, 0), 14);
        addTableRecipe(new ItemStack(blockTable, 1, 0), 6);
        addTableRecipe(new ItemStack(blockTable, 1, 1), 5);
        addTableRecipe(new ItemStack(blockTable, 1, 1), 13);
        addTableRecipe(new ItemStack(blockTable, 1, 2), 3);
        addTableRecipe(new ItemStack(blockTable, 1, 2), 11);

        GameRegistry.addShapelessRecipe(getItemStack(cocoBarWrapper, 15), getDye(DyeColor.L_BLUE), Item.paper, Item.paper, Item.paper, Item.paper, Item.paper);

        GameRegistry.addShapelessRecipe(new ItemStack(Item.paper), getItem(crumpledPaper), getItem(crumpledPaper));
        GameRegistry.addRecipe(getItemStack(cocoBar, 3), "cC ", " Cc", "WWW", 'c', getItem(coconutPowder), 'C', getItem(chocolate), 'W', getItem(cocoBarWrapper));

        addRecipe(new ShapedOreRecipe(getItemStack(cookingPotEggsRaw), "EEE", "EEE", " P ", 'E', Items.EGG, 'P', getItem(cookingPotWater)));
        GameRegistry.addSmelting(getItem(cookingPotEggsRaw).itemID, getItemStack(cookingPotEggs), 3f);
        JaffaCraftingHandler.AddPersistentItem(cookingPotEggs, false, cookingPot);
        GameRegistry.addShapelessRecipe(getItemStack(eggHardBoiled, 6), getItem(cookingPotEggs));

        GameRegistry.addShapelessRecipe(getItemStack(cookingPotCocoaCold), getItem(milkBoxFull), getItem(milkBoxFull), getItem(sweetBeans), getItem(sweetBeans), getItem(cookingPot));
        GameRegistry.addSmelting(getItem(cookingPotCocoaCold).itemID, getItemStack(cookingPotCocoaHot), 3f);
        JaffaCraftingHandler.AddPersistentItem(cookingPotCocoaHot, false, cookingPot);
        GameRegistry.addShapelessRecipe(getItemStack(cupCocoa, 5), getItem(cookingPotCocoaHot), getItem(cup), getItem(cup), getItem(cup), getItem(cup), getItem(cup));

        GameRegistry.addShapelessRecipe(getItemStack(cookingPot), getItem(cookingPotWater));

        GuideBookHelper.generateGuideBook();

        RegistryUtils.registerBlockPackingRecipe(ItemHelper.getItemStackAnyDamage(blockSwitchgrass), new ItemStack(blockSwitchgrassSolid));

        GameRegistry.addShapedRecipe(getItemStack(meatDryer), "SSS", "SIS", "S S", 'S', Item.stick, 'I', Item.silk);

        if (JaffasFood.vanillaRecipesEnabled) {
            registerVanillaLikeRecipes();
        }

        RecipesBoard.addRecipe(new ItemStack(Item.potato), getItemStack(potatesSliced));
        RecipesBoard.addRecipe(potatesSliced, 1, chipsRaw, 1);
        GameRegistry.addShapedRecipe(getItemStack(potatesSlicedInTinRaw), " P ", "P P", " T ", 'P', getItem(potatesSliced), 'T', getItem(cakeTin));
        GameRegistry.addSmelting(getItem(potatesSlicedInTinRaw).itemID, new ItemStack(getItem(potatesSlicedInTin)), 1F);
        GameRegistry.addShapelessRecipe(getItemStack(crisps, 2), getItem(woodenBowl), getItem(woodenBowl), getItem(potatesSlicedInTin));
        JaffaCraftingHandler.AddPersistentItem(potatesSlicedInTin, false, cakeTin);
    }

    public static void addPackRecipe(JaffaItem item) {
        ItemStack output = new ItemStack(getItem(jaffasPack));
        ((ItemJaffaPack) getItem(jaffasPack)).setContent(output, getItem(item).itemID, JAFFAS_PACK_CONTENT_SIZE, 0);
        GameRegistry.addShapelessRecipe(output, new ItemStack(getItem(wrapperJaffas)),
                new ItemStack(getItem(item)), new ItemStack(getItem(item)),
                new ItemStack(getItem(item)), new ItemStack(getItem(item)), new ItemStack(getItem(item)),
                new ItemStack(getItem(item)), new ItemStack(getItem(item)), new ItemStack(getItem(item)));
    }


    public static void addTableRecipe(ItemStack output, int color) {
        addRecipe(new ShapedOreRecipe(output, "CWC", "PPP", "PPP", 'C', new ItemStack(Block.cloth, 1, color), 'W', new ItemStack(Block.cloth, 1, 0), 'P', WOOD_PLANK));
    }

    public static void addPieRecipe(Item lowCostIngredient, JaffaItem rawPie, PieType type, boolean isSweet, Item highCostIngredient) {
        String ingredientsShape = " I ";

        // vanilla
        if (highCostIngredient.itemID == getItem(jamV).itemID) {
            ingredientsShape = "I I";
        }

        Item pastry = getItem(isSweet ? pastrySweet : JaffaItem.pastry);
        if (lowCostIngredient != null) {
            GameRegistry.addRecipe(getItemStack(rawPie), " I ", "III", "PPP", 'I', lowCostIngredient, 'P', pastry);
        }

        if (highCostIngredient != null) {
            GameRegistry.addRecipe(getItemStack(rawPie), ingredientsShape, "PPP", 'I', highCostIngredient, 'P', pastry);
        }

        GameRegistry.addSmelting(getItem(rawPie).itemID, new ItemStack(blockPie, 1, type.ordinal()), 5f);
    }

    private static void registerVanillaLikeRecipes() {
        GameRegistry.addShapedRecipe(new ItemStack(Block.grass), "GGG", "SSS", "SSS", 'S', Block.dirt, 'G', new ItemStack(Block.tallGrass, 1, 1));
        addRecipe(new ShapelessOreRecipe(new ItemStack(Block.dirt),
                TREE_SAPLING, TREE_SAPLING, TREE_SAPLING,
                TREE_SAPLING, TREE_SAPLING, TREE_SAPLING,
                TREE_SAPLING, TREE_SAPLING, TREE_SAPLING));
        addRecipe(new ShapelessOreRecipe(new ItemStack(Block.dirt),
                TREE_LEAVES, TREE_LEAVES, TREE_LEAVES,
                TREE_LEAVES, TREE_LEAVES, TREE_LEAVES,
                TREE_LEAVES, TREE_LEAVES, TREE_LEAVES));
        GameRegistry.addShapedRecipe(new ItemStack(Block.gravel, 3), "SCS", "CSC", "SCS", 'S', Block.stone, 'C', Block.cobblestone);
        GameRegistry.addShapedRecipe(new ItemStack(Block.sand, 3), "GGG", "GWG", "GGG", 'W', Item.bucketWater, 'G', Block.gravel);
        GameRegistry.addShapedRecipe(new ItemStack(Item.bucketWater), "CCC", "CCC", "B", 'C', Block.cactus, 'B', Item.bucketEmpty);
    }

    private static void addJamBreadSliceRecipe(JaffaItem jam) {
        GameRegistry.addShapelessRecipe(getItemStack(breadSliceJam, 6), getItemStackAnyDamage(knifeKitchen), getItem(jam),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted));
    }

    private static void addFryingPanRecipe(Item input, JaffaItem coupledRaw, JaffaItem coupled, JaffaItem output) {
        GameRegistry.addRecipe(getItemStack(coupledRaw), "I", "F", 'I', input, 'F', getItem(fryingPan));
        GameRegistry.addSmelting(getItem(coupledRaw).itemID, getItemStack(coupled), 0f);
        GameRegistry.addShapelessRecipe(getItemStack(output), getItem(coupled));
        JaffaCraftingHandler.AddPersistentItem(coupled, false, fryingPan);
    }

    public static void addFryingPanRecipe(JaffaItem input, JaffaItem coupledRaw, JaffaItem coupled, JaffaItem output) {
        addFryingPanRecipe(getItem(input), coupledRaw, coupled, output);
    }

    public static ItemStack getItemStackAnyDamage(JaffaItem item) {
        return new ItemStack(getItem(item), 1, WILDCARD_VALUE);
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

    public static void addMalletShapedRecipe(ItemStack output, ItemStack input) {
        addRecipe(new ShapedOreRecipe(output, "M", "O", 'M', Items.MALLET, 'O', input));
    }

    public static Item getItem(JaffaItem item) {
        return JaffasFood.getItem(item);
    }

    private static void addMalletRecipes() {
        for (int i = 0; i < ItemManager.mallets.length; i++) {
            GameRegistry.addRecipe(new ItemStack(getItem(ItemManager.mallets[i])), "H", "S", "S", 'H', new ItemStack(getItem(ItemManager.malletHeads[i])), 'S', Item.stick);
        }
    }
}
