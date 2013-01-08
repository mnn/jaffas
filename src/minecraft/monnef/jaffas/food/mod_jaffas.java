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
import monnef.core.IDProvider;
import monnef.core.Version;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;

import java.util.logging.Level;

@Mod(modid = "moen-jaffas", name = "Jaffas", version = Version.Version, dependencies = "after:Forestry;after:BuildCraft|Energy")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"jaffas-01"}, packetHandler = PacketHandler.class)
public class mod_jaffas {
    public static JaffaCreativeTab CreativeTab = new JaffaCreativeTab("jaffas");

    private static MinecraftServer server;


    public static JaffaBombBlock blockJaffaBomb;
    public static int blockJaffaBombID;

    public static BlockFridge blockFridge;
    public static int blockFridgeID;

    public static BlockCross blockCross;
    public static int blockCrossID;

    public static EnumArmorMaterial EnumArmorMaterialJaffas = EnumHelper.addArmorMaterial("JaffaArmor", 10, new int[]{1, 4, 2, 3}, 23);
    public static ItemJaffaPlate itemJaffaPlate;
    public static int itemJaffaPlateID;
    static EnumToolMaterial EnumToolMaterialJaffas = EnumHelper.addToolMaterial("Jaffa", 2, 400, 6.0F, 6, 15);
    static EnumToolMaterial EnumToolMaterialCleaver = EnumHelper.addToolMaterial("JaffaCleaver", 2, 400, 6.0F, 6, 15);
    public static ItemJaffaSword itemJaffaSword;
    public static int itemJaffaSwordID;

    public static int renderID;

    private static IGuiHandler guiHandler;

    @Mod.Instance("moen-jaffas")
    public static mod_jaffas instance;

    public boolean itemsReady = false;
    public boolean checkUpdates;

    public static ItemJaffaPainting itemPainting;
    private int itemPaintingID;

    private static IDProvider idProvider = new IDProvider(3600, 24744);

    public static boolean debug;

    private static int JaffaPaintingEntityID;

    public ItemManager itemManager;
    public ModuleManager moduleManager;
    private Items items;
    boolean forestryDetected;

    public mod_jaffas() {
        this.itemManager = new ItemManager();
        items = new Items();
        items.RegisterItemType(JaffaItemType.basic, ItemJaffaBase.class);
        items.RegisterItemType(JaffaItemType.food, ItemJaffaFood.class);
        items.RegisterItemType(JaffaItemType.tool, ItemJaffaTool.class);
        items.RegisterItemType(JaffaItemType.pack, ItemJaffaPack.class);
        items.InitializeItemInfos();
        ItemManager.mallets = new JaffaItem[]{JaffaItem.mallet, JaffaItem.malletStone, JaffaItem.malletIron, JaffaItem.malletDiamond};
        ItemManager.malletHeads = new JaffaItem[]{JaffaItem.malletHead, JaffaItem.malletHeadStone, JaffaItem.malletHeadIron, JaffaItem.malletHeadDiamond};
    }

    @SidedProxy(clientSide = "monnef.jaffas.food.ClientProxyTutorial", serverSide = "monnef.jaffas.food.CommonProxyTutorial")
    public static CommonProxyTutorial proxy;

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            items.LoadItemsFromConfig(idProvider);

            blockJaffaBombID = idProvider.getBlockIDFromConfig("jaffa bomb");
            blockFridgeID = idProvider.getBlockIDFromConfig("fridge");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            itemJaffaPlateID = idProvider.getItemIDFromConfig("jaffaPlate");
            itemJaffaSwordID = idProvider.getItemIDFromConfig("jaffaSword");

            itemPaintingID = idProvider.getItemIDFromConfig("painting");

            checkUpdates = config.get(Configuration.CATEGORY_GENERAL, "checkUpdates", true).getBoolean(true);

            blockCrossID = idProvider.getBlockIDFromConfig("cross");

            JaffaPaintingEntityID = idProvider.getEntityIDFromConfig("painting");

            this.moduleManager = new ModuleManager();
            ModuleManager.Add(ModulesEnum.food);
            for (ModulesEnum module : ModulesEnum.values()) {
                boolean defaultState = module.getEnabledByDefault();
                boolean enabled = config.get("modules", module.toString(), defaultState).getBoolean(defaultState);
                if (enabled) {
                    moduleManager.Add(module);
                }
            }
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }

        proxy.registerSounds();
    }

    @Init
    public void load(FMLInitializationEvent event) {
        checkJsoup();
        checkForestry();

        registerHandlers();

        createBlocks();
        items.CreateItems();
        createJaffaArmorAndSword();
        createPainting();

        Recipes.install();

        itemsReady = true; // needed?

        //creative tab title
        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas", "en_US", "Jaffas and more!");

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

        proxy.registerRenderThings();
    }

    private void createPainting() {
        itemPainting = new ItemJaffaPainting(this.itemPaintingID);
        LanguageRegistry.addName(itemPainting, "Painting");
        registerEntity(EntityJaffaPainting.class, "jaffaPainting", 160, Integer.MAX_VALUE, false, JaffaPaintingEntityID);
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
        blockCross.setCreativeTab(CreativeTabs.tabBlock);
        GameRegistry.registerBlock(blockCross, "blockCross");
        LanguageRegistry.addName(blockCross, "Cross");
        GameRegistry.registerTileEntity(TileEntityCross.class, "cross");
    }

    private void createJaffaArmorAndSword() {
        int armorRender = proxy.addArmor("Jaffa");
        itemJaffaPlate = new ItemJaffaPlate(itemJaffaPlateID, EnumArmorMaterialJaffas, armorRender, 1);
        itemJaffaPlate.setItemName("JaffaPlate").setIconIndex(90).setCreativeTab(CreativeTab);
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
