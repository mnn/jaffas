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
import monnef.core.IDProvider;
import monnef.core.RegistryUtils;
import monnef.core.Version;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.ItemManager;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.logging.Level;

import static monnef.jaffas.food.mod_jaffas.getItem;
import static monnef.jaffas.trees.DropType.DropsFromGrass;
import static monnef.jaffas.trees.EatableType.EatableNormal;
import static monnef.jaffas.trees.EatableType.NotEatable;

@Mod(modid = "moen-jaffas-trees", name = "Jaffas - trees", version = Version.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = mod_jaffas_trees.channel, packetHandler = PacketHandler.class)
public class mod_jaffas_trees {
    private static MinecraftServer server;
    public static boolean bonemealingAllowed;

    public static JaffaCreativeTab CreativeTab;

    public static final String channel = "jaffas-02";

    public static final String[] treeTypes = new String[]{"normal", "apple", "cocoa", "vanilla", "lemon", "orange", "plum", "coconut"};
    public static final String[] seedsNames = new String[]{"[UNUSED]", "Apple Seeds", "Cocoa Seeds", "Vanilla Seeds", "Lemon Seeds", "Orange Seeds", "Plum Seeds", "Coconut Seeds"};

    private static IGuiHandler guiHandler;

    public static BlockFruitCollector blockFruitCollector;
    public static int blockFruitCollectorID;
    private static final int SEEDS_WEIGHT = 1;

    public static fruitType getActualLeavesType(Block block, int blockMetadata) {
        BlockFruitLeaves b = (BlockFruitLeaves) block;
        return getActualLeavesType(b.serialNumber, blockMetadata);
    }

    public static fruitType getActualLeavesType(int serialNumber, int blockMetadata) {
        int index = serialNumber * 4 + blockMetadata;
        fruitType fruitType = mod_jaffas_trees.fruitType.indexToFruitType(index);
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

    @Mod.Instance("moen-jaffas-trees")
    public static mod_jaffas_trees instance;

    public static ArrayList<LeavesInfo> leavesList = new ArrayList<LeavesInfo>();

    public static final int leavesBlocksAllocated = 3;
    public static final int leavesTypesCount = 7;

    public static boolean debug;

    private int itemLemonID;
    private int itemOrangeID;
    private int itemPlumID;
    private int itemCoconutID;
    public static Item itemLemon;
    public static Item itemOrange;
    public static Item itemPlum;
    public static Item itemCoconut;

    private int itemDebugID;
    public static ItemJaffaTreeDebugTool itemDebug;

    private int itemStickID;
    public static ItemJaffaT itemStick;
    private int itemRodID;
    public static ItemJaffaT itemRod;
    private int itemFruitPickerID;
    public static ItemJaffaT itemFruitPicker;
    private int itemFruitPickerHeadID;
    public static ItemJaffaT itemFruitPickerHead;

    public static enum bushType {
        Coffee, Strawberry, Onion, Paprika, Raspberry, Tomato, Mustard, Peanuts;
    }

    public static EnumMap<bushType, BushInfo> BushesList = new EnumMap<bushType, BushInfo>(bushType.class);

    public final static String textureFile = "/jaffas_02.png";

    public mod_jaffas_trees() {
        instance = this;
    }

    private static IDProvider idProvider = new IDProvider(3500, 25244);

    @SidedProxy(clientSide = "monnef.jaffas.trees.ClientProxyTutorial", serverSide = "monnef.jaffas.trees.CommonProxyTutorial")
    public static CommonProxyTutorial proxy;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        PopulateBushInfo();

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            itemLemonID = idProvider.getItemIDFromConfig("lemon");
            itemOrangeID = idProvider.getItemIDFromConfig("orange");
            itemPlumID = idProvider.getItemIDFromConfig("plum");
            itemCoconutID = idProvider.getItemIDFromConfig("coconut");

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

            blockFruitCollectorID = idProvider.getBlockIDFromConfig("fruit collector");

            itemDebugID = idProvider.getItemIDFromConfig("debug tool");

            itemStickID = idProvider.getItemIDFromConfig("stick");
            itemRodID = idProvider.getItemIDFromConfig("rod");
            itemFruitPickerID = idProvider.getItemIDFromConfig("fruit picker");
            itemFruitPickerHeadID = idProvider.getItemIDFromConfig("fruit picker head");

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
    }

    private Item constructFruit(int id, EatableType type) {
        if (type == NotEatable) {
            return new ItemJaffaBerry(id);
        } else if (type == EatableNormal) {
            return (new ItemJaffaBerryEatable(id)).Setup(2, 0.2f);
        }

        throw new RuntimeException("unknown eatable type");
    }

    private void constructItemsInBushInfo() {
        for (EnumMap.Entry<bushType, BushInfo> entry : BushesList.entrySet()) {
            BushInfo info = entry.getValue();

            ItemJaffaSeeds seeds = new ItemJaffaSeeds(info.itemSeedsID, info.blockID, Block.tilledField.blockID);
            seeds.setItemName(info.getSeedsLanguageName()).setIconIndex(info.seedsTexture);
            LanguageRegistry.addName(seeds, info.seedsTitle);
            info.itemSeeds = seeds;
            if (info.drop == DropsFromGrass) MinecraftForge.addGrassSeed(new ItemStack(seeds), SEEDS_WEIGHT);

            Item fruit = constructFruit(info.itemFruitID, info.eatable);
            fruit.setItemName(info.getFruitLanguageName()).setIconIndex(info.fruitTexture).setCreativeTab(CreativeTab);
            LanguageRegistry.addName(fruit, info.fruitTitle);
            info.itemFruit = fruit;

            BlockJaffaCrops crops = new BlockJaffaCrops(info.blockID, info.plantTexture, info.phases, info.product == null ? info.itemFruit : info.product, info.itemSeeds, info.renderer);
            crops.setBlockName(info.getPlantLanguageName());
            GameRegistry.registerBlock(crops, info.name);
            LanguageRegistry.addName(crops, info.plantTitle);
            info.block = crops;

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
        if (!ModuleManager.IsModuleEnabled(ModulesEnum.trees))
            return;

        CreativeTab = new JaffaCreativeTab("jaffas.trees");
        guiHandler = new GuiHandlerTrees();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);

        AddFruitTreesSequence(0, 0, 32, 4);
        AddFruitTreesSequence(1, 4, 32 + 4, 4);

        for (int i = 1; i < mod_jaffas_trees.leavesTypesCount + 1; i++) {
            MinecraftForge.addGrassSeed(getTreeSeeds(i), SEEDS_WEIGHT);
        }

        GameRegistry.registerTileEntity(TileEntityFruitLeaves.class, "fruitLeaves");
        GameRegistry.registerTileEntity(TileEntityJaffaCrops.class, "jaffaCrops");

        itemLemon = constructFruit(itemLemonID, NotEatable);
        itemLemon.setItemName("lemon").setIconCoord(4, 4);
        LanguageRegistry.addName(itemLemon, "Lemon");

        itemOrange = constructFruit(itemOrangeID, EatableNormal);
        itemOrange.setItemName("orange").setIconCoord(5, 4);
        LanguageRegistry.addName(itemOrange, "Orange");

        itemPlum = constructFruit(itemPlumID, EatableNormal);
        itemPlum.setItemName("plum").setIconCoord(6, 4);
        LanguageRegistry.addName(itemPlum, "Plum");

        itemCoconut = constructFruit(itemCoconutID, NotEatable);
        itemCoconut.setItemName("coconut").setIconCoord(7, 4);
        LanguageRegistry.addName(itemCoconut, "Coconut");

        constructItemsInBushInfo();

        blockFruitCollector = new BlockFruitCollector(blockFruitCollectorID);
        GameRegistry.registerBlock(blockFruitCollector, "blockFruitCollector");
        LanguageRegistry.addName(blockFruitCollector, "Fruit Collector");
        GameRegistry.registerTileEntity(TileEntityFruitCollector.class, "fruitcollector");

        itemDebug = new ItemJaffaTreeDebugTool(itemDebugID);
        itemDebug.setMaxStackSize(1).setItemName("jaffaTreeDebug").setIconCoord(13, 0);
        LanguageRegistry.addName(itemDebug, "Jaffa Tree's Debug Tool");

        itemStick = new ItemJaffaT(itemStickID);
        itemStick.setItemName("stickImpregnated").setIconCoord(0, 10);
        LanguageRegistry.addName(itemStick, "Impregnated Stick");

        itemRod = new ItemJaffaT(itemRodID);
        itemRod.setItemName("rod").setIconCoord(1, 10).setMaxStackSize(1).setMaxDamage(64);
        LanguageRegistry.addName(itemRod, "Reinforced Rod");

        itemFruitPickerHead = new ItemJaffaT(itemFruitPickerHeadID);
        itemFruitPickerHead.setItemName("fruitPickerHead").setIconCoord(2, 10);
        LanguageRegistry.addName(itemFruitPickerHead, "Head of Fruit Picker");

        itemFruitPicker = new ItemJaffaT(itemFruitPickerID);
        itemFruitPicker.setItemName("fruitPicker").setIconCoord(3, 10).setMaxStackSize(1).setMaxDamage(256);
        LanguageRegistry.addName(itemFruitPicker, "Fruit Picker");

        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        //GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());

        //forestry stuff
        CropProviders.cerealCrops.add(new JaffaCropProvider());

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.trees", "en_US", "Jaffas and more! Trees");

        mod_jaffas.PrintInitialized(ModulesEnum.trees);
    }

    private void AddFruitTreesSequence(int i, int leavesTexture, int seedTexture, int subCount) {
        LeavesInfo leaves = leavesList.get(i);
        leaves.leavesBlock = new BlockFruitLeaves(leaves.leavesID, leavesTexture, subCount);
        leaves.leavesBlock.serialNumber = i;
        leaves.leavesBlock.setLeavesRequiresSelfNotify().setBlockName("fruitLeaves" + i).setCreativeTab(CreativeTab).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep);
        RegistryUtils.registerBlock(leaves.leavesBlock);
        LanguageRegistry.addName(leaves.leavesBlock, "Leaves");

        leaves.saplingBlock = new BlockFruitSapling(leaves.saplingID, 15);
        leaves.saplingBlock.serialNumber = i;
        String saplingBlockName = "fruitSapling" + i;
        leaves.saplingBlock.setBlockName(saplingBlockName).setCreativeTab(CreativeTab);
        GameRegistry.registerBlock(leaves.saplingBlock, saplingBlockName);
        LanguageRegistry.addName(leaves.saplingBlock, "Fruit Sapling");

        for (int j = 0; j < subCount; j++) {
            LanguageRegistry.instance().addStringLocalization("item.fruitSeeds" + i + "." + j + ".name", seedsNames[j + i * 4]);
        }
        leaves.seedsItem = new ItemFruitSeeds(leaves.seedsID, leaves.saplingID, seedTexture, subCount);
        leaves.seedsItem.setFirstInSequence();
        leaves.seedsItem.setItemName("fruitSeeds" + i);
        leaves.seedsItem.serialNumber = i;
        LanguageRegistry.addName(leaves.seedsItem, "Fruit Seeds");
    }

    public static ItemStack getTreeSeeds(int type) {
        ItemStack seed;
        ItemFruitSeeds item = mod_jaffas_trees.leavesList.get(type / 4).seedsItem;
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
                new ItemStack(mod_jaffas_trees.itemLemon),
                new ItemStack(mod_jaffas_trees.itemLemon),
                new ItemStack(mod_jaffas_trees.itemLemon),
                new ItemStack(mod_jaffas_trees.itemLemon));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.oranges)),
                new ItemStack(mod_jaffas_trees.itemOrange),
                new ItemStack(mod_jaffas_trees.itemOrange),
                new ItemStack(mod_jaffas_trees.itemOrange),
                new ItemStack(mod_jaffas_trees.itemOrange));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.plums)),
                new ItemStack(mod_jaffas_trees.itemPlum),
                new ItemStack(mod_jaffas_trees.itemPlum),
                new ItemStack(mod_jaffas_trees.itemPlum),
                new ItemStack(mod_jaffas_trees.itemPlum));

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

        GameRegistry.addSmelting(BushesList.get(bushType.Coffee).itemFruit.shiftedIndex, new ItemStack(getJaffaItem(JaffaItem.coffeeRoasted)), 0.5F);

//        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.paprikaChopped)), "K", "M", 'K', new ItemStack(getJaffaItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(BushesList.get(bushType.Paprika).itemFruit));
//        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.tomatoChopped)), "K", "M", 'K', new ItemStack(getJaffaItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(BushesList.get(bushType.Tomato).itemFruit));
        RecipesBoard.addRecipe(getFruitStack(bushType.Paprika), new ItemStack(getJaffaItem(JaffaItem.paprikaChopped)));
        RecipesBoard.addRecipe(getFruitStack(bushType.Tomato), new ItemStack(getJaffaItem(JaffaItem.tomatoChopped)));


        if (!ModuleManager.IsModuleEnabled(ModulesEnum.ores)) {
            GameRegistry.addRecipe(new ItemStack(blockFruitCollector), "IDI", "DRD", "IGI",
                    'I', new ItemStack(Block.blockSteel), 'D', new ItemStack(Item.diamond), 'R', new ItemStack(Block.torchRedstoneActive), 'G', new ItemStack(Block.blockGold));
        }

        installFruitSeedsRecipes();

        GameRegistry.addShapelessRecipe(new ItemStack(itemStick, 4), new ItemStack(Item.stick), new ItemStack(Item.stick), new ItemStack(Item.stick), new ItemStack(Item.stick), new ItemStack(Item.slimeBall));
        GameRegistry.addRecipe(new ItemStack(itemRod), " S ", "ISI", " S ", 'S', new ItemStack(itemStick), 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addRecipe(new ItemStack(itemFruitPickerHead), "III", "WWW", " W ", 'I', new ItemStack(Item.ingotIron), 'W', new ItemStack(Block.cloth, 1, -1));
        GameRegistry.addRecipe(new ItemStack(itemFruitPicker), "H ", " R", 'H', new ItemStack(itemFruitPickerHead), 'R', new ItemStack(itemRod));

        mod_jaffas.instance.AddMalletShapedRecipe(new ItemStack(getJaffaItem(JaffaItem.coconutPowder)), new ItemStack(itemCoconut));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.browniesPastry)), getFruitStack(bushType.Peanuts),
                new ItemStack(getItem(JaffaItem.pastrySweet)), new ItemStack(getItem(JaffaItem.chocolate)));

        RecipesBoard.addRecipe(mod_jaffas_trees.getFruitStack(mod_jaffas_trees.bushType.Onion), new ItemStack(getItem(JaffaItem.onionSliced)));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.bottleKetchup)), Item.sugar, getJaffaItem(JaffaItem.bottleEmpty), getFruitStack(bushType.Tomato), getFruitStack(bushType.Tomato));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.bottleMustard)), getItem(JaffaItem.bottleEmpty), getFruit(bushType.Mustard), getFruit(bushType.Mustard));
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
            ItemFruitSeeds item = mod_jaffas_trees.leavesList.get(type / 4).seedsItem;
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
