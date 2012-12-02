package monnef.jaffas.food;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Side;
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
import monnef.core.IDProvider;
import monnef.core.Version;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
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

    public mod_jaffas() {
        this.itemManager = new ItemManager();
        ItemManager.RegisterItemTypeForModule(ModulesEnum.food, JaffaItemType.basic, ItemJaffaBase.class);
        ItemManager.RegisterItemTypeForModule(ModulesEnum.food, JaffaItemType.food, ItemJaffaFood.class);
        ItemManager.RegisterItemTypeForModule(ModulesEnum.food, JaffaItemType.tool, ItemJaffaTool.class);
        ItemManager.RegisterItemTypeForModule(ModulesEnum.food, JaffaItemType.pack, ItemJaffaPack.class);
        items = new Items();
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

            ItemManager.LoadItemsFromConfig(ModulesEnum.food, idProvider);

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

        registerHandlers();

        createBlocks();
        items.CreateItems();
        createJaffaArmorAndSword();
        createPainting();

        installRecipes();

        itemsReady = true; // needed?

        //creative tab title
        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas", "en_US", "Jaffas and more!");

        printInitializedMessage();
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
        GameRegistry.registerBlock(blockFridge);
        LanguageRegistry.addName(blockFridge, "Fridge");
        GameRegistry.registerTileEntity(TileEntityFridge.class, "Fridge");

        blockJaffaBomb = new JaffaBombBlock(blockJaffaBombID, 35, Material.rock);
        GameRegistry.registerBlock(blockJaffaBomb);
        LanguageRegistry.addName(blockJaffaBomb, "Jaffa Cakes BOMB");

        blockCross = new BlockCross(blockCrossID, 5, Material.rock);
        blockCross.setCreativeTab(CreativeTabs.tabBlock);
        GameRegistry.registerBlock(blockCross);
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

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.pastry)), new ItemStack(Item.sugar),
                new ItemStack(Item.egg), new ItemStack(getItem(JaffaItem.butter)), new ItemStack(getItem(JaffaItem.flour)), new ItemStack(getItem(JaffaItem.flour)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sweetBeans)),
                new ItemStack(getItem(JaffaItem.beans)),
                new ItemStack(Item.sugar));

        GameRegistry.addSmelting(getItem(JaffaItem.sweetBeans).shiftedIndex, new ItemStack(getItem(JaffaItem.chocolate)), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.apples)),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed),
                new ItemStack(Item.appleRed));

        GameRegistry.addShapelessRecipe(new ItemStack(Item.appleRed, 4),
                new ItemStack(getItem(JaffaItem.apples)));

        GameRegistry.addSmelting(getItem(JaffaItem.apples).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamR)), 0.5F);

        /*
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jamO)), "X", "Y", 'X', new ItemStack(Item.dyePowder, 1, 14), 'Y',
                new ItemStack(getItem(JaffaItem.jamR)));
        */

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffa), 12), "X", "Y", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaR), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamR)), 'Z', new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaO), 12),
                "X", "Y", "Z", 'X',
                new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamO)), 'Z',
                new ItemStack(getItem(JaffaItem.cake)));


        GameRegistry.addSmelting(getItem(JaffaItem.pastry).shiftedIndex, new ItemStack(
                getItem(JaffaItem.cake)), 5F);

        AddMalletRecipes();

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHeadDiamond)), "BIS", "IDI", "SIB",
                'B', new ItemStack(Item.slimeBall), 'I', new ItemStack(Item.ingotIron),
                'S', new ItemStack(Item.silk), 'D', new ItemStack(Block.blockDiamond));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHead)), "SP ", "PWP", " P ",
                'S', new ItemStack(Item.silk), 'P', new ItemStack(Block.planks, 1, -1),
                'W', new ItemStack(Block.wood, 1, -1));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHeadStone)), "SC ", "COC", " CS",
                'S', new ItemStack(Item.silk), 'C', new ItemStack(Block.cobblestone),
                'O', new ItemStack(Block.stone));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.malletHeadIron)), "SOS", "OBO", "SOS",
                'S', new ItemStack(Item.silk), 'B', new ItemStack(Block.blockSteel),
                'O', new ItemStack(Block.stone));

        JaffaCraftingHandler.AddPersistentItem(JaffaItem.mallet, true, -1);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.malletStone, true, -1);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.malletIron, true, -1);
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.malletDiamond, true, -1);

        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.beans)), new ItemStack(Item.dyePowder, 1, 3));
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.butter)), new ItemStack(Item.bucketMilk));
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.cakeTin)), new ItemStack(Item.ingotIron));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.brownPastry)), new ItemStack(getItem(JaffaItem.peanut)),
                new ItemStack(getItem(JaffaItem.pastry)), new ItemStack(getItem(JaffaItem.chocolate)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.puffPastry)), new ItemStack(getItem(JaffaItem.butter)),
                new ItemStack(getItem(JaffaItem.butter)), new ItemStack(getItem(JaffaItem.butter)), new ItemStack(Item.egg),
                new ItemStack(getItem(JaffaItem.flour)), new ItemStack(getItem(JaffaItem.flour)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.peanut)), "SSS", 'S', new ItemStack(Item.seeds));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.browniesInTinRaw)), "P", "T", 'P', new ItemStack(getItem(JaffaItem.brownPastry)), 'T', new ItemStack(getItem(JaffaItem.cakeTin)));

        GameRegistry.addSmelting(getItem(JaffaItem.browniesInTinRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.browniesInTin)), 1F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.brownie), 15), "S", "T", 'S', new ItemStack(Item.swordSteel), 'T', new ItemStack(getItem(JaffaItem.browniesInTin)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sweetRollRaw), 10), new ItemStack(getItem(JaffaItem.puffPastry)), new ItemStack(Item.stick));

        GameRegistry.addSmelting(getItem(JaffaItem.sweetRollRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.sweetRoll)), 0.2F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.cream), 4), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.sugar), new ItemStack(Item.bucketMilk));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.creamRoll)), "RC", 'R', new ItemStack(getItem(JaffaItem.sweetRoll)), 'C', new ItemStack(getItem(JaffaItem.cream)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.flour), 3), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat),
                new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.wheat), new ItemStack(Item.paper));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sausageRaw), 5), " F ", "PPP", 'F', new ItemStack(getItem(JaffaItem.flour)), 'P', new ItemStack(Item.porkRaw));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bunRaw), 8), "PP", 'P', new ItemStack(getItem(JaffaItem.pastry)));

        GameRegistry.addSmelting(getItem(JaffaItem.bunRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.bun)), 0.2F);
        GameRegistry.addSmelting(getItem(JaffaItem.sausageRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.sausage)), 0.2F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.hotdog)), "S", "B", 'S', new ItemStack(getItem(JaffaItem.sausage)), 'B', new ItemStack(getItem(JaffaItem.bun)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.chocolateWrapper), 8), "XXX", "XCX", "XXX", 'X', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 5));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.chocolateBar), 2), "C", "C", "W", 'C', new ItemStack(getItem(JaffaItem.chocolate)), 'W', new ItemStack(getItem(JaffaItem.chocolateWrapper)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.wrapperJaffas), 8), "PPP", "PCP", "PPP", 'P', new ItemStack(Item.paper), 'C', new ItemStack(Item.dyePowder, 1, 12));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jaffasPack)), new ItemStack(getItem(JaffaItem.wrapperJaffas)),
                new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)),
                new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)),
                new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)), new ItemStack(getItem(JaffaItem.jaffa)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jaffasPackR)), new ItemStack(getItem(JaffaItem.wrapperJaffas)),
                new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)),
                new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)),
                new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)), new ItemStack(getItem(JaffaItem.jaffaR)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jaffasPackO)), new ItemStack(getItem(JaffaItem.wrapperJaffas)),
                new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)),
                new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)),
                new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)), new ItemStack(getItem(JaffaItem.jaffaO)));

        GameRegistry.addRecipe(new ItemStack(blockJaffaBomb), "J", "R", "T", 'J', new ItemStack(getItem(JaffaItem.jaffasPack)),
                'R', new ItemStack(getItem(JaffaItem.jaffasPackR)), 'T', new ItemStack(Block.tnt));
        GameRegistry.addRecipe(new ItemStack(blockJaffaBomb), "J", "O", "T", 'J', new ItemStack(getItem(JaffaItem.jaffasPack)),
                'O', new ItemStack(getItem(JaffaItem.jaffasPackO)), 'T', new ItemStack(Block.tnt));

        //RecipesFridge.AddRecipe(Block.dirt.blockID, new ItemStack(Block.gravel));

        //GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.vanillaBeans)), new ItemStack(Item.dyePowder, 1, 3), new ItemStack(Item.dyePowder, 1, 11));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.waferIcecream), 40), "PP", "PP", 'P', new ItemStack(getItem(JaffaItem.pastry)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.cone), 30), "P P", " P ", 'P', new ItemStack(getItem(JaffaItem.pastry)));

        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.vanillaPowder)), new ItemStack(getItem(JaffaItem.vanillaBeans)));

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.vanillaIcecreamRaw), 4), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(getItem(JaffaItem.vanillaPowder)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.chocolateIcecreamRaw), 4), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(getItem(JaffaItem.beans)), new ItemStack(Item.snowball));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.icecreamRaw), 4), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(getItem(JaffaItem.cream)), new ItemStack(Item.snowball));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.vanillaIcecream)), "S", "C", 'S', new ItemStack(getItem(JaffaItem.vanillaIcecreamFrozen)), 'C', new ItemStack(getItem(JaffaItem.cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.chocolateIcecream)), "S", "C", 'S', new ItemStack(getItem(JaffaItem.chocolateIcecreamFrozen)), 'C', new ItemStack(getItem(JaffaItem.cone)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.russianIcecream)), "W", "I", "W", 'W', new ItemStack(getItem(JaffaItem.waferIcecream)), 'I', new ItemStack(getItem(JaffaItem.icecreamFrozen)));

        RecipesFridge.AddRecipe(getItem(JaffaItem.icecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.icecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(JaffaItem.vanillaIcecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.vanillaIcecreamFrozen)));
        RecipesFridge.AddRecipe(getItem(JaffaItem.chocolateIcecreamRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.chocolateIcecreamFrozen)));

        GameRegistry.addRecipe(new ItemStack(blockFridge), "GGG", "IMI", "SRS", 'G', new ItemStack(Item.ingotGold), 'I', new ItemStack(Block.blockSteel), 'M', new ItemStack(Block.fenceIron), 'S', new ItemStack(Block.stone), 'R', new ItemStack(Item.redstone));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutRaw)), " P ", "P P", " P ", 'P', new ItemStack(getItem(JaffaItem.pastry)));
        GameRegistry.addSmelting(getItem(JaffaItem.donutRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.donut)), 0.25F);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutChocolate), 8), "C", "D", 'C', new ItemStack(getItem(JaffaItem.chocolate)), 'D', new ItemStack(getItem(JaffaItem.donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutPink), 8), "C", "D", 'C', new ItemStack(getItem(JaffaItem.jamR)), 'D', new ItemStack(getItem(JaffaItem.donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutSugar), 8), "C", "D", 'C', new ItemStack(Item.sugar), 'D', new ItemStack(getItem(JaffaItem.donut)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.donutSprinkled)), "C", "D", 'C', new ItemStack(getItem(JaffaItem.sprinkles)), 'D', new ItemStack(getItem(JaffaItem.donutChocolate)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaL), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamL)), 'Z', new ItemStack(getItem(JaffaItem.cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaP), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamP)), 'Z', new ItemStack(getItem(JaffaItem.cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaV), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamV)), 'Z', new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addSmelting(getItem(JaffaItem.lemons).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamL)), 0.5F);
        GameRegistry.addSmelting(getItem(JaffaItem.oranges).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamO)), 0.5F);
        GameRegistry.addSmelting(getItem(JaffaItem.plums).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamP)), 0.5F);


        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sprinkles), 16), new ItemStack(Item.sugar), new ItemStack(Item.sugar), new ItemStack(Item.sugar),
                new ItemStack(getItem(JaffaItem.jamMix)), new ItemStack(Item.egg));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bagOfSeeds)), "SXS", "SLS", "SSS", 'S', new ItemStack(Item.seeds), 'X', new ItemStack(Item.silk), 'L', new ItemStack(Item.leather));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.magnifier)), "GG ", "GG ", "  I", 'G', new ItemStack(Block.glass), 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.bagOfSeedsIdentified)), new ItemStack(getItem(JaffaItem.magnifier)), new ItemStack(getItem(JaffaItem.bagOfSeeds)));

        GameRegistry.addRecipe(new ItemStack(itemJaffaPlate), "BBB", " J ", " B ", 'B', new ItemStack(Block.cloth, 1, 15), 'J', new ItemStack(getItem(JaffaItem.jaffa)));

        GameRegistry.addSmelting(getItem(JaffaItem.vanillaPowder).shiftedIndex, new ItemStack(getItem(JaffaItem.jamV)), 0.6F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.jamMix), 3),
                new ItemStack(getItem(JaffaItem.jamV)), new ItemStack(getItem(JaffaItem.jamR)),
                new ItemStack(getItem(JaffaItem.jamL)), new ItemStack(getItem(JaffaItem.jamO)),
                new ItemStack(getItem(JaffaItem.jamP))
        );

        GameRegistry.addSmelting(getItem(JaffaItem.raspberries).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamRaspberry)), 0.5F);
        GameRegistry.addSmelting(getItem(JaffaItem.strawberries).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamStrawberry)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaRaspberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamRaspberry)), 'Z', new ItemStack(getItem(JaffaItem.cake)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaStrawberry), 12), "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamStrawberry)), 'Z', new ItemStack(getItem(JaffaItem.cake)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.kettle)), "XS ", " XX", " XX", 'X', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.cupRaw)), "XXX", "XX ", 'X', new ItemStack(Item.clay));
        GameRegistry.addSmelting(getItem(JaffaItem.cupRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.cup)), 3);
        AddMalletShapedRecipe(new ItemStack(getItem(JaffaItem.coffee)), new ItemStack(getItem(JaffaItem.coffeeRoasted)));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.kettleWaterCold)), "W", "K", 'W', new ItemStack(Item.bucketWater), 'K', new ItemStack(getItem(JaffaItem.kettle)));
        GameRegistry.addSmelting(getItem(JaffaItem.kettleWaterCold).shiftedIndex, new ItemStack(getItem(JaffaItem.kettleWaterHot)), 0);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.cupCoffee)), "K", "C", "U",
                'K', new ItemStack(getItem(JaffaItem.kettleWaterHot), 1, -1), 'C', new ItemStack(getItem(JaffaItem.coffee)), 'U', new ItemStack(getItem(JaffaItem.cup)));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.kettleWaterHot, true, JaffaItem.kettle);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.knifeKitchen)), "I  ", " I ", "  S", 'I', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.knifeKitchen, true, -1);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatChopped), 4), "K", "M", 'K', new ItemStack(getItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(Item.porkRaw));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.rollChopped), 1), "K", "M", 'K', new ItemStack(getItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(getItem(JaffaItem.roll)));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.ironSkewer)), "  I", " I ", "I  ", 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.skewerRaw)), new ItemStack(getItem(JaffaItem.ironSkewer)), new ItemStack(getItem(JaffaItem.rollChopped)), new ItemStack(getItem(JaffaItem.meatChopped)));
        GameRegistry.addSmelting(getItem(JaffaItem.skewerRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.skewer)), 2F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.rollRaw), 8), " P", "P ", 'P', new ItemStack(getItem(JaffaItem.pastry)));
        GameRegistry.addSmelting(getItem(JaffaItem.rollRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.roll)), 0.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(JaffaItem.tomatoChopped)));
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getItem(JaffaItem.paprikaChopped)));
        GameRegistry.addSmelting(getItem(JaffaItem.omeletteRaw).shiftedIndex, new ItemStack(getItem(JaffaItem.omelette)), 1.5F);
    }

    private void AddMalletRecipes() {
        for (int i = 0; i < itemManager.mallets.length; i++) {
            GameRegistry.addRecipe(new ItemStack(getItem(itemManager.mallets[i])), "H", "S", "S", 'H', new ItemStack(getItem(itemManager.malletHeads[i])), 'S', Item.stick);
        }
    }

    private void AddMalletShapedRecipe(ItemStack output, ItemStack input) {
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
