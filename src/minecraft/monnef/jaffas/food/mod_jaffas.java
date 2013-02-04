package monnef.jaffas.food;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
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
import monnef.core.ColorHelper;
import monnef.core.IDProvider;
import monnef.core.RegistryUtils;
import monnef.core.Version;
import monnef.jaffas.food.block.*;
import monnef.jaffas.food.client.GuiHandler;
import monnef.jaffas.food.command.CommandFridgeDebug;
import monnef.jaffas.food.command.CommandJaffaHunger;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.common.PacketHandler;
import monnef.jaffas.food.crafting.AchievementsCraftingHandler;
import monnef.jaffas.food.crafting.JaffaCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.entity.EntityDuck;
import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.item.*;
import monnef.jaffas.food.server.PlayerTracker;
import monnef.jaffas.food.server.ServerTickHandler;
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

import java.util.logging.Level;

@Mod(modid = "moen-jaffas", name = "Jaffas", version = Version.Version, dependencies = "after:Forestry;after:BuildCraft|Energy")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"jaffas-01-sstone"}, packetHandler = PacketHandler.class)
public class mod_jaffas {
    public static JaffaCreativeTab CreativeTab;

    private static MinecraftServer server;


    public static JaffaBombBlock blockJaffaBomb;
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

    /*
    CLOTH(5, new int[]{1, 3, 2, 1}, 15),
    CHAIN(15, new int[]{2, 5, 4, 1}, 12),
    IRON(15, new int[]{2, 6, 5, 2}, 9),
    GOLD(7, new int[]{2, 5, 3, 1}, 25),
    DIAMOND(33, new int[]{3, 8, 6, 3}, 10);
    */

    public static EnumArmorMaterial EnumArmorMaterialJaffas = EnumHelper.addArmorMaterial("JaffaArmor", 10, new int[]{1, 4, 2, 3}, 23);
    public static EnumArmorMaterial EnumArmorMaterialWooden = EnumHelper.addArmorMaterial("Wooden", 10, new int[]{1, 4, 2, 3}, 23);
    public static EnumArmorMaterial EnumArmorMaterialWolf = EnumHelper.addArmorMaterial("Wolf", 8, new int[]{1, 3, 2, 1}, 15);
    public static EnumArmorMaterial EnumArmorMaterialJaffarrol = EnumHelper.addArmorMaterial("Jaffarrol", 29, new int[]{4, 7, 5, 3}, 17);
    public static ItemJaffaPlate itemJaffaPlate;
    public static int itemJaffaPlateID;
    public static EnumToolMaterial EnumToolMaterialJaffas = EnumHelper.addToolMaterial("Jaffa", 2, 400, 6.0F, 6, 15);
    public static EnumToolMaterial EnumToolMaterialCleaver = EnumHelper.addToolMaterial("JaffaCleaver", 2, 75, 2.0F, 0, 15);
    public static ItemJaffaSword itemJaffaSword;
    public static int itemJaffaSwordID;

    public static int renderID;

    private static IGuiHandler guiHandler;

    @Mod.Instance("moen-jaffas")
    public static mod_jaffas instance;

    public static ItemJaffaPainting itemPainting;
    private int itemPaintingID;

    private static IDProvider idProvider = new IDProvider(3600, 24744);

    public static boolean debug;
    public static String jaffasTitle;
    public boolean itemsReady = false;
    public boolean checkUpdates;

    private static int JaffaPaintingEntityID;

    public ItemManager itemManager;
    public ModuleManager moduleManager;
    public Items items;
    boolean forestryDetected;
    public static String textureFile = "/jaffas_01.png";

    public static boolean spawnStonesEnabled = true;
    public static int spawnStoneLittleCD;
    public static int spawnStoneMediumCD;
    public static int spawnStoneBigCD;
    public static boolean spawnStoneMultidimensional;

    public static boolean transferItemsFromCraftingMatrix;
    public static boolean ignoreBuildCraftsTables;

    public boolean IsForestryDetected() {
        return this.forestryDetected;
    }

    public mod_jaffas() {
        this.itemManager = new ItemManager();
        items = new Items();
        items.RegisterItemType(JaffaItemType.basic, ItemJaffaBase.class);
        items.RegisterItemType(JaffaItemType.food, ItemJaffaFood.class);
        items.RegisterItemType(JaffaItemType.tool, ItemJaffaTool.class);
        items.RegisterItemType(JaffaItemType.pack, ItemJaffaPack.class);
        ItemManager.mallets = new JaffaItem[]{JaffaItem.mallet, JaffaItem.malletStone, JaffaItem.malletIron, JaffaItem.malletDiamond};
        ItemManager.malletHeads = new JaffaItem[]{JaffaItem.malletHead, JaffaItem.malletHeadStone, JaffaItem.malletHeadIron, JaffaItem.malletHeadDiamond};
    }

    @SidedProxy(clientSide = "monnef.jaffas.food.client.ClientProxy", serverSide = "monnef.jaffas.food.common.CommonProxy")
    public static CommonProxy proxy;

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

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
            jaffasTitle = config.get(Configuration.CATEGORY_GENERAL, "jaffasTitle", "Jaffas").value;
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

            JaffaPaintingEntityID = idProvider.getEntityIDFromConfig("painting");

            spawnStonesEnabled = config.get(Configuration.CATEGORY_GENERAL, "spawnStonesEnable", true).getBoolean(true);
            spawnStoneLittleCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneLittleCD", 27).getInt();
            spawnStoneMediumCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMediumCD", 17).getInt();
            spawnStoneBigCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneBigCD", 7).getInt();
            spawnStoneMultidimensional = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMultidimensional", false, "Experimental!").getBoolean(false);
            transferItemsFromCraftingMatrix = config.get(Configuration.CATEGORY_GENERAL, "transferItemsFromCraftingMatrix", false, "Experimental, try to transfer items created after craft directly to player (e.g. crumpled paper)?").getBoolean(false);
            ignoreBuildCraftsTables = config.get(Configuration.CATEGORY_GENERAL, "ignoreBuildCraftsTables", true, "BC tables has broken recipes handling - wrong stack size or crash on craft").getBoolean(true);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }

        proxy.registerSounds();
        PowerManager.InitializeFactory(new PowerManagersFactory());
    }

    @Init
    public void load(FMLInitializationEvent event) {
        checkJsoup();
        checkForestry();

        CreativeTab = new JaffaCreativeTab("jaffas");

        MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());

        registerHandlers();

        createBlocks();
        items.CreateItems();
        createJaffaArmorAndSword();
        createPainting();
        registerDuck();
        AchievementsCraftingHandler.init();

        Recipes.install();
        MinecraftForge.EVENT_BUS.register(new CustomDrop());

        itemsReady = true; // needed?

        //creative tab title
        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas", "en_US", "Jaffas and more!");

        GameRegistry.registerPlayerTracker(new PlayerTracker());

        printInitializedMessage();
    }

    private void checkForestry() {
        try {
            Class c = Class.forName("forestry.Forestry");
            this.forestryDetected = true;
        } catch (ClassNotFoundException e) {
            this.forestryDetected = false;
        }

        if (debug) {
            System.out.println("Forestry detected: " + this.forestryDetected);
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
    }

    private void createPainting() {
        itemPainting = new ItemJaffaPainting(this.itemPaintingID);
        LanguageRegistry.addName(itemPainting, "Painting");
        registerEntity(EntityJaffaPainting.class, "jaffaPainting", 160, Integer.MAX_VALUE, false, JaffaPaintingEntityID);
    }

    private void registerDuck() {
        // TODO duck ID
        EntityRegistry.registerGlobalEntityID(EntityDuck.class, "jaffasDuck", ModLoader.getUniqueEntityId(), ColorHelper.getInt(0, 127, 75), ColorHelper.getInt(200, 200, 255));
        LanguageRegistry.instance().addStringLocalization("entity.jaffasDuck.name", "en_US", "Duck");
        EntityRegistry.addSpawn(EntityDuck.class, 100, 2, 5, EnumCreatureType.creature);
    }

    private void createBlocks() {
        blockFridge = new BlockFridge(blockFridgeID);
        GameRegistry.registerBlock(blockFridge, "blockFridge");
        LanguageRegistry.addName(blockFridge, "Fridge");
        GameRegistry.registerTileEntity(TileEntityFridge.class, "Fridge");

        blockJaffaBomb = new JaffaBombBlock(blockJaffaBombID, 35, Material.rock);
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
        GameRegistry.registerBlock(blockBoard, blockBoard.getBlockName());
        LanguageRegistry.addName(blockBoard, "Kitchen Board");
        GameRegistry.registerTileEntity(TileEntityBoard.class, "kitchenBoard");

        blockPizza = new BlockPizza(blockPizzaID, 149, Material.cake);
        GameRegistry.registerBlock(blockPizza, blockPizza.getBlockName());
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
    }

    private void createJaffaArmorAndSword() {
        int armorRender = proxy.addArmor("Jaffa");
        itemJaffaPlate = new ItemJaffaPlate(itemJaffaPlateID, EnumArmorMaterialJaffas, armorRender, ItemJaffaPlate.ArmorType.chest, "/jaffabrn1.png", null);
        itemJaffaPlate.setIconIndex(90);
        LanguageRegistry.addName(itemJaffaPlate, "Jaffa Hoodie");

        itemJaffaSword = new ItemJaffaSword(itemJaffaSwordID, EnumToolMaterialJaffas);
        itemJaffaSword.setItemName("jaffaSword").setIconIndex(88);
    }

    private void printInitializedMessage() {
        System.out.println("Mod 'Jaffas and more!' successfully initialized");
        System.out.println("created by monnef and Tiartyos");
        System.out.println("version: " + Version.Version + " ; http://jaffas.maweb.eu");

        System.out.println("enabled modules: " + Joiner.on(", ").join(moduleManager.CompileEnabledModules()));
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
        if (mod_jaffas.debug) System.out.println("Registered: " + entityClass + " id: " + id);
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
        System.out.println("Module " + module + " from 'Jaffas and more!' initialized.");
    }
}
