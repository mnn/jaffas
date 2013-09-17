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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ClassHelper;
import monnef.core.utils.CustomLogger;
import monnef.jaffas.food.achievement.AchievementsHandler;
import monnef.jaffas.food.block.BlockSwitchgrass;
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
import monnef.jaffas.food.crafting.JaffaCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.food.item.ItemJaffaRecipeTool;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.food.item.common.Items;
import monnef.jaffas.food.network.JaffasPacketHandler;
import monnef.jaffas.food.server.PlayerTracker;
import monnef.jaffas.food.server.ServerTickHandler;
import monnef.jaffas.jaffasMod;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;
import java.util.logging.Level;

import static monnef.jaffas.food.ContentHolder.addDungeonLoot;
import static monnef.jaffas.food.ContentHolder.blockSwitchgrass;
import static monnef.jaffas.food.ContentHolder.createBlocks;
import static monnef.jaffas.food.ContentHolder.createJaffaArmorAndSword;
import static monnef.jaffas.food.ContentHolder.itemJaffaPlateID;
import static monnef.jaffas.food.ContentHolder.itemJaffaSwordID;
import static monnef.jaffas.food.ContentHolder.itemPaintingID;
import static monnef.jaffas.food.ContentHolder.loadBlockIDs;
import static monnef.jaffas.food.ContentHolder.registerCleaverRecords;
import static monnef.jaffas.food.ContentHolder.registerDuckSpawns;

@Mod(modid = Reference.ModId, name = Reference.ModName, version = Reference.Version, dependencies = "after:ThermalExpansion;after:MineFactoryReloaded;after:Forestry;after:BuildCraft|Energy;after:ExtrabiomesXL;required-after:monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {JaffasPacketHandler.CHANNEL_SpawnStone, JaffasPacketHandler.CHANNEL_Generic}, packetHandler = JaffasPacketHandler.class)
public class JaffasFood extends jaffasMod {
    public static final String LAST_VERSION_SHOWN = "lastVersionShown";

    public static final String BETA_WARNING_TEXT = "\u00A7lnot finished!\u00A7r";

    private static MinecraftServer server;

    private static IGuiHandler guiHandler;

    @Mod.Instance("Jaffas")
    public static JaffasFood instance;

    public static boolean debug;
    public static String jaffasTitle;
    public static String jaffaTitle;
    public static boolean showUpdateMessages;
    public static String lastVersionShown;

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

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            showUpdateMessages = config.get(Configuration.CATEGORY_GENERAL, "showUpdateMessages", true).getBoolean(true);
            lastVersionShown = config.get(Configuration.CATEGORY_GENERAL, LAST_VERSION_SHOWN, "").getString();

            loadBlockIDs(idProvider);

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
            FuelHandler.switchgrassBurnValue = config.get(Configuration.CATEGORY_GENERAL, "switchgrassBurnValue", 100).getInt();
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

    @Override
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

    private void printInitializedMessage() {
        Log.printInfo("Mod '" + Reference.ModName + "' successfully initialized");
        Log.printInfo("created by " + monnef.core.Reference.MONNEF + " and " + monnef.core.Reference.TIARTYOS + "");
        Log.printInfo("version: " + Reference.Version + " ; " + monnef.core.Reference.URL_JAFFAS_WIKI);

        Log.printInfo("enabled modules: " + Joiner.on(", ").join(ModuleManager.CompileEnabledModules()));
        Log.printInfo("detected mods: " + Joiner.on(", ").join(otherMods.compileDetectedMods()));
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

    public static void PrintInitialized(ModulesEnum module) {
        Log.printInfo("Module " + module + " initialized.");
    }

    public static Item getItem(JaffaItem item) {
        return ContentHolder.getItem(item);
    }
}
