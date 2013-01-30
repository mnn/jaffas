package monnef.jaffas.food.item;

import monnef.jaffas.food.block.BlockPie;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.oredict.OreDictionary;

import static monnef.jaffas.food.item.ItemManager.getItemInfo;
import static monnef.jaffas.food.item.JaffaItem.*;

public class Items extends ItemManagerAccessor {
    public static final String MINCEABLEMEAT = "jaffasMinceAbleMeat";
    public static final String JAFFA = "jaffasAny";
    public static final String JAFFA_FILLED = "jaffasFilled";
    public static final String MUSHROOM = "jaffasMushroom";

    public enum Juice {
        LEMON(JaffaItem.juiceLemon, 181, "Lemon Juice", JaffaItem.glassLemon, 186, ""),
        ORANGE(juiceOrange, 182, "Orange Juice", glassOrange, 187, ""),
        APPLE(juiceApple, 183, "Apple Juice", glassApple, 188, ""),
        RASPBERRY(juiceRaspberry, 184, "Raspberry Juice", glassRaspberry, 189, "");

        public final JaffaItem juiceBottle;
        public final int textureIndex;
        public final String title;
        public final JaffaItem glass;
        public final int textureIndexGlass;
        public final String glassTitle;

        Juice(JaffaItem juiceBottle, int textureIndex, String title, JaffaItem glass, int textureIndexGlass, String glassTitle) {
            this.juiceBottle = juiceBottle;
            this.textureIndex = textureIndex;
            this.title = title;
            this.textureIndexGlass = textureIndexGlass;
            this.glass = glass;

            if (glassTitle.equals("")) {
                this.glassTitle = "Glass of " + this.title;
            } else {
                this.glassTitle = glassTitle;
            }
        }
    }

    @Override
    public ModulesEnum getMyModule() {
        return ModulesEnum.food;
    }

    private ItemJaffaFood createJaffaFood(JaffaItem jaffaItem, int heal, float saturation) {
        return (ItemJaffaFood) createJaffaFood(jaffaItem).Setup(heal, saturation);
    }

    private ItemJaffaTool createJaffaTool(JaffaItem ji, int durability) {
        return (ItemJaffaTool) createJaffaTool(ji).Setup(durability);
    }

    private Item createJaffaPack(JaffaItem ji, ItemStack stack) {
        return createJaffaPack(ji).Setup(stack);
    }

    private Item getItem(JaffaItem jaffaItem) {
        return ItemManager.getItem(jaffaItem);
    }

    private String getJaffaTitle(String type) {
        String title = mod_jaffas.jaffasTitle;

        if (type == null || type.isEmpty()) {
            return title;
        }
        StringBuilder s = new StringBuilder(type);
        s.append(" ");
        s.append(title);
        return s.toString();
    }

    @Override
    public void InitializeItemInfos() {
        AddItemInfo(pastrySweet, "Pastry", 13, "Sweet Pastry");
        AddItemInfo(cake, "Cake", 1, "Sponge Cake");
        AddItemInfo(jamO, "Jam Orange", 2, "Orange Jam");
        AddItemInfo(jamR, "Jam Red", 3, "Apple Jam");
        AddItemInfo(jaffaO, "Jaffa Orange", 4, getJaffaTitle("Orange"));
        AddItemInfo(jaffaR, "Jaffa Red", 5, getJaffaTitle("Apple"));
        AddItemInfo(jaffa, "Jaffa", 6, getJaffaTitle(""));
        AddItemInfo(chocolate, "Chocolate", 7, "Chocolate");
        AddItemInfo(apples, "Apples", 10, "Apples");
        AddItemInfo(beans, "Beans", 8, "Cocoa Powder");
        AddItemInfo(sweetBeans, "Sweet Beans", 9, "Sweet Cocoa Powder");
        AddItemInfo(butter, "Butter", 12, "Butter");
        AddItemInfo(mallet, "Mallet", 23, "Little Wooden Mallet");
        AddItemInfo(malletStone, "Mallet Stone", 24, "Little Stone Mallet");
        AddItemInfo(malletIron, "Mallet Iron", 25, "Little Iron Mallet");
        AddItemInfo(malletDiamond, "Mallet Diamond", 26, "Little Diamond Mallet");
        AddItemInfo(malletHead, "Mallet Head", 27, "Wooden Mallet Head");
        AddItemInfo(malletHeadStone, "Mallet Head Stone", 28, "Stone Mallet Head");
        AddItemInfo(malletHeadIron, "Mallet Head Iron", 29, "Iron Mallet Head");
        AddItemInfo(malletHeadDiamond, "Mallet Head Diamond", 30, "Diamond Mallet Head");
        AddItemInfo(browniesPastry, "Brown Pastry", 14, "Brownies Pastry");
        AddItemInfo(puffPastry, "Puff Pastry", 15, "Puff Pastry");
        //AddItemInfo(JaffaItem.peanut, "Peanut", 16, "Peanut");
        AddItemInfo(cream, "Cream", 17, "Cream");
        AddItemInfo(sweetRoll, "Roll", 18, "Roll");
        AddItemInfo(creamRoll, "Cream Roll", 19, "Cream Roll");
        AddItemInfo(cakeTin, "Cake Tin", 20, "Cake Tin");
        AddItemInfo(browniesInTin, "Brownies", 21, "Brownies");
        AddItemInfo(brownie, "Brownie", 22, "Brownie");
        AddItemInfo(sweetRollRaw, "Roll Raw", 31, "Raw Roll");
        AddItemInfo(browniesInTinRaw, "Raw Brownies", 32, "Raw Brownies");
        AddItemInfo(bunRaw, "Raw Bun", 44, "Raw Bun");
        AddItemInfo(bun, "Bun", 45, "Bun");
        AddItemInfo(sausageRaw, "Sausage Raw", 46, "Raw Sausage");
        AddItemInfo(sausage, "Sausage", 47, "Sausage");
        AddItemInfo(hotdog, "Hotdog", 48, "Hotdog");
        AddItemInfo(flour, "Flour", 49, "Flour");
        AddItemInfo(chocolateWrapper, "Chocolate Wrapper", 33, "Chocolate Wrapper");
        AddItemInfo(chocolateBar, "Chocolate Bar", 34, "Chocolate Bar");
        AddItemInfo(wrapperJaffas, "Wrapper Jaffas", 50, mod_jaffas.jaffasTitle + " Wrapper");
        AddItemInfo(jaffasPack, "Jaffa Cakes Pack", 51, mod_jaffas.jaffasTitle + " Pack");
        AddItemInfo(jaffasPackO, "Orange Jaffa Cakes Pack", 51, "Orange " + mod_jaffas.jaffasTitle + " Pack");
        AddItemInfo(jaffasPackR, "Red Jaffa Cakes Pack", 51, "Apple " + mod_jaffas.jaffasTitle + " Pack");
        AddItemInfo(vanillaBeans, "Vanilla Beans", 52, "Vanilla Beans");
        AddItemInfo(waferIcecream, "Wafer Ice-cream", 53, "Wafer");
        AddItemInfo(cone, "Icecream Cone", 54, "Cone");
        AddItemInfo(vanillaPowder, "Vanilla Powder", 55, "Vanilla Powder");
        AddItemInfo(vanillaIcecreamRaw, "Vanilla Ice-cream Raw", 56, "Vanilla Ice-cream");
        AddItemInfo(chocolateIcecreamRaw, "Chocolate Ice-cream Raw", 57, "Chocolate Ice-cream");
        AddItemInfo(icecreamRaw, "Ice-cream Raw", 58, "Ice-cream");
        AddItemInfo(vanillaIcecream, "Vanilla Scooped Ice-cream", 59, "Scooped Ice-cream");
        AddItemInfo(chocolateIcecream, "Chocolate Scooped Ice-cream", 60, "Scooped Ice-cream");
        AddItemInfo(russianIcecream, "Russian Ice-cream", 61, "Russian Ice-cream");
        AddItemInfo(vanillaIcecreamFrozen, "Vanilla Ice-cream Frozen", 62, "Vanilla Ice-cream *");
        AddItemInfo(chocolateIcecreamFrozen, "Chocolate Ice-cream Frozen", 63, "Chocolate Ice-cream *");
        AddItemInfo(icecreamFrozen, "Ice-cream Frozen", 64, "Ice-cream *");
        AddItemInfo(donutRaw, "Donut Raw", 71, "Raw Donut");
        AddItemInfo(donut, "Donut", 72, "Donut");
        AddItemInfo(donutChocolate, "Donut Chocolate", 73, "Chocolate Donut");
        AddItemInfo(donutPink, "Donut Apple", 74, "Apple Donut");
        AddItemInfo(donutSugar, "Donut Sugar", 75, "Powdered Donut");
        AddItemInfo(donutSprinkled, "Donut Sprinkled", 76, "Sprinkled Donut");
        AddItemInfo(jaffaV, "Jaffa Vanilla", 77, getJaffaTitle("Vanilla"));
        AddItemInfo(jaffaL, "Jaffa Lemon", 78, getJaffaTitle("Lemon"));
        AddItemInfo(jamP, "Jam Plum", 79, "Plum Jam");
        AddItemInfo(jamL, "Jam Lemon", 80, "Lemon Jam");
        AddItemInfo(jamV, "Vanilla Jam", 81, "Vanilla Jam");
        AddItemInfo(lemons, "Lemons", 82, "Lemons");
        AddItemInfo(oranges, "Oranges", 83, "Oranges");
        AddItemInfo(plums, "Plums", 84, "Plums");
        AddItemInfo(sprinkles, "Sprinkles", 87, "Sprinkles");
        AddItemInfo(bagOfSeeds, "Bag Of Seeds Unidentified", 89, "Bag Of Seeds [Unidentified]");
        AddItemInfo(bagOfSeedsIdentified, "Bag Of Seeds", 89, "Bag Of Seeds");
        AddItemInfo(magnifier, "Magnifier", 91, "Magnifier");
        AddItemInfo(jaffaP, "Jaffa Plum", 86, getJaffaTitle("Plum"));
        AddItemInfo(jamMix, "Jam Mix", 110, "Mix of Jams");

        AddItemInfo(kettle, "Kettle", 92, "Empty Kettle");
        AddItemInfo(cup, "Cup", 93, "Cup");
        AddItemInfo(cupCoffee, "Coffee Cup", 94, "Cup of Coffee");
        AddItemInfo(cupRaw, "Raw Cup", 109, "Raw Cup");
        AddItemInfo(kettleWaterCold, "Kettle Cold", 92, "Kettle With Cold Water");
        AddItemInfo(kettleWaterHot, "Kettle Hot", 92, "Kettle With Hot Water");

        AddItemInfo(omeletteRaw, "Omelette Raw", 97, "Raw Omelette");
        AddItemInfo(omelette, "Omelette", 98, "Omelette");
        AddItemInfo(tomatoChopped, "Tomato Chopped", 99, "Chopped Tomatos");
        AddItemInfo(paprikaChopped, "Pepper Chopped", 100, "Chopped Peppers");

        AddItemInfo(grinderMeat, "Meat Grinder", 101, "Meat Grinder");
        AddItemInfo(wienerCocktail, "Cocktail Wiener", 102, "Cocktail Wiener");
        AddItemInfo(jaffaStrawberry, "Jaffa Strawberry", 103, getJaffaTitle("Strawberry"));
        AddItemInfo(jaffaRaspberry, "Jaffa Raspberry", 104, getJaffaTitle("Raspberry"));
        AddItemInfo(raspberries, "Raspberries", 105, "Raspberries");
        AddItemInfo(strawberries, "Strawberries", 106, "Strawberries");
        AddItemInfo(jamRaspberry, "Jam Raspberry", 107, "Raspberry Jam");
        AddItemInfo(jamStrawberry, "Jam Strawberry", 108, "Strawberry Jam");

        AddItemInfo(rollRaw, "Not-Sweet Raw Roll", 111, "Raw Roll");
        AddItemInfo(roll, "Not-Sweet Roll", 65, "Roll");
        AddItemInfo(rollChopped, "Chopped Not-Sweet Roll", 66, "Chopped Roll");
        AddItemInfo(meatChopped, "Chopped Meat", 67, "Chopped Meat");
        AddItemInfo(skewer, "Skewer", 68, "Skewer");
        AddItemInfo(ironSkewer, "Iron Skewer", 69, "Iron Skewer");
        AddItemInfo(knifeKitchen, "Kitchen Knife", 70, "Kitchen Knife");

        AddItemInfo(coffee, "Coffee", 8, "Coffee");
        AddItemInfo(coffeeRoasted, "Roasted Coffee", 112, "Roasted Coffee");

        AddItemInfo(skewerRaw, "Skewer Raw", 85, "Raw Skewer");

        AddItemInfo(brownPastry, "Brown Pastry 2", 14, "Brown Pastry");

        AddItemInfo(coconutPowder, "Coconut Powder", 139, "Coconut Powder");
        AddItemInfo(honey, "Honey", 140, "Honey");

        AddItemInfo(gingerbread, "Gingerbread", 14, "Gingerbread");

        AddItemInfo(hamburgerBunRaw, "Hamburger Bun Raw", 113, "Raw Hamburger Bun");
        AddItemInfo(hamburgerBun, "Hamburger Bun", 114, "Hamburger Bun");
        AddItemInfo(cheese, "Cheese", 115, "Cheese");
        AddItemInfo(cheeseSlice, "Cheese Slice", 116, "Slice of Cheese");
        AddItemInfo(rawBurger, "Raw Burger", 117, "Raw Burger");
        AddItemInfo(burger, "Burger", 118, "Burger");
        AddItemInfo(onionSliced, "Sliced Onion", 119, "Sliced Onion");
        AddItemInfo(hamburger, "Hamburger", 120, "Hamburger");
        AddItemInfo(cheeseburger, "Cheeseburger", 121, "Cheeseburger");
        AddItemInfo(fryingPan, "Frying Pan", 122, "Frying Pan");
        AddItemInfo(fryingPanBurgerRaw, "Frying Pan Burger Raw", 123, "Raw Burger in Frying Pan");
        AddItemInfo(fryingPanEggRaw, "Frying Pan Egg Raw", 124, "Raw Egg in Frying Pan");
        AddItemInfo(fryingPanBurger, "Frying Pan Burger", 123, "Burger in Frying Pan");
        AddItemInfo(fryingPanEgg, "Frying Pan Egg", 124, "Egg in Frying Pan");
        AddItemInfo(eggFried, "Fried Egg", 125, "Fried Egg");
        AddItemInfo(breadRaw, "Raw Bread", 126, "Raw Bread");
        AddItemInfo(bread, "Bread", 127, "Bread");
        AddItemInfo(breadSlice, "Bread Slice", 128, "Bread Slice");
        AddItemInfo(breadSliceToasted, "Bread Slice Toasted", 129, "Bread Slice Toasted");
        AddItemInfo(breadSliceJam, "Bread Slice with Jam", 130, "Bread Slice with Jam");
        AddItemInfo(breadSliceButter, "Bread Slice with Butter", 131, "Bread Slice with Butter");
        AddItemInfo(breadSliceEgg, "Bread Slice with Egg", 132, "Bread Slice with Egg");

        AddItemInfo(bottleEmpty, "Bottle", 133, "Empty Bottle");
        AddItemInfo(bottleKetchup, "Bottle of Ketchup", 134, "Bottle of Catchup");
        AddItemInfo(bottleMustard, "Bottle of Mustard", 135, "Bottle of Mustard");
        AddItemInfo(bottleBrownMustard, "Bottle of Brown Mustard", 136, "Bottle of Brown Mustard");

        AddItemInfo(meatCleaver, "Meat Cleaver", 137, "Meat Cleaver");
        AddItemInfo(mincedMeat, "Minced Meat", 138, "Minced Meat");

        AddItemInfo(sink, "Faucet", 141, "Basin");
        AddItemInfo(coneRaw, "Cone Raw", 143, "Raw Cone");
        AddItemInfo(waferIcecreamRaw, "Wafer Ice-cream Raw", 144, "Raw Wafer");

        AddItemInfo(grater, "Grater", 145, "Grater");
        AddItemInfo(cheeseGrated, "Grated Cheese", 146, "Grated Cheese");
        AddItemInfo(salami, "Salami", 147, "Salami");
        AddItemInfo(salamiSliced, "Sliced Salami", 148, "Sliced Salami");

        AddItemInfo(pizza, "Pizza In Tin", 149, "Pizza");
        AddItemInfo(pizzaRaw, "Pizza In Tin Raw", 150, "Raw Pizza");

        AddItemInfo(wolfHelmet, "Wolf Helmet", 153, "Wolf Helmet");
        AddItemInfo(wolfBoots, "Wolf Boots", 154, "Wolf Boots");
        AddItemInfo(wolfChest, "Wolf Chest", 152, "Wolf Chestplate");
        AddItemInfo(wolfLeggins, "Wolf Leggings", 155, "Wolf Leggings");
        AddItemInfo(wolfSkin, "Wolf Skin", 151, "Wolf Skin");

        AddItemInfo(pastry, "Sugar-Free Pastry", 13, "Pastry");

        AddItemInfo(milkBoxEmpty, "Milk Box", 164, "Empty Milk Box");
        AddItemInfo(milkBoxFull, "Milk Box Full", 165, "Milk");

        AddItemInfo(crumpledPaper, "Crumpled Paper", 162, "Crumpled Paper");
        AddItemInfo(scrap, "Scrap", 149, "Scrap");

        AddItemInfo(JaffaItem.chipsRaw, "Raw Chips", 166, "Raw Chips");
        AddItemInfo(JaffaItem.chips, "Chips", 167, "Chips");
        AddItemInfo(JaffaItem.fryingPanChipsRaw, "Frying Pan Chips Raw", 168, "Raw Chips in Frying Pan");
        AddItemInfo(JaffaItem.fryingPanChips, "Frying Pan Chips", 168, "Chips in Frying Pan");

        AddItemInfo(JaffaItem.pieStrawberryRaw, "Raw Strawberry Pie", BlockPie.textureIndexFromMeta[0] + 13, "");
        AddItemInfo(JaffaItem.pieRaspberryRaw, "Raw Raspberry Pie", BlockPie.textureIndexFromMeta[1] + 13, "");
        AddItemInfo(JaffaItem.pieVanillaRaw, "Raw Vanilla Pie", BlockPie.textureIndexFromMeta[2] + 13, "");
        AddItemInfo(JaffaItem.piePlumRaw, "Raw Plum Pie", BlockPie.textureIndexFromMeta[3] + 13, "");

        AddItemInfo(JaffaItem.spawnStoneLittle, "Little Spawn Stone", 173, "Iron Home Stone");
        AddItemInfo(JaffaItem.spawnStoneMedium, "Crude Spawn Stone", 174, "Golden Home Stone");
        AddItemInfo(JaffaItem.spawnStoneBig, "Fine Spawn Stone", 175, "Diamond Home Stone");

        AddItemInfo(JaffaItem.jaffarrolHelmet, "Jarmor Helmet", 177, "");
        AddItemInfo(JaffaItem.jaffarrolBoots, "Jarmor Boots", 178, "");
        AddItemInfo(JaffaItem.jaffarrolChest, "Jarmor Chestplate", 176, "");
        AddItemInfo(JaffaItem.jaffarrolLeggins, "Jarmor Leggings", 179, "");

        AddItemInfo(JaffaItem.juiceBottle, "Juice Bottle", 180, "");
        for (Juice juice : Juice.values()) {
            AddItemInfo(juice.juiceBottle, juice.juiceBottle.toString().toLowerCase(), juice.textureIndex, juice.title);
            AddItemInfo(juice.glass, juice.glass.toString().toLowerCase(), juice.textureIndexGlass, juice.glassTitle);
        }
        AddItemInfo(JaffaItem.glassEmpty, "Glass", 185, "");
        AddItemInfo(JaffaItem.glassMilk, "glassMilk", 190, "Glass of Milk");

        AddItemInfo(JaffaItem.woodenBowl, "Wooden Bowl", 195, "");
        AddItemInfo(JaffaItem.cookedMushroomsRaw, "Raw Mushrooms", 196, "");
        AddItemInfo(JaffaItem.cookedMushrooms, "Cooked Mushrooms", 197, "");

        AddItemInfo(JaffaItem.peanutsSugar, "Sugarcoated Peanuts", 191, "");
        AddItemInfo(JaffaItem.peanutsCaramelized, "Caramelized Peanuts", 192, "");

        AddItemInfo(JaffaItem.pepperStuffedRaw, "Raw Stuffed Pepper", 193, "");
        AddItemInfo(JaffaItem.pepperStuffed, "Stuffed Pepper", 194, "");
    }

    @Override
    public void CreateItems() {
        createJaffaItem(pastrySweet);
        createJaffaItem(jamO);
        createJaffaItem(jamR);

        createJaffaFood(cake, 1, 0.2F);
        createJaffaFood(jaffaO, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(jaffaR, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(jaffa, 2, 0.5F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.2F);

        createJaffaItem(chocolate);
        createJaffaItem(apples);
        createJaffaItem(beans);
        createJaffaItem(sweetBeans);
        createJaffaItem(butter);

        createJaffaTool(mallet, 8);
        createJaffaTool(malletStone, 16);
        createJaffaTool(malletIron, 64);
        createJaffaTool(malletDiamond, 512);

        createJaffaItem(malletHead);
        createJaffaItem(malletHeadStone);
        createJaffaItem(malletHeadIron);
        createJaffaItem(malletHeadDiamond);

        createJaffaItem(browniesPastry);
        createJaffaItem(puffPastry);
        //createJaffaItem(JaffaItem.peanut);
        createJaffaItem(cream);
        createJaffaItem(sweetRoll);
        createJaffaItem(cakeTin);
        createJaffaItem(browniesInTin);

        createJaffaItem(sweetRollRaw);
        createJaffaItem(browniesInTinRaw);

        createJaffaFood(creamRoll, 4, 1F).setPotionEffect(Potion.digSpeed.id, 60, 1, 0.15F);
        createJaffaFood(brownie, 2, 0.6F).setPotionEffect(Potion.jump.id, 60, 1, 0.15F);

        createJaffaItem(bunRaw);
        createJaffaItem(bun);
        createJaffaItem(sausageRaw);
        createJaffaItem(sausage);
        createJaffaItem(flour);

        createJaffaFood(hotdog, 3, 0.7F).setPotionEffect(Potion.damageBoost.id, 60, 1, 0.15F);

        createJaffaItem(chocolateWrapper);

        createJaffaFood(chocolateBar, 1, 0.5F).setPotionEffect(Potion.moveSpeed.id, 60, 1, 0.15F);

        createJaffaItem(wrapperJaffas);

        createJaffaPack(jaffasPack, new ItemStack(getItem(jaffa), 8)).setMaxStackSize(16);
        createJaffaPack(jaffasPackR, new ItemStack(getItem(jaffaR), 8)).setMaxStackSize(16);
        createJaffaPack(jaffasPackO, new ItemStack(getItem(jaffaO), 8)).setMaxStackSize(16);

        createJaffaItem(vanillaBeans);
        createJaffaItem(waferIcecream);
        createJaffaItem(cone);
        createJaffaItem(vanillaPowder);
        createJaffaItem(vanillaIcecreamRaw);
        createJaffaItem(chocolateIcecreamRaw);
        createJaffaItem(icecreamRaw);
        createJaffaItem(vanillaIcecreamFrozen);
        createJaffaItem(chocolateIcecreamFrozen);
        createJaffaItem(icecreamFrozen);
        createJaffaFood(vanillaIcecream, 2, 0.3F).setPotionEffect(Potion.moveSpeed.id, 70, 1, 0.25F);
        createJaffaFood(chocolateIcecream, 2, 0.3F).setPotionEffect(Potion.moveSpeed.id, 70, 1, 0.25F);
        createJaffaFood(russianIcecream, 2, 0.3F).setPotionEffect(Potion.moveSpeed.id, 70, 1, 0.25F);

        createJaffaItem(donutRaw);
        createJaffaItem(donut);
        createJaffaItem(jamP);
        createJaffaItem(jamL);
        createJaffaItem(jamV);
        createJaffaItem(lemons);
        createJaffaItem(oranges);
        createJaffaItem(plums);
        createJaffaItem(sprinkles);
        createJaffaItem(bagOfSeeds);
        //createJaffaItem(JaffaItem.bagOfSeedsIdentified);
        createJaffaItem(magnifier);

        createJaffaFood(jaffaP, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(jaffaV, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(jaffaL, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);

        createJaffaFood(donutChocolate, 2, 0.3F).setPotionEffect(Potion.digSpeed.id, 60, 1, 0.15F);
        createJaffaFood(donutPink, 2, 0.3F).setPotionEffect(Potion.digSpeed.id, 60, 1, 0.15F);
        createJaffaFood(donutSugar, 2, 0.3F).setPotionEffect(Potion.damageBoost.id, 60, 1, 0.15F);
        createJaffaFood(donutSprinkled, 2, 0.9F).setPotionEffect(Potion.damageBoost.id, 60, 1, 0.20F);

        //createBagOfSeed(JaffaItem.bagOfSeedsIdentified);
        this.createJaffaItemManual(bagOfSeedsIdentified, ItemBagOfSeeds.class);

        createJaffaItem(jamMix);

        createJaffaItem(kettle);
        createJaffaItem(kettleWaterCold);
        createJaffaItem(kettleWaterHot).setMaxDamage(5).setMaxStackSize(1);
        createJaffaItem(cup);
        createJaffaFood(cupCoffee, 1, 0.2F).
                setReturnItem(new ItemStack(getItem(cup))).setIsDrink().
                setPotionEffect(Potion.digSpeed.id, 35, 1, 1F).setAlwaysEdible().setMaxStackSize(16);
        createJaffaItem(cupRaw);
        createJaffaItem(omeletteRaw);
        createJaffaFood(omelette, 3, 0.5F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.2F).setMaxStackSize(16);
        createJaffaItem(tomatoChopped);
        createJaffaItem(paprikaChopped);
        createJaffaItem(grinderMeat);
        createJaffaItem(wienerCocktail);
        createJaffaItem(jamRaspberry);
        createJaffaItem(jamStrawberry);
        createJaffaItem(raspberries);
        createJaffaItem(strawberries);
        createJaffaItem(rollRaw);
        createJaffaItem(roll);
        createJaffaItem(rollChopped);
        createJaffaItem(meatChopped);
        createJaffaItem(ironSkewer);
        createJaffaFood(skewer, 4, 0.5F).setReturnItem(new ItemStack(getItem(ironSkewer))).setPotionEffect(Potion.jump.id, 60, 1, 0.15F);
        createJaffaItem(skewerRaw);
        createJaffaItem(knifeKitchen).setMaxDamage(256).setMaxStackSize(1);

        createJaffaFood(jaffaStrawberry, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(jaffaRaspberry, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);

        createJaffaItem(coffee);
        createJaffaItem(coffeeRoasted);

        createJaffaItem(brownPastry);

        createJaffaItem(coconutPowder);
        createJaffaItem(honey);
        createJaffaItem(gingerbread);

        createJaffaItem(hamburgerBunRaw);
        createJaffaItem(hamburgerBun);
        createJaffaItem(cheese);
        createJaffaItem(cheeseSlice);
        createJaffaItem(rawBurger);
        createJaffaItem(burger);
        createJaffaItem(onionSliced);

        createJaffaFood(hamburger).Setup(6, 0.9f);
        createJaffaFood(cheeseburger).Setup(6, 0.9f);

        createJaffaItem(fryingPan);
        createJaffaItem(fryingPanBurgerRaw);
        createJaffaItem(fryingPanEggRaw);
        createJaffaItem(fryingPanBurger);
        createJaffaItem(fryingPanEgg);
        createJaffaItem(eggFried);
        createJaffaItem(bread);
        createJaffaItem(breadRaw);
        createJaffaItem(breadSlice);

        createJaffaFood(breadSliceToasted).Setup(3, 0.33f);
        createJaffaFood(breadSliceJam).Setup(3, 0.33f);
        createJaffaFood(breadSliceButter).Setup(2, 0.33f);
        createJaffaFood(breadSliceEgg).Setup(3, 0.33f);

        createJaffaItem(bottleEmpty);
        createJaffaItem(bottleKetchup);
        createJaffaItem(bottleMustard);
        createJaffaItem(bottleBrownMustard);
        createJaffaItem(mincedMeat);
        createJaffaItemManual(meatCleaver,
                new ItemCleaver(getItemInfo(meatCleaver).getId(), mod_jaffas.EnumToolMaterialCleaver));

        createJaffaItemManual(sink, ItemSink.class);

        createJaffaItem(coneRaw);
        createJaffaItem(waferIcecreamRaw);

        createJaffaItem(grater);
        createJaffaItem(cheeseGrated);
        createJaffaItem(salami);
        createJaffaItem(salamiSliced);
        createJaffaItem(pizzaRaw);
        createJaffaItemManual(pizza, ItemPizza.class);

        createJaffaItem(wolfSkin);
        int renderIndex = mod_jaffas.proxy.addArmor("wolf");
        createJaffaArmor(wolfHelmet, mod_jaffas.EnumArmorMaterialWolf, renderIndex, ItemJaffaPlate.ArmorType.helm, "/jaffas_wolf1.png", wolfSkin);
        createJaffaArmor(wolfChest, mod_jaffas.EnumArmorMaterialWolf, renderIndex, ItemJaffaPlate.ArmorType.chest, "/jaffas_wolf1.png", wolfSkin);
        createJaffaArmor(wolfLeggins, mod_jaffas.EnumArmorMaterialWolf, renderIndex, ItemJaffaPlate.ArmorType.leggings, "/jaffas_wolf2.png", wolfSkin);
        createJaffaArmor(wolfBoots, mod_jaffas.EnumArmorMaterialWolf, renderIndex, ItemJaffaPlate.ArmorType.boots, "/jaffas_wolf1.png", wolfSkin);

        createJaffaItem(pastry);

        createJaffaItem(milkBoxEmpty);
        createJaffaItem(milkBoxFull);

        createJaffaItem(crumpledPaper);
        createJaffaItem(scrap);

        createJaffaFood(chips).Setup(3, 0.7f);
        createJaffaItem(chipsRaw);
        createJaffaItem(fryingPanChips);
        createJaffaItem(fryingPanChipsRaw);

        createJaffaItem(pieStrawberryRaw);
        createJaffaItem(pieRaspberryRaw);
        createJaffaItem(pieVanillaRaw);
        createJaffaItem(piePlumRaw);

        if (mod_jaffas.spawnStonesEnabled) {
            createJaffaItemManual(spawnStoneLittle, new ItemSpawnStone(getItemInfo(spawnStoneLittle), mod_jaffas.spawnStoneLittleCD));
            createJaffaItemManual(spawnStoneMedium, new ItemSpawnStone(getItemInfo(spawnStoneMedium), mod_jaffas.spawnStoneMediumCD));
            createJaffaItemManual(spawnStoneBig, new ItemSpawnStone(getItemInfo(spawnStoneBig), mod_jaffas.spawnStoneBigCD));
        }

        createJaffaItem(juiceBottle);
        createJaffaItem(glassEmpty);
        for (Juice juice : Juice.values()) {
            ((ItemJaffaFood) (createJaffaFood(juice.juiceBottle).Setup(12, 1f))).setIsDrink().setReturnItem(new ItemStack(getItem(juiceBottle)));
            ((ItemJaffaFood) createJaffaFood(juice.glass).Setup(5, 0.25f)).setIsDrink().setReturnItem(new ItemStack(getItem(glassEmpty)));
        }

        ((ItemJaffaFood) createJaffaFood(glassMilk).Setup(1, 0.1f)).setIsDrink().setReturnItem(new ItemStack(getItem(glassEmpty)));

        createJaffaItem(woodenBowl);
        createJaffaItem(cookedMushroomsRaw);
        ((ItemJaffaFood) createJaffaFood(cookedMushrooms).Setup(6, 0.5f)).setReturnItem(new ItemStack(getItem(woodenBowl))).setMaxStackSize(32);

        createJaffaItem(pepperStuffedRaw);
        createJaffaFood(pepperStuffed, 5, 1.0f).setPotionEffect(Potion.resistance.id, 35, 1, 0.2F).setMaxStackSize(16);

        createJaffaItem(peanutsSugar);
        createJaffaFood(peanutsCaramelized, 4, 0.2f).setPotionEffect(Potion.jump.id, 15, 1, 0.2F);

        createItemsRegistration();
    }

    public void createJaffaArmor(JaffaItem item, EnumArmorMaterial material, int renderIndex, ItemJaffaPlate.ArmorType type, String texture, Item repairItem) {
        createJaffaItemManual(item, new ItemJaffaPlate(getItemInfo(item).getId(), material, renderIndex, type, texture, repairItem));
    }

    public void createJaffaArmor(JaffaItem item, EnumArmorMaterial material, int renderIndex, ItemJaffaPlate.ArmorType type, String texture, JaffaItem repairItem) {
        createJaffaArmor(item, material, renderIndex, type, texture, repairItem == null ? null : getItem(repairItem));
    }

    private void createItemsRegistration() {
        OreDictionary.registerOre(MINCEABLEMEAT, Item.porkRaw);
        OreDictionary.registerOre(MINCEABLEMEAT, Item.fishRaw);
        OreDictionary.registerOre(MINCEABLEMEAT, Item.beefRaw);
        OreDictionary.registerOre(MINCEABLEMEAT, Item.chickenRaw);

        JaffaItem jaffas[] = new JaffaItem[]{jaffa, jaffaR, jaffaO, jaffaP, jaffaL, jaffaV, jaffaStrawberry, jaffaRaspberry};
        for (int i = 0; i < jaffas.length; i++) {
            Item item = getItem(jaffas[i]);
            if (i != 0) {
                OreDictionary.registerOre(JAFFA_FILLED, item);
            }
            OreDictionary.registerOre(JAFFA, item);
        }

        OreDictionary.registerOre(MUSHROOM, Block.mushroomBrown);
        OreDictionary.registerOre(MUSHROOM, Block.mushroomRed);
    }
}
