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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.logging.Level;

@Mod(modid = "moen-jaffas", name = "Jaffas", version = Version.Version, dependencies = "after:Forestry;after:BuildCraft|Energy")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"jaffas-01"}, packetHandler = PacketHandler.class)
public class mod_jaffas {
    public enum ModulesEnum {
        ores(true), xmas(true), trees(true);

        private final boolean enabledByDefault;

        ModulesEnum(boolean enabledByDefault) {
            this.enabledByDefault = enabledByDefault;
        }

        public boolean getEnabledByDefault() {
            return enabledByDefault;
        }
    }

    public static HashSet<ModulesEnum> ModulesEnabled;

    public static Hashtable<JaffaItem, JaffaItemInfo> ItemsInfo;
    private static MinecraftServer server;
    public static JaffaItem[] mallets;
    public static JaffaItem[] malletHeads;

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

    public enum JaffaItem {
        pastry, cake, jamO, jamR, jaffaO, jaffaR, jaffa, chocolate, apples, beans, sweetBeans,
        butter, mallet, malletStone, malletIron, malletDiamond, malletHead, malletHeadStone, malletHeadIron, malletHeadDiamond,
        brownPastry, puffPastry, peanut, cream, sweetRoll, creamRoll, cakeTin, browniesInTin, brownie, sweetRollRaw, browniesInTinRaw,
        bunRaw, bun, sausageRaw, sausage, hotdog, flour, chocolateWrapper, chocolateBar, wrapperJaffas, jaffasPack, jaffasPackO,
        jaffasPackR, vanillaBeans, waferIcecream, cone, vanillaPowder, vanillaIcecreamRaw, chocolateIcecreamRaw, icecreamRaw,
        vanillaIcecream, chocolateIcecream, russianIcecream, vanillaIcecreamFrozen, chocolateIcecreamFrozen, icecreamFrozen,
        donutRaw, donut, donutChocolate, donutPink, donutSugar, donutSprinkled, jaffaV, jaffaL,
        jamP, jamL, jamV, lemons, oranges, plums, jaffaP, sprinkles, bagOfSeeds, bagOfSeedsIdentified, magnifier, jamMix,
        kettle, kettleWaterCold, kettleWaterHot, cup, cupCoffee, cupRaw, omeletteRaw, omelette, tomatoChopped, paprikaChopped,
        grinderMeat, wienerCocktail, jaffaStrawberry, jaffaRaspberry, raspberries, strawberries,
        jamRaspberry, jamStrawberry,
        rollRaw, roll, rollChopped, meatChopped, skewer, ironSkewer, knifeKitchen, coffee, coffeeRoasted, skewerRaw
    }

    private static IDProvider idProvider = new IDProvider(3600, 24744);

    public static boolean debug;

    public static boolean IsModuleEnable(ModulesEnum module) {
        return ModulesEnabled.contains(module);
    }

    private void AddItemInfo(JaffaItem item, String name, int iconIndex, String title) {
        JaffaItemInfo newItem = new JaffaItemInfo(name);
        newItem.setIconIndex(iconIndex);
        newItem.setTitle(title);
        ItemsInfo.put(item, newItem);
    }

    private void InitializeItemInfos() {
        ItemsInfo = new Hashtable<JaffaItem, JaffaItemInfo>();
        AddItemInfo(JaffaItem.pastry, "Pastry", 13, "Pastry");
        AddItemInfo(JaffaItem.cake, "Cake", 1, "Sponge Cake");
        AddItemInfo(JaffaItem.jamO, "Jam Orange", 2, "Orange Jam");
        AddItemInfo(JaffaItem.jamR, "Jam Red", 3, "Apple Jam");
        AddItemInfo(JaffaItem.jaffaO, "Jaffa Orange", 4, "Orange Jaffa Cake");
        AddItemInfo(JaffaItem.jaffaR, "Jaffa Red", 5, "Apple Jaffa Cake");
        AddItemInfo(JaffaItem.jaffa, "Jaffa", 6, "Jaffa Cake");
        AddItemInfo(JaffaItem.chocolate, "Chocolate", 7, "Chocolate");
        AddItemInfo(JaffaItem.apples, "Apples", 10, "Apples");
        AddItemInfo(JaffaItem.beans, "Beans", 8, "Cocoa Powder");
        AddItemInfo(JaffaItem.sweetBeans, "Sweet Beans", 9, "Sweet Cocoa Powder");
        AddItemInfo(JaffaItem.butter, "Butter", 12, "Butter");
        AddItemInfo(JaffaItem.mallet, "Mallet", 23, "Little Wooden Mallet");
        AddItemInfo(JaffaItem.malletStone, "Mallet Stone", 24, "Little Stone Mallet");
        AddItemInfo(JaffaItem.malletIron, "Mallet Iron", 25, "Little Iron Mallet");
        AddItemInfo(JaffaItem.malletDiamond, "Mallet Diamond", 26, "Little Diamond Mallet");
        AddItemInfo(JaffaItem.malletHead, "Mallet Head", 27, "Wooden Mallet Head");
        AddItemInfo(JaffaItem.malletHeadStone, "Mallet Head Stone", 28, "Stone Mallet Head");
        AddItemInfo(JaffaItem.malletHeadIron, "Mallet Head Iron", 29, "Iron Mallet Head");
        AddItemInfo(JaffaItem.malletHeadDiamond, "Mallet Head Diamond", 30, "Diamond Mallet Head");
        AddItemInfo(JaffaItem.brownPastry, "Brown Pastry", 14, "Brown Pastry");
        AddItemInfo(JaffaItem.puffPastry, "Puff Pastry", 15, "Puff Pastry");
        AddItemInfo(JaffaItem.peanut, "Peanut", 16, "Peanut");
        AddItemInfo(JaffaItem.cream, "Cream", 17, "Cream");
        AddItemInfo(JaffaItem.sweetRoll, "Roll", 18, "Roll");
        AddItemInfo(JaffaItem.creamRoll, "Cream Roll", 19, "Cream Roll");
        AddItemInfo(JaffaItem.cakeTin, "Cake Tin", 20, "Cake Tin");
        AddItemInfo(JaffaItem.browniesInTin, "Brownies", 21, "Brownies");
        AddItemInfo(JaffaItem.brownie, "Brownie", 22, "Brownie");
        AddItemInfo(JaffaItem.sweetRollRaw, "Roll Raw", 31, "Raw Roll");
        AddItemInfo(JaffaItem.browniesInTinRaw, "Raw Brownies", 32, "Raw Brownies");
        AddItemInfo(JaffaItem.bunRaw, "Raw Bun", 44, "Raw Bun");
        AddItemInfo(JaffaItem.bun, "Bun", 45, "Bun");
        AddItemInfo(JaffaItem.sausageRaw, "Sausage Raw", 46, "Raw Sausage");
        AddItemInfo(JaffaItem.sausage, "Sausage", 47, "Sausage");
        AddItemInfo(JaffaItem.hotdog, "Hotdog", 48, "Hotdog");
        AddItemInfo(JaffaItem.flour, "Flour", 49, "Flour");
        AddItemInfo(JaffaItem.chocolateWrapper, "Chocolate Wrapper", 33, "Chocolate Wrapper");
        AddItemInfo(JaffaItem.chocolateBar, "Chocolate Bar", 34, "Chocolate Bar");
        AddItemInfo(JaffaItem.wrapperJaffas, "Wrapper Jaffas", 50, "Jaffa Cakes Wrapper");
        AddItemInfo(JaffaItem.jaffasPack, "Jaffa Cakes Pack", 51, "Jaffa Cakes Pack");
        AddItemInfo(JaffaItem.jaffasPackO, "Orange Jaffa Cakes Pack", 51, "Orange Jaffa Cakes Pack");
        AddItemInfo(JaffaItem.jaffasPackR, "Red Jaffa Cakes Pack", 51, "Apple Jaffa Cakes Pack");
        AddItemInfo(JaffaItem.vanillaBeans, "Vanilla Beans", 52, "Vanilla Beans");
        AddItemInfo(JaffaItem.waferIcecream, "Wafer Ice-cream", 53, "Wafer");
        AddItemInfo(JaffaItem.cone, "Icecream Cone", 54, "Cone");
        AddItemInfo(JaffaItem.vanillaPowder, "Vanilla Powder", 55, "Vanilla Powder");
        AddItemInfo(JaffaItem.vanillaIcecreamRaw, "Vanilla Ice-cream Raw", 56, "Vanilla Ice-cream");
        AddItemInfo(JaffaItem.chocolateIcecreamRaw, "Chocolate Ice-cream Raw", 57, "Chocolate Ice-cream");
        AddItemInfo(JaffaItem.icecreamRaw, "Ice-cream Raw", 58, "Ice-cream");
        AddItemInfo(JaffaItem.vanillaIcecream, "Vanilla Scooped Ice-cream", 59, "Scooped Ice-cream");
        AddItemInfo(JaffaItem.chocolateIcecream, "Chocolate Scooped Ice-cream", 60, "Scooped Ice-cream");
        AddItemInfo(JaffaItem.russianIcecream, "Russian Ice-cream", 61, "Russian Ice-cream");
        AddItemInfo(JaffaItem.vanillaIcecreamFrozen, "Vanilla Ice-cream Frozen", 62, "Vanilla Ice-cream *");
        AddItemInfo(JaffaItem.chocolateIcecreamFrozen, "Chocolate Ice-cream Frozen", 63, "Chocolate Ice-cream *");
        AddItemInfo(JaffaItem.icecreamFrozen, "Ice-cream Frozen", 64, "Ice-cream *");
        AddItemInfo(JaffaItem.donutRaw, "Donut Raw", 71, "Raw Donut");
        AddItemInfo(JaffaItem.donut, "Donut", 72, "Donut");
        AddItemInfo(JaffaItem.donutChocolate, "Donut Chocolate", 73, "Chocolate Donut");
        AddItemInfo(JaffaItem.donutPink, "Donut Apple", 74, "Apple Donut");
        AddItemInfo(JaffaItem.donutSugar, "Donut Sugar", 75, "Powdered Donut");
        AddItemInfo(JaffaItem.donutSprinkled, "Donut Sprinkled", 76, "Sprinkled Donut");
        AddItemInfo(JaffaItem.jaffaV, "Jaffa Vanilla", 77, "Vanilla Jaffa Cake");
        AddItemInfo(JaffaItem.jaffaL, "Jaffa Lemon", 78, "Lemon Jaffa Cake");
        AddItemInfo(JaffaItem.jamP, "Jam Plum", 79, "Plum Jam");
        AddItemInfo(JaffaItem.jamL, "Jam Lemon", 80, "Lemon Jam");
        AddItemInfo(JaffaItem.jamV, "Vanilla Jam", 81, "Vanilla Jam");
        AddItemInfo(JaffaItem.lemons, "Lemons", 82, "Lemons");
        AddItemInfo(JaffaItem.oranges, "Oranges", 83, "Oranges");
        AddItemInfo(JaffaItem.plums, "Plums", 84, "Plums");
        AddItemInfo(JaffaItem.sprinkles, "Sprinkles", 87, "Sprinkles");
        AddItemInfo(JaffaItem.bagOfSeeds, "Bag Of Seeds Unidentified", 89, "Bag Of Seeds [Unidentified]");
        AddItemInfo(JaffaItem.bagOfSeedsIdentified, "Bag Of Seeds", 89, "Bag Of Seeds");
        AddItemInfo(JaffaItem.magnifier, "Magnifier", 91, "Magnifier");
        AddItemInfo(JaffaItem.jaffaP, "Jaffa Plum", 86, "Plum Jaffa Cake");
        AddItemInfo(JaffaItem.jamMix, "Jam Mix", 110, "Mix of Jams");

        AddItemInfo(JaffaItem.kettle, "Kettle", 92, "Empty Kettle");
        AddItemInfo(JaffaItem.cup, "Cup", 93, "Cup");
        AddItemInfo(JaffaItem.cupCoffee, "Coffee Cup", 94, "Cup of Coffee");
        AddItemInfo(JaffaItem.cupRaw, "Raw Cup", 109, "Raw Cup");
        AddItemInfo(JaffaItem.kettleWaterCold, "Kettle Cold", 92, "Kettle With Cold Water");
        AddItemInfo(JaffaItem.kettleWaterHot, "Kettle Hot", 92, "Kettle With Hot Water");

        AddItemInfo(JaffaItem.omeletteRaw, "Omelette Raw", 97, "Raw Omelette");
        AddItemInfo(JaffaItem.omelette, "Omelette", 98, "Omelette");
        AddItemInfo(JaffaItem.tomatoChopped, "Tomato Chopped", 99, "Chopped Tomatos");
        AddItemInfo(JaffaItem.paprikaChopped, "Pepper Chopped", 100, "Chopped Peppers");

        AddItemInfo(JaffaItem.grinderMeat, "Meat Grinder", 101, "Meat Grinder");
        AddItemInfo(JaffaItem.wienerCocktail, "Cocktail Wiener", 102, "Cocktail Wiener");
        AddItemInfo(JaffaItem.jaffaStrawberry, "Jaffa Strawberry", 103, "Strawberry Jaffa Cake");
        AddItemInfo(JaffaItem.jaffaRaspberry, "Jaffa Raspberry", 104, "Raspberry Jaffa Cake");
        AddItemInfo(JaffaItem.raspberries, "Raspberries", 105, "Raspberries");
        AddItemInfo(JaffaItem.strawberries, "Strawberries", 106, "Strawberries");
        AddItemInfo(JaffaItem.jamRaspberry, "Jam Raspberry", 107, "Raspberry Jam");
        AddItemInfo(JaffaItem.jamStrawberry, "Jam Strawberry", 108, "Strawberry Jam");

        AddItemInfo(JaffaItem.rollRaw, "Not-Sweet Raw Roll", 111, "Raw Roll");
        AddItemInfo(JaffaItem.roll, "Not-Sweet Roll", 65, "Roll");
        AddItemInfo(JaffaItem.rollChopped, "Chopped Not-Sweet Roll", 66, "Chopped Roll");
        AddItemInfo(JaffaItem.meatChopped, "Chopped Meat", 67, "Chopped Meat");
        AddItemInfo(JaffaItem.skewer, "Skewer", 68, "Skewer");
        AddItemInfo(JaffaItem.ironSkewer, "Iron Skewer", 69, "Iron Skewer");
        AddItemInfo(JaffaItem.knifeKitchen, "Kitchen Knife", 70, "Kitchen Knife");

        AddItemInfo(JaffaItem.coffee, "Coffee", 8, "Coffee");
        AddItemInfo(JaffaItem.coffeeRoasted, "Roasted Coffee", 112, "Roasted Coffee");

        AddItemInfo(JaffaItem.skewerRaw, "Skewer Raw", 85, "Raw Skewer");
    }

    public mod_jaffas() {
        InitializeItemInfos();
        instance = this;
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

            for (JaffaItem item : JaffaItem.values()) {
                JaffaItemInfo info = ItemsInfo.get(item);
                if (info == null) {
                    throw new RuntimeException("got null in item list - " + item);
                }
                String configName = info.getConfigName();

                int id = idProvider.getItemIDFromConfig(configName);

                info.setId(id);
            }

            blockJaffaBombID = idProvider.getBlockIDFromConfig("jaffa bomb");
            blockFridgeID = idProvider.getBlockIDFromConfig("fridge");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            itemJaffaPlateID = idProvider.getItemIDFromConfig("jaffaPlate");
            itemJaffaSwordID = idProvider.getItemIDFromConfig("jaffaSword");

            itemPaintingID = idProvider.getItemIDFromConfig("painting");

            checkUpdates = config.get(Configuration.CATEGORY_GENERAL, "checkUpdates", true).getBoolean(true);

            blockCrossID = idProvider.getBlockIDFromConfig("cross");

            ModulesEnabled = new HashSet<ModulesEnum>();
            for (ModulesEnum module : ModulesEnum.values()) {
                boolean defaultState = module.getEnabledByDefault();
                boolean enabled = config.get("modules", module.toString(), defaultState).getBoolean(defaultState);
                if (enabled) {
                    ModulesEnabled.add(module);
                }
            }
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }

        proxy.registerSounds();
    }

    private void finilizeItemSetup(JaffaItemInfo info, Item item) {
        item.setItemName(info.getTitle());
        item.setIconIndex(info.getIconIndex());
        info.setItem(item);
        LanguageRegistry.addName(item, info.getTitle());
    }

    private ItemBagOfSeeds createBagOfSeed(JaffaItem ji) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemBagOfSeeds newJaffaItem = new ItemBagOfSeeds(info.getId());
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaBase createJaffaItem(JaffaItem ji) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaBase newJaffaItem = new ItemJaffaBase(info.getId());
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaPack createJaffaPack(JaffaItem ji, ItemStack content) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaPack newJaffaItem = new ItemJaffaPack(info.getId(), content);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaFood createJaffaFood(JaffaItem ji, int healAmount, float saturation) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaFood newJaffaItem = new ItemJaffaFood(info.getId(), healAmount, saturation);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaTool createJaffaTool(JaffaItem ji, int usageCount) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaTool newJaffaItem = new ItemJaffaTool(info.getId(), usageCount);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }


    @Init
    public void load(FMLInitializationEvent event) {
        checkJsoup();

        GameRegistry.registerTileEntity(TileEntityCross.class, "cross");

        proxy.registerTickHandler();
        TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);

        blockFridge = new BlockFridge(blockFridgeID);
        GameRegistry.registerBlock(blockFridge);
        LanguageRegistry.addName(blockFridge, "Fridge");
        GameRegistry.registerTileEntity(TileEntityFridge.class, "Fridge");

        guiHandler = new GuiHandler();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);

        blockJaffaBomb = new JaffaBombBlock(blockJaffaBombID, 35, Material.rock);
        GameRegistry.registerBlock(blockJaffaBomb);
        LanguageRegistry.addName(blockJaffaBomb, "Jaffa Cakes BOMB");

        blockCross = new BlockCross(blockCrossID, 5, Material.rock);
        blockCross.setCreativeTab(CreativeTabs.tabBlock);
        GameRegistry.registerBlock(blockCross);
        LanguageRegistry.addName(blockCross, "Cross");

        createItems();

        installRecipes();

        itemsReady = true;

        // texture stuff
        proxy.registerRenderThings();

        GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());

        itemPainting = new ItemJaffaPainting(this.itemPaintingID);
        LanguageRegistry.addName(itemPainting, "Painting");

        // TODO: id from config
        registerEntity(EntityJaffaPainting.class, "jaffaPainting", 160, Integer.MAX_VALUE, false);

        System.out.println("Mod 'Jaffas and more!' successfully initialized");
        System.out.println("created by monnef and Tiartyos");
        System.out.println("version: " + Version.Version + " ; http://jaffas.maweb.eu");

        System.out.println("enabled modules: " + Joiner.on(", ").join(ModulesEnabled));
    }

    private void checkJsoup() {
        try {
            Class c = Class.forName("org.jsoup.Jsoup");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Jsoup library not found, cannot continue.");
        }
    }

    private void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        int id = ModLoader.getUniqueEntityId();
        if (mod_jaffas.debug) System.out.println("Registered: " + entityClass + " id: " + id);
        EntityRegistry.registerGlobalEntityID(entityClass, entityName, id);
        EntityRegistry.registerModEntity(entityClass, entityName, id, this, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    private void createItems() {
        createJaffaItem(JaffaItem.pastry);
        createJaffaItem(JaffaItem.jamO);
        createJaffaItem(JaffaItem.jamR);

        createJaffaFood(JaffaItem.cake, 1, 0.2F);
        createJaffaFood(JaffaItem.jaffaO, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(JaffaItem.jaffaR, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(JaffaItem.jaffa, 2, 0.5F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.2F);

        createJaffaItem(JaffaItem.chocolate);
        createJaffaItem(JaffaItem.apples);
        createJaffaItem(JaffaItem.beans);
        createJaffaItem(JaffaItem.sweetBeans);
        createJaffaItem(JaffaItem.butter);

        createJaffaTool(JaffaItem.mallet, 8);
        createJaffaTool(JaffaItem.malletStone, 16);
        createJaffaTool(JaffaItem.malletIron, 64);
        createJaffaTool(JaffaItem.malletDiamond, 512);

        mallets = new JaffaItem[]{JaffaItem.mallet, JaffaItem.malletStone, JaffaItem.malletIron, JaffaItem.malletDiamond};

        createJaffaItem(JaffaItem.malletHead);
        createJaffaItem(JaffaItem.malletHeadStone);
        createJaffaItem(JaffaItem.malletHeadIron);
        createJaffaItem(JaffaItem.malletHeadDiamond);

        malletHeads = new JaffaItem[]{JaffaItem.malletHead, JaffaItem.malletHeadStone, JaffaItem.malletHeadIron, JaffaItem.malletHeadDiamond};

        createJaffaItem(JaffaItem.brownPastry);
        createJaffaItem(JaffaItem.puffPastry);
        createJaffaItem(JaffaItem.peanut);
        createJaffaItem(JaffaItem.cream);
        createJaffaItem(JaffaItem.sweetRoll);
        createJaffaItem(JaffaItem.cakeTin);
        createJaffaItem(JaffaItem.browniesInTin);

        createJaffaItem(JaffaItem.sweetRollRaw);
        createJaffaItem(JaffaItem.browniesInTinRaw);

        createJaffaFood(JaffaItem.creamRoll, 4, 1F).setPotionEffect(Potion.digSpeed.id, 60, 1, 0.15F);
        createJaffaFood(JaffaItem.brownie, 2, 0.6F).setPotionEffect(Potion.jump.id, 60, 1, 0.15F);

        createJaffaItem(JaffaItem.bunRaw);
        createJaffaItem(JaffaItem.bun);
        createJaffaItem(JaffaItem.sausageRaw);
        createJaffaItem(JaffaItem.sausage);
        createJaffaItem(JaffaItem.flour);

        createJaffaFood(JaffaItem.hotdog, 3, 0.7F).setPotionEffect(Potion.damageBoost.id, 60, 1, 0.15F);

        createJaffaItem(JaffaItem.chocolateWrapper);

        createJaffaFood(JaffaItem.chocolateBar, 1, 0.5F).setPotionEffect(Potion.moveSpeed.id, 60, 1, 0.15F);

        createJaffaItem(JaffaItem.wrapperJaffas);

        createJaffaPack(JaffaItem.jaffasPack, new ItemStack(getItem(JaffaItem.jaffa), 8)).setMaxStackSize(16);
        createJaffaPack(JaffaItem.jaffasPackR, new ItemStack(getItem(JaffaItem.jaffaR), 8)).setMaxStackSize(16);
        createJaffaPack(JaffaItem.jaffasPackO, new ItemStack(getItem(JaffaItem.jaffaO), 8)).setMaxStackSize(16);

        createJaffaItem(JaffaItem.vanillaBeans);
        createJaffaItem(JaffaItem.waferIcecream);
        createJaffaItem(JaffaItem.cone);
        createJaffaItem(JaffaItem.vanillaPowder);
        createJaffaItem(JaffaItem.vanillaIcecreamRaw);
        createJaffaItem(JaffaItem.chocolateIcecreamRaw);
        createJaffaItem(JaffaItem.icecreamRaw);
        createJaffaItem(JaffaItem.vanillaIcecreamFrozen);
        createJaffaItem(JaffaItem.chocolateIcecreamFrozen);
        createJaffaItem(JaffaItem.icecreamFrozen);
        createJaffaFood(JaffaItem.vanillaIcecream, 2, 0.3F).setPotionEffect(Potion.moveSpeed.id, 70, 1, 0.25F);
        createJaffaFood(JaffaItem.chocolateIcecream, 2, 0.3F).setPotionEffect(Potion.moveSpeed.id, 70, 1, 0.25F);
        createJaffaFood(JaffaItem.russianIcecream, 2, 0.3F).setPotionEffect(Potion.moveSpeed.id, 70, 1, 0.25F);

        int armorRender = proxy.addArmor("Jaffa");
        itemJaffaPlate = new ItemJaffaPlate(itemJaffaPlateID, EnumArmorMaterialJaffas, armorRender, 1);
        itemJaffaPlate.setItemName("JaffaPlate").setIconIndex(90).setCreativeTab(CreativeTabs.tabCombat);
        LanguageRegistry.addName(itemJaffaPlate, "Jaffa Hoodie");

        itemJaffaSword = new ItemJaffaSword(itemJaffaSwordID, EnumToolMaterialJaffas);
        itemJaffaSword.setItemName("jaffaSword").setIconIndex(88);

        createJaffaItem(JaffaItem.donutRaw);
        createJaffaItem(JaffaItem.donut);
        createJaffaItem(JaffaItem.jamP);
        createJaffaItem(JaffaItem.jamL);
        createJaffaItem(JaffaItem.jamV);
        createJaffaItem(JaffaItem.lemons);
        createJaffaItem(JaffaItem.oranges);
        createJaffaItem(JaffaItem.plums);
        createJaffaItem(JaffaItem.sprinkles);
        createJaffaItem(JaffaItem.bagOfSeeds);
        //createJaffaItem(JaffaItem.bagOfSeedsIdentified);
        createJaffaItem(JaffaItem.magnifier);

        createJaffaFood(JaffaItem.jaffaP, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(JaffaItem.jaffaV, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(JaffaItem.jaffaL, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);

        createJaffaFood(JaffaItem.donutChocolate, 2, 0.3F).setPotionEffect(Potion.digSpeed.id, 60, 1, 0.15F);
        createJaffaFood(JaffaItem.donutPink, 2, 0.3F).setPotionEffect(Potion.digSpeed.id, 60, 1, 0.15F);
        createJaffaFood(JaffaItem.donutSugar, 2, 0.3F).setPotionEffect(Potion.damageBoost.id, 60, 1, 0.15F);
        createJaffaFood(JaffaItem.donutSprinkled, 2, 0.9F).setPotionEffect(Potion.damageBoost.id, 60, 1, 0.20F);

        createBagOfSeed(JaffaItem.bagOfSeedsIdentified);

        createJaffaItem(JaffaItem.jamMix);

        createJaffaItem(JaffaItem.kettle);
        createJaffaItem(JaffaItem.kettleWaterCold);
        createJaffaItem(JaffaItem.kettleWaterHot).setMaxDamage(5).setMaxStackSize(1);
        createJaffaItem(JaffaItem.cup);
        createJaffaFood(JaffaItem.cupCoffee, 1, 0.2F).
                setReturnItem(new ItemStack(getJaffaItem(JaffaItem.cup))).setIsDrink().
                setPotionEffect(Potion.digSpeed.id, 35, 1, 1F).setAlwaysEdible().setMaxStackSize(16);
        createJaffaItem(JaffaItem.cupRaw);
        createJaffaItem(JaffaItem.omeletteRaw);
        createJaffaFood(JaffaItem.omelette, 3, 0.5F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.2F).setMaxStackSize(16);
        createJaffaItem(JaffaItem.tomatoChopped);
        createJaffaItem(JaffaItem.paprikaChopped);
        createJaffaItem(JaffaItem.grinderMeat);
        createJaffaItem(JaffaItem.wienerCocktail);
        createJaffaItem(JaffaItem.jamRaspberry);
        createJaffaItem(JaffaItem.jamStrawberry);
        createJaffaItem(JaffaItem.raspberries);
        createJaffaItem(JaffaItem.strawberries);
        createJaffaItem(JaffaItem.rollRaw);
        createJaffaItem(JaffaItem.roll);
        createJaffaItem(JaffaItem.rollChopped);
        createJaffaItem(JaffaItem.meatChopped);
        createJaffaItem(JaffaItem.ironSkewer);
        createJaffaFood(JaffaItem.skewer, 4, 0.5F).setReturnItem(new ItemStack(getJaffaItem(JaffaItem.ironSkewer))).setPotionEffect(Potion.jump.id, 60, 1, 0.15F);
        createJaffaItem(JaffaItem.skewerRaw);
        createJaffaItem(JaffaItem.knifeKitchen).setMaxDamage(4096).setMaxStackSize(1);

        createJaffaFood(JaffaItem.jaffaStrawberry, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);
        createJaffaFood(JaffaItem.jaffaRaspberry, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 2, 1, 0.4F);

        createJaffaItem(JaffaItem.coffee);
        createJaffaItem(JaffaItem.coffeeRoasted);
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

        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.kettle)), "XS ", " XX", " XX", 'X', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.cupRaw)), "XXX", "XX ", 'X', new ItemStack(Item.clay));
        GameRegistry.addSmelting(getJaffaItem(JaffaItem.cupRaw).shiftedIndex, new ItemStack(getJaffaItem(JaffaItem.cup)), 3);
        AddMalletShapedRecipe(new ItemStack(getJaffaItem(JaffaItem.coffee)), new ItemStack(getJaffaItem(JaffaItem.coffeeRoasted)));
        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.kettleWaterCold)), "W", "K", 'W', new ItemStack(Item.bucketWater), 'K', new ItemStack(getJaffaItem(JaffaItem.kettle)));
        GameRegistry.addSmelting(getJaffaItem(JaffaItem.kettleWaterCold).shiftedIndex, new ItemStack(getJaffaItem(JaffaItem.kettleWaterHot)), 0);
        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.cupCoffee)), "K", "C", "U",
                'K', new ItemStack(getJaffaItem(JaffaItem.kettleWaterHot), 1, -1), 'C', new ItemStack(getJaffaItem(JaffaItem.coffee)), 'U', new ItemStack(getJaffaItem(JaffaItem.cup)));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.kettleWaterHot, true, JaffaItem.kettle);

        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.knifeKitchen)), "I  ", " I ", "  S", 'I', new ItemStack(Item.ingotIron), 'S', new ItemStack(Item.stick));
        JaffaCraftingHandler.AddPersistentItem(JaffaItem.knifeKitchen, true, -1);
        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.meatChopped), 4), "K", "M", 'K', new ItemStack(getJaffaItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(Item.porkRaw));
        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.rollChopped), 1), "K", "M", 'K', new ItemStack(getJaffaItem(JaffaItem.knifeKitchen), 1, -1), 'M', new ItemStack(getJaffaItem(JaffaItem.roll)));

        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.ironSkewer)), "  I", " I ", "I  ", 'I', new ItemStack(Item.ingotIron));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.skewerRaw)), new ItemStack(getJaffaItem(JaffaItem.ironSkewer)), new ItemStack(getJaffaItem(JaffaItem.rollChopped)), new ItemStack(getJaffaItem(JaffaItem.meatChopped)));
        GameRegistry.addSmelting(getJaffaItem(JaffaItem.skewerRaw).shiftedIndex, new ItemStack(getJaffaItem(JaffaItem.skewer)), 2F);

        GameRegistry.addRecipe(new ItemStack(getJaffaItem(JaffaItem.rollRaw), 8), " P", "P ", 'P', new ItemStack(getJaffaItem(JaffaItem.pastry)));
        GameRegistry.addSmelting(getJaffaItem(JaffaItem.rollRaw).shiftedIndex, new ItemStack(getJaffaItem(JaffaItem.roll)), 0.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getJaffaItem(JaffaItem.tomatoChopped)));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(JaffaItem.omeletteRaw), 3), new ItemStack(Item.egg), new ItemStack(Item.egg), new ItemStack(Item.egg),
                new ItemStack(getJaffaItem(JaffaItem.paprikaChopped)));
        GameRegistry.addSmelting(getJaffaItem(JaffaItem.omeletteRaw).shiftedIndex, new ItemStack(getJaffaItem(JaffaItem.omelette)), 1.5F);
    }

    private void AddMalletRecipes() {
        for (int i = 0; i < mallets.length; i++) {
            GameRegistry.addRecipe(new ItemStack(getItem(mallets[i])), "H", "S", "S", 'H', new ItemStack(getItem(malletHeads[i])), 'S', Item.stick);
        }
    }

    private void AddMalletShapedRecipe(ItemStack output, ItemStack input) {
        for (int i = 0; i < mallets.length; i++) {
            GameRegistry.addRecipe(output, "M", "O", 'M', new ItemStack(getItem(mallets[i]), 1, -1), 'O', input);
        }
    }

    private Item getItem(JaffaItem item) {
        return ItemsInfo.get(item).getItem();
    }

    public static Item getJaffaItem(JaffaItem item) {
        return instance.getItem(item);
    }

    public static void PrintInitialized(ModulesEnum module) {
        System.out.println("Module " + module + " from 'Jaffas and more!' initialized.");
    }
}
