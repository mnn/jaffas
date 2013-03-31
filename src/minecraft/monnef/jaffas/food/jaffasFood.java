package monnef.jaffas.food;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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
import extrabiomes.api.Api;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.CustomLogger;
import monnef.core.utils.ExtrabiomesHelper;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.block.BlockBoard;
import monnef.jaffas.food.block.BlockColumn;
import monnef.jaffas.food.block.BlockCross;
import monnef.jaffas.food.block.BlockFridge;
import monnef.jaffas.food.block.BlockJaffaBomb;
import monnef.jaffas.food.block.BlockJaffaStatue;
import monnef.jaffas.food.block.BlockPie;
import monnef.jaffas.food.block.BlockPizza;
import monnef.jaffas.food.block.BlockSink;
import monnef.jaffas.food.block.BlockSwitchgrass;
import monnef.jaffas.food.block.BlockTable;
import monnef.jaffas.food.block.ItemBlockPie;
import monnef.jaffas.food.block.ItemBlockSwitchgrass;
import monnef.jaffas.food.block.ItemBlockTable;
import monnef.jaffas.food.block.TileEntityBoard;
import monnef.jaffas.food.block.TileEntityColumn;
import monnef.jaffas.food.block.TileEntityCross;
import monnef.jaffas.food.block.TileEntityFridge;
import monnef.jaffas.food.block.TileEntityJaffaStatue;
import monnef.jaffas.food.block.TileEntityPie;
import monnef.jaffas.food.block.TileEntityPizza;
import monnef.jaffas.food.block.TileEntitySink;
import monnef.jaffas.food.client.GuiHandler;
import monnef.jaffas.food.command.CommandFridgeDebug;
import monnef.jaffas.food.command.CommandJaffaHunger;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.common.FuelHandler;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.common.PacketHandler;
import monnef.jaffas.food.crafting.AchievementsCraftingHandler;
import monnef.jaffas.food.crafting.JaffaCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.entity.EntityDuck;
import monnef.jaffas.food.entity.EntityDuckEgg;
import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.food.item.ItemJaffaPack;
import monnef.jaffas.food.item.ItemJaffaPainting;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.ItemJaffaSword;
import monnef.jaffas.food.item.ItemJaffaTool;
import monnef.jaffas.food.item.JaffaCreativeTab;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.food.item.common.Items;
import monnef.jaffas.food.server.PlayerTracker;
import monnef.jaffas.food.server.ServerTickHandler;
import monnef.jaffas.jaffasMod;
import monnef.jaffas.power.PowerManagersFactory;
import monnef.jaffas.power.api.PowerManager;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import static net.minecraft.world.biome.BiomeGenBase.beach;
import static net.minecraft.world.biome.BiomeGenBase.forest;
import static net.minecraft.world.biome.BiomeGenBase.forestHills;
import static net.minecraft.world.biome.BiomeGenBase.jungle;
import static net.minecraft.world.biome.BiomeGenBase.jungleHills;
import static net.minecraft.world.biome.BiomeGenBase.plains;
import static net.minecraft.world.biome.BiomeGenBase.river;
import static net.minecraft.world.biome.BiomeGenBase.swampland;
import static net.minecraft.world.biome.BiomeGenBase.taiga;
import static net.minecraft.world.biome.BiomeGenBase.taigaHills;

@Mod(modid = Reference.ModId, name = Reference.ModName, version = Reference.Version, dependencies = "after:Forestry;after:BuildCraft|Energy;after:ExtrabiomesXL;required-after:" + monnef.core.Reference.ModId)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"jaffas-01-sstone"}, packetHandler = PacketHandler.class)
public class jaffasFood extends jaffasMod {
    public static JaffaCreativeTab CreativeTab;

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

    private static IGuiHandler guiHandler;

    @Mod.Instance("Jaffas")
    public static jaffasFood instance;

    public static ItemJaffaPainting itemPainting;
    private int itemPaintingID;

    private static IDProvider idProvider = new IDProvider(3600, 24744);

    public static boolean debug;
    public static String jaffasTitle;
    public static String jaffaTitle;
    public boolean checkUpdates;

    private static int JaffaPaintingEntityID;
    private static int DuckEntityID;
    private static int DuckEggEntityID;

    public ItemManager itemManager;
    public ModuleManager moduleManager;
    public Items items;
    public static CustomLogger Log = new CustomLogger("Jaffas");
    boolean forestryDetected;
    public static String[] textureFile = new String[]{"/jaffas_01_a.png", "/jaffas_01_b.png"};
    boolean extraBiomes;

    public static boolean spawnStonesEnabled = true;
    public static int spawnStoneLittleCD;
    public static int spawnStoneMediumCD;
    public static int spawnStoneBigCD;
    public static boolean spawnStoneMultidimensional;

    public static boolean transferItemsFromCraftingMatrix;
    public static boolean ignoreBuildCraftsTables;

    public static boolean genDisabled;
    public static boolean genDisabledForNonStandardDimensions;

    public boolean IsForestryDetected() {
        return this.forestryDetected;
    }

    public boolean IsExtraBiomesDetected() {
        return this.extraBiomes;
    }

    public jaffasFood() {
        super();

        this.itemManager = new ItemManager();
        items = new Items();
        items.RegisterItemType(JaffaItemType.basic, ItemJaffaBase.class);
        items.RegisterItemType(JaffaItemType.food, ItemJaffaFood.class);
        items.RegisterItemType(JaffaItemType.tool, ItemJaffaTool.class);
        items.RegisterItemType(JaffaItemType.pack, ItemJaffaPack.class);
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
    public void PreLoad(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

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

            checkUpdates = config.get(Configuration.CATEGORY_GENERAL, "checkUpdates", true).getBoolean(true);

            blockCrossID = idProvider.getBlockIDFromConfig("cross");
            blockSinkID = idProvider.getBlockIDFromConfig("sink");
            blockBoardID = idProvider.getBlockIDFromConfig("board");
            blockPizzaID = idProvider.getBlockIDFromConfig("pizza");
            blockColumnID = idProvider.getBlockIDFromConfig("column");
            blockJaffaStatueID = idProvider.getBlockIDFromConfig("statue");
            blockPieID = idProvider.getBlockIDFromConfig("pie");
            blockTableID = idProvider.getBlockIDFromConfig("table");
            blockSwitchgrassID = idProvider.getBlockIDFromConfig("switchgrass");

            JaffaPaintingEntityID = idProvider.getEntityIDFromConfig("painting");
            createPainting();
            DuckEntityID = idProvider.getEntityIDFromConfig("duck");
            DuckEntityID = idProvider.getEntityIDFromConfig("duckEgg");
            registerDuck();

            spawnStonesEnabled = config.get(Configuration.CATEGORY_GENERAL, "spawnStonesEnable", true).getBoolean(true);
            spawnStoneLittleCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneLittleCD", 27).getInt();
            spawnStoneMediumCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMediumCD", 17).getInt();
            spawnStoneBigCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneBigCD", 7).getInt();
            spawnStoneMultidimensional = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMultidimensional", false, "Experimental!").getBoolean(false);
            transferItemsFromCraftingMatrix = config.get(Configuration.CATEGORY_GENERAL, "transferItemsFromCraftingMatrix", false, "Experimental, try to transfer items created after craft directly to player (e.g. crumpled paper)?").getBoolean(false);
            ignoreBuildCraftsTables = config.get(Configuration.CATEGORY_GENERAL, "ignoreBuildCraftsTables", true, "BC tables has broken recipes handling - wrong stack size or crash on craft").getBoolean(true);
            genDisabled = config.get(Configuration.CATEGORY_GENERAL, "genDisabled", false, "This option applies to all modules").getBoolean(false);
            genDisabledForNonStandardDimensions = config.get(Configuration.CATEGORY_GENERAL, "genDisabledForNonStandardDimensions", false, "This option applies to all modules").getBoolean(false);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }

        proxy.registerSounds();
        PowerManager.InitializeFactory(new PowerManagersFactory());
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        checkCore(); // really necessary?
        checkJsoup();
        checkForestry();
        checkExtrabiomes();

        CreativeTab = new JaffaCreativeTab("jaffas");

        MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());

        createBlocks();
        items.CreateItems();
        createJaffaArmorAndSword();
        registerDuckSpawns();
        AchievementsCraftingHandler.init();

        registerHandlers();

        Recipes.install();
        MinecraftForge.EVENT_BUS.register(new CustomDrop());

        //creative tab title
        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas", "en_US", "Jaffas and more!");

        GameRegistry.registerPlayerTracker(new PlayerTracker());

        printInitializedMessage();
    }

    private void checkCore() {
        if (!MonnefCorePlugin.isInitialized()) {
            throw new RuntimeException("Core is not properly initialized!");
        }
    }

    private void checkForestry() {
        try {
            Class c = Class.forName("forestry.Forestry");
            this.forestryDetected = true;
        } catch (ClassNotFoundException e) {
            this.forestryDetected = false;
        }
    }

    private void registerHandlers() {
        proxy.registerTickHandler();
        TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);

        guiHandler = new GuiHandler();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);

        GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());
        GameRegistry.registerCraftingHandler(new AchievementsCraftingHandler());

        proxy.registerRenderThings();
        GameRegistry.registerFuelHandler(new FuelHandler());
    }

    private void createPainting() {
        itemPainting = new ItemJaffaPainting(this.itemPaintingID);
        LanguageRegistry.addName(itemPainting, "Painting");
        registerEntity(EntityJaffaPainting.class, "jaffaPainting", 160, Integer.MAX_VALUE, false, JaffaPaintingEntityID);
    }

    private void registerDuck() {
        EntityRegistry.registerGlobalEntityID(EntityDuck.class, "jaffasDuck", DuckEntityID, ColorHelper.getInt(0, 127, 75), ColorHelper.getInt(200, 200, 255));
        EntityRegistry.registerModEntity(EntityDuck.class, "jaffasDuck", DuckEntityID, this, 160, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.jaffasDuck.name", "en_US", "Duck");
        EntityRegistry.registerModEntity(EntityDuckEgg.class, "duckEgg", DuckEggEntityID, this, 160, 1, true);
    }

    private void checkExtrabiomes() {
        extraBiomes = false;
        try {
            if (Api.isExtrabiomesXLActive())
                extraBiomes = true;
        } catch (Exception e) {
        }
    }

    private void registerDuckSpawns() {
        EntityRegistry.addSpawn(EntityDuck.class, 7, 1, 2, EnumCreatureType.creature, taigaHills, jungle, jungleHills); // low
        EntityRegistry.addSpawn(EntityDuck.class, 10, 1, 3, EnumCreatureType.creature, plains, taiga, forestHills);     // med
        EntityRegistry.addSpawn(EntityDuck.class, 15, 2, 6, EnumCreatureType.creature, swampland, river, beach, forest);// high

        if (extraBiomes) {
            // low - med
            ExtrabiomesHelper.addSpawn(EntityDuck.class, 9, 1, 3, EnumCreatureType.creature, "ALPINE", "FORESTEDHILLS", "MEADOW", "MINIJUNGLE", "PINEFOREST", "SAVANNA");
            // high
            ExtrabiomesHelper.addSpawn(EntityDuck.class, 16, 2, 6, EnumCreatureType.creature, "AUTUMNWOODS", "BIRCHFOREST", "FORESTEDISLAND", "GREENHILLS", "GREENSWAMP", "MARSH", "SHRUBLAND", "TEMPORATERAINFOREST", "WOODLANDS");
        }
    }

    private void createBlocks() {
        blockFridge = new BlockFridge(blockFridgeID);
        GameRegistry.registerBlock(blockFridge, "blockFridge");
        LanguageRegistry.addName(blockFridge, "Fridge");
        GameRegistry.registerTileEntity(TileEntityFridge.class, "Fridge");

        blockJaffaBomb = new BlockJaffaBomb(blockJaffaBombID, 35, Material.rock);
        GameRegistry.registerBlock(blockJaffaBomb, "blockJaffaBomb");
        LanguageRegistry.addName(blockJaffaBomb, "Jaffa Cakes BOMB");

        blockCross = new BlockCross(blockCrossID, 5, Material.rock);
        GameRegistry.registerBlock(blockCross, "blockCross");
        LanguageRegistry.addName(blockCross, "Cross");
        GameRegistry.registerTileEntity(TileEntityCross.class, "cross");

        blockSink = new BlockSink(blockSinkID, 141);
        GameRegistry.registerBlock(blockSink, "blockSink");
        LanguageRegistry.addName(blockSink, "Faucet");
        GameRegistry.registerTileEntity(TileEntitySink.class, "sink");

        blockBoard = new BlockBoard(blockBoardID, 142, Material.wood);
        RegistryUtils.registerBlock(blockBoard, "Kitchen Board");
        GameRegistry.registerTileEntity(TileEntityBoard.class, "kitchenBoard");

        blockPizza = new BlockPizza(blockPizzaID, 149, Material.cake);
        RegistryUtils.registerBlock(blockPizza);
        LanguageRegistry.addName(blockPizza, "Block of Pizza");
        GameRegistry.registerTileEntity(TileEntityPizza.class, "pizza");

        blockColumn = new BlockColumn(blockColumnID, 160, Material.rock);
        RegistryUtils.registerBlock(blockColumn);
        LanguageRegistry.addName(blockColumn, "Column");
        GameRegistry.registerTileEntity(TileEntityColumn.class, "column");

        blockJaffaStatue = new BlockJaffaStatue(blockJaffaStatueID, 6, Material.iron);
        RegistryUtils.registerBlock(blockJaffaStatue);
        LanguageRegistry.addName(blockJaffaStatue, "Jaffa Statue");
        GameRegistry.registerTileEntity(TileEntityJaffaStatue.class, "jaffaStatue");

        blockPie = new BlockPie(blockPieID, 156);
        RegistryUtils.registerMultiBlock(blockPie, ItemBlockPie.class, BlockPie.multiBlockNames);
        GameRegistry.registerTileEntity(TileEntityPie.class, "jaffaPie");

        blockTable = new BlockTable(blockTableID, 0, Material.wood);
        RegistryUtils.registerMultiBlock(blockTable, ItemBlockTable.class, BlockTable.multiBlockNames);

        blockSwitchgrass = new BlockSwitchgrass(blockSwitchgrassID, 238);
        RegistryUtils.registerMultiBlock(blockSwitchgrass, ItemBlockSwitchgrass.class, blockSwitchgrass.subBlockNames);
    }

    private void createJaffaArmorAndSword() {
        int armorRender = proxy.addArmor("Jaffa");
        itemJaffaPlate = new ItemJaffaPlate(itemJaffaPlateID, EnumArmorMaterialJaffas, armorRender, ItemJaffaPlate.ArmorType.chest, "/jaffabrn1.png", null, 90);
        LanguageRegistry.addName(itemJaffaPlate, "Jaffa Hoodie");

        itemJaffaSword = new ItemJaffaSword(itemJaffaSwordID, EnumToolMaterialJaffas);
        RegistryUtils.registerItem(itemJaffaSword, "jaffaSword", "Jaffa Sword");
        itemJaffaSword.setCustomIconIndex(88);
        itemJaffaSword.setSheetNumber(1);
    }

    private void printInitializedMessage() {
        Log.printInfo("Mod 'Jaffas and more!' successfully initialized");
        Log.printInfo("created by monnef and Tiartyos");
        Log.printInfo("version: " + Reference.Version + " ; " + monnef.core.Reference.URL);

        Log.printInfo("enabled modules: " + Joiner.on(", ").join(moduleManager.CompileEnabledModules()));
        Log.printInfo("detected mods: " + Joiner.on(", ").join(compileDetectedMods()));
    }

    private Iterable<String> compileDetectedMods() {
        ArrayList<String> list = new ArrayList<String>();
        if (IsForestryDetected()) list.add("forestry");
        if (IsExtraBiomesDetected()) list.add("extrabiomesxl");
        if (list.size() == 0) list.add("none");
        return list;
    }

    private void checkJsoup() {
        try {
            Class c = Class.forName("org.jsoup.Jsoup");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Jsoup library not found, cannot continue.");
        }
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
        if (debug) {
            manager.registerCommand(new CommandJaffaHunger());
            manager.registerCommand(new CommandFridgeDebug());
        }
    }

    public void AddMalletShapedRecipe(ItemStack output, ItemStack input) {
        for (int i = 0; i < itemManager.mallets.length; i++) {
            GameRegistry.addRecipe(output, "M", "O", 'M', new ItemStack(getItem(itemManager.mallets[i]), 1, -1), 'O', input);
        }
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
