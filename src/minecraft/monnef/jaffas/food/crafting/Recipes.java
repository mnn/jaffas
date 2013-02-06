package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemManager;
import monnef.jaffas.food.item.Items;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static monnef.jaffas.food.block.TileEntityPie.PieType;
import static monnef.jaffas.food.item.JaffaItem.*;
import static monnef.jaffas.food.mod_jaffas.*;

public class Recipes {
    public static void install() {
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(pastrySweet)), new ItemStack(Item.sugar),
                new ItemStack(Item.egg), new ItemStack(getItem(butter)), new ItemStack(getItem(flour)), new ItemStack(getItem(flour)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(pastry)), new ItemStack(Item.egg), new ItemStack(getItem(butter)), new ItemStack(getItem(flour)), new ItemStack(getItem(flour)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sweetBeans)),
                new ItemStack(getItem(beans)),
                new ItemStack(Item.sugar));

        GameRegistry.addSmelting(getItem(sweetBeans).shiftedIndex, new ItemStack(getItem(chocolate)), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(apples)),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed));

        GameRegistry.addShapelessRecipe(new ItemStack(Item.appleRed, 4),
                new ItemStack(getItem(apples)));

        GameRegistry.addSmelting(getItem(apples).shiftedIndex, new ItemStack(
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


        GameRegistry.addSmelting(getItem(pastrySweet).shiftedIndex, new ItemStack(
                getItem(cake)), 5F);

        AddMalletRecipes();

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadDiamond)), "BIS", "IDI", "SIB",
                'B', new ItemStack(Item.slimeBall), 'I', new ItemStack(Item.ingotIron),
                'S', new ItemStack(Item.silk), 'D', new ItemStack(Block.blockDiamond));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHead)), "SP ", "PWP", " P ",
                'S', new ItemStack(Item.silk), 'P', new ItemStack(Block.planks, 1, -1),
                'W', new ItemStack(Block.wood, 1, -1));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadStone)), "SC ", "COC", " CS",
                'S', new ItemStack(Item.silk), 'C', new ItemStack(Block.cobblestone),
                'O', new ItemStack(Block.stone));

        GameRegistry.addRecipe(new ItemStack(getItem(malletHeadIron)), "SOS", "OBO", "SOS",
                'S', new ItemStack(Item.silk), 'B', new ItemStack(Block.blockSteel),
                'O', new ItemStack(Block.stone));

        JaffaCraftingHandler.AddPersistentItem(mallet, true, -1);
        JaffaCraftingHandler.AddPersistentItem(malletStone, true, -1);
        JaffaCraftingHandler.AddPersistentItem(malletIron, true, -1);
        JaffaCraftingHandler.AddPersistentItem(malletDiamond, true, -1);

        AddMalletShapedRecipe(new ItemStack(getItem(beans)), new ItemStack(Item.dyePowder, 1, 3));
        AddMalletShapedRecipe(new ItemStack(getItem(butter)), getItemStack(milkBoxFull));
        AddMalletShapedRecipe(new ItemStack(getItem(cakeTin)), new ItemStack(Item.ingotIron));

        // moved to the trees module because of the peanut
//        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.browniesPastry)), new ItemStack(getItem(JaffaItem.peanut)),
//                new ItemStack(getItem(JaffaItem.pastrySweet)), new ItemStack(getItem(JaffaItem.chocolate)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(puffPastry)), new ItemStack(getItem(butter)),
                new ItemStack(getItem(butter)), new ItemStack(getItem(butter)), new ItemStack(Item.egg),
                new ItemStack(getItem(flour)), new ItemStack(getItem(flour)));

        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.peanut)), "SSS", 'S', new ItemStack(Item.seeds));

        GameRegistry.addRecipe(new ItemStack(getItem(browniesInTinRaw)), "P", "T", 'P', new ItemStack(getItem(browniesPastry)), 'T', new ItemStack(getItem(cakeTin)));
        GameRegistry.addSmelting(getItem(browniesInTinRaw).shiftedIndex, new ItemStack(getItem(browniesInTin)), 1F);
        GameRegistry.addRecipe(new ItemStack(getItem(brownie), 15), "S", "T", 'S', new ItemStack(getItem(knifeKitchen), 1, -1), 'T', getItem(browniesInTin));
        JaffaCraftingHandler.AddPersistentItem(browniesInTin, false, cakeTin);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sweetRollRaw), 10), new ItemStack(getItem(puffPastry)), new ItemStack(Item.stick));

        GameRegistry.addSmelting(getItem(sweetRollRaw).shiftedIndex, new ItemStack(getItem(sweetRoll)), 0.2F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(cream), 4), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.sugar), getItem(milkBoxFull));

        GameRegistry.addRecipe(new ItemStack(getItem(creamRoll)), "RC", 'R', new ItemStack(getItem(sweetRoll)), 'C', new ItemStack(getItem(cream)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(flour), 3), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat),
                new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.paper));

        GameRegistry.addRecipe(new ItemStack(getItem(sausageRaw), 3), " F ", "PPP", 'F', new ItemStack(getItem(flour)), 'P', getItem(mincedMeat));

        GameRegistry.addRecipe(new ItemStack(getItem(bunRaw), 8), "PP", 'P', new ItemStack(getItem(pastry)));

        GameRegistry.addSmelting(getItem(bunRaw).shiftedIndex, new ItemStack(getItem(bun)), 0.2F);
        GameRegistry.addSmelting(getItem(sausageRaw).shiftedIndex, new ItemStack(getItem(sausage)), 0.2F);

        GameRegistry.addRecipe(new ItemStack(getItem(hotdog)), "S", "B", 'S', new ItemStack(getItem(sausage)), 'B', new ItemStack(getItem(bun)));

        GameRegistry.addRecipe(new ItemStack(getItem(chocolateWrapper), 8), "XXX", "XCX", "XXX", 'X', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 5));

        GameRegistry.addRecipe(new ItemStack(getItem(chocolateBar), 2), "C", "C", "W", 'C', new ItemStack(getItem(chocolate)), 'W', new ItemStack(getItem(chocolateWrapper)));

        GameRegistry.addRecipe(new ItemStack(getItem(wrapperJaffas), 8), "PPP", "PCP", "PPP", 'P', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 12));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(jaffasPack)), new ItemStack(getItem(wrapperJaffas)),
                new ItemStack(getItem(jaffa)), new ItemStack(getItem(jaffa)),
                new ItemStack(getItem(jaffa)), new ItemStack(getItem(jaffa)), new ItemStack(getItem(jaffa)),
                new ItemStack(getItem(jaffa)), new ItemStack(getItem(jaffa)), new ItemStack(getItem(jaffa)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(jaffasPackR)), new ItemStack(getItem(wrapperJaffas)),
                new ItemStack(getItem(jaffaR)), new ItemStack(getItem(jaffaR)),
                new ItemStack(getItem(jaffaR)), new ItemStack(getItem(jaffaR)), new ItemStack(getItem(jaffaR)),
                new ItemStack(getItem(jaffaR)), new ItemStack(getItem(jaffaR)), new ItemStack(getItem(jaffaR)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(jaffasPackO)), new ItemStack(getItem(wrapperJaffas)),
                new ItemStack(getItem(jaffaO)), new ItemStack(getItem(jaffaO)),
                new ItemStack(getItem(jaffaO)), new ItemStack(getItem(jaffaO)), new ItemStack(getItem(jaffaO)),
                new ItemStack(getItem(jaffaO)), new ItemStack(getItem(jaffaO)), new ItemStack(getItem(jaffaO)));

        GameRegistry.addRecipe(new ItemStack(instance.blockJaffaBomb), "J", "R", "T", 'J', new ItemStack(getItem(jaffasPack)),
                'R', new ItemStack(getItem(jaffasPackR)), 'T', new ItemStack(Block.tnt));
        GameRegistry.addRecipe(new ItemStack(instance.blockJaffaBomb), "J", "O", "T", 'J', new ItemStack(getItem(jaffasPack)),
                'O', new ItemStack(getItem(jaffasPackO)), 'T', new ItemStack(Block.tnt));

        //RecipesFridge.AddRecipe(Block.dirt.blockID, new ItemStack(Block.gravel));

        //GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.vanillaBeans)), new ItemStack(Item.dyePowder, 1, 3), new ItemStack(Item.dyePowder, 1, 11));
        GameRegistry.addRecipe(new ItemStack(getItem(waferIcecreamRaw), 2), "PP", "PP", 'P', new ItemStack(getItem(pastrySweet)));
        GameRegistry.addRecipe(new ItemStack(getItem(coneRaw), 1), "P P", " P ", 'P', new ItemStack(getItem(pastrySweet)));

        AddMalletShapedRecipe(new ItemStack(getItem(vanillaPowder)), new ItemStack(getItem(vanillaBeans)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(vanillaIcecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(vanillaPowder)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(chocolateIcecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(beans)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(icecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(cream)), new ItemStack(Item.snowball));

        GameRegistry.addRecipe(new ItemStack(getItem(vanillaIcecream), 4), "S", "C", 'S', new ItemStack(getItem(vanillaIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(chocolateIcecream), 4), "S", "C", 'S', new ItemStack(getItem(chocolateIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(russianIcecream), 4), "W", "I", "W", 'W', new ItemStack(getItem(waferIcecream)), 'I', new ItemStack(getItem(icecreamFrozen)));

        RecipesFridge.AddRecipe(getItem(icecreamRaw).shiftedIndex, new ItemStack(getItem(icecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(vanillaIcecreamRaw).shiftedIndex, new ItemStack(getItem(vanillaIcecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(chocolateIcecreamRaw).shiftedIndex, new ItemStack(getItem(chocolateIcecreamFrozen)));

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.ores)) {
            GameRegistry.addRecipe(new ItemStack(instance.blockFridge), "GGG", "IMI", "SRS", 'G', new ItemStack(Item.ingotGold), 'I', new ItemStack(Block.blockSteel), 'M', new ItemStack(Block.fenceIron), 'S', new ItemStack(Block.stone), 'R', new ItemStack(Item.redstone));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(donutRaw)), " P ", "P P", " P ", 'P', new ItemStack(getItem(pastrySweet)));
        GameRegistry.addSmelting(getItem(donutRaw).shiftedIndex, new ItemStack(getItem(donut)), 0.25F);
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

        GameRegistry.addSmelting(getItem(lemons).shiftedIndex, new ItemStack(
                getItem(jamL)), 0.5F);
        GameRegistry.addSmelting(getItem(oranges).shiftedIndex, new ItemStack(
                getItem(jamO)), 0.5F);
        GameRegistry.addSmelting(getItem(plums).shiftedIndex, new ItemStack(
                getItem(jamP)), 0.5F);


        GameRegistry.addShapelessRecipe(new ItemStack(getItem(sprinkles), 16), new ItemStack(Item.sugar), new ItemStack(Item.sugar), new ItemStack(Item.sugar),
                new ItemStack(getItem(jamMix)), new ItemStack(Item.egg));

        GameRegistry.addRecipe(new ItemStack(getItem(bagOfSeeds)), "SXS", "SLS", "SSS", 'S', new ItemStack(Item.seeds), 'X', new ItemStack(Item.silk), 'L', new ItemStack(Item.leather));
        GameRegistry.addRecipe(new ItemStack(getItem(magnifier)), "GG ", "GG ", "  I", 'G', new ItemStack(Block.glass), 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(bagOfSeedsIdentified)), new ItemStack(getItem(magnifier)), new ItemStack(getItem(bagOfSeeds)));

        GameRegistry.addRecipe(new ItemStack(instance.itemJaffaPlate), "BBB", " J ", " B ", 'B', new ItemStack(Block.cloth, 1, 15), 'J', new ItemStack(getItem(jaffa)));

        GameRegistry.addSmelting(getItem(vanillaPowder).shiftedIndex, new ItemStack(getItem(jamV)), 0.6F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(jamMix), 3),
                new ItemStack(getItem(jamV)), new ItemStack(getItem(jamR)),
                new ItemStack(getItem(jamL)), new ItemStack(getItem(jamO)),
                new ItemStack(getItem(jamP))
        );

        GameRegistry.addSmelting(getItem(raspberries).shiftedIndex, new ItemStack(
                getItem(jamRaspberry)), 0.5F);
        GameRegistry.addSmelting(getItem(strawberries).shiftedIndex, new ItemStack(
                getItem(jamStrawberry)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(jaffaRaspberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamRaspberry)), 'Z', new ItemStack(getItem(cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(jaffaStrawberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(chocolate)), 'Y',
                new ItemStack(getItem(jamStrawberry)), 'Z', new ItemStack(getItem(cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(kettle)), "XS ", " XX", " XX", 'X', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        GameRegistry.addRecipe(new ItemStack(getItem(cupRaw)), "XXX", "XX ", 'X', new ItemStack(Item.clay));
        GameRegistry.addSmelting(getItem(cupRaw).shiftedIndex, new ItemStack(getItem(cup)), 3);
        AddMalletShapedRecipe(new ItemStack(getItem(coffee)), new ItemStack(getItem(coffeeRoasted)));
        GameRegistry.addRecipe(new ItemStack(getItem(kettleWaterCold)), "W", "K", 'W', new ItemStack(Item.bucketWater), 'K', new ItemStack(getItem(kettle)));
        GameRegistry.addSmelting(getItem(kettleWaterCold).shiftedIndex, new ItemStack(getItem(kettleWaterHot)), 0);
        GameRegistry.addRecipe(new ItemStack(getItem(cupCoffee)), "K", "C", "U",
                'K', new ItemStack(getItem(kettleWaterHot), 1, -1), 'C', new ItemStack(getItem(coffee)), 'U', new ItemStack(getItem(cup)));
        JaffaCraftingHandler.AddPersistentItem(kettleWaterHot, true, kettle);

        GameRegistry.addRecipe(new ItemStack(getItem(knifeKitchen)), "I  ", " I ", "  S", 'I', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        JaffaCraftingHandler.AddPersistentItem(knifeKitchen, true, -1);

        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatChopped), 4), "K", "M", 'K', new ItemStack(getItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(Item.porkRaw));
        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.rollChopped), 1), "K", "M", 'K', new ItemStack(getItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(getItem(JaffaItem.roll)));
        RecipesBoard.addRecipeSimple(roll, rollChopped);
        RecipesBoard.addRecipe(new ItemStack(Item.porkRaw), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.beefRaw), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.chickenRaw), new ItemStack(getItem(meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.fishRaw), new ItemStack(getItem(meatChopped)));

        GameRegistry.addRecipe(new ItemStack(getItem(ironSkewer)), "  I", " I ", "I  ", 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(skewerRaw)), new ItemStack(getItem(ironSkewer)), new ItemStack(getItem(rollChopped)), new ItemStack(getItem(meatChopped)));
        GameRegistry.addSmelting(getItem(skewerRaw).shiftedIndex, new ItemStack(getItem(skewer)), 2F);

        GameRegistry.addRecipe(new ItemStack(getItem(rollRaw), 8), " P", "P ", 'P', new ItemStack(getItem(pastry)));
        GameRegistry.addSmelting(getItem(rollRaw).shiftedIndex, new ItemStack(getItem(roll)), 0.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(tomatoChopped)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(paprikaChopped)));
        GameRegistry.addSmelting(getItem(omeletteRaw).shiftedIndex, new ItemStack(getItem(omelette)), 1.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(brownPastry)), new ItemStack(getItem(pastrySweet)), new ItemStack(getItem(chocolate)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(gingerbread)), new ItemStack(getItem(pastrySweet)), new ItemStack(getItem(honey)));

        //honey recipe
        if (instance.IsForestryDetected()) {
            ItemStack i = forestry.api.core.ItemInterface.getItem("honeyDrop");
            GameRegistry.addRecipe(new ItemStack(getItem(honey)), "H", "H", "B", 'H', i, 'B', Item.glassBottle);
        } else {
            GameRegistry.addRecipe(new ItemStack(getItem(honey)), "SSS", "SYS", " B ", 'B', Item.glassBottle, 'S', Item.sugar, 'Y', new ItemStack(Item.dyePowder, 1, 11));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(hamburgerBunRaw)), " O ", "OOO", 'O', getItem(pastry));
        GameRegistry.addSmelting(getItem(hamburgerBunRaw).shiftedIndex, new ItemStack(getItem(hamburgerBun)), 0.5f);
        AddMalletShapedRecipe(new ItemStack(getItem(cheese)), new ItemStack(getItem(butter)));

        RecipesBoard.addRecipe(cheese, 1, cheeseSlice, 4);

        GameRegistry.addSmelting(getItem(coneRaw).shiftedIndex, new ItemStack(getItem(cone)), 1f);
        GameRegistry.addSmelting(getItem(waferIcecreamRaw).shiftedIndex, new ItemStack(getItem(waferIcecream)), 1f);

        //GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.mincedMeat),2),);
        //addOreRecipe(new ShapedOreRecipe(new ItemStack(getItem(JaffaItem.mincedMeat), 2), true, new Object[]{""}));
        addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(mincedMeat), 2), Items.MINCEABLEMEAT, getItem(grinderMeat)));
        JaffaCraftingHandler.AddPersistentItem(grinderMeat);

        JaffaCraftingHandler.AddPersistentItem(grater);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(cheeseGrated)), getItem(grater), getItem(cheese));
        RecipesBoard.addRecipe(salami, 1, salamiSliced, 1);

        GameRegistry.addSmelting(getItem(pizzaRaw).shiftedIndex, new ItemStack(getItem(pizza)), 5f);
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
        GameRegistry.addSmelting(getItem(breadRaw).shiftedIndex, getItemStack(bread), 0.5f);

        GameRegistry.addRecipe(getItemStack(rawBurger), "PPP", "PPP", 'P', getItem(mincedMeat));
        //GameRegistry.addSmelting(getItem(JaffaItem.rawBurger).shiftedIndex, getItemStack(JaffaItem.burger), 2f);
        addFryingPanRecipe(rawBurger, fryingPanBurgerRaw, fryingPanBurger, burger);
        RecipesBoard.addRecipe(bread, 1, breadSlice, 12);
        GameRegistry.addShapelessRecipe(getItemStack(hamburger, 5), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(burger), getItem(onionSliced));
        GameRegistry.addShapelessRecipe(getItemStack(cheeseburger, 6), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(burger), getItem(onionSliced), getItem(cheeseSlice));
        addFryingPanRecipe(Item.egg, fryingPanEggRaw, fryingPanEgg, eggFried);
        addFryingPanRecipe(chipsRaw, fryingPanChipsRaw, fryingPanChips, chips);
        RecipesBoard.addRecipe(new ItemStack(Item.potato), getItemStack(chipsRaw));

        addJamBreadSliceRecipe(jamStrawberry);
        addJamBreadSliceRecipe(jamRaspberry);
        addJamBreadSliceRecipe(jamR);
        GameRegistry.addShapelessRecipe(getItemStack(breadSliceButter, 6), getItemStackAnyDamage(knifeKitchen), getItem(butter),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted));
        GameRegistry.addRecipe(getItemStack(breadSliceEgg), "E", "T", 'E', getItem(eggFried), 'T', getItem(breadSliceToasted));
        GameRegistry.addSmelting(getItem(breadSlice).shiftedIndex, getItemStack(breadSliceToasted), 0.1f);

        // 15 ~ white
        GameRegistry.addRecipe(new ItemStack(blockColumn), "SSS", "DSD", "SSS", 'S', Block.stone, 'D', new ItemStack(Item.dyePowder, 1, 15));
        addRecipe(new ShapedOreRecipe(blockJaffaStatue, "JIJ", "III", "JIJ", 'J', Items.JAFFA, 'I', Item.ingotIron));

        for (Items.Juice juice : Items.Juice.values()) {
            JaffaCraftingHandler.AddPersistentItem(juice.juiceBottle, false, juiceBottle);
            GameRegistry.addShapelessRecipe(getItemStack(juice.glass, 3), getItem(juice.juiceBottle), getItem(glassEmpty), getItem(glassEmpty), getItem(glassEmpty));
        }
        GameRegistry.addRecipe(getItemStack(glassEmpty, 4), "G G", "GGG", 'G', Block.glass);
        GameRegistry.addShapelessRecipe(getItemStack(glassMilk, 2), getItem(milkBoxFull), getItem(glassEmpty), getItem(glassEmpty));

        GameRegistry.addRecipe(getItemStack(woodenBowl, 2), "W W", " S ", 'W', new ItemStack(Block.planks, 1, -1), 'S', new ItemStack(Block.woodSingleSlab, 1, -1));
        addRecipe(new ShapelessOreRecipe(getItem(cookedMushroomsRaw), getItem(woodenBowl), Items.MUSHROOM, Items.MUSHROOM));
        GameRegistry.addSmelting(getItem(cookedMushroomsRaw).shiftedIndex, getItemStack(cookedMushrooms), 0.3f);

        GameRegistry.addSmelting(getItem(pepperStuffedRaw).shiftedIndex, getItemStack(pepperStuffed), 0.2f);
        GameRegistry.addSmelting(getItem(peanutsSugar).shiftedIndex, getItemStack(peanutsCaramelized), 0.2f);

        GameRegistry.addSmelting(getItem(wolfMeatRaw).shiftedIndex, getItemStack(wolfMeat), 0.3f);
        GameRegistry.addSmelting(getItem(muttonRaw).shiftedIndex, getItemStack(mutton), 0.3f);
        GameRegistry.addSmelting(getItem(spiderLegRaw).shiftedIndex, getItemStack(spiderLeg), 0.3f);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(strawberryIcecreamRaw), 4), new ItemStack(getItem(cream)), new ItemStack(getItem(jamStrawberry)), new ItemStack(Item.snowball));
        RecipesFridge.AddRecipe(getItem(strawberryIcecreamRaw).shiftedIndex, getItemStack(strawberryIcecreamFrozen));
        GameRegistry.addRecipe(new ItemStack(getItem(strawberryIcecream), 4), "S", "C", 'S', new ItemStack(getItem(strawberryIcecreamFrozen)), 'C', new ItemStack(getItem(cone)));

        GameRegistry.addRecipe(new ItemStack(getItem(duckHelmet)), " S ", "S S", 'S', getItem(featherDuck));
        GameRegistry.addRecipe(getItemStack(duckChest), "S S", "SCS", "CCC", 'S', getItem(featherDuck), 'C', new ItemStack(Block.cloth, 1, -1));
        GameRegistry.addRecipe(getItemStack(duckLeggins), "SCS", "S S", "C C", 'S', getItem(featherDuck), 'C', Item.leather);
        GameRegistry.addRecipe(getItemStack(duckBoots), "S S", "S S", 'S', getItem(featherDuck));

        GameRegistry.addRecipe(new ItemStack(getItem(chocIceStick), 4), "S ", " S", 'S', Item.stick);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(chocIce), 2), getItem(chocIceStick), getItem(chocIceStick), getItem(icecreamFrozen), getItem(chocolate));

        // 4x paper + sweet pastry -> 4x raw muffin
        // raw muffin => unfinished muffin
        // 4x unfinished muffin + chocolate -> 4x muffin

        // beans
        // chopped tomatoes   ->  raw beans with tomato sauce => baked beans with tomato sauce
        // dish

        // bread slice (not toasted)
        // slice of cheese + sliced salami -> sandwich
        // bread slice

        // raw mutton
        // pea         -> raw lamb with peas => lamb with peas (in tin) | + plate -> lamb with peas (plate) + tin
        // tin
    }

    public static void AddPieRecipe(Item lowCostIngredient, JaffaItem rawPie, PieType type, boolean isSweet, Item highCostIngredient) {
        String ingredientsShape = " I ";

        // vanilla
        if (highCostIngredient.shiftedIndex == getItem(jamV).shiftedIndex) {
            ingredientsShape = "I I";
        }

        Item pastry = getItem(isSweet ? pastrySweet : JaffaItem.pastry);
        if (lowCostIngredient != null) {
            GameRegistry.addRecipe(getItemStack(rawPie), " I ", "III", "PPP", 'I', lowCostIngredient, 'P', pastry);
        }

        if (highCostIngredient != null) {
            GameRegistry.addRecipe(getItemStack(rawPie), ingredientsShape, "PPP", 'I', highCostIngredient, 'P', pastry);
        }

        GameRegistry.addSmelting(getItem(rawPie).shiftedIndex, new ItemStack(blockPie, 1, type.ordinal()), 5f);
    }

    private static void addJamBreadSliceRecipe(JaffaItem jam) {
        GameRegistry.addShapelessRecipe(getItemStack(breadSliceJam, 6), getItemStackAnyDamage(knifeKitchen), getItem(jam),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted),
                getItem(breadSliceToasted), getItem(breadSliceToasted), getItem(breadSliceToasted));
    }

    private static void addFryingPanRecipe(Item input, JaffaItem coupledRaw, JaffaItem coupled, JaffaItem output) {
        GameRegistry.addRecipe(getItemStack(coupledRaw), "I", "F", 'I', input, 'F', getItem(fryingPan));
        GameRegistry.addSmelting(getItem(coupledRaw).shiftedIndex, getItemStack(coupled), 0f);
        GameRegistry.addShapelessRecipe(getItemStack(output), getItem(coupled));
        JaffaCraftingHandler.AddPersistentItem(coupled, false, fryingPan);
    }

    private static void addFryingPanRecipe(JaffaItem input, JaffaItem coupledRaw, JaffaItem coupled, JaffaItem output) {
        addFryingPanRecipe(getItem(input), coupledRaw, coupled, output);
    }

    private static ItemStack getItemStackAnyDamage(JaffaItem item) {
        return new ItemStack(getItem(item), 1, -1);
    }

    private static ItemStack getItemStack(JaffaItem item, int size) {
        ItemStack stack = getItemStack(item);
        stack.stackSize = size;
        return stack;
    }

    private static ItemStack getItemStack(JaffaItem item) {
        return new ItemStack(getItem(item));
    }

    private static void addRecipe(IRecipe recipe) {
        CraftingManager.getInstance().getRecipeList().add(recipe);
    }

    private static void AddMalletShapedRecipe(ItemStack output, ItemStack input) {
        instance.AddMalletShapedRecipe(output, input);
    }

    private static Item getItem(JaffaItem item) {
        return mod_jaffas.getItem(item);
    }

    private static void AddMalletRecipes() {
        for (int i = 0; i < ItemManager.mallets.length; i++) {
            GameRegistry.addRecipe(new ItemStack(getItem(ItemManager.mallets[i])), "H", "S", "S", 'H', new ItemStack(getItem(ItemManager.malletHeads[i])), 'S', Item.stick);
        }
    }
}
