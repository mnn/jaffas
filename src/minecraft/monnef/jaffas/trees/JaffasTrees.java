/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import forestry.api.cultivation.CropProviders;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.TileEntityPie;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.JaffaCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.jaffasMod;
import monnef.jaffas.trees.block.BlockFruitCollector;
import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.block.BlockFruitSapling;
import monnef.jaffas.trees.block.BlockJaffaCrops;
import monnef.jaffas.trees.block.TileEntityFruitCollector;
import monnef.jaffas.trees.block.TileEntityFruitLeaves;
import monnef.jaffas.trees.block.TileEntityJaffaCrops;
import monnef.jaffas.trees.client.GuiHandlerTrees;
import monnef.jaffas.trees.client.JaffaCreativeTab;
import monnef.jaffas.trees.client.PacketHandler;
import monnef.jaffas.trees.common.BushInfo;
import monnef.jaffas.trees.common.CommonProxy;
import monnef.jaffas.trees.common.DropType;
import monnef.jaffas.trees.common.EatableType;
import monnef.jaffas.trees.common.ItemFromFruitResult;
import monnef.jaffas.trees.common.JaffaCropProvider;
import monnef.jaffas.trees.common.LeavesInfo;
import monnef.jaffas.trees.item.ItemFruitSeeds;
import monnef.jaffas.trees.item.ItemJaffaBerry;
import monnef.jaffas.trees.item.ItemJaffaBerryEatable;
import monnef.jaffas.trees.item.ItemJaffaSeeds;
import monnef.jaffas.trees.item.ItemJaffaTreeDebugTool;
import monnef.jaffas.trees.item.ItemTrees;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.logging.Level;

import static monnef.jaffas.food.JaffasFood.getItem;
import static monnef.jaffas.food.crafting.Recipes.AddPieRecipe;
import static monnef.jaffas.food.crafting.Recipes.getItemStack;
import static monnef.jaffas.food.item.JaffaItem.beansWithTomato;
import static monnef.jaffas.food.item.JaffaItem.beansWithTomatoRaw;
import static monnef.jaffas.food.item.JaffaItem.cakeTin;
import static monnef.jaffas.food.item.JaffaItem.duckRaw;
import static monnef.jaffas.food.item.JaffaItem.jamP;
import static monnef.jaffas.food.item.JaffaItem.jamRaspberry;
import static monnef.jaffas.food.item.JaffaItem.jamStrawberry;
import static monnef.jaffas.food.item.JaffaItem.jamV;
import static monnef.jaffas.food.item.JaffaItem.juiceApple;
import static monnef.jaffas.food.item.JaffaItem.juiceBottle;
import static monnef.jaffas.food.item.JaffaItem.juiceLemon;
import static monnef.jaffas.food.item.JaffaItem.juiceOrange;
import static monnef.jaffas.food.item.JaffaItem.juiceRaspberry;
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
import static monnef.jaffas.food.item.JaffaItem.tinDuckOrange;
import static monnef.jaffas.food.item.JaffaItem.tinDuckOrangeRaw;
import static monnef.jaffas.food.item.JaffaItem.tomatoChopped;
import static monnef.jaffas.food.item.JaffaItem.woodenBowl;
import static monnef.jaffas.trees.common.DropType.DropsFromGrass;
import static monnef.jaffas.trees.common.EatableType.EatableNormal;
import static monnef.jaffas.trees.common.EatableType.NotEatable;
import static monnef.jaffas.trees.common.Reference.ModId;
import static monnef.jaffas.trees.common.Reference.ModName;
import static monnef.jaffas.trees.common.Reference.Version;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = JaffasTrees.channel, packetHandler = PacketHandler.class)
public class JaffasTrees extends jaffasMod {
    private static MinecraftServer server;
    public static boolean bonemealingAllowed;

    public static JaffaCreativeTab CreativeTab;

    public static final String channel = "jaffas-02";

    public static final String[] treeTypes = new String[]{"normal", "apple", "cocoa", "vanilla", "lemon", "orange", "plum", "coconut"};
    public static final String[] seedsNames = new String[]{"[UNUSED]", "Apple Seeds", "Cocoa Seeds", "Vanilla Seeds", "Lemon Seeds", "Orange Seeds", "Plum Seeds", "Coconut Seeds"};

    private static IGuiHandler guiHandler;

    public static BlockFruitCollector blockFruitCollector;
    public static int blockFruitCollectorID;
    private static final int SEEDS_WEIGHT = 20;
    public static ArrayList<ItemStack> seedsList = new ArrayList<ItemStack>();

    public static fruitType getActualLeavesType(Block block, int blockMetadata) {
        BlockFruitLeaves b = (BlockFruitLeaves) block;
        return getActualLeavesType(b.serialNumber, blockMetadata);
    }

    public static fruitType getActualLeavesType(int serialNumber, int blockMetadata) {
        int index = serialNumber * 4 + blockMetadata;
        fruitType fruitType = JaffasTrees.fruitType.indexToFruitType(index);
        if (fruitType == null) {
            throw new RuntimeException("fruit not found!");
        }

        return fruitType;
    }

    public static enum fruitType {
        Normal(0), Apple(1), Cocoa(2), Vanilla(3), Lemon(4), Orange(5), Plum(6), Coconut(7);
        private int value;
        private int blockNumber;
        private int metaNumber;

        fruitType(int value) {
            this.value = value;
            this.blockNumber = value % 4;
            this.metaNumber = value / 4;

            mapper.indexToFruitMap.put(value, this);
        }

        public int getValue() {
            return value;
        }

        public int getBlockNumber() {
            return blockNumber;
        }

        public static fruitType indexToFruitType(int index) {
            return mapper.indexToFruitMap.get(index);
        }

        private static class mapper {
            private static HashMap<Integer, fruitType> indexToFruitMap;

            static {
                indexToFruitMap = new HashMap<Integer, fruitType>();
            }
        }
    }

    @Mod.Instance("Jaffas-Trees")
    public static JaffasTrees instance;

    public static ArrayList<LeavesInfo> leavesList = new ArrayList<LeavesInfo>();

    public static final int leavesBlocksAllocated = 3;
    public static final int leavesTypesCount = 7;

    public static boolean debug;

    private int itemLemonID;
    private int itemOrangeID;
    private int itemPlumID;
    private int itemCoconutID;
    public static ItemJaffaBase itemLemon;
    public static ItemJaffaBase itemOrange;
    public static ItemJaffaBase itemPlum;
    public static ItemJaffaBase itemCoconut;

    private int itemDebugID;
    public static ItemJaffaTreeDebugTool itemDebug;

    private int itemStickID;
    public static ItemTrees itemStick;
    private int itemRodID;
    public static ItemTrees itemRod;
    private int itemFruitPickerID;
    public static ItemTrees itemFruitPicker;
    private int itemFruitPickerHeadID;
    public static ItemTrees itemFruitPickerHead;

    private int itemUnknownSeedsID;
    public static ItemTrees itemUnknownSeeds;

    public static enum bushType {
        Coffee, Strawberry, Onion, Paprika, Raspberry, Tomato, Mustard, Peanuts, Pea, Bean
    }

    public static EnumMap<bushType, BushInfo> BushesList = new EnumMap<bushType, BushInfo>(bushType.class);

    public final static String textureFile = "/jaffas_02.png";

    public JaffasTrees() {
        instance = this;
    }

    private static IDProvider idProvider = new IDProvider(3500, 25244);

    @SidedProxy(clientSide = "monnef.jaffas.trees.client.ClientProxy", serverSide = "monnef.jaffas.trees.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        PopulateBushInfo();

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.linkWithConfig(config);

            itemLemonID = idProvider.getItemIDFromConfig("lemon");
            itemOrangeID = idProvider.getItemIDFromConfig("orange");
            itemPlumID = idProvider.getItemIDFromConfig("plum");
            itemCoconutID = idProvider.getItemIDFromConfig("coconut");

            blockFruitCollectorID = idProvider.getBlockIDFromConfig("fruit collector");

            for (int i = 0; i < leavesBlocksAllocated; i++) {
                int leavesID = idProvider.getBlockIDFromConfig("fruit leaves " + i);
                int saplingID = idProvider.getBlockIDFromConfig("fruit tree sapling " + i);
                int seedsID = idProvider.getItemIDFromConfig("fruit seeds " + i);

                leavesList.add(new LeavesInfo(leavesID, saplingID, seedsID, i));
            }

            for (EnumMap.Entry<bushType, BushInfo> entry : BushesList.entrySet()) {
                BushInfo info = entry.getValue();

                info.itemSeedsID = idProvider.getItemIDFromConfig(info.getSeedsConfigName());
                info.blockID = idProvider.getBlockIDFromConfig(info.getBlockConfigName());
                info.itemFruitID = idProvider.getItemIDFromConfig(info.getFruitConfigName());
            }

            itemDebugID = idProvider.getItemIDFromConfig("debug tool");

            itemStickID = idProvider.getItemIDFromConfig("stick");
            itemRodID = idProvider.getItemIDFromConfig("rod");
            itemFruitPickerID = idProvider.getItemIDFromConfig("fruit picker");
            itemFruitPickerHeadID = idProvider.getItemIDFromConfig("fruit picker head");
            itemUnknownSeedsID = idProvider.getItemIDFromConfig("unknownSeeds");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);
            bonemealingAllowed = config.get(Configuration.CATEGORY_GENERAL, "bonemeal", false).getBoolean(false);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (trees) can't read config file.");
        } finally {
            config.save();
        }
    }

    private void PopulateBushInfo() {
        AddBushInfo(bushType.Coffee, "coffee", "Coffee Seeds", 34, "Coffee Plant", 96, "Coffee Beans", 128, null, 2, 1, NotEatable, DropsFromGrass);
        AddBushInfo(bushType.Strawberry, "strawberry", "Strawberry Seeds", 34, "Strawberry Plant", 99, "Strawberry", 129, null, 2, 1, EatableNormal, DropsFromGrass);

        AddBushInfo(bushType.Onion, "onion", "Onion Seeds", 34, "Onion Plant", 102, "Onion", 130, null, 2, 1, NotEatable, DropsFromGrass);
        AddBushInfo(bushType.Paprika, "paprika", "Pepper Seeds", 34, "Pepper Plant", 105, "Pepper", 131, null, 2, 1, EatableNormal, DropsFromGrass);
        AddBushInfo(bushType.Raspberry, "raspberry", "Raspberry Seeds", 34, "Raspberry Plant", 108, "Raspberry", 132, null, 2, 1, EatableNormal, DropsFromGrass);
        AddBushInfo(bushType.Tomato, "tomato", "Tomato Seeds", 34, "Tomato Plant", 111, "Tomato", 133, null, 2, 1, EatableNormal, DropsFromGrass);

        AddBushInfo(bushType.Mustard, "mustard", "Little Mustard Seeds", 34, "Mustard Plant", 114, "Mustard", 134, null, 2, 1, NotEatable, DropsFromGrass);
        AddBushInfo(bushType.Peanuts, "peanuts", "Little Peanuts", 34, "Peanuts Plant", 117, "Peanuts", 135, null, 2, 1, EatableNormal, DropsFromGrass);

        AddBushInfo(bushType.Pea, "pea", "Little Peas", 34, "Pea Plant", 120, "Pea Pod", 136, null, 2, 1, EatableNormal, DropsFromGrass);
        AddBushInfo(bushType.Bean, "bean", "Little Beans", 34, "Bean Plant", 123, "Beans", 137, null, 2, 1, NotEatable, DropsFromGrass);
    }

    private ItemJaffaBase constructFruit(int id, EatableType type, int textureOffset, String name, String title) {
        ItemJaffaBase res;
        if (type == NotEatable) {
            res = new ItemJaffaBerry(id);
        } else if (type == EatableNormal) {
            res = (ItemJaffaBase) (new ItemJaffaBerryEatable(id)).Setup(2, 0.2f);
        } else {
            throw new RuntimeException("unknown eatable type");
        }

        res.setUnlocalizedName(name);
        res.setCustomIconIndex(textureOffset);
        LanguageRegistry.addName(res, title);
        return res;
    }

    private void constructItemsInBushInfo() {
        boolean first = true;
        for (EnumMap.Entry<bushType, BushInfo> entry : BushesList.entrySet()) {
            BushInfo info = entry.getValue();

            ItemJaffaSeeds seeds = new ItemJaffaSeeds(info.itemSeedsID, info.blockID, Block.tilledField.blockID);
            RegistryUtils.registerItem(seeds, info.getSeedsLanguageName(), info.seedsTitle);
            seeds.setCustomIconIndex(info.seedsTexture);

            info.itemSeeds = seeds;
            if (info.drop == DropsFromGrass) {
                seedsList.add(new ItemStack(seeds));
            }

            Item fruit = constructFruit(info.itemFruitID, info.eatable, info.fruitTexture, info.getFruitLanguageName(), info.fruitTitle);
            fruit.setCreativeTab(CreativeTab);
            info.itemFruit = fruit;

            BlockJaffaCrops crops = new BlockJaffaCrops(info.blockID, info.plantTexture, info.phases, info.product == null ? info.itemFruit : info.product, info.itemSeeds, info.renderer);
            crops.setUnlocalizedName(info.getPlantLanguageName());
            GameRegistry.registerBlock(crops, info.name);
            LanguageRegistry.addName(crops, info.plantTitle);
            info.block = crops;
            if (first) {
                first = false;
                MinecraftForge.EVENT_BUS.register(crops);
            }
        }
    }

    private void AddBushInfo(bushType type, String name, String seedsTitle, int seedsTexture, String plantTitle, int plantTexture, String fruitTitle, int fruitTexture, Item product, int phases, int renderer, EatableType eatable, DropType drop) {
        BushInfo info = new BushInfo();

        info.name = name;
        info.seedsTitle = seedsTitle;
        info.seedsTexture = seedsTexture;
        info.plantTitle = plantTitle;
        info.plantTexture = plantTexture;
        info.fruitTitle = fruitTitle;
        info.fruitTexture = fruitTexture;
        info.product = product;
        info.phases = phases;
        info.renderer = renderer;
        info.type = type;
        info.eatable = eatable;
        info.drop = drop;

        BushesList.put(type, info);
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.trees))
            return;

        CreativeTab = new JaffaCreativeTab("jaffas.trees");
        guiHandler = new GuiHandlerTrees();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);

        AddFruitTreesSequence(0, 0, 32, 4);
        AddFruitTreesSequence(1, 4, 32 + 4, 4);

        for (int i = 1; i < JaffasTrees.leavesTypesCount + 1; i++) {
            seedsList.add(getTreeSeeds(i));
        }

        GameRegistry.registerTileEntity(TileEntityFruitLeaves.class, "fruitLeaves");
        GameRegistry.registerTileEntity(TileEntityJaffaCrops.class, "jaffaCrops");

        itemLemon = constructFruit(itemLemonID, NotEatable, 68, "lemon", "Lemon");

        itemOrange = constructFruit(itemOrangeID, EatableNormal, 69, "orange", "Orange");

        itemPlum = constructFruit(itemPlumID, EatableNormal, 70, "plum", "Plum");

        itemCoconut = constructFruit(itemCoconutID, NotEatable, 71, "coconut", "Coconut");

        constructItemsInBushInfo();

        blockFruitCollector = new BlockFruitCollector(blockFruitCollectorID);
        GameRegistry.registerBlock(blockFruitCollector, "blockFruitCollector");
        LanguageRegistry.addName(blockFruitCollector, "Fruit Collector");
        GameRegistry.registerTileEntity(TileEntityFruitCollector.class, "fruitcollector");

        itemDebug = new ItemJaffaTreeDebugTool(itemDebugID);
        itemDebug.setMaxStackSize(1).setUnlocalizedName("jaffaTreeDebug");
        LanguageRegistry.addName(itemDebug, "Jaffa Tree's Debug Tool");

        itemStick = new ItemTrees(itemStickID);
        itemStick.setUnlocalizedName("stickImpregnated");
        itemStick.setCustomIconIndex(160);
        LanguageRegistry.addName(itemStick, "Impregnated Stick");

        itemRod = new ItemTrees(itemRodID);
        itemRod.setUnlocalizedName("rod").setMaxStackSize(1).setMaxDamage(64);
        itemRod.setCustomIconIndex(161);
        LanguageRegistry.addName(itemRod, "Reinforced Rod");

        itemFruitPickerHead = new ItemTrees(itemFruitPickerHeadID);
        itemFruitPickerHead.setUnlocalizedName("fruitPickerHead");
        itemFruitPickerHead.setCustomIconIndex(162);
        LanguageRegistry.addName(itemFruitPickerHead, "Head of Fruit Picker");

        itemFruitPicker = new ItemTrees(itemFruitPickerID);
        itemFruitPicker.setUnlocalizedName("fruitPicker").setMaxStackSize(1).setMaxDamage(256);
        itemFruitPicker.setCustomIconIndex(163);
        LanguageRegistry.addName(itemFruitPicker, "Fruit Picker");

        itemUnknownSeeds = new ItemTrees(itemUnknownSeedsID);
        itemUnknownSeeds.setCustomIconIndex(34);
        RegistryUtils.registerItem(itemUnknownSeeds, "unknownSeeds", "Unknown Seeds");

        MinecraftForge.addGrassSeed(new ItemStack(itemUnknownSeeds), SEEDS_WEIGHT);

        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        //GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());

        //forestry stuff
        CropProviders.cerealCrops.add(new JaffaCropProvider());

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.trees", "en_US", "Jaffas and more! Trees");

        JaffasFood.PrintInitialized(ModulesEnum.trees);
    }

    private void AddFruitTreesSequence(int i, int leavesTexture, int seedTexture, int subCount) {
        LeavesInfo leaves = leavesList.get(i);
        leaves.leavesBlock = new BlockFruitLeaves(leaves.leavesID, leavesTexture, subCount);
        leaves.leavesBlock.serialNumber = i;
        leaves.leavesBlock.setUnlocalizedName("fruitLeaves" + i).setCreativeTab(CreativeTab).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep);
        RegistryUtils.registerBlock(leaves.leavesBlock);
        LanguageRegistry.addName(leaves.leavesBlock, "Leaves");

        leaves.saplingBlock = new BlockFruitSapling(leaves.saplingID, 15);
        leaves.saplingBlock.serialNumber = i;
        String saplingBlockName = "fruitSapling" + i;
        leaves.saplingBlock.setUnlocalizedName(saplingBlockName).setCreativeTab(CreativeTab);
        GameRegistry.registerBlock(leaves.saplingBlock, saplingBlockName);
        LanguageRegistry.addName(leaves.saplingBlock, "Fruit Sapling");

        leaves.seedsItem = new ItemFruitSeeds(leaves.seedsID, leaves.saplingID, seedTexture, subCount);
        leaves.seedsItem.serialNumber = i;
        for (int j = 0; j < subCount; j++) {
            String combinedName = leaves.seedsItem.getUnlocalizedName() + "." + j + ".name";
            LanguageRegistry.instance().addStringLocalization(combinedName, seedsNames[j + i * 4]);
        }

        if (i == 0) {
            leaves.seedsItem.setFirstInSequence();
            // bonemeal event
            MinecraftForge.EVENT_BUS.register(leaves.saplingBlock);
        }
    }

    public static ItemStack getTreeSeeds(int type) {
        if (type == 0 || type >= JaffasTrees.treeTypes.length) {
            throw new InvalidParameterException("type = " + type);
        }

        ItemStack seed;
        ItemFruitSeeds item = JaffasTrees.leavesList.get(type / 4).seedsItem;
        int meta = type % 4;

        seed = new ItemStack(item, 1, meta);
        return seed;
    }

    @Mod.ServerStarting
    public void serverStarting(FMLServerStartingEvent event) {
        server = ModLoader.getMinecraftServerInstance();
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

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.lemons)),
                new ItemStack(JaffasTrees.itemLemon),
                new ItemStack(JaffasTrees.itemLemon),
                new ItemStack(JaffasTrees.itemLemon),
                new ItemStack(JaffasTrees.itemLemon));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.oranges)),
                new ItemStack(JaffasTrees.itemOrange),
                new ItemStack(JaffasTrees.itemOrange),
                new ItemStack(JaffasTrees.itemOrange),
                new ItemStack(JaffasTrees.itemOrange));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.plums)),
                new ItemStack(JaffasTrees.itemPlum),
                new ItemStack(JaffasTrees.itemPlum),
                new ItemStack(JaffasTrees.itemPlum),
                new ItemStack(JaffasTrees.itemPlum));

        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.strawberries)),
                new ItemStack(BushesList.get(bushType.Strawberry).itemFruit),
                new ItemStack(BushesList.get(bushType.Strawberry).itemFruit),
                new ItemStack(BushesList.get(bushType.Strawberry).itemFruit),
                new ItemStack(BushesList.get(bushType.Strawberry).itemFruit));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.raspberries)),
                new ItemStack(BushesList.get(bushType.Raspberry).itemFruit),
                new ItemStack(BushesList.get(bushType.Raspberry).itemFruit),
                new ItemStack(BushesList.get(bushType.Raspberry).itemFruit),
                new ItemStack(BushesList.get(bushType.Raspberry).itemFruit));

        GameRegistry.addSmelting(BushesList.get(bushType.Coffee).itemFruit.itemID, new ItemStack(getJaffaItem(JaffaItem.coffeeRoasted)), 0.5F);

        RecipesBoard.addRecipe(getFruitStack(bushType.Paprika), new ItemStack(getJaffaItem(JaffaItem.paprikaChopped)));
        RecipesBoard.addRecipe(getFruitStack(bushType.Tomato), new ItemStack(getJaffaItem(JaffaItem.tomatoChopped)));


        if (!ModuleManager.IsModuleEnabled(ModulesEnum.technic)) {
            GameRegistry.addRecipe(new ItemStack(blockFruitCollector), "IDI", "DRD", "IGI",
                    'I', new ItemStack(Block.blockSteel), 'D', new ItemStack(Item.diamond), 'R', new ItemStack(Block.torchRedstoneActive), 'G', new ItemStack(Block.blockGold));
        }

        installFruitSeedsRecipes();

        GameRegistry.addShapelessRecipe(new ItemStack(itemStick, 4), new ItemStack(Item.stick), new ItemStack(Item.stick), new ItemStack(Item.stick), new ItemStack(Item.stick), new ItemStack(Item.slimeBall));
        GameRegistry.addRecipe(new ItemStack(itemRod), " S ", "ISI", " S ", 'S', new ItemStack(itemStick), 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addRecipe(new ItemStack(itemFruitPickerHead), "III", "WWW", " W ", 'I', new ItemStack(Item.ingotIron), 'W', new ItemStack(Block.cloth, 1, WILDCARD_VALUE));
        GameRegistry.addRecipe(new ItemStack(itemFruitPicker), "H ", " R", 'H', new ItemStack(itemFruitPickerHead), 'R', new ItemStack(itemRod));

        Recipes.AddMalletShapedRecipe(new ItemStack(getJaffaItem(JaffaItem.coconutPowder)), new ItemStack(itemCoconut));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.browniesPastry)), getFruitStack(bushType.Peanuts),
                new ItemStack(getItem(JaffaItem.pastrySweet)), new ItemStack(getItem(JaffaItem.chocolate)));

        RecipesBoard.addRecipe(JaffasTrees.getFruitStack(JaffasTrees.bushType.Onion), new ItemStack(getItem(JaffaItem.onionSliced)));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.bottleKetchup)), Item.sugar, getJaffaItem(JaffaItem.bottleEmpty), getFruitStack(bushType.Tomato), getFruitStack(bushType.Tomato));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.bottleMustard)), getItem(JaffaItem.bottleEmpty), getFruit(bushType.Mustard), getFruit(bushType.Mustard));

        AddPieRecipe(getFruit(bushType.Strawberry), pieStrawberryRaw, TileEntityPie.PieType.STRAWBERRY, true, getItem(jamStrawberry));
        AddPieRecipe(getFruit(bushType.Raspberry), pieRaspberryRaw, TileEntityPie.PieType.RASPBERRY, true, getItem(jamRaspberry));
        AddPieRecipe(null, pieVanillaRaw, TileEntityPie.PieType.VANILLA, true, getItem(jamV));
        AddPieRecipe(itemPlum, piePlumRaw, TileEntityPie.PieType.PLUM, true, getItem(jamP));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(juiceOrange)), getItem(juiceBottle), itemOrange, itemOrange, itemOrange, itemOrange);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(juiceLemon)), getItem(juiceBottle), itemLemon, itemLemon, itemLemon, Item.sugar);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(juiceApple)), getItem(juiceBottle), Item.appleRed, Item.appleRed, Item.appleRed, Item.appleRed);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(juiceRaspberry)), getItem(juiceBottle), getFruit(bushType.Raspberry), getFruit(bushType.Raspberry), getFruit(bushType.Raspberry));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(peanutsSugar)), Item.sugar, getFruit(bushType.Peanuts));
        GameRegistry.addRecipe(new ItemStack(getItem(pepperStuffedRaw)), "M", "P", 'M', getItem(mincedMeat), 'P', getFruit(bushType.Paprika));

        // raw mutton
        // pea         -> raw lamb with peas => lamb with peas (in tin) | + plate -> lamb with peas (plate) + tin
        // tin
        GameRegistry.addRecipe(getItemStack(lambWithPeasInTinRaw), " M ", "PPP", " T ", 'M', getItem(muttonRaw), 'P', getFruit(bushType.Pea), 'T', getItem(cakeTin));
        GameRegistry.addSmelting(getItem(lambWithPeasInTinRaw).itemID, getItemStack(lambWithPeasInTin), 5f);
        GameRegistry.addShapelessRecipe(getItemStack(lambWithPeas, 3), getItemStack(lambWithPeasInTin), getItem(plate), getItem(plate), getItem(plate));
        JaffaCraftingHandler.AddPersistentItem(lambWithPeasInTin, false, getItem(cakeTin).itemID);

        // beans
        // chopped tomatoes   ->  raw beans with tomato sauce => baked beans with tomato sauce
        // dish
        GameRegistry.addRecipe(getItemStack(beansWithTomatoRaw), "B", "T", "D", 'B', getFruit(bushType.Bean), 'T', getItem(tomatoChopped), 'D', getItem(woodenBowl));
        GameRegistry.addSmelting(getItem(beansWithTomatoRaw).itemID, getItemStack(beansWithTomato), 3f);

        GameRegistry.addRecipe(getItemStack(tinDuckOrangeRaw), "OSO", "ODO", " T ", 'D', getItem(duckRaw), 'O', itemOrange, 'S', Item.sugar, 'T', getItem(cakeTin));
        GameRegistry.addSmelting(getItem(tinDuckOrangeRaw).itemID, getItemStack(tinDuckOrange), 5f);
        GameRegistry.addShapelessRecipe(getItemStack(plateDuckOrange, 3), getItemStack(tinDuckOrange), getItem(plate), getItem(plate), getItem(plate));
        JaffaCraftingHandler.AddPersistentItem(tinDuckOrange, false, getItem(cakeTin).itemID);
    }

    public static Item getFruit(bushType type) {
        return BushesList.get(type).itemFruit;
    }

    public static ItemStack getFruitStack(bushType type) {
        return getFruitStack(type, 1);
    }

    public static ItemStack getFruitStack(bushType type, int count) {
        return new ItemStack(getFruit(type), count);
    }

    private void installFruitSeedsRecipes() {
        for (int i = 1; i <= leavesTypesCount; i++) {
            int type = i;
            ItemFruitSeeds item = JaffasTrees.leavesList.get(type / 4).seedsItem;
            int meta = type % 4;

            ItemStack seed = new ItemStack(item, 1, meta);

            ItemFromFruitResult info = TileEntityFruitLeaves.getItemFromFruit(fruitType.indexToFruitType(i));

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

            seed.stackSize = 2;
            GameRegistry.addShapelessRecipe(seed,
                    fruit.copy(), fruit.copy(), fruit.copy(),
                    fruit.copy(), fruit.copy(), fruit.copy(),
                    fruit.copy(), fruit.copy(), fruit.copy()
            );
        }
    }
}
