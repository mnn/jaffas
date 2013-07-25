/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BiomeHelper;
import monnef.core.utils.ClassHelper;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.CustomLogger;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.achievement.AchievementsHandler;
import monnef.jaffas.food.block.BlockBoard;
import monnef.jaffas.food.block.BlockColumn;
import monnef.jaffas.food.block.BlockCross;
import monnef.jaffas.food.block.BlockFridge;
import monnef.jaffas.food.block.BlockJDirectional;
import monnef.jaffas.food.block.BlockJaffaBomb;
import monnef.jaffas.food.block.BlockJaffaStatue;
import monnef.jaffas.food.block.BlockMeatDryer;
import monnef.jaffas.food.block.BlockPie;
import monnef.jaffas.food.block.BlockPizza;
import monnef.jaffas.food.block.BlockSink;
import monnef.jaffas.food.block.BlockSwitchgrass;
import monnef.jaffas.food.block.BlockSwitchgrassSolid;
import monnef.jaffas.food.block.BlockTable;
import monnef.jaffas.food.block.ItemBlockPie;
import monnef.jaffas.food.block.ItemBlockSwitchgrass;
import monnef.jaffas.food.block.ItemBlockTable;
import monnef.jaffas.food.block.TileEntityBoard;
import monnef.jaffas.food.block.TileEntityColumn;
import monnef.jaffas.food.block.TileEntityCross;
import monnef.jaffas.food.block.TileEntityFridge;
import monnef.jaffas.food.block.TileEntityJaffaStatue;
import monnef.jaffas.food.block.TileEntityMeatDryer;
import monnef.jaffas.food.block.TileEntityPie;
import monnef.jaffas.food.block.TileEntityPizza;
import monnef.jaffas.food.block.TileEntitySink;
import monnef.jaffas.food.client.GuiHandler;
import monnef.jaffas.food.command.CommandFridgeDebug;
import monnef.jaffas.food.command.CommandJaffaHunger;
import monnef.jaffas.food.command.CommandJaffas;
import monnef.jaffas.food.command.CommandJaffasOP;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.common.FuelHandler;
import monnef.jaffas.food.common.JaffaCreativeTab;
import monnef.jaffas.food.common.JaffasRegistryHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.common.OtherModsHelper;
import monnef.jaffas.food.common.PlateUnequipper;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.food.common.SwitchgrassBonemealHandler;
import monnef.jaffas.food.crafting.JaffaCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.entity.EntityDuck;
import monnef.jaffas.food.entity.EntityDuckEgg;
import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.entity.EntityLittleSpider;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.food.item.ItemJaffaPainting;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.ItemJaffaRecipeTool;
import monnef.jaffas.food.item.ItemJaffaSword;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.food.item.common.Items;
import monnef.jaffas.food.network.JaffasPacketHandler;
import monnef.jaffas.food.server.PlayerTracker;
import monnef.jaffas.food.server.ServerTickHandler;
import monnef.jaffas.jaffasMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.minefactoryreloaded.api.FarmingRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import static net.minecraftforge.common.BiomeDictionary.Type;

@Mod(modid = Reference.ModId, name = Reference.ModName, version = Reference.Version, dependencies = "after:ThermalExpansion;after:MineFactoryReloaded;after:Forestry;after:BuildCraft|Energy;after:ExtrabiomesXL;required-after:monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {JaffasPacketHandler.CHANNEL_SpawnStone, JaffasPacketHandler.CHANNEL_Generic}, packetHandler = JaffasPacketHandler.class)
public class JaffasFood extends jaffasMod {
    public static final String LAST_VERSION_SHOWN = "lastVersionShown";

    private static MinecraftServer server;

    public static BlockJaffaBomb blockJaffaBomb;
    public static int blockJaffaBombID;

    public static BlockFridge blockFridge;
    public static int blockFridgeID;

    public static BlockCross blockCross;
    public static int blockCrossID;

    public static BlockSink blockSink;
    public static int blockSinkID;

    public static BlockBoard blockBoard;
    public static int blockBoardID;

    public static BlockPizza blockPizza;
    public static int blockPizzaID;

    public static int blockColumnID;
    public static BlockColumn blockColumn;

    public static int blockJaffaStatueID;
    public static BlockJaffaStatue blockJaffaStatue;

    public static int blockPieID;
    public static BlockPie blockPie;

    public static int blockTableID;
    public static BlockTable blockTable;

    public static int blockSwitchgrassID;
    public static BlockSwitchgrass blockSwitchgrass;

    public static int blockSwitchgrassSolidID;
    public static BlockJDirectional blockSwitchgrassSolid;

    public static int blockDirDebug1ID;
    public static BlockJDirectional blockDir1;

    public static int blockDirDebug2ID;
    public static BlockJDirectional blockDir2;

    public static int blockMeatDryerID;
    public static BlockMeatDryer blockMeatDryer;

    /*
    CLOTH(5, new int[]{1, 3, 2, 1}, 15),
    CHAIN(15, new int[]{2, 5, 4, 1}, 12),
    IRON(15, new int[]{2, 6, 5, 2}, 9),
    GOLD(7, new int[]{2, 5, 3, 1}, 25),
    DIAMOND(33, new int[]{3, 8, 6, 3}, 10);
    */

    public static EnumArmorMaterial EnumArmorMaterialJaffas = EnumHelper.addArmorMaterial("JaffaArmor", 10, new int[]{1, 4, 2, 3}, 23);
    public static EnumArmorMaterial EnumArmorMaterialDuck = EnumHelper.addArmorMaterial("Duck", 10, new int[]{1, 3, 2, 1}, 23);
    public static EnumArmorMaterial EnumArmorMaterialWolf = EnumHelper.addArmorMaterial("Wolf", 13, new int[]{3, 3, 2, 2}, 15);
    public static EnumArmorMaterial EnumArmorMaterialJaffarrol = EnumHelper.addArmorMaterial("Jaffarrol", 29, new int[]{4, 7, 5, 3}, 17);
    public static ItemJaffaPlate itemJaffaPlate;
    public static int itemJaffaPlateID;
    public static EnumToolMaterial EnumToolMaterialJaffas = EnumHelper.addToolMaterial("Jaffa", 2, 400, 6.0F, 6, 15);
    public static EnumToolMaterial EnumToolMaterialCleaver = EnumHelper.addToolMaterial("JaffaCleaver", 2, 75, 2.0F, 0, 15);
    public static ItemJaffaSword itemJaffaSword;
    public static int itemJaffaSwordID;

 /* WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    EMERALD(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);*/

    public static int renderID;
    public static int renderSwitchgrassID;
    public static int renderDirectionalBlockID;
    public static int renderBlockID;

    private static IGuiHandler guiHandler;

    @Mod.Instance("Jaffas")
    public static JaffasFood instance;

    public static ItemJaffaPainting itemPainting;
    private int itemPaintingID;

    public static boolean debug;
    public static String jaffasTitle;
    public static String jaffaTitle;
    public static boolean showUpdateMessages;
    public static String lastVersionShown;

    private static int jaffaPaintingEntityID;
    private static int duckEntityID;
    private static int duckEggEntityID;
    public static int spiderEntityID;

    public ItemManager itemManager;
    public ModuleManager moduleManager;
    public Items items;
    public static CustomLogger Log = new CustomLogger("Jaffas");

    public static boolean spawnStonesEnabled = true;
    public static int spawnStoneLittleCD;
    public static int spawnStoneMediumCD;
    public static int spawnStoneBigCD;
    public static boolean spawnStoneMultidimensional;

    public static boolean transferItemsFromCraftingMatrix;
    public static boolean ignoreBuildCraftsTables;

    public static boolean genDisabled;
    public static boolean genDisabledForNonStandardDimensions;

    public static boolean achievementsDisabled;
    public static Random rand = new Random();

    public static int duckSpawnProbabilityHigh;
    public static int duckSpawnProbabilityMed;
    public static int duckSpawnProbabilityLow;

    public static boolean slimeSpawningEnabled;
    public static boolean vanillaRecipesEnabled;
    public static boolean dungeonLootEnabled;
    public static boolean disableAutoUnEquip;

    public static OtherModsHelper otherMods;
    public ItemStack guideBook;

    public JaffasFood() {
        super();

        this.itemManager = new ItemManager();
        items = new Items();
        items.RegisterItemType(JaffaItemType.basic, ItemJaffaBase.class);
        items.RegisterItemType(JaffaItemType.food, ItemJaffaFood.class);
        items.RegisterItemType(JaffaItemType.tool, ItemJaffaRecipeTool.class);
        ItemManager.mallets = new JaffaItem[]{JaffaItem.mallet, JaffaItem.malletStone, JaffaItem.malletIron, JaffaItem.malletDiamond};
        ItemManager.malletHeads = new JaffaItem[]{JaffaItem.malletHead, JaffaItem.malletHeadStone, JaffaItem.malletHeadIron, JaffaItem.malletHeadDiamond};
        thisIsMainModule = true;
    }

    @Override
    protected void fillModuleSpecificMetadata(ModMetadata data) {
        data.description = "Adding famous Jaffa Cakes and a lot more into Minecraft (recipes are quite complex).";
    }

    @SidedProxy(clientSide = "monnef.jaffas.food.client.ClientProxy", serverSide = "monnef.jaffas.food.common.CommonProxy")
    public static CommonProxy proxy;

    @PreInit
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);
        otherMods = new OtherModsHelper();
        if (ClassHelper.isClassPresent("sharose.mods.idresolver.IDResolverMod")) {
            Log.printSevere("ID Resolver detected, do *not* use ID moving or face consequences.");
            Log.printSevere("I warned you.");
        }

        try {
            config.load();
            idProvider.linkWithConfig(config);

            this.moduleManager = new ModuleManager();
            ModuleManager.Add(ModulesEnum.food);
            for (ModulesEnum module : ModulesEnum.values()) {
                boolean defaultState = module.getEnabledByDefault();
                boolean enabled = config.get("modules", module.toString(), defaultState).getBoolean(defaultState);
                if (enabled) {
                    moduleManager.Add(module);
                }
            }

            itemJaffaPlateID = idProvider.getItemIDFromConfig("jaffaPlate");
            itemJaffaSwordID = idProvider.getItemIDFromConfig("jaffaSword");

            itemPaintingID = idProvider.getItemIDFromConfig("painting");

            // careful - order is important!
            jaffasTitle = config.get(Configuration.CATEGORY_GENERAL, "jaffasTitle", "Jaffas").getString();
            jaffaTitle = config.get(Configuration.CATEGORY_GENERAL, "jaffaTitle", "Jaffa").getString();
            items.InitializeItemInfos();
            items.LoadItemsFromConfig(idProvider);

            blockJaffaBombID = idProvider.getBlockIDFromConfig("jaffa bomb");
            blockFridgeID = idProvider.getBlockIDFromConfig("fridge");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            showUpdateMessages = config.get(Configuration.CATEGORY_GENERAL, "showUpdateMessages", true).getBoolean(true);
            lastVersionShown = config.get(Configuration.CATEGORY_GENERAL, LAST_VERSION_SHOWN, "").getString();

            blockCrossID = idProvider.getBlockIDFromConfig("cross");
            blockSinkID = idProvider.getBlockIDFromConfig("sink");
            blockBoardID = idProvider.getBlockIDFromConfig("board");
            blockPizzaID = idProvider.getBlockIDFromConfig("pizza");
            blockColumnID = idProvider.getBlockIDFromConfig("column");
            blockJaffaStatueID = idProvider.getBlockIDFromConfig("statue");
            blockPieID = idProvider.getBlockIDFromConfig("pie");
            blockTableID = idProvider.getBlockIDFromConfig("table");
            blockSwitchgrassID = idProvider.getBlockIDFromConfig("switchgrass");
            blockSwitchgrassSolidID = idProvider.getBlockIDFromConfig("switchgrassSolid");
            blockMeatDryerID = idProvider.getBlockIDFromConfig("meatDryer");
            if (MonnefCorePlugin.debugEnv) {
                blockDirDebug1ID = idProvider.getBlockIDFromConfig("blockDir1");
                blockDirDebug2ID = idProvider.getBlockIDFromConfig("blockDir2");
            }

            jaffaPaintingEntityID = idProvider.getEntityIDFromConfig("painting");
            createPainting();
            duckEntityID = idProvider.getEntityIDFromConfig("duck");
            registerDuck();
            duckEggEntityID = idProvider.getEntityIDFromConfig("duckEgg");
            registerDuckEgg();
            spiderEntityID = idProvider.getEntityIDFromConfig("littleSpider");
            registerLittleSpider();

            spawnStonesEnabled = config.get(Configuration.CATEGORY_GENERAL, "spawnStonesEnable", true).getBoolean(true);
            spawnStoneLittleCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneLittleCD", 27).getInt();
            spawnStoneMediumCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMediumCD", 17).getInt();
            spawnStoneBigCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneBigCD", 7).getInt();
            spawnStoneMultidimensional = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMultidimensional", true).getBoolean(true);
            transferItemsFromCraftingMatrix = config.get(Configuration.CATEGORY_GENERAL, "transferItemsFromCraftingMatrix", false, "Experimental, try to transfer items created after craft directly to player (e.g. crumpled paper)?").getBoolean(false);
            ignoreBuildCraftsTables = config.get(Configuration.CATEGORY_GENERAL, "ignoreBuildCraftsTables", true, "BC tables has broken recipes handling - wrong stack size or crash on craft").getBoolean(true);
            genDisabled = config.get(Configuration.CATEGORY_GENERAL, "genDisabled", false, "This option applies to all modules").getBoolean(false);
            genDisabledForNonStandardDimensions = config.get(Configuration.CATEGORY_GENERAL, "genDisabledForNonStandardDimensions", false, "This option applies to all modules").getBoolean(false);
            achievementsDisabled = config.get(Configuration.CATEGORY_GENERAL, "achievementsDisabled", false).getBoolean(false);
            duckSpawnProbabilityLow = config.get(Configuration.CATEGORY_GENERAL, "duckSpawnProbabilityLow", 10).getInt();
            duckSpawnProbabilityMed = config.get(Configuration.CATEGORY_GENERAL, "duckSpawnProbabilityMed", 12).getInt();
            duckSpawnProbabilityHigh = config.get(Configuration.CATEGORY_GENERAL, "duckSpawnProbabilityHigh", 16).getInt();
            FuelHandler.SWITCHGRASS_BURN_VALUE = config.get(Configuration.CATEGORY_GENERAL, "switchgrassBurnValue", 100).getInt();
            slimeSpawningEnabled = config.get(Configuration.CATEGORY_GENERAL, "slimeSpawningEnabled", true).getBoolean(true);
            JaffasRegistryHelper.compatibilityMode = config.get(Configuration.CATEGORY_GENERAL, "dontPrefixTileEntityIDs", false, "Set to true if you're playing map created with 0.4.20 or older. Do not use in new worlds, because it will be eventually removed.").getBoolean(false);
            vanillaRecipesEnabled = config.get(Configuration.CATEGORY_GENERAL, "vanillaRecipesEnabled", true, "These are recipes producing vanilla items/blocks from vanilla items/blocks - e.g. grass block").getBoolean(true);
            dungeonLootEnabled = config.get(Configuration.CATEGORY_GENERAL, "dungeonLootEnabled", true).getBoolean(true);
            disableAutoUnEquip = config.get(Configuration.CATEGORY_GENERAL, "disableAutoUnEquip", false).getBoolean(false);

            AchievementsHandler.setStartingId(config.get(Configuration.CATEGORY_GENERAL, "achievementOffset", 9790).getInt());
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }

        proxy.registerSounds();
    }

    @Override
    protected int getStartOfItemsIdInterval() {
        return 24744;
    }

    @Override
    protected int getStartOfBlocksIdInterval() {
        return 3600;
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);
        creativeTab = new JaffaCreativeTab("jaffas");

        OtherModsHelper.checkCore();

        MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());

        createBlocks();
        items.CreateItems();
        createJaffaArmorAndSword();
        registerDuckSpawns();
        registerCleaverRecords();
        AchievementsHandler.init();
        addDungeonLoot();

        registerHandlers();

        Recipes.installRecipes();
        MinecraftForge.EVENT_BUS.register(new CustomDrop());

        //creative tab title
        creativeTab.setup(JaffaItem.jaffaP);
        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas", "en_US", "Jaffas and more!");

        GameRegistry.registerPlayerTracker(new PlayerTracker());

        MinecraftForge.addGrassPlant(blockSwitchgrass, BlockSwitchgrass.VALUE_TOP, 5);

        printInitializedMessage();
    }

    @Mod.PostInit
    public void postLoad(FMLPostInitializationEvent event) {
        Recipes.postLoadInstallRecipes();
    }

    private void addDungeonLoot() {
        if (!JaffasFood.dungeonLootEnabled) return;

        // 100 ~ trash, 5 ~ treasure
        addToDungeons(JaffaItem.malletIron, 25, 1, 1);
        addToDungeons(JaffaItem.honey, 90, 5, 15);
        addToDungeons(JaffaItem.kettle, 30, 1, 1);
        addToDungeons(new ItemStack(blockSwitchgrass, 1, BlockSwitchgrass.VALUE_TOP), 90, 5, 64);
        addToDungeons(JaffaItem.magnifier, 30, 1, 1);
        addToDungeons(JaffaItem.spawnStoneLittle, 10, 1, 1);
        addToDungeons(JaffaItem.spawnStoneMedium, 5, 1, 1);
        addToDungeons(JaffaItem.jaffa, 20, 5, 15);
        addToDungeons(JaffaItem.scrap, 40, 1, 64);

        ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(Recipes.getItemStack(JaffaItem.mallet), 1, 1, 4));

        // 20 ~ trash, 3 ~ treasure
        addToPyramids(JaffaItem.spawnStoneBig, 3, 1, 1);
        addToPyramids(JaffaItem.malletHeadDiamond, 3, 1, 1);
        addToPyramids(JaffaItem.spiderLegRaw, 7, 1, 4);
    }

    public static void addToDungeons(JaffaItem item, int weight, int min, int max) {
        addToDungeons(Recipes.getItem(item), weight, min, max);
    }

    public static void addToDungeons(Item item, int weight, int min, int max) {
        addToDungeons(new ItemStack(item), weight, min, max);
    }

    public static void addToDungeons(Block block, int weight, int min, int max) {
        addToDungeons(new ItemStack(block), weight, min, max);
    }

    public static void addToDungeons(ItemStack stack, int weight, int min, int max) {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(stack, min, max, weight));
    }

    public static void addToPyramids(JaffaItem item, int weight, int min, int max) {
        addToPyramids(Recipes.getItemStack(item), weight, min, max);
    }

    public static void addToPyramids(ItemStack stack, int weight, int min, int max) {
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(stack, min, max, weight));
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(stack, min, max, weight));
    }

    private void registerCleaverRecords() {
        ItemCleaverHookContainer.registerMeatFromAnimal(EntityDuck.class, new ItemStack(getItem(JaffaItem.duckRaw)));
        ItemCleaverHookContainer.registerMeatFromAnimal(EntitySheep.class, new ItemStack(getItem(JaffaItem.muttonRaw)));
        ItemCleaverHookContainer.registerMeatFromAnimal(EntityWolf.class, new ItemStack(getItem(JaffaItem.wolfMeatRaw)));
    }

    private void registerHandlers() {
        proxy.registerTickHandler();
        TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);

        guiHandler = new GuiHandler();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);

        GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());

        proxy.registerRenderThings();
        GameRegistry.registerFuelHandler(new FuelHandler());

        if (!disableAutoUnEquip) {
            MinecraftForge.EVENT_BUS.register(new PlateUnequipper());
        }
    }

    private void createPainting() {
        itemPainting = new ItemJaffaPainting(this.itemPaintingID);
        LanguageRegistry.addName(itemPainting, "Painting");
        registerEntity(EntityJaffaPainting.class, "jaffaPainting", 160, Integer.MAX_VALUE, false, jaffaPaintingEntityID);
    }

    private void registerDuck() {
        EntityRegistry.registerGlobalEntityID(EntityDuck.class, "jaffasDuck", duckEntityID, ColorHelper.getInt(0, 127, 75), ColorHelper.getInt(200, 200, 255));
        EntityRegistry.registerModEntity(EntityDuck.class, "jaffasDuck", duckEntityID, this, 160, 1, true);
        //EntityHelper.setTrackingRange(EntityDuck.class, 160);
        LanguageRegistry.instance().addStringLocalization("entity.jaffasDuck.name", "en_US", "Duck");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FarmingRegistry.registerGrindable(new EntityDuck.MFR());
            FarmingRegistry.registerBreederFood(EntityDuck.class, new ItemStack(Item.seeds));
        }
    }

    private void registerDuckEgg() {
        EntityRegistry.registerGlobalEntityID(EntityDuckEgg.class, "duckEgg", duckEggEntityID);
        EntityRegistry.registerModEntity(EntityDuckEgg.class, "duckEgg", duckEggEntityID, this, 160, 1, true);
    }

    private void registerLittleSpider() {
        EntityRegistry.registerGlobalEntityID(EntityLittleSpider.class, "jaffasSpider", spiderEntityID, ColorHelper.getInt(122, 122, 122), ColorHelper.getInt(0, 0, 202));
        EntityRegistry.registerModEntity(EntityLittleSpider.class, "jaffasSpider", spiderEntityID, this, 160, 1, true);
        //EntityHelper.setTrackingRange(EntityLittleSpider.class, 160);
        LanguageRegistry.instance().addStringLocalization("entity.jaffasSpider.name", "en_US", "Little Spider");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FarmingRegistry.registerGrindable(new EntityLittleSpider.MFR());
            //FarmingRegistry.registerBreederFood(EntityLittleSpider.class, new ItemStack(Item.seeds));
        }
    }

    private void registerDuckSpawns() {
        BiomeDictionary.registerAllBiomes();

        EntityRegistry.addSpawn(EntityDuck.class, duckSpawnProbabilityHigh, 4, 6, EnumCreatureType.creature, // high
                BiomeHelper.compileListOrAsArray(new Type[]{Type.SWAMP, Type.BEACH}));
        EntityRegistry.addSpawn(EntityDuck.class, duckSpawnProbabilityMed, 2, 5, EnumCreatureType.creature,  // med
                BiomeHelper.compileListOrAsArray(new Type[]{Type.PLAINS, Type.FOREST}));
        EntityRegistry.addSpawn(EntityDuck.class, duckSpawnProbabilityLow, 1, 4, EnumCreatureType.creature,  // low
                BiomeHelper.compileListOrAsArray(new Type[]{Type.JUNGLE}));
    }

    private void createBlocks() {
        blockFridge = new BlockFridge(blockFridgeID);
        GameRegistry.registerBlock(blockFridge, "blockFridge");
        LanguageRegistry.addName(blockFridge, "Fridge");
        JaffasRegistryHelper.registerTileEntity(TileEntityFridge.class, "Fridge");

        blockJaffaBomb = new BlockJaffaBomb(blockJaffaBombID, 35, Material.rock);
        GameRegistry.registerBlock(blockJaffaBomb, "blockJaffaBomb");
        LanguageRegistry.addName(blockJaffaBomb, "Jaffa Cakes BOMB");

        blockCross = new BlockCross(blockCrossID, 5, Material.rock);
        GameRegistry.registerBlock(blockCross, "blockCross");
        LanguageRegistry.addName(blockCross, "Cross");
        JaffasRegistryHelper.registerTileEntity(TileEntityCross.class, "cross");

        blockSink = new BlockSink(blockSinkID, 141);
        GameRegistry.registerBlock(blockSink, "blockSink");
        LanguageRegistry.addName(blockSink, "Faucet");
        JaffasRegistryHelper.registerTileEntity(TileEntitySink.class, "sink");

        blockBoard = new BlockBoard(blockBoardID, 142, Material.wood);
        RegistryUtils.registerBlock(blockBoard, "Kitchen Board");
        JaffasRegistryHelper.registerTileEntity(TileEntityBoard.class, "kitchenBoard");

        blockPizza = new BlockPizza(blockPizzaID, 149, Material.cake);
        RegistryUtils.registerBlock(blockPizza);
        LanguageRegistry.addName(blockPizza, "Block of Pizza");
        JaffasRegistryHelper.registerTileEntity(TileEntityPizza.class, "pizza");

        blockColumn = new BlockColumn(blockColumnID, 160, Material.rock);
        RegistryUtils.registerBlock(blockColumn);
        LanguageRegistry.addName(blockColumn, "Column");
        JaffasRegistryHelper.registerTileEntity(TileEntityColumn.class, "column");

        blockJaffaStatue = new BlockJaffaStatue(blockJaffaStatueID, 6, Material.iron);
        RegistryUtils.registerBlock(blockJaffaStatue);
        LanguageRegistry.addName(blockJaffaStatue, "Jaffa Statue");
        JaffasRegistryHelper.registerTileEntity(TileEntityJaffaStatue.class, "jaffaStatue");

        blockPie = new BlockPie(blockPieID, 156);
        RegistryUtils.registerMultiBlock(blockPie, ItemBlockPie.class, BlockPie.multiBlockNames);
        JaffasRegistryHelper.registerTileEntity(TileEntityPie.class, "jaffaPie");

        blockTable = new BlockTable(blockTableID, 0, Material.wood);
        RegistryUtils.registerMultiBlock(blockTable, ItemBlockTable.class, BlockTable.multiBlockNames);
        blockTable.setSheetNumber(7);

        blockSwitchgrass = new BlockSwitchgrass(blockSwitchgrassID, 238);
        RegistryUtils.registerMultiBlock(blockSwitchgrass, ItemBlockSwitchgrass.class, blockSwitchgrass.subBlockNames);
        MinecraftForge.EVENT_BUS.register(new SwitchgrassBonemealHandler());
        if (otherMods.isMineFactoryReloadedDetected()) {
            FarmingRegistry.registerFertilizable(blockSwitchgrass);
            FarmingRegistry.registerHarvestable(blockSwitchgrass);
            FarmingRegistry.registerPlantable(blockSwitchgrass);
        }

        blockSwitchgrassSolid = new BlockSwitchgrassSolid(blockSwitchgrassSolidID, 240, 2);
        RegistryUtils.registerBlock(blockSwitchgrassSolid, "switchgrassSolid", "Block of Switchgrass");

        blockMeatDryer = new BlockMeatDryer(blockMeatDryerID, 250);
        RegistryUtils.registerBlock(blockMeatDryer, "meatDryer", "Meat Dryer");
        JaffasRegistryHelper.registerTileEntity(TileEntityMeatDryer.class, "MeatDryer");

        if (MonnefCorePlugin.debugEnv) {
            blockDir1 = new BlockJDirectional(blockDirDebug1ID, 35, 2, Material.rock, BlockJDirectional.TextureMappingType.LOG_LIKE);
            RegistryUtils.registerBlock(blockDir1, "dir1", "Dir 1 - Log like");

            blockDir2 = new BlockJDirectional(blockDirDebug2ID, 35, 6, Material.rock, BlockJDirectional.TextureMappingType.ALL_SIDES);
            RegistryUtils.registerBlock(blockDir2, "dir2", "Dir 2 - All sides");
        }
    }

    private void createJaffaArmorAndSword() {
        int armorRender = proxy.addArmor("Jaffa");
        itemJaffaPlate = new ItemJaffaPlate(itemJaffaPlateID, EnumArmorMaterialJaffas, armorRender, ItemJaffaPlate.ArmorType.chest, "/jaffabrn1.png", null, 90);
        LanguageRegistry.addName(itemJaffaPlate, "Jaffa Hoodie");

        itemJaffaSword = new ItemJaffaSword(itemJaffaSwordID, 88, EnumToolMaterialJaffas);
        RegistryUtils.registerItem(itemJaffaSword, "jaffaSword", "Jaffa Sword");
    }

    private void printInitializedMessage() {
        Log.printInfo("Mod '" + Reference.ModName + "' successfully initialized");
        Log.printInfo("created by " + monnef.core.Reference.MONNEF + " and " + monnef.core.Reference.TIARTYOS + "");
        Log.printInfo("version: " + Reference.Version + " ; " + monnef.core.Reference.URL_JAFFAS_WIKI);

        Log.printInfo("enabled modules: " + Joiner.on(", ").join(ModuleManager.CompileEnabledModules()));
        Log.printInfo("detected mods: " + Joiner.on(", ").join(otherMods.compileDetectedMods()));
    }

    private void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int id) {
        //Log.printDebug("Registered: " + entityClass + " id: " + id);
        EntityRegistry.registerGlobalEntityID(entityClass, entityName, id);
        EntityRegistry.registerModEntity(entityClass, entityName, id, this, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    @Mod.ServerStarting
    public void serverStarting(FMLServerStartingEvent event) {
        server = ModLoader.getMinecraftServerInstance();
        ICommandManager commandManager = server.getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        addCommands(serverCommandManager);
    }

    private void addCommands(ServerCommandManager manager) {
        if (debug || MonnefCorePlugin.debugEnv) {
            manager.registerCommand(new CommandJaffaHunger());
            manager.registerCommand(new CommandFridgeDebug());
        }
        manager.registerCommand(new CommandJaffas());
        manager.registerCommand(new CommandJaffasOP());
    }

    public static Item getItem(JaffaItem item) {
        return ItemManager.getItem(item);
    }

    public static void PrintInitialized(ModulesEnum module) {
        Log.printInfo("Module " + module + " initialized.");
    }

    private static final Set<Integer> defaultDimensions = new HashSet<Integer>(Arrays.asList(-1, 0, 1));

    public static boolean isGenerationEnabled(int dimensionID) {
        if (genDisabled) {
            return false;
        }
        if (defaultDimensions.contains(dimensionID)) {
            return true;
        }

        if (genDisabledForNonStandardDimensions) {
            return false;
        } else {
            return true;
        }
    }
}
