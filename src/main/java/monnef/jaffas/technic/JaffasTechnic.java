/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.power.WrenchHelper;
import monnef.core.utils.DyeColor;
import monnef.core.utils.DyeHelper;
import monnef.core.utils.EntityHelper;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.RegistryUtils;
import monnef.external.ThermalExpansionCustomHelper;
import monnef.jaffas.JaffasModBase;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.BlockJDirectional;
import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.common.JaffasRegistryHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.GuideBookHelper;
import monnef.jaffas.food.crafting.PersistentItemsCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.server.PlayerTracker;
import monnef.jaffas.power.block.TileGrinder;
import monnef.jaffas.technic.block.BlockCobbleBreaker;
import monnef.jaffas.technic.block.BlockCompostCore;
import monnef.jaffas.technic.block.BlockConstruction;
import monnef.jaffas.technic.block.BlockConstructionDummy;
import monnef.jaffas.technic.block.BlockFermenter;
import monnef.jaffas.technic.block.BlockFungiBox;
import monnef.jaffas.technic.block.BlockHighPlant;
import monnef.jaffas.technic.block.BlockKeg;
import monnef.jaffas.technic.block.BlockLamp;
import monnef.jaffas.technic.block.BlockLampDummy;
import monnef.jaffas.technic.block.BlockOre;
import monnef.jaffas.technic.block.BlockTechnic;
import monnef.jaffas.technic.block.TileCobbleBreaker;
import monnef.jaffas.technic.block.TileCompostCore;
import monnef.jaffas.technic.block.TileConstructionDummy;
import monnef.jaffas.technic.block.TileFermenter;
import monnef.jaffas.technic.block.TileFermenterInventoryRouter;
import monnef.jaffas.technic.block.TileFungiBox;
import monnef.jaffas.technic.block.TileHighPlant;
import monnef.jaffas.technic.block.TileKeg;
import monnef.jaffas.technic.block.redstone.BlockAnalogRepeater;
import monnef.jaffas.technic.block.redstone.BlockMultiLamp;
import monnef.jaffas.technic.block.redstone.BlockRandomizer;
import monnef.jaffas.technic.block.redstone.BlockSampler;
import monnef.jaffas.technic.block.redstone.ItemBlockRedstone;
import monnef.jaffas.technic.block.redstone.TileAnalogRepeater;
import monnef.jaffas.technic.block.redstone.TileRandomizer;
import monnef.jaffas.technic.block.redstone.TileSampler;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.common.CompostRegister;
import monnef.jaffas.technic.common.EnchantRecipe;
import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.MushroomCropProvider;
import monnef.jaffas.technic.common.RepairRecipe;
import monnef.jaffas.technic.entity.EntityCombineHarvester;
import monnef.jaffas.technic.entity.EntityLocomotive;
import monnef.jaffas.technic.item.CentralUnitEnum;
import monnef.jaffas.technic.item.ItemAxeTechnic;
import monnef.jaffas.technic.item.ItemCentralUnit;
import monnef.jaffas.technic.item.ItemCombineHarvester;
import monnef.jaffas.technic.item.ItemCompost;
import monnef.jaffas.technic.item.ItemFermenter;
import monnef.jaffas.technic.item.ItemFungus;
import monnef.jaffas.technic.item.ItemHightPlantPost;
import monnef.jaffas.technic.item.ItemHoeTechnic;
import monnef.jaffas.technic.item.ItemKeg;
import monnef.jaffas.technic.item.ItemLocomotive;
import monnef.jaffas.technic.item.ItemMushroomKnife;
import monnef.jaffas.technic.item.ItemPickaxeTechnic;
import monnef.jaffas.technic.item.ItemSpadeTechnic;
import monnef.jaffas.technic.item.ItemSwordTechnic;
import monnef.jaffas.technic.item.ItemTechnic;
import monnef.jaffas.technic.network.FruitCollectorPacket;
import monnef.jaffas.trees.BushType;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

import static monnef.core.utils.RegistryUtils.registerMultiBlock;
import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.crafting.Recipes.ANY_DMG;
import static monnef.jaffas.food.crafting.Recipes.WOOD_PLANK;
import static monnef.jaffas.food.crafting.Recipes.getItemStack;
import static monnef.jaffas.food.item.JaffaItem._last;
import static monnef.jaffas.food.item.JaffaItem.beerMugEmpty;
import static monnef.jaffas.food.item.JaffaItem.bottleKetchup;
import static monnef.jaffas.food.item.JaffaItem.bottleMustard;
import static monnef.jaffas.food.item.JaffaItem.breadCrumbs;
import static monnef.jaffas.food.item.JaffaItem.bun;
import static monnef.jaffas.food.item.JaffaItem.cakeTin;
import static monnef.jaffas.food.item.JaffaItem.cheeseSlice;
import static monnef.jaffas.food.item.JaffaItem.cookedMushroomsRaw;
import static monnef.jaffas.food.item.JaffaItem.cookingPot;
import static monnef.jaffas.food.item.JaffaItem.fishFillet;
import static monnef.jaffas.food.item.JaffaItem.flyAgaricChopped;
import static monnef.jaffas.food.item.JaffaItem.friedMushrooms;
import static monnef.jaffas.food.item.JaffaItem.friedMushroomsInTinCooked;
import static monnef.jaffas.food.item.JaffaItem.friedMushroomsInTinRaw;
import static monnef.jaffas.food.item.JaffaItem.hamburgerBun;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolBoots;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolChest;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolHelmet;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolLeggins;
import static monnef.jaffas.food.item.JaffaItem.juiceBottle;
import static monnef.jaffas.food.item.JaffaItem.mincedMushrooms;
import static monnef.jaffas.food.item.JaffaItem.onionSliced;
import static monnef.jaffas.food.item.JaffaItem.plate;
import static monnef.jaffas.food.item.JaffaItem.roll;
import static monnef.jaffas.food.item.JaffaItem.sausage;
import static monnef.jaffas.food.item.JaffaItem.shroomburger;
import static monnef.jaffas.food.item.JaffaItem.shroomburgerInBun;
import static monnef.jaffas.food.item.JaffaItem.shroomburgerInBunWithCheese;
import static monnef.jaffas.food.item.JaffaItem.shroomburgerRaw;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneBig;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneLittle;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneMedium;
import static monnef.jaffas.food.item.JaffaItem.wolfSkin;
import static monnef.jaffas.food.item.JaffaItem.woodenBowl;
import static monnef.jaffas.technic.Reference.ModId;
import static monnef.jaffas.technic.Reference.ModName;
import static monnef.jaffas.technic.Reference.Version;
import static monnef.jaffas.technic.common.FungiCatalog.PARASOL_ID;
import static monnef.jaffas.technic.common.FungiCatalog.PORCINO_ID;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas;after:Jaffas-Trees")
public class JaffasTechnic extends JaffasModBase {
    private static final int NUGGETS_COUT_FOR_EACH_INGOT_IN_RECYCLE = 5;
    @Mod.Instance(ModId)
    public static JaffasTechnic instance;

    @SidedProxy(clientSide = "monnef.jaffas.technic.client.ClientProxy", serverSide = "monnef.jaffas.technic.common.CommonProxy")
    public static CommonProxy proxy;

    private static final String FORESTRY_FARM_FUNGAL = "farmShroom";
    public static final String MUSHROOMS_EATABLE = "jaffasMushroomEatable";
    public boolean debug;

    public static ItemTechnic jaffarrol;
    public static ItemTechnic jaffarrolRaw;
    public static ItemTechnic jaffarrolRefined;
    public static ItemTechnic limsew;
    public static ItemTechnic jaffarrolDust;
    public static BlockTechnic blockJaffarrol;
    public static ItemTechnic jaffarrolNugget;
    public static BlockTechnic blockLimsew;
    public static ItemCentralUnit itemCentralUnit;
    public static ItemTechnic funnel;
    public static ItemTechnic itemCasing;
    public static ItemTechnic itemCasingRefined;
    public static BlockOre blockJaffarrolOre;
    public static BlockOre blockLimsewOre;
    public static ItemCompost compost;
    public static ItemCombineHarvester itemCombineHarvester;

    public static ItemAxeTechnic axeJaffarrol;
    public static ItemPickaxeTechnic pickaxeJaffarrol;
    public static ItemSpadeTechnic spadeJaffarrol;
    public static ItemHoeTechnic hoeJaffarrol;
    public static ItemSwordTechnic swordJaffarrol;

    public static boolean generateOres;
    public static boolean disableOreRecipes;
    public static float switchgrassProbability;

    public static ItemLocomotive itemLocomotive;
    public static BlockFungiBox fungiBox;
    public static ItemFungus fungus;
    public static ItemMushroomKnife mushroomKnife;

    public static BlockConstruction constructionBlock;
    public static BlockConstructionDummy dummyConstructionBlock;
    public static BlockCompostCore compostCore;

    public static BlockMultiLamp lamp;
    public static BlockLampDummy lampDummy;
    public static BlockLamp lampDeco;

    public static ItemTechnic rainbowDust;
    public static ItemTechnic gems;

    public static BlockHighPlant highPlant;
    public static ItemHightPlantPost highPlantPost;
    public static ItemTechnic hop;
    public static ItemTechnic processedHop;
    public static ItemTechnic processedHopInBucket;
    public static ItemTechnic brewedHopInBucket;
    public static ItemTechnic hopWeatMixture;
    public static ItemTechnic hopSeeds;
    public static ItemKeg itemKeg;
    public static BlockKeg keg;
    public static ItemFermenter itemFermenter;
    public static BlockFermenter fermenter;

    public static BlockCobbleBreaker cobbleBreaker;

    public static BlockAnalogRepeater repeater;
    public static BlockSampler sampler;
    public static BlockRandomizer randomizer;

    /*
    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    EMERALD(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);
    */
    public static Item.ToolMaterial EnumToolMaterialJaffarrol = EnumHelper.addToolMaterial("Jaffarrol", 3, 1000, 9.0F, 3, 12);

    public static int lampRenderID;
    private boolean disableRedstoneGadgets;
    public static boolean disableLampParticles;

    @Mod.EventHandler
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);

        try {
            config.load();

            disableRedstoneGadgets = config.get(Configuration.CATEGORY_GENERAL, "disableRedstoneGadgets", false).getBoolean(false);

            if (!disableRedstoneGadgets) {
                // multi lamp
                lampDummy = new BlockLampDummy(41);
            }

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            generateOres = config.get(Configuration.CATEGORY_GENERAL, "generateOres", true).getBoolean(true);
            switchgrassProbability = (float) config.get(Configuration.CATEGORY_GENERAL, "switchgrassProbability", 0.005, "Do not go too high, or face stack overflow caused by recursive chunk generation").getDouble(0.005);
            disableOreRecipes = config.get(Configuration.CATEGORY_GENERAL, "disableOreRecipes", true).getBoolean(true);
            if (config.get(Configuration.CATEGORY_GENERAL, "preciseTikcingOfFungiBox", false).getBoolean(false)) {
                TileFungiBox.tickQuantum = 1;
            }
            disableLampParticles = config.get(Configuration.CATEGORY_GENERAL, "disableLampParticles", false).getBoolean(false);
            TileCobbleBreaker.setTimer(config.get(Configuration.CATEGORY_GENERAL, "cobbleBreakerTimer", 12).getInt());

            ItemHoeTechnic.falloutScanAllowed = config.get(Configuration.CATEGORY_GENERAL, "hoeHarvestingFalloutScanAllowed", true).getBoolean(true);
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "Mod Jaffas (technic) can't read config file.");
        } finally {
            config.save();
        }

        if (!ModuleManager.isModuleEnabled(ModulesEnum.technic))
            return;

        creativeTab = new monnef.jaffas.food.common.JaffaCreativeTab("jaffas.technic");

        createItemsAndBlocks();
        createFungiStuff();
        installRecipes();
        addDrops();
        addDungeonLoot();
        registerEntities();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.technic", "en_US", "Jaffas and more! Technic");
        creativeTab.setup(JaffasTechnic.jaffarrolRefined);

        itemCentralUnit.registerNames();

        TechnicWorldGen generator = new TechnicWorldGen();
        GameRegistry.registerWorldGenerator(generator, 100); // not a clue what weight should be

        JaffasFood.packetHandler.manager().registerPacket(2, FruitCollectorPacket.class);

        FMLCommonHandler.instance().bus().register(new PlayerTracker());
    }

    private void registerEntities() {
        EntityHelper.registerModEntity(EntityLocomotive.class, "locomotive", 100, 5, false, this);
        EntityHelper.registerModEntity(EntityCombineHarvester.class, "combineHarvester", 100, 5, false, this);
    }

    @Override
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        super.load(event);
        JaffasFood.printInitialized(ModulesEnum.technic);
    }

    private void addDungeonLoot() {
        if (!ConfigurationManager.dungeonLootEnabled) return;
        ContentHolder.addToDungeons(jaffarrolRaw, 30, 2, 10);
        ContentHolder.addToDungeons(limsew, 10, 2, 5);
        ContentHolder.addToDungeons(hopSeeds, 5, 2, 10);
        if (ModuleManager.isModuleEnabled(ModulesEnum.trees)) {
            ContentHolder.addToDungeons(JaffasTrees.itemUnknownSeeds, 100, 3, 20);
        }
    }

    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent event) {
        Item omniWrench = ItemHelper.findItemByName("omniwrench");
        if (omniWrench != null) {
            Log.printInfo("Omni wrench detected, compatibility with pipe wrench engaged.");
            WrenchHelper.registerWrenchItem(omniWrench);
        }

        CompostRegister.fillWithVanillaItems();
        CompostRegister.fillWithFoodModuleItems();
        handleGuideBook();
        installThermalExpansionSupport();
    }

    private void handleGuideBook() {
        GuideBookHelper.generateGuideBook();
        GameRegistry.addShapelessRecipe(JaffasFood.instance.guideBook.copy(), Items.writable_book, jaffarrolNugget);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        TileKeg.init();
    }

    private void createFungiStuff() {
        fungiBox = new BlockFungiBox(27);
        RegistryUtils.registerBlockWithName(fungiBox, "fungiBox");
        JaffasRegistryHelper.registerTileEntity(TileFungiBox.class, "jaffasFungiBox");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerHarvestable", fungiBox);
        }
        if (otherMods.isForestryDetected()) {
            otherMods.insertFarmable(FORESTRY_FARM_FUNGAL, new MushroomCropProvider());
        }

        fungus = new ItemFungus(99);
        RegistryUtils.registerItem(fungus, "jaffasFungus");
        FungiCatalog.registerShroomGroups();

        mushroomKnife = new ItemMushroomKnife(28);
        RegistryUtils.registerItem(mushroomKnife, "knifeMushroom");
    }

    private void installThermalExpansionSupport() {
        if (JaffasFood.otherMods.isTEDetected()) {
            try {
                ThermalExpansionCustomHelper.init();
                ThermalExpansionCustomHelper.addPulverizerOreToDustRecipe(new ItemStack(blockJaffarrolOre), new ItemStack(jaffarrolDust));
                ThermalExpansionCustomHelper.addPulverizerIngotToDustRecipe(new ItemStack(jaffarrol), new ItemStack(jaffarrolDust));
                ThermalExpansionCustomHelper.addPulverizerIngotToDustRecipe(new ItemStack(jaffarrolRefined), new ItemStack(jaffarrolDust));
                ThermalExpansionCustomHelper.addSmelterDustToIngotsRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrol));
                ThermalExpansionCustomHelper.addSmelterOreToIngotsRecipe(new ItemStack(blockJaffarrolOre), new ItemStack(jaffarrol));
            } catch (Exception e) {
                Log.printSevere("Thermal Expansion integration failed - the API may have changed.");
                e.printStackTrace();
            }
        }
    }

    private void addDrops() {
        CustomDrop.addDrop(EntitySkeleton.class, limsew, 0.05f);
        CustomDrop.addDrop(EntityZombie.class, jaffarrolRaw, 0.01f);
        CustomDrop.addDrop(EntityZombie.class, jaffarrolNugget, 0.15f);
        CustomDrop.addDrop(EntityZombie.class, jaffarrolNugget, 0.15f);
        CustomDrop.addDrop(EntityEnderman.class, limsew, 0.25f);
        CustomDrop.addDrop(EntityEnderman.class, limsew, 0.10f);
        CustomDrop.addDrop(EntityCreeper.class, limsew, 0.05f);
        CustomDrop.addDrop(EntitySlime.class, limsew, 0.25f);
    }

    private void createItemsAndBlocks() {
        jaffarrol = new ItemTechnic(0);
        RegistryUtils.registerItem(jaffarrol, "jaffarrol");

        jaffarrolRaw = new ItemTechnic(1);
        RegistryUtils.registerItem(jaffarrolRaw, "jaffarrolRaw");

        jaffarrolRefined = new ItemTechnic(2);
        RegistryUtils.registerItem(jaffarrolRefined, "jaffarrolRefined");

        limsew = new ItemTechnic(3);
        RegistryUtils.registerItem(limsew, "limsewDust");

        blockJaffarrol = new BlockTechnic(4, Material.iron);
        blockJaffarrol.setBlockName("blockOfJaffarrol").setHardness(6.0F).setResistance(12.0F);
        RegistryUtils.registerBlock(blockJaffarrol);

        blockLimsew = new BlockTechnic(5, Material.iron);
        blockLimsew.setBlockName("blockOfLimsew").setHardness(4f).setResistance(7f);
        RegistryUtils.registerBlock(blockLimsew);

        itemCentralUnit = new ItemCentralUnit(6);
        RegistryUtils.registerItem(itemCentralUnit);

        funnel = new ItemTechnic(16);
        RegistryUtils.registerItem(funnel, "funnel");

        itemCasing = new ItemTechnic(13);
        RegistryUtils.registerItem(itemCasing, "casing");

        itemCasingRefined = new ItemTechnic(14);
        RegistryUtils.registerItem(itemCasingRefined, "casingRefined");

        // jarmor
        int renderIndexJaffarrol = JaffasFood.proxy.addArmor("jarmor");
        JaffasFood.instance.items.createJaffaArmor(jaffarrolHelmet, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.helm, "jaffas_jarmor1.png", _last);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolChest, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.chest, "jaffas_jarmor1.png", _last);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolLeggins, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.leggings, "jaffas_jarmor2.png", _last);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolBoots, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.boots, "jaffas_jarmor1.png", _last);

        blockJaffarrolOre = new BlockOre(17);
        blockJaffarrolOre.setHarvestLevel("pickaxe", 2);
        RegistryUtils.registerBlockWithName(blockJaffarrolOre, "jaffarrolOre");

        blockLimsewOre = new BlockOre(18);
        blockLimsewOre.setHarvestLevel("pickaxe", 2);
        RegistryUtils.registerBlockWithName(blockLimsewOre, "limsewOre");

        itemLocomotive = new ItemLocomotive(24);
        RegistryUtils.registerItem(itemLocomotive);

        jaffarrolDust = new ItemTechnic(25);
        RegistryUtils.registerItem(jaffarrolDust, "jaffarrolDust");

        constructionBlock = new BlockConstruction(30);
        registerMultiBlock(constructionBlock, ItemBlockJaffas.class, new String[]{"alloy", "glass"});

        JaffasRegistryHelper.registerTileEntity(TileConstructionDummy.class, "jaffasConstructionDummy");
        JaffasRegistryHelper.registerTileEntity(TileCompostCore.class, "jaffasCompostCore");

        dummyConstructionBlock = new BlockConstructionDummy(17);
        RegistryUtils.registerBlockWithName(dummyConstructionBlock, "dummyConstruction");

        compostCore = new BlockCompostCore(35, Material.iron);
        RegistryUtils.registerBlockWithName(compostCore, "compostCore");

        compost = new ItemCompost(29);
        RegistryUtils.registerItem(compost, "compost");
        if (JaffasFood.otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerFertilizer", compost);
        }

        if (!disableRedstoneGadgets) {
            lampDeco = new BlockLamp(37, 38);
            registerMultiBlock(lampDeco, ItemBlockJaffas.class, lampDeco.generateSubNames());
            Item.getItemFromBlock(lampDeco).setFull3D();

            lamp = new BlockMultiLamp(37);
            registerRedstoneBlock(lamp, "multiLamp");

            gems = new ItemTechnic(40);
            RegistryUtils.registerItem(gems, "colourfulGems");
            gems.setMaxStackSize(16);

            rainbowDust = new ItemTechnic(39);
            RegistryUtils.registerItem(rainbowDust, "rainbowDust");
            rainbowDust.setInfo("Maybe a key to the secret cow level?");

            repeater = new BlockAnalogRepeater(52, 3);
            registerRedstoneBlock(repeater, "repeater");
            JaffasRegistryHelper.registerTileEntity(TileAnalogRepeater.class, "repeater");

            sampler = new BlockSampler(60, 3);
            registerRedstoneBlock(sampler, "sampler");
            JaffasRegistryHelper.registerTileEntity(TileSampler.class, "sampler");

            randomizer = new BlockRandomizer(63, 3);
            registerRedstoneBlock(randomizer, "randomizer");
            JaffasRegistryHelper.registerTileEntity(TileRandomizer.class, "randomizer");
        }

        highPlant = new BlockHighPlant(44);
        RegistryUtils.registerBlockWithName(highPlant, "highPlant");

        highPlantPost = new ItemHightPlantPost(44);
        RegistryUtils.registerItem(highPlantPost, "highPlantPost");
        JaffasRegistryHelper.registerTileEntity(TileHighPlant.class, "highPlantPost");

        hop = new ItemTechnic(45);
        RegistryUtils.registerItem(hop, "hop");

        processedHop = new ItemTechnic(47);
        RegistryUtils.registerItem(processedHop, "hopProcessed");

        processedHopInBucket = new ItemTechnic(48);
        RegistryUtils.registerItem(processedHopInBucket, "hopProcessedInBucket");
        processedHopInBucket.setContainerItem(Items.bucket);

        brewedHopInBucket = new ItemTechnic(49);
        RegistryUtils.registerItem(brewedHopInBucket, "brewedHopInBucket");
        brewedHopInBucket.setContainerItem(Items.bucket);

        hopSeeds = new ItemTechnic(46);
        RegistryUtils.registerItem(hopSeeds, "hopSeeds");

        hopWeatMixture = new ItemTechnic(50);
        RegistryUtils.registerItem(hopWeatMixture, "hopWeatMixture");

        cobbleBreaker = new BlockCobbleBreaker(50, 2, Material.rock, BlockJDirectional.TextureMappingType.ALL_SIDES);
        RegistryUtils.registerBlockWithName(cobbleBreaker, "cobbleBreaker");
        JaffasRegistryHelper.registerTileEntity(TileCobbleBreaker.class, "cobbleBreaker");

        itemKeg = new ItemKeg(42);
        RegistryUtils.registerItem(itemKeg, "itemKeg");

        keg = new BlockKeg(42);
        RegistryUtils.registerBlockWithName(keg, "keg");
        JaffasRegistryHelper.registerTileEntity(TileKeg.class, "keg");

        itemFermenter = new ItemFermenter(43);
        RegistryUtils.registerItem(itemFermenter, "itemFermenter");

        fermenter = new BlockFermenter(43);
        RegistryUtils.registerBlockWithName(fermenter, "fermenter");
        JaffasRegistryHelper.registerTileEntity(TileFermenter.class, "fermenter");
        JaffasRegistryHelper.registerTileEntity(TileFermenterInventoryRouter.class, "fermenterInvRouter");

        jaffarrolNugget = new ItemTechnic(103);
        RegistryUtils.registerItem(jaffarrolNugget, "jaffarrolNugget");

        createTools();

        itemCombineHarvester = new ItemCombineHarvester(103, 0);
        RegistryUtils.registerItem(itemCombineHarvester, "combineHarvesterGreen");
    }

    private void registerRedstoneBlock(Block block, String name) {
        registerMultiBlock(block, ItemBlockRedstone.class, name);
    }

    private void createTools() {
        EnumToolMaterialJaffarrol.customCraftingMaterial = jaffarrol;

        axeJaffarrol = new ItemAxeTechnic(21, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(axeJaffarrol, "axeJaffarrol");
        axeJaffarrol.setHarvestLevel("axe", 3);

        pickaxeJaffarrol = new ItemPickaxeTechnic(20, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(pickaxeJaffarrol, "pickaxeJaffarrol");
        pickaxeJaffarrol.setHarvestLevel("pickaxe", 3);
        TileCobbleBreaker.registerJaffarrolTool(pickaxeJaffarrol);

        spadeJaffarrol = new ItemSpadeTechnic(19, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(spadeJaffarrol, "spadeJaffarrol");
        spadeJaffarrol.setHarvestLevel("shovel", 3);

        hoeJaffarrol = new ItemHoeTechnic(22, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(hoeJaffarrol, "hoeJaffarrol");

        swordJaffarrol = new ItemSwordTechnic(23, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(swordJaffarrol, "swordJaffarrol");
    }

    private void installRecipes() {
        if (!disableOreRecipes) {
            GameRegistry.addShapelessRecipe(new ItemStack(jaffarrolRaw, 4),
                    new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot),
                    new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot),
                    new ItemStack(Items.redstone));
        }

        GameRegistry.addSmelting(jaffarrolRaw, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(jaffarrol, new ItemStack(jaffarrolRefined), 0);

        GameRegistry.addShapelessRecipe(new ItemStack(blockJaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol),
                new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol),
                new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol));

        GameRegistry.addShapelessRecipe(new ItemStack(blockLimsew), new ItemStack(limsew), new ItemStack(limsew),
                new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew),
                new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew));

        if (!disableOreRecipes) {
            GameRegistry.addShapelessRecipe(new ItemStack(limsew, 2), new ItemStack(Items.diamond), new ItemStack(Items.glowstone_dust),
                    new ItemStack(Items.glowstone_dust), new ItemStack(Items.redstone), new ItemStack(Items.redstone), new ItemStack(Items.redstone),
                    new ItemStack(Items.redstone), new ItemStack(Items.redstone), new ItemStack(Items.redstone));
        }

        GameRegistry.addShapelessRecipe(new ItemStack(limsew, 9), new ItemStack(blockLimsew));
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrol, 9), new ItemStack(blockJaffarrol));

        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 0), "JRJ", "RLR", "JRJ", 'J', jaffarrolNugget, 'R', Items.redstone, 'L', limsew);
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 1), "JRJ", "CLC", "JRJ", 'J', jaffarrolRefined, 'R', Items.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 2), "LJL", "CLC", "LJL", 'J', jaffarrolRefined, 'R', Items.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 1));

        GameRegistry.addRecipe(new ItemStack(funnel), "I I", "J J", " J ", 'I', Items.iron_ingot, 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(itemCasing, 1, 0), "JJJ", "J J", "JJJ", 'J', jaffarrol);
        GameRegistry.addSmelting(itemCasing, new ItemStack(itemCasingRefined), 1f);

        GameRegistry.addRecipe(new ItemStack(ContentHolder.blockFridge), "I&I", "JBJ", "ICI", 'I', Items.iron_ingot,
                '&', itemCasing, 'J', jaffarrol, 'B', Blocks.iron_bars, 'C', createCentralUnitStack(CentralUnitEnum.SIMPLE));
        GameRegistry.addRecipe(new ItemStack(JaffasTrees.blockFruitCollector), "JFJ", "J@J", "JCJ",
                'J', jaffarrol, 'F', funnel, '@', itemCasing, 'C', createCentralUnitStack(CentralUnitEnum.ADVANCED));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.fryingPan)), "  J", "II ", "II ", 'I', Items.iron_ingot, 'J', JaffasTechnic.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatCleaver)), "II", "II", " J", 'I', Items.iron_ingot, 'J', JaffasTechnic.jaffarrol);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sink)), "J", "W", "I", 'J', JaffasTechnic.jaffarrol, 'W', Items.bucket, 'I', Blocks.iron_block);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grater)), " J ", "III", "III", 'J', JaffasTechnic.jaffarrol, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bottleEmpty), 4), " J ", "G G", "GGG", 'J', JaffasTechnic.jaffarrolNugget, 'G', Blocks.glass);

        Recipes.addOreRecipe(new ItemStack(ContentHolder.blockBoard), "  W", "JJ ", "JJ ", 'W', Recipes.WOOD_LOG, 'J', JaffasTechnic.jaffarrol);

        // jarmor
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolHelmet)), "JJJ", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolChest)), "J J", "JJJ", "JJJ", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolBoots)), "J J", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolLeggins)), "JJJ", "J J", "J J", 'J', jaffarrol);

        // spawn stones
        if (ConfigurationManager.spawnStonesEnabled) {
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneLittle)), " I ", "IJI", " I ", 'I', Items.iron_ingot, 'J', blockJaffarrol);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneMedium)), "LGR", "GSG", "RGL", 'G', Items.gold_ingot, 'S', getItem(spawnStoneLittle), 'R', Items.redstone, 'L', limsew);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneBig)), "DLB", "GSL", "BGD", 'L', limsew, 'B', Items.blaze_powder, 'D', Items.diamond, 'G', Items.glowstone_dust, 'S', getItem(spawnStoneMedium));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(juiceBottle), 2), "GJG", "G G", "GGG", 'G', Blocks.glass, 'J', jaffarrolNugget);

        GameRegistry.addSmelting(blockJaffarrolOre, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(blockLimsewOre, new ItemStack(limsew), 1f);

        Recipes.addOreRecipe(swordJaffarrol, "J", "J", "G", 'J', jaffarrol, 'G', Items.gold_ingot);
        Recipes.addOreRecipe(hoeJaffarrol, "JJ", " S", " S", 'J', jaffarrol, 'S', Recipes.WOOD_STICK);
        Recipes.addOreRecipe(spadeJaffarrol, "J", "S", "S", 'J', jaffarrol, 'S', Recipes.WOOD_STICK);
        Recipes.addOreRecipe(axeJaffarrol, "JJ ", "JS ", " S ", 'J', jaffarrol, 'S', Recipes.WOOD_STICK);

        Recipes.addOreRecipe(ItemHelper.constructDamagedItemStack(pickaxeJaffarrol, .25f), "JJJ", " S ", " S ", 'J', jaffarrol, 'S', Recipes.WOOD_STICK);
        Recipes.addOreRecipe(ItemHelper.constructDamagedItemStack(pickaxeJaffarrol, .5f), "JJJ", "DS ", " S ", 'J', jaffarrol, 'S', Recipes.WOOD_STICK, 'D', limsew);
        Recipes.addOreRecipe(pickaxeJaffarrol, "JJJ", "DSD", " S ", 'J', jaffarrol, 'S', Recipes.WOOD_STICK, 'D', limsew);

        GameRegistry.addRecipe(getItemStack(cookingPot), "I I", "I I", "IJI", 'I', Items.iron_ingot, 'J', jaffarrol);

        OreDictionary.registerOre("oreJaffarrol", blockJaffarrolOre);
        OreDictionary.registerOre("oreLimsew", blockLimsewOre);

        GameRegistry.addRecipe(new ItemStack(itemLocomotive), "I F", "BCB", "III", 'F', funnel, 'I', Items.iron_ingot, 'B', Blocks.iron_block, 'C', itemCasingRefined);

        RecipeSorter.register(Reference.ModId + ":repair", RepairRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register(Reference.ModId + ":enchant", EnchantRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        GameRegistry.addRecipe(new RepairRecipe(2, 1, new ItemStack(JaffasTechnic.swordJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(3, 1, new ItemStack(JaffasTechnic.axeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(2, 1, new ItemStack(JaffasTechnic.hoeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(3, 1, new ItemStack(JaffasTechnic.pickaxeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(1, 1, new ItemStack(JaffasTechnic.spadeJaffarrol), 333));

        GameRegistry.addRecipe(new RepairRecipe(7, 1, getItemStack(jaffarrolLeggins), 250));
        GameRegistry.addRecipe(new RepairRecipe(5, 1, getItemStack(jaffarrolHelmet), 250));
        GameRegistry.addRecipe(new RepairRecipe(4, 1, getItemStack(jaffarrolBoots), 250));
        GameRegistry.addRecipe(new RepairRecipe(7, 1, getItemStack(jaffarrolChest), 250));

        addEnchantRecipe(swordJaffarrol, Items.golden_sword);
        addEnchantRecipe(axeJaffarrol, Items.golden_axe);
        addEnchantRecipe(hoeJaffarrol, Items.golden_hoe);
        addEnchantRecipe(pickaxeJaffarrol, Items.golden_pickaxe);
        addEnchantRecipe(spadeJaffarrol, Items.golden_shovel);

        addEnchantRecipe(getItem(jaffarrolLeggins), Items.golden_leggings);
        addEnchantRecipe(getItem(jaffarrolHelmet), Items.golden_helmet);
        addEnchantRecipe(getItem(jaffarrolBoots), Items.golden_boots);
        addEnchantRecipe(getItem(jaffarrolChest), Items.golden_chestplate);

        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(blockJaffarrolOre));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrol));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrolRaw));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrolRefined));
        GameRegistry.addSmelting(jaffarrolDust, new ItemStack(jaffarrol), 0);

        GameRegistry.addRecipe(new ItemStack(constructionBlock, 8, 0), "JIJ", "IJI", "JIJ", 'J', jaffarrol, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(constructionBlock, 8, 1), "JIJ", "I I", "JIJ", 'J', jaffarrol, 'I', Blocks.glass);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mushroomKnife), " J", "W ", 'J', jaffarrol, 'W', "plankWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fungiBox), " W ", "PDP", "PPP", 'W', Items.water_bucket, 'D', Blocks.dirt, 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(compostCore), "JPJ", "PCP", "JPJ", 'J', jaffarrol, 'P', "plankWood", 'C', createCentralUnitStack(CentralUnitEnum.NORMAL)));

        // temporal recipes
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.brown_mushroom),
                new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.brown_mushroom),
                new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID));

        Recipes.addRecipe(new ShapelessOreRecipe(getItem(cookedMushroomsRaw), getItem(woodenBowl), MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE));
        Recipes.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(cookedMushroomsRaw), 3), getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl), MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE));

        Recipes.addRecipe(new ShapedOreRecipe(getItem(friedMushroomsInTinRaw), "MSM", "PPP", " T ", 'M', MUSHROOMS_EATABLE, 'S', getItem(sausage), 'P', JaffasTrees.getFruit(BushType.Pea), 'T', getItem(cakeTin)));
        GameRegistry.addSmelting(getItem(friedMushroomsInTinRaw), getItemStack(friedMushroomsInTinCooked), 2f);
        PersistentItemsCraftingHandler.AddPersistentItem(friedMushroomsInTinCooked, false, cakeTin);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(friedMushrooms), 3), getItem(plate), getItem(plate), getItem(plate), getItem(friedMushroomsInTinCooked));
        //Recipes.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(mincedMushrooms), 2), MUSHROOMS_EATABLE, getItem(grinderMeat)));
        TileGrinder.addOreDictRecipe(MUSHROOMS_EATABLE, new ItemStack(getItem(mincedMushrooms), 2), 120);
        GameRegistry.addRecipe(getItemStack(shroomburgerRaw), "PPP", "PPP", 'P', getItem(mincedMushrooms));
        Recipes.addFryingPanRecipe(shroomburgerRaw, JaffaItem.fryingPanShroomburgerRaw, JaffaItem.fryingPanShroomburger, shroomburger);
        GameRegistry.addShapelessRecipe(getItemStack(shroomburgerInBun, 5), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(shroomburger), getItem(onionSliced));
        GameRegistry.addShapelessRecipe(getItemStack(shroomburgerInBunWithCheese, 6), getItem(bottleKetchup), getItem(bottleMustard), getItem(hamburgerBun), getItem(shroomburger), getItem(onionSliced), getItem(cheeseSlice));

        RecipesBoard.addRecipe(new ItemStack(JaffasTechnic.fungus, 1, FungiCatalog.FLYAGARIC_ID), getItemStack(flyAgaricChopped, 1));
        GameRegistry.addShapelessRecipe(DyeHelper.getDye(DyeColor.RED), new ItemStack(fungus, 1, FungiCatalog.FLYAGARIC_ID), new ItemStack(fungus, 1, FungiCatalog.FLYAGARIC_ID));

        if (!disableRedstoneGadgets) {
            GameRegistry.addShapelessRecipe(new ItemStack(rainbowDust, 4),
                    DyeHelper.getDye(DyeColor.RED),
                    DyeHelper.getDye(DyeColor.YELLOW),
                    DyeHelper.getDye(DyeColor.BLUE),
                    DyeHelper.getDye(DyeColor.GREEN),
                    DyeHelper.getDye(DyeColor.BLACK),
                    DyeHelper.getDye(DyeColor.WHITE),
                    DyeHelper.getDye(DyeColor.BROWN),
                    JaffasTechnic.limsew,
                    JaffasTechnic.jaffarrolDust
            );
            GameRegistry.addSmelting(rainbowDust, new ItemStack(gems), 7f);

            GameRegistry.addShapedRecipe(new ItemStack(lamp), "IGI", "RBL", "IRI", 'L', Items.glowstone_dust, 'I', Items.iron_ingot, 'R', Items.redstone, 'G', gems, 'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_GLASSY));

            GameRegistry.addShapedRecipe(new ItemStack(repeater, 2), " R ", "JCJ", 'C', Items.repeater, 'R', Items.redstone, 'J', jaffarrolRefined);
            GameRegistry.addShapedRecipe(new ItemStack(sampler), " R ", "JCJ", 'C', Items.comparator, 'R', Items.quartz, 'J', jaffarrolRefined);

            for (int i = 0; i <= 15; i++) {
                GameRegistry.addShapedRecipe(new ItemStack(lampDeco, 2, i), "IGI", "RBR", "IDI",
                        'I', Items.iron_ingot, 'R', Items.glowstone_dust, 'G', gems, 'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_GLASSY),
                        'D', DyeHelper.getDye(i));
            }

            Recipes.addOreRecipe(randomizer, "SQS", "RJT", "SSS", 'S', Recipes.STONE, 'Q', Items.quartz, 'R', Items.redstone, 'J', jaffarrolRefined, 'T', Blocks.redstone_torch);
        }

        //GameRegistry.addShapelessRecipe(new ItemStack(processedHop), getItem(grinderMeat), hop, Item.wheat);
        TileGrinder.addRecipe(new ItemStack(hopWeatMixture), new ItemStack(processedHop), 400);

        GameRegistry.addShapelessRecipe(new ItemStack(processedHopInBucket), Items.water_bucket, Items.bucket, processedHop, processedHop, processedHop, processedHop, processedHop, processedHop);
        GameRegistry.addSmelting(processedHopInBucket, new ItemStack(brewedHopInBucket), 1f);
        GameRegistry.addShapelessRecipe(new ItemStack(hopSeeds), hop);
        Recipes.addRecipe(new ShapedOreRecipe(itemKeg, "PSP", "PJP", "PSP", 'P', "plankWood", 'S', "slabWood", 'J', jaffarrol));
        GameRegistry.addShapedRecipe(new ItemStack(getItem(beerMugEmpty), 2), "GG ", "GGG", "GG ", 'G', Blocks.glass);

        Recipes.addOreRecipe(cobbleBreaker, "SSS", "JLJ", "TFT", 'S', Recipes.WOOD_STICK, 'J', jaffarrolRefined, 'L', Items.slime_ball, 'T', DyeHelper.getDye(DyeColor.YELLOW), 'F', Blocks.furnace);
        GameRegistry.addShapedRecipe(new ItemStack(itemFermenter), "BFB", "CAC", "BBB",
                'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_ALLOY), 'C', createCentralUnitStack(CentralUnitEnum.SIMPLE),
                'F', funnel, 'A', itemCasingRefined);

        GameRegistry.addShapedRecipe(new ItemStack(Items.shears), " J", "J ", 'J', jaffarrol);
        Recipes.addOreRecipe(highPlantPost, "SSS", " S ", " S ", 'S', Recipes.WOOD_STICK);
        GameRegistry.addShapelessRecipe(new ItemStack(hopWeatMixture), hop, Items.wheat);
        GameRegistry.addShapedRecipe(ItemHelper.constructDamagedItemStack(Items.flint_and_steel, 0.33f), "J ", " F", 'J', jaffarrolRefined, 'F', Items.flint);

        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrolNugget, 9), jaffarrol);
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrol), ItemHelper.constructStackArray(new ItemStack(jaffarrolNugget), 9));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ContentHolder.blockRipeningBox), "SSS", "P P", "NSN", 'S', Recipes.WOOD_SLAB, 'P', Recipes.WOOD_PLANK, 'N', jaffarrolNugget
        ));

        addToolRecycleRecipe(pickaxeJaffarrol, 3);
        addToolRecycleRecipe(hoeJaffarrol, 2);
        addToolRecycleRecipe(axeJaffarrol, 3);
        addToolRecycleRecipe(spadeJaffarrol, 1);
        addToolRecycleRecipe(swordJaffarrol, 2);

        if (ModuleManager.isModuleEnabled(ModulesEnum.trees)) {
            registerPlantingBagRecipes(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE));
            registerPlantingBagRecipes(getItemStack(wolfSkin));
            registerPlantingBagRecipes(new ItemStack(Items.leather));
        }

        TileGrinder.addRecipe(new ItemStack(Items.fish), getItemStack(fishFillet), 400);
        TileGrinder.addRecipe(new ItemStack(Items.bread), getItemStack(breadCrumbs), 1500);
        TileGrinder.addRecipe(getItemStack(roll), getItemStack(breadCrumbs), 400);
        TileGrinder.addRecipe(getItemStack(bun), getItemStack(breadCrumbs), 400);

        for (int i = 0; i <= 15; i++) {
            GameRegistry.addShapedRecipe(new ItemStack(ContentHolder.blockBricks, 16, i),
                    "BNB", "NDN", "BNB",
                    'N', jaffarrolNugget,
                    'B', Blocks.stonebrick,
                    'D', DyeHelper.getDye(i)
            );
            Recipes.addOreRecipe(new ItemStack(ContentHolder.blockStainedPlanks, 16, i),
                    "BNB", "NDN", "BNB",
                    'N', jaffarrolNugget,
                    'B', WOOD_PLANK,
                    'D', DyeHelper.getDye(i)
            );
        }
    }

    private void registerPlantingBagRecipes(ItemStack s) {
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemPlantingBagSmall), " S ", "WDW", "WMW", 'S', Items.string, 'W', s, 'D', JaffasTechnic.limsew, 'M', Items.iron_ingot);
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemPlantingBagMedium), " S ", "WBW", "DWM", 'S', Items.string, 'W', s, 'M', Items.gold_ingot, 'B', JaffasTrees.itemPlantingBagSmall, 'D', JaffasTechnic.limsew);
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemPlantingBagBig), " S ", "WBW", "DMD", 'S', Items.string, 'W', s, 'M', Items.diamond, 'B', JaffasTrees.itemPlantingBagMedium, 'D', JaffasTechnic.limsew);
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemCollectingBag), " S ", "WBW", "WWW", 'S', Items.string, 'W', s, 'B', JaffasTrees.itemPlantingBagSmall);
    }

    private void addToolRecycleRecipe(Item tool, int ingotsUsedForCreation) {
        int nuggetsCount = ingotsUsedForCreation * NUGGETS_COUT_FOR_EACH_INGOT_IN_RECYCLE;
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrolNugget, nuggetsCount), new ItemStack(tool, 1, tool.getMaxDamage()));
    }

    private void addEnchantRecipe(Item toEnchant, Item toDestroy) {
        GameRegistry.addRecipe(new EnchantRecipe(new ItemStack(toEnchant, 1, ANY_DMG), new ItemStack(toDestroy), 1, 4));
    }

    private Item getItem(JaffaItem item) {
        return JaffasFood.getItem(item);
    }

    public static ItemStack createCentralUnitStack(CentralUnitEnum type) {
        return new ItemStack(itemCentralUnit, 1, type.ordinal());
    }
}
