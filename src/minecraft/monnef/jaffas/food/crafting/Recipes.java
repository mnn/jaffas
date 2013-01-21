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
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Recipes {
    public static void install() {
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.pastry)), new ItemStack(Item.sugar),
                new ItemStack(Item.egg), new ItemStack(getItem(JaffaItem.butter)), new ItemStack(getItem(JaffaItem.flour)), new ItemStack(getItem(JaffaItem.flour)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sweetBeans)),
                new ItemStack(getItem(JaffaItem.beans)),
                new ItemStack(Item.sugar));

        GameRegistry.addSmelting(getItem(JaffaItem.sweetBeans).shiftedIndex, new ItemStack(getItem(JaffaItem.chocolate)), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.apples)),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed));

        GameRegistry.addShapelessRecipe(new ItemStack(Item.appleRed, 4),
                new ItemStack(getItem(JaffaItem.apples)));

        GameRegistry.addSmelting(getItem(JaffaItem.apples).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamR)), 0.5F);

        /*
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jamO)), "X", "Y", 'X', new ItemStack(Item.dyePowder, 1, 14), 'Y',
                new ItemStack(getItem(JaffaItem.jamR)));
        */

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffa), 12), "X", "Y", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaR), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamR)), 'Z', new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaO), 12),
                "X", "Y", "Z", 'X',
                new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamO)), 'Z',
                new ItemStack(getItem(JaffaItem.cake)));


        GameRegistry.addSmelting(getItem(JaffaItem.pastry).shiftedIndex, new ItemStack(
                getItem(JaffaItem.cake)), 5F);

        AddMalletRecipes();

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHeadDiamond)), "BIS", "IDI", "SIB",
                'B', new ItemStack(Item.slimeBall), 'I', new ItemStack(Item.ingotIron),
                'S', new ItemStack(Item.silk), 'D', new ItemStack(Block.blockDiamond));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHead)), "SP ", "PWP", " P ",
                'S', new ItemStack(Item.silk), 'P', new ItemStack(Block.planks, 1, -1),
                'W', new ItemStack(Block.wood, 1, -1));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHeadStone)), "SC ", "COC", " CS",
                'S', new ItemStack(Item.silk), 'C', new ItemStack(Block.cobblestone),
                'O', new ItemStack(Block.stone));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHeadIron)), "SOS", "OBO", "SOS",
                'S', new ItemStack(Item.silk), 'B', new ItemStack(Block.blockSteel),
                'O', new ItemStack(Block.stone));

        JaffaCraftingHandler.AddPersistentItem(JaffaItem.mallet, true, -1);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.malletStone, true, -1);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.malletIron, true, -1);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.malletDiamond, true, -1);

        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.beans)), new ItemStack(Item.dyePowder, 1, 3));
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.butter)), new ItemStack(Item.bucketMilk));
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.cakeTin)), new ItemStack(Item.ingotIron));

        // moved to the trees module because of the peanut
//        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.browniesPastry)), new ItemStack(getItem(JaffaItem.peanut)),
//                new ItemStack(getItem(JaffaItem.pastry)), new ItemStack(getItem(JaffaItem.chocolate)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.puffPastry)), new ItemStack(getItem(JaffaItem.butter)),
                new ItemStack(getItem(JaffaItem.butter)), new ItemStack(getItem(JaffaItem.butter)), new ItemStack(Item.egg),
                new ItemStack(getItem(JaffaItem.flour)), new ItemStack(getItem(JaffaItem.flour)));

        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.peanut)), "SSS", 'S', new ItemStack(Item.seeds));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.browniesInTinRaw)), "P", "T", 'P', new ItemStack(getItem(JaffaItem.browniesPastry)), 'T', new ItemStack(getItem(JaffaItem.cakeTin)));

        GameRegistry.addSmelting(getItem(JaffaItem.browniesInTinRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.browniesInTin)), 1F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.brownie), 15), "S", "T", 'S', new ItemStack(Item.swordSteel), 'T', new ItemStack(getItem(JaffaItem.browniesInTin)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sweetRollRaw), 10), new ItemStack(getItem(JaffaItem.puffPastry)), new ItemStack(Item.stick));

        GameRegistry.addSmelting(getItem(JaffaItem.sweetRollRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.sweetRoll)), 0.2F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.cream), 4), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.sugar), new ItemStack(Item.bucketMilk));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.creamRoll)), "RC", 'R', new ItemStack(getItem(JaffaItem.sweetRoll)), 'C', new ItemStack(getItem(JaffaItem.cream)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.flour), 3), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat),
                new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.paper));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sausageRaw), 5), " F ", "PPP", 'F', new ItemStack(getItem(JaffaItem.flour)), 'P', new ItemStack(Item.porkRaw));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bunRaw), 8), "PP", 'P', new ItemStack(getItem(JaffaItem.pastry)));

        GameRegistry.addSmelting(getItem(JaffaItem.bunRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.bun)), 0.2F);
        GameRegistry.addSmelting(getItem(JaffaItem.sausageRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.sausage)), 0.2F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.hotdog)), "S", "B", 'S', new ItemStack(getItem(JaffaItem.sausage)), 'B', new ItemStack(getItem(JaffaItem.bun)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.chocolateWrapper), 8), "XXX", "XCX", "XXX", 'X', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 5));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.chocolateBar), 2), "C", "C", "W", 'C', new ItemStack(getItem(JaffaItem.chocolate)), 'W', new ItemStack(getItem(JaffaItem.chocolateWrapper)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.wrapperJaffas), 8), "PPP", "PCP", "PPP", 'P', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 12));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jaffasPack)), new ItemStack(getItem(JaffaItem.wrapperJaffas)),
                new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)),
                new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)),
                new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jaffasPackR)), new ItemStack(getItem(JaffaItem.wrapperJaffas)),
                new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)),
                new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)),
                new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jaffasPackO)), new ItemStack(getItem(JaffaItem.wrapperJaffas)),
                new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)),
                new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)),
                new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)));

        GameRegistry.addRecipe(new ItemStack(mod_jaffas.instance.blockJaffaBomb), "J", "R", "T", 'J', new ItemStack(getItem(JaffaItem.jaffasPack)),
                'R', new ItemStack(getItem(JaffaItem.jaffasPackR)), 'T', new ItemStack(Block.tnt));
        GameRegistry.addRecipe(new ItemStack(mod_jaffas.instance.blockJaffaBomb), "J", "O", "T", 'J', new ItemStack(getItem(JaffaItem.jaffasPack)),
                'O', new ItemStack(getItem(JaffaItem.jaffasPackO)), 'T', new ItemStack(Block.tnt));

        //RecipesFridge.AddRecipe(Block.dirt.blockID, new ItemStack(Block.gravel));

        //GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.vanillaBeans)), new ItemStack(Item.dyePowder, 1, 3), new ItemStack(Item.dyePowder, 1, 11));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.waferIcecreamRaw), 2), "PP", "PP", 'P', new ItemStack(getItem(JaffaItem.pastry)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.coneRaw), 1), "P P", " P ", 'P', new ItemStack(getItem(JaffaItem.pastry)));

        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.vanillaPowder)), new ItemStack(getItem(JaffaItem.vanillaBeans)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.vanillaIcecreamRaw), 4), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(getItem(JaffaItem.vanillaPowder)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.chocolateIcecreamRaw), 4), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(getItem(JaffaItem.beans)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.icecreamRaw), 4), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(Item.snowball));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.vanillaIcecream), 4), "S", "C", 'S', new ItemStack(getItem(JaffaItem.vanillaIcecreamFrozen)), 'C', new ItemStack(getItem(JaffaItem.cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.chocolateIcecream), 4), "S", "C", 'S', new ItemStack(getItem(JaffaItem.chocolateIcecreamFrozen)), 'C', new ItemStack(getItem(JaffaItem.cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.russianIcecream), 4), "W", "I", "W", 'W', new ItemStack(getItem(JaffaItem.waferIcecream)), 'I', new ItemStack(getItem(JaffaItem.icecreamFrozen)));

        RecipesFridge.AddRecipe(getItem(JaffaItem.icecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.icecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(JaffaItem.vanillaIcecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.vanillaIcecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(JaffaItem.chocolateIcecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.chocolateIcecreamFrozen)));

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.ores)) {
            GameRegistry.addRecipe(new ItemStack(mod_jaffas.instance.blockFridge), "GGG", "IMI", "SRS", 'G', new ItemStack(Item.ingotGold), 'I', new ItemStack(Block.blockSteel), 'M', new ItemStack(Block.fenceIron), 'S', new ItemStack(Block.stone), 'R', new ItemStack(Item.redstone));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutRaw)), " P ", "P P", " P ", 'P', new ItemStack(getItem(JaffaItem.pastry)));
        GameRegistry.addSmelting(getItem(JaffaItem.donutRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.donut)), 0.25F);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutChocolate), 8), "C", "D", 'C', new ItemStack(getItem(JaffaItem.chocolate)), 'D', new ItemStack(getItem(JaffaItem.donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutPink), 8), "C", "D", 'C', new ItemStack(getItem(JaffaItem.jamR)), 'D', new ItemStack(getItem(JaffaItem.donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutSugar), 8), "C", "D", 'C', new ItemStack(Item.sugar), 'D', new ItemStack(getItem(JaffaItem.donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutSprinkled)), "C", "D", 'C', new ItemStack(getItem(JaffaItem.sprinkles)), 'D', new ItemStack(getItem(JaffaItem.donutChocolate)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaL), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamL)), 'Z', new ItemStack(getItem(JaffaItem.cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaP), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamP)), 'Z', new ItemStack(getItem(JaffaItem.cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaV), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamV)), 'Z', new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addSmelting(getItem(JaffaItem.lemons).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamL)), 0.5F);
        GameRegistry.addSmelting(getItem(JaffaItem.oranges).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamO)), 0.5F);
        GameRegistry.addSmelting(getItem(JaffaItem.plums).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamP)), 0.5F);


        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sprinkles), 16), new ItemStack(Item.sugar), new ItemStack(Item.sugar), new ItemStack(Item.sugar),
                new ItemStack(getItem(JaffaItem.jamMix)), new ItemStack(Item.egg));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bagOfSeeds)), "SXS", "SLS", "SSS", 'S', new ItemStack(Item.seeds), 'X', new ItemStack(Item.silk), 'L', new ItemStack(Item.leather));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.magnifier)), "GG ", "GG ", "  I", 'G', new ItemStack(Block.glass), 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.bagOfSeedsIdentified)), new ItemStack(getItem(JaffaItem.magnifier)), new ItemStack(getItem(JaffaItem.bagOfSeeds)));

        GameRegistry.addRecipe(new ItemStack(mod_jaffas.instance.itemJaffaPlate), "BBB", " J ", " B ", 'B', new ItemStack(Block.cloth, 1, 15), 'J', new ItemStack(getItem(JaffaItem.jaffa)));

        GameRegistry.addSmelting(getItem(JaffaItem.vanillaPowder).shiftedIndex, new ItemStack(getItem(JaffaItem.jamV)), 0.6F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jamMix), 3),
                new ItemStack(getItem(JaffaItem.jamV)), new ItemStack(getItem(JaffaItem.jamR)),
                new ItemStack(getItem(JaffaItem.jamL)), new ItemStack(getItem(JaffaItem.jamO)),
                new ItemStack(getItem(JaffaItem.jamP))
        );

        GameRegistry.addSmelting(getItem(JaffaItem.raspberries).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamRaspberry)), 0.5F);
        GameRegistry.addSmelting(getItem(JaffaItem.strawberries).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamStrawberry)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaRaspberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamRaspberry)), 'Z', new ItemStack(getItem(JaffaItem.cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaStrawberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamStrawberry)), 'Z', new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.kettle)), "XS ", " XX", " XX", 'X', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.cupRaw)), "XXX", "XX ", 'X', new ItemStack(Item.clay));
        GameRegistry.addSmelting(getItem(JaffaItem.cupRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.cup)), 3);
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.coffee)), new ItemStack(getItem(JaffaItem.coffeeRoasted)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.kettleWaterCold)), "W", "K", 'W', new ItemStack(Item.bucketWater), 'K', new ItemStack(getItem(JaffaItem.kettle)));
        GameRegistry.addSmelting(getItem(JaffaItem.kettleWaterCold).shiftedIndex, new ItemStack(getItem(JaffaItem.kettleWaterHot)), 0);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.cupCoffee)), "K", "C", "U",
                'K', new ItemStack(getItem(JaffaItem.kettleWaterHot), 1, -1), 'C', new ItemStack(getItem(JaffaItem.coffee)), 'U', new ItemStack(getItem(JaffaItem.cup)));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.kettleWaterHot, true, JaffaItem.kettle);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.knifeKitchen)), "I  ", " I ", "  S", 'I', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.knifeKitchen, true, -1);

        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatChopped), 4), "K", "M", 'K', new ItemStack(getItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(Item.porkRaw));
        //GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.rollChopped), 1), "K", "M", 'K', new ItemStack(getItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(getItem(JaffaItem.roll)));
        RecipesBoard.addRecipeSimple(JaffaItem.roll, JaffaItem.rollChopped);
        RecipesBoard.addRecipe(new ItemStack(Item.porkRaw), new ItemStack(getItem(JaffaItem.meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.beefRaw), new ItemStack(getItem(JaffaItem.meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.chickenRaw), new ItemStack(getItem(JaffaItem.meatChopped)));
        RecipesBoard.addRecipe(new ItemStack(Item.fishRaw), new ItemStack(getItem(JaffaItem.meatChopped)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.ironSkewer)), "  I", " I ", "I  ", 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.skewerRaw)), new ItemStack(getItem(JaffaItem.ironSkewer)), new ItemStack(getItem(JaffaItem.rollChopped)), new ItemStack(getItem(JaffaItem.meatChopped)));
        GameRegistry.addSmelting(getItem(JaffaItem.skewerRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.skewer)), 2F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.rollRaw), 8), " P", "P ", 'P', new ItemStack(getItem(JaffaItem.pastry)));
        GameRegistry.addSmelting(getItem(JaffaItem.rollRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.roll)), 0.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(JaffaItem.tomatoChopped)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(JaffaItem.paprikaChopped)));
        GameRegistry.addSmelting(getItem(JaffaItem.omeletteRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.omelette)), 1.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.brownPastry)), new ItemStack(getItem(JaffaItem.pastry)), new ItemStack(getItem(JaffaItem.chocolate)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.gingerbread)), new ItemStack(getItem(JaffaItem.pastry)), new ItemStack(getItem(JaffaItem.honey)));

        //honey recipe
        if (mod_jaffas.instance.IsForestryDetected()) {
            ItemStack i = forestry.api.core.ItemInterface.getItem("honeyDrop");
            GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.honey)), "H", "H", "B", 'H', i, 'B', Item.glassBottle);
        } else {
            GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.honey)), "SSS", "SYS", " B ", 'B', Item.glassBottle, 'S', Item.sugar, 'Y', new ItemStack(Item.dyePowder, 1, 11));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.hamburgerBunRaw)), " O ", "OOO", 'O', getItem(JaffaItem.pastry));
        GameRegistry.addSmelting(getItem(JaffaItem.hamburgerBunRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.hamburgerBun)), 0.5f);
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.cheese)), new ItemStack(getItem(JaffaItem.butter)));

        RecipesBoard.addRecipe(JaffaItem.cheese, 1, JaffaItem.cheeseSlice, 4);

        GameRegistry.addSmelting(getItem(JaffaItem.coneRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.cone)), 1f);
        GameRegistry.addSmelting(getItem(JaffaItem.waferIcecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.waferIcecream)), 1f);

        //GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.mincedMeat),2),);
        //addOreRecipe(new ShapedOreRecipe(new ItemStack(getItem(JaffaItem.mincedMeat), 2), true, new Object[]{""}));
        addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(JaffaItem.mincedMeat), 2), Items.MINCEABLEMEAT, getItem(JaffaItem.grinderMeat)));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.grinderMeat);

        JaffaCraftingHandler.AddPersistentItem(JaffaItem.grater);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.cheeseGrated)), getItem(JaffaItem.grater), getItem(JaffaItem.cheese));
        RecipesBoard.addRecipe(JaffaItem.salami, 1, JaffaItem.salamiSliced, 1);

        GameRegistry.addSmelting(getItem(JaffaItem.pizzaRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.pizza)), 5f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.salami)), "M  ", "SM ", "  M", 'M', getItem(JaffaItem.mincedMeat), 'S', Item.silk);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.pizzaRaw)), "SCK", "PPP", " T ", 'S', getItem(JaffaItem.salamiSliced), 'C', getItem(JaffaItem.cheeseGrated), 'K', getItem(JaffaItem.bottleKetchup), 'P', getItem(JaffaItem.pastry), 'T', getItem(JaffaItem.cakeTin));

        JaffaCraftingHandler.AddPersistentItem(JaffaItem.bottleKetchup, false, JaffaItem.bottleEmpty);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.bottleBrownMustard, false, JaffaItem.bottleEmpty);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.bottleMustard, false, JaffaItem.bottleEmpty);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.wolfHelmet)), " S ", "S S", 'S', getItem(JaffaItem.wolfSkin));
        GameRegistry.addRecipe(getItemStack(JaffaItem.wolfChest), "S S", "SCS", "CCC", 'S', getItem(JaffaItem.wolfSkin), 'C', new ItemStack(Block.cloth, 1, 14));
        GameRegistry.addRecipe(getItemStack(JaffaItem.wolfLeggins), "SCS", "S S", "C C", 'S', getItem(JaffaItem.wolfSkin), 'C', Item.leather);
        GameRegistry.addRecipe(getItemStack(JaffaItem.wolfBoots), "S S", "S S", 'S', getItem(JaffaItem.wolfSkin));
    }

    private static ItemStack getItemStack(JaffaItem item) {
        return new ItemStack(getItem(item));
    }

    private static void addRecipe(IRecipe recipe) {
        CraftingManager.getInstance().getRecipeList().add(recipe);
    }

    private static void AddMalletShapedRecipe(ItemStack output, ItemStack input) {
        mod_jaffas.instance.AddMalletShapedRecipe(output, input);
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
