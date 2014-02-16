/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
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
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.CustomLogger;
import monnef.jaffas.JaffasModBase;
import monnef.jaffas.food.achievement.AchievementsHandler;
import monnef.jaffas.food.block.BlockSwitchgrass;
import monnef.jaffas.food.client.GuiHandler;
import monnef.jaffas.food.command.CommandFridgeDebug;
import monnef.jaffas.food.command.CommandJaffaHunger;
import monnef.jaffas.food.command.CommandJaffas;
import monnef.jaffas.food.command.CommandJaffasOP;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.common.FuelHandler;
import monnef.jaffas.food.common.JaffaCreativeTab;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.common.OtherModsHelper;
import monnef.jaffas.food.common.PlateUnequipper;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.food.common.VillagersTradeHandler;
import monnef.jaffas.food.common.VillagersTradeHandlerWrapper;
import monnef.jaffas.food.crafting.LeftoversCraftingHandler;
import monnef.jaffas.food.crafting.PersistentItemsCraftingHandler;
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

import static monnef.jaffas.food.common.ContentHolder.addDungeonLoot;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrass;
import static monnef.jaffas.food.common.ContentHolder.createBlocks;
import static monnef.jaffas.food.common.ContentHolder.createJaffaArmorAndSword;
import static monnef.jaffas.food.common.ContentHolder.itemJaffaPlateID;
import static monnef.jaffas.food.common.ContentHolder.itemJaffaSwordID;
import static monnef.jaffas.food.common.ContentHolder.itemPaintingID;
import static monnef.jaffas.food.common.ContentHolder.loadBlockIDs;
import static monnef.jaffas.food.common.ContentHolder.registerCleaverRecords;
import static monnef.jaffas.food.common.ContentHolder.registerDuckSpawns;

@Mod(modid = Reference.ModId, name = Reference.ModName, version = Reference.Version, dependencies = "after:ThermalExpansion;after:MineFactoryReloaded;after:Forestry;after:BuildCraft|Energy;after:ExtrabiomesXL;required-after:monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {JaffasPacketHandler.CHANNEL_SpawnStone, JaffasPacketHandler.CHANNEL_Generic}, packetHandler = JaffasPacketHandler.class)
public class JaffasFood extends JaffasModBase {
    public static final String LAST_VERSION_SHOWN = "lastVersionShown";

    private static MinecraftServer server;

    private static IGuiHandler guiHandler;

    @Mod.Instance("Jaffas")
    public static JaffasFood instance;

    public static boolean debug;

    public ItemManager itemManager;
    public ModuleManager moduleManager;
    public Items items;
    public static CustomLogger Log = new CustomLogger("Jaffas");

    public static Random rand = new Random();

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
        data.description = "Adding famous Jaffa Cakes, tons of foods, new trees, crops and a lot more into Minecraft (recipes are quite complex).";
    }

    @SidedProxy(clientSide = "monnef.jaffas.food.client.ClientProxy", serverSide = "monnef.jaffas.food.common.CommonProxy")
    public static CommonProxy proxy;

    @Override
    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);
        otherMods = new OtherModsHelper();
        if (otherMods.isIDResolverDetected()) {
            Log.printSevere("ID Resolver detected, do *not* use ID moving or face consequences.");
            Log.printSevere("I warned you.");
        }

        try {
            config.load();
            idProvider.linkWithConfig(config);
            ConfigurationManager.loadSettings(config);

            initializeModuleManager();

            itemJaffaPlateID = idProvider.getItemIDFromConfig("jaffaPlate");
            itemJaffaSwordID = idProvider.getItemIDFromConfig("jaffaSword");

            itemPaintingID = idProvider.getItemIDFromConfig("painting");

            // careful - order is important!
            ConfigurationManager.jaffasTitle = config.get(Configuration.CATEGORY_GENERAL, "jaffasTitle", "Jaffas").getString();
            ConfigurationManager.jaffaTitle = config.get(Configuration.CATEGORY_GENERAL, "jaffaTitle", "Jaffa").getString();
            items.InitializeItemInfos();
            items.LoadItemsFromConfig(idProvider);

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            loadBlockIDs(idProvider);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }

        proxy.registerSounds();
    }

    private void initializeModuleManager() {
        this.moduleManager = new ModuleManager();
        ModuleManager.Add(ModulesEnum.food);
        for (ModulesEnum module : ModulesEnum.values()) {
            boolean defaultState = module.getEnabledByDefault();
            boolean enabled = config.get("modules", module.toString(), defaultState).getBoolean(defaultState);
            if (enabled) {
                ModuleManager.Add(module);
            }
        }
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
    @Mod.EventHandler
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

    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent event) {
        Recipes.postLoadInstallRecipes();
        for (int i = 0; i <= 4; i++) {
            VillagerRegistry.instance().registerVillageTradeHandler(i, new VillagersTradeHandlerWrapper(new VillagersTradeHandler()));
        }
    }

    private void registerHandlers() {
        proxy.registerTickHandler();
        TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);

        guiHandler = new GuiHandler();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);

        GameRegistry.registerCraftingHandler(new PersistentItemsCraftingHandler());
        GameRegistry.registerCraftingHandler(new LeftoversCraftingHandler());

        proxy.registerRenderThings();
        GameRegistry.registerFuelHandler(new FuelHandler());

        if (!ConfigurationManager.disableAutoUnEquip) {
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

    @Mod.EventHandler
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

    public static boolean itemsHaveSameID(JaffaItem item, ItemStack stack) {
        if (stack == null) return false;
        return getItem(item).itemID == stack.itemID;
    }
}
