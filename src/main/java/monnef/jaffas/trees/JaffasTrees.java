/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.JaffasModBase;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.TilePie;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.common.JaffaCreativeTab;
import monnef.jaffas.food.common.JaffasRegistryHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.PersistentItemsCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.food.item.juice.Juices$;
import monnef.jaffas.technic.common.CompostRegister;
import monnef.jaffas.trees.block.BlockFruitCollector;
import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.block.BlockFruitLeavesDummy;
import monnef.jaffas.trees.block.BlockFruitSapling;
import monnef.jaffas.trees.block.BlockJaffaCrops;
import monnef.jaffas.trees.block.TileFruitCollector;
import monnef.jaffas.trees.block.TileFruitLeaves;
import monnef.jaffas.trees.block.TileJaffaCrops;
import monnef.jaffas.trees.client.GuiHandlerTrees;
import monnef.jaffas.trees.common.BushInfo;
import monnef.jaffas.trees.common.CommonProxy;
import monnef.jaffas.trees.common.EatableType;
import monnef.jaffas.trees.common.ItemFromFruitResult;
import monnef.jaffas.trees.common.JaffaCropProvider;
import monnef.jaffas.trees.common.LeavesInfo;
import monnef.jaffas.trees.compatibility.WailaCrops$;
import monnef.jaffas.trees.item.ItemBagCollecting;
import monnef.jaffas.trees.item.ItemBagPlanting;
import monnef.jaffas.trees.item.ItemBlockFruitSapling;
import monnef.jaffas.trees.item.ItemFruitSeeds;
import monnef.jaffas.trees.item.ItemJaffaBerry;
import monnef.jaffas.trees.item.ItemJaffaBerryEatable;
import monnef.jaffas.trees.item.ItemJaffaSeeds;
import monnef.jaffas.trees.item.ItemJaffaTreeDebugTool;
import monnef.jaffas.trees.item.ItemTrees;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.JaffasFood.getItem;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.common.ModulesEnum.technic;
import static monnef.jaffas.food.crafting.Recipes.ANY_DMG;
import static monnef.jaffas.food.crafting.Recipes.addPieRecipe;
import static monnef.jaffas.food.crafting.Recipes.getItemStack;
import static monnef.jaffas.food.item.JaffaItem.bananaInChocolate;
import static monnef.jaffas.food.item.JaffaItem.beansWithTomato;
import static monnef.jaffas.food.item.JaffaItem.beansWithTomatoRaw;
import static monnef.jaffas.food.item.JaffaItem.cakeTin;
import static monnef.jaffas.food.item.JaffaItem.chocolate;
import static monnef.jaffas.food.item.JaffaItem.duckRaw;
import static monnef.jaffas.food.item.JaffaItem.fruitSalad;
import static monnef.jaffas.food.item.JaffaItem.jamP;
import static monnef.jaffas.food.item.JaffaItem.jamRaspberry;
import static monnef.jaffas.food.item.JaffaItem.jamStrawberry;
import static monnef.jaffas.food.item.JaffaItem.jamV;
import static monnef.jaffas.food.item.JaffaItem.lambWithPeas;
import static monnef.jaffas.food.item.JaffaItem.lambWithPeasInTin;
import static monnef.jaffas.food.item.JaffaItem.lambWithPeasInTinRaw;
import static monnef.jaffas.food.item.JaffaItem.mincedMeat;
import static monnef.jaffas.food.item.JaffaItem.muttonRaw;
import static monnef.jaffas.food.item.JaffaItem.peanutsSugar;
import static monnef.jaffas.food.item.JaffaItem.pepperStuffedRaw;
import static monnef.jaffas.food.item.JaffaItem.piePlumRaw;
import static monnef.jaffas.food.item.JaffaItem.pieRaspberryRaw;
import static monnef.jaffas.food.item.JaffaItem.pieStrawberryRaw;
import static monnef.jaffas.food.item.JaffaItem.pieVanillaRaw;
import static monnef.jaffas.food.item.JaffaItem.plate;
import static monnef.jaffas.food.item.JaffaItem.plateDuckOrange;
import static monnef.jaffas.food.item.JaffaItem.soupPeaCooked;
import static monnef.jaffas.food.item.JaffaItem.soupPeaRaw;
import static monnef.jaffas.food.item.JaffaItem.soupTomatoCooked;
import static monnef.jaffas.food.item.JaffaItem.soupTomatoRaw;
import static monnef.jaffas.food.item.JaffaItem.tinDuckOrange;
import static monnef.jaffas.food.item.JaffaItem.tinDuckOrangeRaw;
import static monnef.jaffas.food.item.JaffaItem.tomatoChopped;
import static monnef.jaffas.food.item.JaffaItem.woodenBowl;
import static monnef.jaffas.technic.common.CompostRegister.DEFAULT_BLOCK_COMPOSTING_VALUE;
import static monnef.jaffas.technic.common.CompostRegister.DEFAULT_FRUIT_COMPOSTING_VALUE;
import static monnef.jaffas.trees.BushManager.bushesList;
import static monnef.jaffas.trees.common.DropType.DropsFromGrass;
import static monnef.jaffas.trees.common.EatableType.EatableNormal;
import static monnef.jaffas.trees.common.EatableType.NotEatable;
import static monnef.jaffas.trees.common.Reference.ModId;
import static monnef.jaffas.trees.common.Reference.ModName;
import static monnef.jaffas.trees.common.Reference.Version;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas")
public class JaffasTrees extends JaffasModBase {
    public static final String FORESTRY_FARM_VEGETABLES = "farmVegetables";
    private static MinecraftServer server;
    public static boolean bonemealingAllowed;

    public static final String channel = "jaffas-02";

    public static final String[] treeTypeNames = new String[]{"normal", "apple", "cocoa", "vanilla", "lemon", "orange", "plum", "coconut", "banana"};
    public static final String[] seedsTitles = new String[]{"[UNUSED]", "Apple Seeds", "Cocoa Seeds", "Vanilla Seeds", "Lemon Seeds", "Orange Seeds", "Plum Seeds", "Coconut Seeds", "Banana Seeds"};
    public static final String[] saplingTitles = new String[]{"[UNUSED]", "Apple Sapling", "Cocoa Sapling", "Vanilla Sapling", "Lemon Sapling", "Orange Sapling", "Plum Sapling", "Coconut Sapling", "Banana Sapling"};

    private static IGuiHandler guiHandler;

    public static BlockFruitCollector blockFruitCollector;
    private static final int SEEDS_WEIGHT = 20;
    public static ArrayList<ItemStack> seedsList = new ArrayList<ItemStack>();
    public static int leavesRenderID;

    public static final String LEMON = "fruitLemon";
    public static final String ORANGE = "fruitOrange";
    private static final String FRUIT = "jaffasFruit";

    public static FruitType getActualLeavesType(Block block, int blockMetadata) {
        BlockFruitLeaves b = (BlockFruitLeaves) block;
        return getActualLeavesType(b.serialNumber, blockMetadata);
    }

    public static FruitType getActualLeavesType(int serialNumber, int blockMetadata) {
        int index = serialNumber * 4 + blockMetadata;
        FruitType fruitType = FruitType.indexToFruitType(index);
        if (fruitType == null) {
            throw new RuntimeException("fruit not found!");
        }

        return fruitType;
    }

    @Mod.Instance("Jaffas-Trees")
    public static JaffasTrees instance;

    public static ArrayList<LeavesInfo> leavesList = new ArrayList<LeavesInfo>();

    public static final int leavesBlocksAllocated = 4;
    public static final int leavesTypesCount = 8;

    public static boolean debug;

    public static ItemJaffaBase itemLemon;
    public static ItemJaffaBase itemOrange;
    public static ItemJaffaBase itemPlum;
    public static ItemJaffaBase itemCoconut;
    public static ItemJaffaBase itemBanana;

    public static ItemJaffaTreeDebugTool itemDebug;

    public static ItemTrees itemStick;
    public static ItemTrees itemRod;
    public static ItemTrees itemFruitPicker;
    public static ItemTrees itemFruitPickerHead;

    public static ItemTrees itemUnknownSeeds;

    public static ItemBagPlanting itemPlantingBagSmall;
    public static ItemBagPlanting itemPlantingBagMedium;
    public static ItemBagPlanting itemPlantingBagBig;
    public static ItemBagCollecting itemCollectingBag;

    public static BlockFruitLeavesDummy dummyLeaves;


    public JaffasTrees() {
        instance = this;
    }

    @SidedProxy(clientSide = "monnef.jaffas.trees.client.ClientProxy", serverSide = "monnef.jaffas.trees.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);
        BushManager.populateBushInfo();

        try {
            config.load();

            dummyLeaves = new BlockFruitLeavesDummy();

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);
            bonemealingAllowed = config.get(Configuration.CATEGORY_GENERAL, "bonemeal", true).getBoolean(true);

            for (int i = 0; i < leavesBlocksAllocated; i++) {
                leavesList.add(new LeavesInfo(i));
            }

            ItemBagPlanting.blackList().loadFromString(
                    config.get(Configuration.CATEGORY_GENERAL, "plantingBagBlackList", "", "Planting bag will ignore these items. Format of item (separated by ',' or ';'): <id>[:meta]").getString()
            );
            ItemBagPlanting.blackList().printToLog(Log);
            TileFruitCollector.fruitCollectorRange = config.get(Configuration.CATEGORY_GENERAL, "fruitCollectorRange", 16).getInt();
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "Mod Jaffas (trees) can't read config file.");
        } finally {
            config.save();
        }

        if (!ModuleManager.isModuleEnabled(ModulesEnum.trees))
            return;

        creativeTab = new JaffaCreativeTab("jaffas.trees");
        guiHandler = new GuiHandlerTrees();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

        JaffasRegistryHelper.registerTileEntity(TileFruitLeaves.class, "fruitLeaves");
        JaffasRegistryHelper.registerTileEntity(TileJaffaCrops.class, "jaffaCrops");

        CompostRegister.init();
        createItems();

        MinecraftForge.addGrassSeed(new ItemStack(itemUnknownSeeds), SEEDS_WEIGHT);

        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        registerForestryStuff();

        creativeTab.setup(ItemManager.getItem(JaffaItem.oranges));
        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.trees", "en_US", "Jaffas and more! Trees");
    }

    private ItemJaffaBase constructFruit(EatableType type, int textureOffset, String name) {
        ItemJaffaBase res;
        if (type == NotEatable) {
            res = new ItemJaffaBerry();
        } else if (type == EatableNormal) {
            res = (ItemJaffaBase) (new ItemJaffaBerryEatable()).Setup(2, 0.2f);
        } else {
            throw new RuntimeException("unknown eatable type");
        }

        res.setUnlocalizedName(name);
        RegistryUtils.registerItem(res, name);

        res.setCustomIconIndex(textureOffset);
        return res;
    }

    private void constructItemsInBushInfo() {
        boolean first = true;
        for (EnumMap.Entry<BushType, BushInfo> entry : bushesList().entrySet()) {
            BushInfo info = entry.getValue();

            Item fruit = constructFruit(info.eatable, info.fruitTexture, info.getFruitLanguageName());
            fruit.setCreativeTab(creativeTab);
            info.itemFruit = fruit;
            if (BushType.isFruit(entry.getKey())) {
                registerFruitItem(fruit);
            }

            Item dropFromPlant = info.product == null ? info.itemFruit : info.product;
            BlockJaffaCrops crops = new BlockJaffaCrops(info.plantTexture, info.lastPhase, info.renderer);
            crops.setBlockName(info.getPlantLanguageName());
            RegistryUtils.registerBlockWithName(crops, info.name);
            info.block = crops;
            if (first) {
                first = false;
                MinecraftForge.EVENT_BUS.register(crops);
            }
            if (otherMods.isMineFactoryReloadedDetected()) {
                FactoryRegistry.sendMessage("registerHarvestable", crops);
                FactoryRegistry.sendMessage("registerFertilizable", crops);
            }
            if (ModuleManager.isModuleEnabled(technic)) {
                CompostRegister.addStack(dropFromPlant, DEFAULT_FRUIT_COMPOSTING_VALUE);
            }

            ItemJaffaSeeds seeds = new ItemJaffaSeeds(info.block, Blocks.farmland);
            RegistryUtils.registerItem(seeds, info.getSeedsLanguageName());
            seeds.setCustomIconIndex(info.seedsTexture);
            if (otherMods.isMineFactoryReloadedDetected()) {
                FactoryRegistry.sendMessage("registerPlantable", seeds);
            }

            info.itemSeeds = seeds;
            if (info.drop == DropsFromGrass) {
                seedsList.add(new ItemStack(seeds));
            }

            crops.setSeeds(seeds);
            crops.setProduct(dropFromPlant);
        }
    }

    public static void registerFruitItem(Item fruit) {
        OreDictionary.registerOre(FRUIT, fruit);
    }

    @Override
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        super.load(event);
        JaffasFood.printInitialized(ModulesEnum.trees);
        TileFruitCollector.onLoad();
        FMLInterModComms.sendMessage("Waila", "register", "monnef.jaffas.trees.compatibility.WailaCallback.callbackRegister");
    }

    private void registerForestryStuff() {
        if (!otherMods.isForestryDetected()) return;

        if (otherMods.isForestryDetected()) {
            otherMods.insertFarmable(FORESTRY_FARM_VEGETABLES, new JaffaCropProvider());
        }

        //OtherModsHelper.dumpForestryRegister(FORESTRY_FARM_VEGETABLES);
    }

    private void createItems() {
        addFruitTreesSequence(0, 0, 32, 4);
        addFruitTreesSequence(1, 4, 32 + 4, 4);
        addFruitTreesSequence(2, 8, 32 + 4, 1);

        for (int i = 1; i < JaffasTrees.leavesTypesCount + 1; i++) {
            seedsList.add(getTreeSeeds(i));
        }

        itemLemon = constructFruit(NotEatable, 68, "lemon");
        OreDictionary.registerOre(LEMON, itemLemon);
        registerFruitItem(itemLemon);

        itemOrange = constructFruit(EatableNormal, 69, "orange");
        OreDictionary.registerOre(ORANGE, itemOrange);
        registerFruitItem(itemOrange);

        itemPlum = constructFruit(EatableNormal, 70, "plum");
        registerFruitItem(itemPlum);

        itemCoconut = constructFruit(NotEatable, 71, "coconut");
        registerFruitItem(itemCoconut);

        itemBanana = constructFruit(EatableNormal, 72, "banana");
        registerFruitItem(itemBanana);

        registerFruitItem(Items.apple);

        if (ModuleManager.isModuleEnabled(technic)) {
            CompostRegister.addStack(itemLemon, DEFAULT_FRUIT_COMPOSTING_VALUE);
            CompostRegister.addStack(itemOrange, DEFAULT_FRUIT_COMPOSTING_VALUE);
            CompostRegister.addStack(itemPlum, DEFAULT_FRUIT_COMPOSTING_VALUE);
            CompostRegister.addStack(itemCoconut, DEFAULT_FRUIT_COMPOSTING_VALUE);
            CompostRegister.addStack(itemBanana, DEFAULT_FRUIT_COMPOSTING_VALUE);
        }

        constructItemsInBushInfo();

        blockFruitCollector = new BlockFruitCollector();
        GameRegistry.registerBlock(blockFruitCollector, "blockFruitCollector");
        JaffasRegistryHelper.registerTileEntity(TileFruitCollector.class, "fruitcollector");

        itemDebug = new ItemJaffaTreeDebugTool();
        itemDebug.setMaxStackSize(1).setUnlocalizedName("jaffaTreeDebug");
        RegistryUtils.registerItem(itemDebug);

        itemStick = new ItemTrees();
        itemStick.setUnlocalizedName("stickImpregnated");
        itemStick.setCustomIconIndex(160);
        RegistryUtils.registerItem(itemStick);

        itemRod = new ItemTrees();
        itemRod.setUnlocalizedName("rod").setMaxStackSize(1).setMaxDamage(64);
        itemRod.setCustomIconIndex(161);
        RegistryUtils.registerItem(itemRod);

        itemFruitPickerHead = new ItemTrees();
        itemFruitPickerHead.setUnlocalizedName("fruitPickerHead");
        itemFruitPickerHead.setCustomIconIndex(162);
        RegistryUtils.registerItem(itemFruitPickerHead);

        itemFruitPicker = new ItemTrees();
        itemFruitPicker.setUnlocalizedName("fruitPicker").setMaxStackSize(1).setMaxDamage(256);
        itemFruitPicker.setCustomIconIndex(163);
        RegistryUtils.registerItem(itemFruitPicker);

        itemUnknownSeeds = new ItemTrees();
        itemUnknownSeeds.setCustomIconIndex(34);
        itemUnknownSeeds.setInfo("Magnifier is needed for identification");
        RegistryUtils.registerItem(itemUnknownSeeds, "unknownSeeds");

        itemPlantingBagSmall = new ItemBagPlanting(164, 1);
        RegistryUtils.registerItem(itemPlantingBagSmall, "plantingBagSmall");
        itemPlantingBagMedium = new ItemBagPlanting(165, 2);
        RegistryUtils.registerItem(itemPlantingBagMedium, "plantingBagMedium");
        itemPlantingBagBig = new ItemBagPlanting(166, 3);
        RegistryUtils.registerItem(itemPlantingBagBig, "plantingBagBig");

        itemCollectingBag = new ItemBagCollecting(167);
        RegistryUtils.registerItem(itemCollectingBag, "collectingBag");
    }

    private void addFruitTreesSequence(int i, int leavesTexture, int seedTexture, int subCount) {
        LeavesInfo leaves = leavesList.get(i);
        leaves.leavesBlock = new BlockFruitLeaves(leavesTexture, subCount);
        leaves.leavesBlock.serialNumber = i;
        leaves.leavesBlock.setBlockName("fruitLeaves" + i).setCreativeTab(creativeTab).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundTypeGrass);
        RegistryUtils.registerBlock(leaves.leavesBlock);

        leaves.saplingBlock = new BlockFruitSapling(15, subCount);
        leaves.saplingBlock.serialNumber = i;
        String saplingBlockName = "fruitSapling" + i;
        leaves.saplingBlock.setBlockName(saplingBlockName).setCreativeTab(creativeTab);
        RegistryUtils.registerMultiBlock(leaves.saplingBlock, ItemBlockFruitSapling.class, constructSubNames(treeTypeNames, i, subCount));
        OreDictionary.registerOre("treeSapling", new ItemStack(leaves.saplingBlock, 1, ANY_DMG));
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerFertilizable", leaves.saplingBlock);
            FactoryRegistry.sendMessage("registerPlantable", leaves.saplingBlock);
        }

        leaves.seedsItem = new ItemFruitSeeds(leaves.saplingBlock, seedTexture, subCount);
        RegistryUtils.registerItem(leaves.seedsItem, "treeSeeds" + i);
        leaves.seedsItem.serialNumber = i;
        for (int j = 0; j < subCount; j++) {
            String combinedName = leaves.seedsItem.getUnlocalizedName() + "." + j + ".name";
            LanguageRegistry.instance().addStringLocalization(combinedName, seedsTitles[j + i * 4]);
        }
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerPlantable", leaves.seedsItem);
        }

        // bonemeal event
        MinecraftForge.EVENT_BUS.register(leaves.saplingBlock);
        if (i == 0) {
            leaves.seedsItem.setFirstInSequence();
        }

        if (ModuleManager.isModuleEnabled(technic)) {
            CompostRegister.addStack(leaves.leavesBlock, DEFAULT_BLOCK_COMPOSTING_VALUE);
            CompostRegister.addStack(leaves.saplingBlock, DEFAULT_FRUIT_COMPOSTING_VALUE);
        }
    }

    public static String[] constructSubNames(String[] names, int groupNumber, int subCount) {
        String[] res = new String[subCount];
        System.arraycopy(names, groupNumber * 4, res, 0, subCount);
        return res;
    }

    public static ItemStack getTreeSeeds(int type) {
        if (type == 0 || type >= JaffasTrees.treeTypeNames.length) {
            throw new InvalidParameterException("type = " + type);
        }

        ItemStack seed;
        ItemFruitSeeds item = JaffasTrees.leavesList.get(type / 4).seedsItem;
        int meta = type % 4;

        seed = new ItemStack(item, 1, meta);
        return seed;
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();
        ICommandManager commandManager = server.getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        addCommands(serverCommandManager);
    }

    private void addCommands(ServerCommandManager manager) {
        if (debug) {
            manager.registerCommand(new CommandFruitDebug());
        }
    }

    private Item getJaffaItem(JaffaItem item) {
        return ItemManager.getItem(item);
    }

    private static void addRecipe(IRecipe recipe) {
        CraftingManager.getInstance().getRecipeList().add(recipe);
    }

    private void installRecipes() {
        addRecipe(new ShapelessOreRecipe(new ItemStack(getJaffaItem(JaffaItem.lemons)), LEMON, LEMON, LEMON, LEMON));

        addRecipe(new ShapelessOreRecipe(new ItemStack(getJaffaItem(JaffaItem.oranges)), ORANGE, ORANGE, ORANGE, ORANGE));

        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.plums)),
                new ItemStack(JaffasTrees.itemPlum),
                new ItemStack(JaffasTrees.itemPlum),
                new ItemStack(JaffasTrees.itemPlum),
                new ItemStack(JaffasTrees.itemPlum));

        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.strawberries)),
                new ItemStack(bushesList().get(BushType.Strawberry).itemFruit),
                new ItemStack(bushesList().get(BushType.Strawberry).itemFruit),
                new ItemStack(bushesList().get(BushType.Strawberry).itemFruit),
                new ItemStack(bushesList().get(BushType.Strawberry).itemFruit));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.raspberries)),
                new ItemStack(bushesList().get(BushType.Raspberry).itemFruit),
                new ItemStack(bushesList().get(BushType.Raspberry).itemFruit),
                new ItemStack(bushesList().get(BushType.Raspberry).itemFruit),
                new ItemStack(bushesList().get(BushType.Raspberry).itemFruit));

        GameRegistry.addSmelting(bushesList().get(BushType.Coffee).itemFruit, new ItemStack(getJaffaItem(JaffaItem.coffeeRoasted)), 0.5F);

        RecipesBoard.addRecipe(getFruitStack(BushType.Paprika), new ItemStack(getJaffaItem(JaffaItem.paprikaChopped)));
        RecipesBoard.addRecipe(getFruitStack(BushType.Tomato), new ItemStack(getJaffaItem(JaffaItem.tomatoChopped)));


        if (!ModuleManager.isModuleEnabled(technic)) {
            GameRegistry.addRecipe(new ItemStack(blockFruitCollector), "IDI", "DRD", "IGI",
                    'I', new ItemStack(Blocks.iron_block), 'D', new ItemStack(Items.diamond), 'R', new ItemStack(Blocks.redstone_torch), 'G', new ItemStack(Blocks.gold_block));
        }

        installTreeSeedsRecipes();
        BushManager.installSeedRecipes();

        Recipes.addShapelessOreRecipe(new ItemStack(itemStick, 4), Recipes.WOOD_STICK, Recipes.WOOD_STICK, Recipes.WOOD_STICK, Recipes.WOOD_STICK, Items.slime_ball);
        GameRegistry.addRecipe(new ItemStack(itemRod), " S ", "ISI", " S ", 'S', itemStick, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(itemFruitPickerHead), "III", "WWW", " W ", 'I', Items.iron_ingot, 'W', new ItemStack(Blocks.wool, 1, ANY_DMG));
        GameRegistry.addRecipe(new ItemStack(itemFruitPicker), "H ", " R", 'H', new ItemStack(itemFruitPickerHead), 'R', new ItemStack(itemRod));

        Recipes.addMalletShapedRecipe(new ItemStack(getJaffaItem(JaffaItem.coconutPowder)), new ItemStack(itemCoconut));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.browniesPastry)), getFruitStack(BushType.Peanuts),
                new ItemStack(getItem(JaffaItem.pastrySweet)), new ItemStack(getItem(JaffaItem.chocolate)));

        RecipesBoard.addRecipe(JaffasTrees.getFruitStack(BushType.Onion), new ItemStack(getItem(JaffaItem.onionSliced)));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.bottleKetchup)), Items.sugar, getJaffaItem(JaffaItem.bottleEmpty), getFruitStack(BushType.Tomato), getFruitStack(BushType.Tomato));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.bottleMustard)), getItem(JaffaItem.bottleEmpty), getFruit(BushType.Mustard), getFruit(BushType.Mustard));

        addPieRecipe(getFruit(BushType.Strawberry), pieStrawberryRaw, TilePie.PieType.STRAWBERRY, true, getItem(jamStrawberry));
        addPieRecipe(getFruit(BushType.Raspberry), pieRaspberryRaw, TilePie.PieType.RASPBERRY, true, getItem(jamRaspberry));
        addPieRecipe(null, pieVanillaRaw, TilePie.PieType.VANILLA, true, getItem(jamV));
        addPieRecipe(itemPlum, piePlumRaw, TilePie.PieType.PLUM, true, getItem(jamP));

        // new juice recipes
        Juices$.MODULE$.addRecipes();

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(peanutsSugar)), Items.sugar, getFruit(BushType.Peanuts));
        GameRegistry.addRecipe(new ItemStack(getItem(pepperStuffedRaw)), "M", "P", 'M', getItem(mincedMeat), 'P', getFruit(BushType.Paprika));

        // raw mutton
        // pea         -> raw lamb with peas => lamb with peas (in tin) | + plate -> lamb with peas (plate) + tin
        // tin
        GameRegistry.addRecipe(getItemStack(lambWithPeasInTinRaw), " M ", "PPP", " T ", 'M', getItem(muttonRaw), 'P', getFruit(BushType.Pea), 'T', getItem(cakeTin));
        GameRegistry.addSmelting(getItem(lambWithPeasInTinRaw), getItemStack(lambWithPeasInTin), 5f);
        GameRegistry.addShapelessRecipe(getItemStack(lambWithPeas, 3), getItemStack(lambWithPeasInTin), getItem(plate), getItem(plate), getItem(plate));
        PersistentItemsCraftingHandler.AddPersistentItemRetuningNonJaffaItem(lambWithPeasInTin, false, getItem(cakeTin));

        // beans
        // chopped tomatoes   ->  raw beans with tomato sauce => baked beans with tomato sauce
        // dish
        GameRegistry.addRecipe(getItemStack(beansWithTomatoRaw), "B", "T", "D", 'B', getFruit(BushType.Bean), 'T', getItem(tomatoChopped), 'D', getItem(woodenBowl));
        GameRegistry.addSmelting(getItem(beansWithTomatoRaw), getItemStack(beansWithTomato), 3f);

        addRecipe(new ShapedOreRecipe(getItemStack(tinDuckOrangeRaw), "OSO", "ODO", " T ", 'D', getItem(duckRaw), 'O', ORANGE, 'S', Items.sugar, 'T', getItem(cakeTin)));

        GameRegistry.addSmelting(getItem(tinDuckOrangeRaw), getItemStack(tinDuckOrange), 5f);
        GameRegistry.addShapelessRecipe(getItemStack(plateDuckOrange, 3), getItemStack(tinDuckOrange), getItem(plate), getItem(plate), getItem(plate));
        PersistentItemsCraftingHandler.AddPersistentItemRetuningNonJaffaItem(tinDuckOrange, false, getItem(cakeTin));

        GameRegistry.addShapelessRecipe(getItemStack(bananaInChocolate, 2), itemBanana, getItem(chocolate), itemBanana);

        addRecipe(new ShapedOreRecipe(getItemStack(fruitSalad), "FFF", " B ", 'B', getItem(woodenBowl), 'F', FRUIT));
        addRecipe(new ShapedOreRecipe(getItemStack(fruitSalad, 3), "FFF", "FFF", "BBB", 'B', getItem(woodenBowl), 'F', FRUIT));

        Recipes.addSoupRecipes(getFruit(BushType.Pea), soupPeaRaw, soupPeaCooked, false);
        Recipes.addSoupRecipes(getFruit(BushType.Tomato), soupTomatoRaw, soupTomatoCooked, false);

        Recipes.addOreRecipe(new ItemStack(ContentHolder.blockCoconutLamp, 2), "S", "C", 'S', Recipes.STRING, 'C', itemCoconut);
    }

    public static Item getFruit(BushType type) {
        return bushesList().get(type).itemFruit;
    }

    public static ItemStack getFruitStack(BushType type) {
        return getFruitStack(type, 1);
    }

    public static ItemStack getFruitStack(BushType type, int count) {
        return new ItemStack(getFruit(type), count);
    }

    private void installTreeSeedsRecipes() {
        for (int i = 1; i <= leavesTypesCount; i++) {
            int type = i;
            ItemFruitSeeds item = JaffasTrees.leavesList.get(type / 4).seedsItem;
            int meta = type % 4;

            ItemStack seed = new ItemStack(item, 1, meta);

            ItemFromFruitResult info = TileFruitLeaves.getItemFromFruit(FruitType.indexToFruitType(i));

            if (info.exception != null) {
                throw (RuntimeException) info.exception;
            }

            if (info.getMessage() != null) {
                System.err.println(info.getMessage());
                throw new RuntimeException("unknown error during fruits->seeds recipe construction");
            }

            ItemStack fruit = info.getStack();

            if (fruit == null) {
                throw new RuntimeException("null in fruit!");
            }

            GameRegistry.addShapelessRecipe(seed, fruit.copy());
        }
    }
}
