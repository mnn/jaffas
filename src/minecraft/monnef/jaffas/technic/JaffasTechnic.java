/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.DyeColor;
import monnef.core.utils.DyeHelper;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.RegistryUtils;
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
import monnef.jaffas.technic.common.EnchantRecipe;
import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.FungusInfo;
import monnef.jaffas.technic.common.MushroomCropProvider;
import monnef.jaffas.technic.common.RepairRecipe;
import monnef.jaffas.technic.entity.EntityLocomotive;
import monnef.jaffas.technic.item.CentralUnitEnum;
import monnef.jaffas.technic.item.ItemAxeTechnic;
import monnef.jaffas.technic.item.ItemCentralUnit;
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
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import thermalexpansion.api.crafting.CraftingHelpers;

import java.util.Map;
import java.util.logging.Level;

import static monnef.core.utils.RegistryUtils.registerMultiBlock;
import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.crafting.Recipes.ANY_DMG;
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
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class JaffasTechnic extends JaffasModBase {
    private static final int NUGGETS_COUT_FOR_EACH_INGOT_IN_RECYCLE = 5;
    @Mod.Instance(ModId)
    public static JaffasTechnic instance;

    @SidedProxy(clientSide = "monnef.jaffas.technic.client.ClientProxy", serverSide = "monnef.jaffas.technic.common.CommonProxy")
    public static CommonProxy proxy;

    private static final String FORESTRY_FARM_FUNGAL = "farmShroom";
    public static final String MUSHROOMS_EATABLE = "jaffasMushroomEatable";
    public boolean debug;

    private int JaffarrolID;
    public static ItemTechnic jaffarrol;

    private int JaffarrolRawID;
    public static ItemTechnic jaffarrolRaw;

    private int JaffarrolRefinedID;
    public static ItemTechnic jaffarrolRefined;

    private int LimsewID;
    public static ItemTechnic limsew;

    private int jaffarrolDustID;
    public static ItemTechnic jaffarrolDust;

    private int BlockJaffarrolID;
    public static BlockTechnic blockJaffarrol;

    private int jaffarrolNuggetID;
    public static ItemTechnic jaffarrolNugget;

    private int BlockLimsewID;
    public static BlockTechnic blockLimsew;

    private int ItemCentralUnitID;
    public static ItemCentralUnit itemCentralUnit;

    private int FunnelID;
    public static ItemTechnic funnel;

    private int ItemCasingID;
    public static ItemTechnic itemCasing;

    private int ItemCasingRefinedID;
    public static ItemTechnic itemCasingRefined;

    private int BlockJaffarrolOreID;
    public static BlockOre blockJaffarrolOre;

    private int BlockLimsewOreID;
    public static BlockOre blockLimsewOre;

    private int itemCompostID;
    public static ItemCompost compost;

    private int ItemJAxeID;
    private int ItemJSpadeID;
    private int ItemJPickaxeID;
    private int ItemJHoeID;
    private int ItemJSwordID;

    public static ItemAxeTechnic axeJaffarrol;
    public static ItemPickaxeTechnic pickaxeJaffarrol;
    public static ItemSpadeTechnic spadeJaffarrol;
    public static ItemHoeTechnic hoeJaffarrol;
    public static ItemSwordTechnic swordJaffarrol;

    public static boolean generateOres;
    public static boolean disableOreRecipes;
    public static float switchgrassProbability;

    private int LocomotiveEntityID;

    private int ItemLocomotiveID;
    public static ItemLocomotive itemLocomotive;

    private int blockFungiBoxID;
    public static BlockFungiBox fungiBox;

    private int ItemFungusID;
    public static ItemFungus fungus;

    private int ItemMushroomKnifeID;
    public static ItemMushroomKnife mushroomKnife;

    private int blockConstructionID;
    public static BlockConstruction constructionBlock;
    private int blockConstructionDummyID;
    public static BlockConstructionDummy dummyConstructionBlock;
    private int blockCompostCoreID;
    public static BlockCompostCore compostCore;

    private int blockMultiLampID;
    private int blockDecoLampID;
    public static BlockMultiLamp lamp;
    public static BlockLampDummy lampDummy;
    public static BlockLamp lampDeco;

    private int itemRainbowDustID;
    public static ItemTechnic rainbowDust;

    private int itemGemsID;
    public static ItemTechnic gems;

    private int blockHighPlantID;
    public static BlockHighPlant highPlant;

    private int itemHighPlantPostID;
    public static ItemHightPlantPost highPlantPost;

    private int itemHopID;
    public static ItemTechnic hop;

    private int itemProcessedHopID;
    public static ItemTechnic processedHop;

    private int itemProcessedHopInBucketID;
    public static ItemTechnic processedHopInBucket;

    private int itemBrewedHopInBucketID;
    public static ItemTechnic brewedHopInBucket;

    private int itemHopWeatMixtureID;
    public static ItemTechnic hopWeatMixture;

    private int itemCobbleBreakerID;
    public static BlockCobbleBreaker cobbleBreaker;

    private int itemHopSeedsID;
    public static ItemTechnic hopSeeds;

    private int itemKegID;
    public static ItemKeg itemKeg;

    private int blockKegID;
    public static BlockKeg keg;

    private int itemFermenterID;
    public static ItemFermenter itemFermenter;

    private int blockFermenterID;
    public static BlockFermenter fermenter;

    private int blockRepeaterID;
    public static BlockAnalogRepeater repeater;

    private int blockSamplerID;
    public static BlockSampler sampler;

    private int blockRandomizerID;
    public static BlockRandomizer randomizer;

    /*
    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    EMERALD(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);
    */
    public static EnumToolMaterial EnumToolMaterialJaffarrol = EnumHelper.addToolMaterial("Jaffarrol", 3, 1000, 9.0F, 3, 12);

    public static int lampRenderID;
    private boolean disableRedstoneGadgets;
    public static boolean disableLampParticles;

    public static int omniWrenchId;

    @Mod.EventHandler
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);

        try {
            config.load();
            idProvider.linkWithConfig(config);

            JaffarrolID = idProvider.getItemIDFromConfig("jaffarrol");
            JaffarrolRawID = idProvider.getItemIDFromConfig("jaffarrolRaw");
            JaffarrolRefinedID = idProvider.getItemIDFromConfig("jaffarrolRefined");
            LimsewID = idProvider.getItemIDFromConfig("limsew");

            jaffarrolDustID = idProvider.getItemIDFromConfig("jaffarrolDust");

            BlockJaffarrolID = idProvider.getBlockIDFromConfig("jaffarrolBlock");
            BlockLimsewID = idProvider.getBlockIDFromConfig("limsewBlock");

            ItemCentralUnitID = idProvider.getItemIDFromConfig("central unit");

            ItemCasingID = idProvider.getItemIDFromConfig("casing");
            ItemCasingRefinedID = idProvider.getItemIDFromConfig("casingRefined");

            FunnelID = idProvider.getItemIDFromConfig("funnel");

            BlockJaffarrolOreID = idProvider.getBlockIDFromConfig("jaffarrolOre");
            BlockLimsewOreID = idProvider.getBlockIDFromConfig("limsewOre");

            blockFungiBoxID = idProvider.getBlockIDFromConfig("fungiBox");

            ItemJAxeID = idProvider.getItemIDFromConfig("jaffarrolAxe");
            ItemJPickaxeID = idProvider.getItemIDFromConfig("jaffarrolPickaxe");
            ItemJHoeID = idProvider.getItemIDFromConfig("jaffarrolHow");
            ItemJSpadeID = idProvider.getItemIDFromConfig("jaffarrolSpade");
            ItemJSwordID = idProvider.getItemIDFromConfig("jaffarrolSword");
            ItemLocomotiveID = idProvider.getBlockIDFromConfig("locomotive");

            LocomotiveEntityID = idProvider.getEntityIDFromConfig("locomotive");

            ItemFungusID = idProvider.getItemIDFromConfig("fungus");
            ItemMushroomKnifeID = idProvider.getItemIDFromConfig("mushroomKnife");

            blockConstructionID = idProvider.getBlockIDFromConfig("contructionBlock");
            blockConstructionDummyID = idProvider.getBlockIDFromConfig("dummyContructionBlock");
            blockCompostCoreID = idProvider.getBlockIDFromConfig("compostCore");

            disableRedstoneGadgets = config.get(Configuration.CATEGORY_GENERAL, "disableRedstoneGadgets", false).getBoolean(false);

            if (!disableRedstoneGadgets) {
                // multi lamp
                blockMultiLampID = idProvider.getBlockIDFromConfig("multiLamp");
                blockDecoLampID = idProvider.getBlockIDFromConfig("decoLamp");
                int lampDummyId = idProvider.getTempBlockId();
                lampDummy = new BlockLampDummy(lampDummyId, 41);
                idProvider.safelyRemoveTempBlock(lampDummyId, lampDummy);
                itemRainbowDustID = idProvider.getItemIDFromConfig("rainbowDust");
                itemGemsID = idProvider.getItemIDFromConfig("gems");
                blockRepeaterID = idProvider.getBlockIDFromConfig("repeater");
                blockSamplerID = idProvider.getBlockIDFromConfig("sampler");
                blockRandomizerID = idProvider.getBlockIDFromConfig("randomizer");
            }

            itemCompostID = idProvider.getItemIDFromConfig("compost");

            blockHighPlantID = idProvider.getBlockIDFromConfig("highPlant");
            itemHighPlantPostID = idProvider.getItemIDFromConfig("highPlantPost");

            itemHopID = idProvider.getItemIDFromConfig("hop");
            itemProcessedHopID = idProvider.getItemIDFromConfig("processedHop");
            itemProcessedHopInBucketID = idProvider.getItemIDFromConfig("processedHopBucket");
            itemBrewedHopInBucketID = idProvider.getItemIDFromConfig("brewedHopBucket");
            itemHopSeedsID = idProvider.getItemIDFromConfig("hopSeeds");

            itemCobbleBreakerID = idProvider.getBlockIDFromConfig("cobbleBreaker");

            itemKegID = idProvider.getItemIDFromConfig("itemKeg");
            blockKegID = idProvider.getBlockIDFromConfig("keg");

            itemFermenterID = idProvider.getItemIDFromConfig("itemFermenter");
            blockFermenterID = idProvider.getBlockIDFromConfig("fermenter");

            itemHopWeatMixtureID = idProvider.getItemIDFromConfig("hopWheatMixture");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            generateOres = config.get(Configuration.CATEGORY_GENERAL, "generateOres", true).getBoolean(true);
            switchgrassProbability = (float) config.get(Configuration.CATEGORY_GENERAL, "switchgrassProbability", 0.005, "Do not go too high, or face stack overflow caused by recursive chunk generation").getDouble(0.005);
            disableOreRecipes = config.get(Configuration.CATEGORY_GENERAL, "disableOreRecipes", true).getBoolean(true);
            if (config.get(Configuration.CATEGORY_GENERAL, "preciseTikcingOfFungiBox", false).getBoolean(false)) {
                TileFungiBox.tickQuantum = 1;
            }
            disableLampParticles = config.get(Configuration.CATEGORY_GENERAL, "disableLampParticles", false).getBoolean(false);
            TileCobbleBreaker.setTimer(config.get(Configuration.CATEGORY_GENERAL, "cobbleBreakerTimer", 12).getInt());

            jaffarrolNuggetID = idProvider.getItemIDFromConfig("jaffarrolNugget");
            ItemHoeTechnic.falloutScanAllowed = config.get(Configuration.CATEGORY_GENERAL, "hoeHarvestingFalloutScanAllowed", true).getBoolean(true);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (technic) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Override
    protected int getStartOfItemsIdInterval() {
        return 26244;
    }

    @Override
    protected int getStartOfBlocksIdInterval() {
        return 3450;
    }

    @Override
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.isModuleEnabled(ModulesEnum.technic))
            return;

        creativeTab = new monnef.jaffas.food.common.JaffaCreativeTab("jaffas.technic");

        createItemsAndBlocks();
        createFungiStuff();
        installRecipes();
        addDrops();
        addDungeonLoot();

        EntityRegistry.registerModEntity(EntityLocomotive.class, "locomotive", LocomotiveEntityID, this, 100, 5, false);

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.technic", "en_US", "Jaffas and more! Ores");
        creativeTab.setup(JaffasTechnic.jaffarrolRefined);

        itemCentralUnit.registerNames();

        TechnicWorldGen generator = new TechnicWorldGen();
        GameRegistry.registerWorldGenerator(generator);

        installThermalExpansionSupport();

        JaffasFood.PrintInitialized(ModulesEnum.technic);
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
        omniWrenchId = ItemHelper.findItemIdByName("omniwrench");
        if (omniWrenchId != 0) {
            Log.printInfo("Omni wrench detected (" + omniWrenchId + "), compatibility with pipe wrench engaged.");
        }

        GuideBookHelper.generateGuideBook();
        GameRegistry.addShapelessRecipe(JaffasFood.instance.guideBook.copy(), Item.writableBook, jaffarrolNugget);
    }

    private void createFungiStuff() {
        fungiBox = new BlockFungiBox(blockFungiBoxID, 27);
        RegistryUtils.registerBlock(fungiBox, "fungiBox", "Fungi Box");
        JaffasRegistryHelper.registerTileEntity(TileFungiBox.class, "jaffasFungiBox");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.registerHarvestable(fungiBox);
        }
        if (otherMods.isForestryDetected()) {
            otherMods.insertFarmable(FORESTRY_FARM_FUNGAL, new MushroomCropProvider());
        }

        fungus = new ItemFungus(ItemFungusID, 99);
        RegistryUtils.registerItem(fungus, "jaffasFungus", "Fungus");
        for (Map.Entry<Integer, FungusInfo> item : FungiCatalog.catalog.entrySet()) {
            if (item.getValue().ordinalItemBind) {
                LanguageRegistry.addName(new ItemStack(fungus, 1, item.getKey()), item.getValue().title);
            }
        }
        FungiCatalog.registerShroomGroups();

        mushroomKnife = new ItemMushroomKnife(ItemMushroomKnifeID, 28);
        RegistryUtils.registerItem(mushroomKnife, "knifeMushroom", "Mushroom Knife");
    }

    private void installThermalExpansionSupport() {
        if (JaffasFood.otherMods.isTEDetected()) {
            try {
                CraftingHelpers.addPulverizerOreToDustRecipe(new ItemStack(blockJaffarrolOre), new ItemStack(jaffarrolDust));
                CraftingHelpers.addPulverizerIngotToDustRecipe(new ItemStack(jaffarrol), new ItemStack(jaffarrolDust));
                CraftingHelpers.addPulverizerIngotToDustRecipe(new ItemStack(jaffarrolRefined), new ItemStack(jaffarrolDust));
                CraftingHelpers.addSmelterDustToIngotsRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrol));
                CraftingHelpers.addSmelterOreToIngotsRecipe(new ItemStack(blockJaffarrolOre), new ItemStack(jaffarrol));
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
        jaffarrol = new ItemTechnic(JaffarrolID, 0);
        RegistryUtils.registerItem(jaffarrol, "jaffarrol", "Jaffarrol Ingot");

        jaffarrolRaw = new ItemTechnic(JaffarrolRawID, 1);
        RegistryUtils.registerItem(jaffarrolRaw, "jaffarrolRaw", "Raw Jaffarrol");

        jaffarrolRefined = new ItemTechnic(JaffarrolRefinedID, 2);
        jaffarrolRefined.setUnlocalizedName("jaffarrolRefined");
        LanguageRegistry.addName(jaffarrolRefined, "Refined Jaffarrol");

        limsew = new ItemTechnic(LimsewID, 3);
        limsew.setUnlocalizedName("limsewDust");
        LanguageRegistry.addName(limsew, "Limsew Dust");

        blockJaffarrol = new BlockTechnic(BlockJaffarrolID, 4, Material.iron);
        blockJaffarrol.setUnlocalizedName("blockOfJaffarrol").setHardness(6.0F).setResistance(12.0F);
        RegistryUtils.registerBlock(blockJaffarrol);
        LanguageRegistry.addName(blockJaffarrol, "Block of Jaffarrol");

        blockLimsew = new BlockTechnic(BlockLimsewID, 5, Material.iron);
        blockLimsew.setUnlocalizedName("blockOfLimsew").setHardness(4f).setResistance(7f);
        RegistryUtils.registerBlock(blockLimsew);
        LanguageRegistry.addName(blockLimsew, "Block of Limsew");

        itemCentralUnit = new ItemCentralUnit(ItemCentralUnitID, 6);

        funnel = new ItemTechnic(FunnelID, 16);
        funnel.setUnlocalizedName("funnel");
        LanguageRegistry.addName(funnel, "Funnel");

        itemCasing = new ItemTechnic(ItemCasingID, 13);
        itemCasing.setUnlocalizedName("casing");
        LanguageRegistry.addName(itemCasing, "Casing");

        itemCasingRefined = new ItemTechnic(ItemCasingRefinedID, 14);
        itemCasingRefined.setUnlocalizedName("casingRefined");
        LanguageRegistry.addName(itemCasingRefined, "Refined Casing");

        // jarmor
        int renderIndexJaffarrol = JaffasFood.proxy.addArmor("jarmor");
        JaffasFood.instance.items.createJaffaArmor(jaffarrolHelmet, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.helm, "jaffas_jarmor1.png", _last);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolChest, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.chest, "jaffas_jarmor1.png", _last);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolLeggins, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.leggings, "jaffas_jarmor2.png", _last);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolBoots, ContentHolder.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.boots, "jaffas_jarmor1.png", _last);

        blockJaffarrolOre = new BlockOre(BlockJaffarrolOreID, 17);
        MinecraftForge.setBlockHarvestLevel(blockJaffarrolOre, "pickaxe", 2);
        RegistryUtils.registerBlock(blockJaffarrolOre, "jaffarrolOre", "Jaffarrol Ore");

        blockLimsewOre = new BlockOre(BlockLimsewOreID, 18);
        MinecraftForge.setBlockHarvestLevel(blockLimsewOre, "pickaxe", 2);
        RegistryUtils.registerBlock(blockLimsewOre, "limsewOre", "Limsew Ore");

        itemLocomotive = new ItemLocomotive(ItemLocomotiveID, 24);
        LanguageRegistry.addName(itemLocomotive, "Mini-Locomotive");

        jaffarrolDust = new ItemTechnic(jaffarrolDustID, 25);
        RegistryUtils.registerItem(jaffarrolDust, "jaffarrolDust", "Jaffarrol Dust");

        constructionBlock = new BlockConstruction(blockConstructionID, 30);
        registerMultiBlock(constructionBlock, ItemBlockJaffas.class, new String[]{"Construction Block - Alloy", "Construction Block - Alloy-Glass"}, new String[]{"alloy", "glass"});

        JaffasRegistryHelper.registerTileEntity(TileConstructionDummy.class, "jaffasConstructionDummy");
        JaffasRegistryHelper.registerTileEntity(TileCompostCore.class, "jaffasCompostCore");

        dummyConstructionBlock = new BlockConstructionDummy(blockConstructionDummyID, 17);
        RegistryUtils.registerBlock(dummyConstructionBlock, "dummyConstruction", "Dummy Construction Block - are you a cheater?");

        compostCore = new BlockCompostCore(blockCompostCoreID, 35, Material.iron);
        RegistryUtils.registerBlock(compostCore, "compostCore", "Compost Core Block");

        compost = new ItemCompost(itemCompostID, 29);
        RegistryUtils.registerItem(compost, "compost", "Compost");
        if (JaffasFood.otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.registerFertilizer(compost);
        }

        if (!disableRedstoneGadgets) {
            lampDeco = new BlockLamp(blockDecoLampID, 37, 38);
            registerMultiBlock(lampDeco, ItemBlockJaffas.class, lampDeco.generateTitles(), lampDeco.generateSubNames());
            Item.itemsList[lampDeco.blockID].setFull3D();

            lamp = new BlockMultiLamp(blockMultiLampID, 37);
            registerRedstoneBlock(lamp, "multiLamp", "Multi-Lamp");

            gems = new ItemTechnic(itemGemsID, 40);
            RegistryUtils.registerItem(gems, "colourfulGems", "Colourful Gems");
            gems.setMaxStackSize(16);

            rainbowDust = new ItemTechnic(itemRainbowDustID, 39);
            RegistryUtils.registerItem(rainbowDust, "rainbowDust", "Rainbow Dust");
            rainbowDust.setInfo("Maybe a key to the secret cow level?");

            repeater = new BlockAnalogRepeater(blockRepeaterID, 52, 3);
            registerRedstoneBlock(repeater, "repeater", "Analog Repeater");
            JaffasRegistryHelper.registerTileEntity(TileAnalogRepeater.class, "repeater");

            sampler = new BlockSampler(blockSamplerID, 60, 3);
            registerRedstoneBlock(sampler, "sampler", "Sample-and-hold");
            JaffasRegistryHelper.registerTileEntity(TileSampler.class, "sampler");

            randomizer = new BlockRandomizer(blockRandomizerID, 63, 3);
            registerRedstoneBlock(randomizer, "randomizer", "Randomizer");
            JaffasRegistryHelper.registerTileEntity(TileRandomizer.class, "randomizer");
        }

        highPlant = new BlockHighPlant(blockHighPlantID, 44);
        RegistryUtils.registerBlock(highPlant, "highPlant", "High Plant");

        highPlantPost = new ItemHightPlantPost(itemHighPlantPostID, 44);
        RegistryUtils.registerItem(highPlantPost, "highPlantPost", "Plant Post");
        JaffasRegistryHelper.registerTileEntity(TileHighPlant.class, "highPlantPost");

        hop = new ItemTechnic(itemHopID, 45);
        RegistryUtils.registerItem(hop, "hop", "Hop");

        processedHop = new ItemTechnic(itemProcessedHopID, 47);
        RegistryUtils.registerItem(processedHop, "hopProcessed", "Beer Mixture");

        processedHopInBucket = new ItemTechnic(itemProcessedHopInBucketID, 48);
        RegistryUtils.registerItem(processedHopInBucket, "hopProcessedInBucket", "Beer Mixture In Bucket");
        processedHopInBucket.setContainerItem(Item.bucketEmpty);

        brewedHopInBucket = new ItemTechnic(itemBrewedHopInBucketID, 49);
        RegistryUtils.registerItem(brewedHopInBucket, "brewedHopInBucket", "Hopped Wort");
        brewedHopInBucket.setContainerItem(Item.bucketEmpty);

        hopSeeds = new ItemTechnic(itemHopSeedsID, 46);
        RegistryUtils.registerItem(hopSeeds, "hopSeeds", "Hop Seeds");

        hopWeatMixture = new ItemTechnic(itemHopWeatMixtureID, 50);
        RegistryUtils.registerItem(hopWeatMixture, "hopWeatMixture", "Mixture of Hop and Wheat");

        cobbleBreaker = new BlockCobbleBreaker(itemCobbleBreakerID, 50, 2, Material.rock, BlockJDirectional.TextureMappingType.ALL_SIDES);
        RegistryUtils.registerBlock(cobbleBreaker, "cobbleBreaker", "Cobble Breaker");
        JaffasRegistryHelper.registerTileEntity(TileCobbleBreaker.class, "cobbleBreaker");

        itemKeg = new ItemKeg(itemKegID, 42);
        RegistryUtils.registerItem(itemKeg, "itemKeg", "Keg");
        itemKeg.registerTexts();

        keg = new BlockKeg(blockKegID, 42);
        RegistryUtils.registerBlock(keg, "keg", "Keg");
        JaffasRegistryHelper.registerTileEntity(TileKeg.class, "keg");

        itemFermenter = new ItemFermenter(itemFermenterID, 43);
        RegistryUtils.registerItem(itemFermenter, "itemFermenter", "Fermenter");

        fermenter = new BlockFermenter(blockFermenterID, 43);
        RegistryUtils.registerBlock(fermenter, "fermenter", "Fermenter Block");
        JaffasRegistryHelper.registerTileEntity(TileFermenter.class, "fermenter");
        JaffasRegistryHelper.registerTileEntity(TileFermenterInventoryRouter.class, "fermenterInvRouter");

        jaffarrolNugget = new ItemTechnic(jaffarrolNuggetID, 103);
        RegistryUtils.registerItem(jaffarrolNugget, "jaffarrolNugget", "Jaffarrol Nugget");

        createTools();
    }

    private void registerRedstoneBlock(Block block, String name, String title) {
        registerMultiBlock(block, ItemBlockRedstone.class, name, title);
    }

    private void createTools() {
        EnumToolMaterialJaffarrol.customCraftingMaterial = jaffarrol;

        axeJaffarrol = new ItemAxeTechnic(ItemJAxeID, 21, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(axeJaffarrol, "axeJaffarrol", "Jaffarrol Axe");
        MinecraftForge.setToolClass(axeJaffarrol, "axe", 3);

        pickaxeJaffarrol = new ItemPickaxeTechnic(ItemJPickaxeID, 20, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(pickaxeJaffarrol, "pickaxeJaffarrol", "Jaffarrol Pickaxe");
        MinecraftForge.setToolClass(pickaxeJaffarrol, "pickaxe", 3);
        TileCobbleBreaker.registerJaffarrolTool(pickaxeJaffarrol);

        spadeJaffarrol = new ItemSpadeTechnic(ItemJSpadeID, 19, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(spadeJaffarrol, "spadeJaffarrol", "Jaffarrol Shovel");
        MinecraftForge.setToolClass(spadeJaffarrol, "shovel", 3);

        hoeJaffarrol = new ItemHoeTechnic(ItemJHoeID, 22, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(hoeJaffarrol, "hoeJaffarrol", "Jaffarrol Hoe");

        swordJaffarrol = new ItemSwordTechnic(ItemJSwordID, 23, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(swordJaffarrol, "swordJaffarrol", "Jaffarrol Sword");
    }

    private void installRecipes() {
        if (!disableOreRecipes) {
            GameRegistry.addShapelessRecipe(new ItemStack(jaffarrolRaw, 4),
                    new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron),
                    new ItemStack(Item.ingotIron), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotGold),
                    new ItemStack(Item.redstone));
        }

        GameRegistry.addSmelting(jaffarrolRaw.itemID, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(jaffarrol.itemID, new ItemStack(jaffarrolRefined), 0);

        GameRegistry.addShapelessRecipe(new ItemStack(blockJaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol),
                new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol),
                new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol));

        GameRegistry.addShapelessRecipe(new ItemStack(blockLimsew), new ItemStack(limsew), new ItemStack(limsew),
                new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew),
                new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew));

        if (!disableOreRecipes) {
            GameRegistry.addShapelessRecipe(new ItemStack(limsew, 2), new ItemStack(Item.diamond), new ItemStack(Item.glowstone),
                    new ItemStack(Item.glowstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone),
                    new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone));
        }

        GameRegistry.addShapelessRecipe(new ItemStack(limsew, 9), new ItemStack(blockLimsew));
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrol, 9), new ItemStack(blockJaffarrol));

        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 0), "JRJ", "RLR", "JRJ", 'J', jaffarrolNugget, 'R', Item.redstone, 'L', limsew);
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 1), "JRJ", "CLC", "JRJ", 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 2), "LJL", "CLC", "LJL", 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 1));

        GameRegistry.addRecipe(new ItemStack(funnel), "I I", "J J", " J ", 'I', Item.ingotIron, 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(itemCasing, 1, 0), "JJJ", "J J", "JJJ", 'J', jaffarrol);
        GameRegistry.addSmelting(itemCasing.itemID, new ItemStack(itemCasingRefined), 1f);

        GameRegistry.addRecipe(new ItemStack(ContentHolder.blockFridge), "I&I", "JBJ", "ICI", 'I', Item.ingotIron,
                '&', itemCasing, 'J', jaffarrol, 'B', Block.fenceIron, 'C', createCentralUnitStack(CentralUnitEnum.SIMPLE));
        GameRegistry.addRecipe(new ItemStack(JaffasTrees.blockFruitCollector), "JFJ", "J@J", "JCJ",
                'J', jaffarrol, 'F', funnel, '@', itemCasing, 'C', createCentralUnitStack(CentralUnitEnum.ADVANCED));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.fryingPan)), "  J", "II ", "II ", 'I', Item.ingotIron, 'J', JaffasTechnic.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatCleaver)), "II", "II", " J", 'I', Item.ingotIron, 'J', JaffasTechnic.jaffarrol);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sink)), "J", "W", "I", 'J', JaffasTechnic.jaffarrol, 'W', Item.bucketEmpty, 'I', Block.blockIron);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grater)), " J ", "III", "III", 'J', JaffasTechnic.jaffarrol, 'I', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bottleEmpty), 4), " J ", "G G", "GGG", 'J', JaffasTechnic.jaffarrolNugget, 'G', Block.glass);

        GameRegistry.addRecipe(new ItemStack(ContentHolder.blockBoard), "  W", "JJ ", "JJ ", 'W', Block.wood, 'J', JaffasTechnic.jaffarrol);

        // jarmor
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolHelmet)), "JJJ", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolChest)), "J J", "JJJ", "JJJ", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolBoots)), "J J", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolLeggins)), "JJJ", "J J", "J J", 'J', jaffarrol);

        // spawn stones
        if (ConfigurationManager.spawnStonesEnabled) {
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneLittle)), " I ", "IJI", " I ", 'I', Item.ingotIron, 'J', blockJaffarrol);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneMedium)), "LGR", "GSG", "RGL", 'G', Item.ingotGold, 'S', getItem(spawnStoneLittle), 'R', Item.redstone, 'L', limsew);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneBig)), "DLB", "GSL", "BGD", 'L', limsew, 'B', Item.blazePowder, 'D', Item.diamond, 'G', Item.glowstone, 'S', getItem(spawnStoneMedium));
        }

        GameRegistry.addRecipe(new ItemStack(getItem(juiceBottle), 2), "GJG", "G G", "GGG", 'G', Block.glass, 'J', jaffarrolNugget);

        GameRegistry.addSmelting(blockJaffarrolOre.blockID, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(blockLimsewOre.blockID, new ItemStack(limsew), 1f);

        GameRegistry.addRecipe(new ItemStack(swordJaffarrol), "J", "J", "G", 'J', jaffarrol, 'G', Item.ingotGold);
        GameRegistry.addRecipe(new ItemStack(hoeJaffarrol), "JJ", " S", " S", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(spadeJaffarrol), "J", "S", "S", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(axeJaffarrol), "JJ ", "JS ", " S ", 'J', jaffarrol, 'S', Item.stick);

        GameRegistry.addRecipe(ItemHelper.constructDamagedItemStack(pickaxeJaffarrol, .25f), "JJJ", " S ", " S ", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(ItemHelper.constructDamagedItemStack(pickaxeJaffarrol, .5f), "JJJ", "DS ", " S ", 'J', jaffarrol, 'S', Item.stick, 'D', limsew);
        GameRegistry.addRecipe(new ItemStack(pickaxeJaffarrol, 1, 0), "JJJ", "DSD", " S ", 'J', jaffarrol, 'S', Item.stick, 'D', limsew);

        GameRegistry.addRecipe(getItemStack(cookingPot), "I I", "I I", "IJI", 'I', Item.ingotIron, 'J', jaffarrol);

        OreDictionary.registerOre("oreJaffarrol", blockJaffarrolOre);
        OreDictionary.registerOre("oreLimsew", blockLimsewOre);

        GameRegistry.addRecipe(new ItemStack(itemLocomotive), "I F", "BCB", "III", 'F', funnel, 'I', Item.ingotIron, 'B', Block.blockIron, 'C', itemCasingRefined);

        GameRegistry.addRecipe(new RepairRecipe(2, 1, new ItemStack(JaffasTechnic.swordJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(3, 1, new ItemStack(JaffasTechnic.axeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(2, 1, new ItemStack(JaffasTechnic.hoeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(3, 1, new ItemStack(JaffasTechnic.pickaxeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(1, 1, new ItemStack(JaffasTechnic.spadeJaffarrol), 333));

        GameRegistry.addRecipe(new RepairRecipe(7, 1, getItemStack(jaffarrolLeggins), 250));
        GameRegistry.addRecipe(new RepairRecipe(5, 1, getItemStack(jaffarrolHelmet), 250));
        GameRegistry.addRecipe(new RepairRecipe(4, 1, getItemStack(jaffarrolBoots), 250));
        GameRegistry.addRecipe(new RepairRecipe(7, 1, getItemStack(jaffarrolChest), 250));

        addEnchantRecipe(swordJaffarrol, Item.swordGold);
        addEnchantRecipe(axeJaffarrol, Item.axeGold);
        addEnchantRecipe(hoeJaffarrol, Item.hoeGold);
        addEnchantRecipe(pickaxeJaffarrol, Item.pickaxeGold);
        addEnchantRecipe(spadeJaffarrol, Item.shovelGold);

        addEnchantRecipe(getItem(jaffarrolLeggins), Item.legsGold);
        addEnchantRecipe(getItem(jaffarrolHelmet), Item.helmetGold);
        addEnchantRecipe(getItem(jaffarrolBoots), Item.bootsGold);
        addEnchantRecipe(getItem(jaffarrolChest), Item.plateGold);

        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(blockJaffarrolOre));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrol));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrolRaw));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrolRefined));
        GameRegistry.addSmelting(jaffarrolDust.itemID, new ItemStack(jaffarrol), 0);

        GameRegistry.addRecipe(new ItemStack(constructionBlock, 8, 0), "JIJ", "IJI", "JIJ", 'J', jaffarrol, 'I', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(constructionBlock, 8, 1), "JIJ", "I I", "JIJ", 'J', jaffarrol, 'I', Block.glass);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mushroomKnife), " J", "W ", 'J', jaffarrol, 'W', "plankWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fungiBox), " W ", "PDP", "PPP", 'W', Item.bucketWater, 'D', Block.dirt, 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(compostCore), "JPJ", "PCP", "JPJ", 'J', jaffarrol, 'P', "plankWood", 'C', createCentralUnitStack(CentralUnitEnum.NORMAL)));

        // temporal recipes
        GameRegistry.addShapelessRecipe(new ItemStack(Block.mushroomBrown),
                new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.mushroomBrown),
                new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID));

        Recipes.addRecipe(new ShapelessOreRecipe(getItem(cookedMushroomsRaw), getItem(woodenBowl), MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE));
        Recipes.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(cookedMushroomsRaw), 3), getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl), MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE));

        Recipes.addRecipe(new ShapedOreRecipe(getItem(friedMushroomsInTinRaw), "MSM", "PPP", " T ", 'M', MUSHROOMS_EATABLE, 'S', getItem(sausage), 'P', JaffasTrees.getFruit(JaffasTrees.bushType.Pea), 'T', getItem(cakeTin)));
        GameRegistry.addSmelting(getItem(friedMushroomsInTinRaw).itemID, getItemStack(friedMushroomsInTinCooked), 2f);
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
            GameRegistry.addSmelting(rainbowDust.itemID, new ItemStack(gems), 7f);

            GameRegistry.addShapedRecipe(new ItemStack(lamp), "IGI", "RBL", "IRI", 'L', Item.glowstone, 'I', Item.ingotIron, 'R', Item.redstone, 'G', gems, 'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_GLASSY));

            GameRegistry.addShapedRecipe(new ItemStack(repeater, 2), " R ", "JCJ", 'C', Item.redstoneRepeater, 'R', Item.redstone, 'J', jaffarrolRefined);
            GameRegistry.addShapedRecipe(new ItemStack(sampler), " R ", "JCJ", 'C', Item.comparator, 'R', Item.netherQuartz, 'J', jaffarrolRefined);

            for (int i = 0; i <= 15; i++) {
                GameRegistry.addShapedRecipe(new ItemStack(lampDeco, 2, i), "IGI", "RBR", "IDI",
                        'I', Item.ingotIron, 'R', Item.glowstone, 'G', gems, 'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_GLASSY),
                        'D', DyeHelper.getDye(i));
            }

            GameRegistry.addShapedRecipe(new ItemStack(randomizer), "SQS", "RJT", "SSS", 'S', Block.stone, 'Q', Item.netherQuartz, 'R', Item.redstone, 'J', jaffarrolRefined, 'T', Block.torchRedstoneActive);
        }

        //GameRegistry.addShapelessRecipe(new ItemStack(processedHop), getItem(grinderMeat), hop, Item.wheat);
        TileGrinder.addRecipe(new ItemStack(hopWeatMixture), new ItemStack(processedHop), 400);

        GameRegistry.addShapelessRecipe(new ItemStack(processedHopInBucket), Item.bucketWater, Item.bucketEmpty, processedHop, processedHop, processedHop, processedHop, processedHop, processedHop);
        GameRegistry.addSmelting(processedHopInBucket.itemID, new ItemStack(brewedHopInBucket), 1f);
        GameRegistry.addShapelessRecipe(new ItemStack(hopSeeds), hop);
        Recipes.addRecipe(new ShapedOreRecipe(itemKeg, "PSP", "PJP", "PSP", 'P', "plankWood", 'S', "slabWood", 'J', jaffarrol));
        GameRegistry.addShapedRecipe(new ItemStack(getItem(beerMugEmpty), 2), "GG ", "GGG", "GG ", 'G', Block.glass);

        GameRegistry.addShapedRecipe(new ItemStack(cobbleBreaker), "SSS", "JLJ", "TFT", 'S', Item.stick, 'J', jaffarrolRefined, 'L', Item.slimeBall, 'T', DyeHelper.getDye(DyeColor.YELLOW), 'F', Block.furnaceIdle);
        GameRegistry.addShapedRecipe(new ItemStack(itemFermenter), "BFB", "CAC", "BBB",
                'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_ALLOY), 'C', createCentralUnitStack(CentralUnitEnum.SIMPLE),
                'F', funnel, 'A', itemCasingRefined);

        GameRegistry.addShapedRecipe(new ItemStack(Item.shears), " J", "J ", 'J', jaffarrol);
        GameRegistry.addShapedRecipe(new ItemStack(highPlantPost), "SSS", " S ", " S ", 'S', Item.stick);
        GameRegistry.addShapelessRecipe(new ItemStack(hopWeatMixture), hop, Item.wheat);
        GameRegistry.addShapedRecipe(ItemHelper.constructDamagedItemStack(Item.flintAndSteel, 0.33f), "J ", " F", 'J', jaffarrolRefined, 'F', Item.flint);

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
            registerPlantingBagRecipes(new ItemStack(Block.cloth, 1, OreDictionary.WILDCARD_VALUE));
            registerPlantingBagRecipes(getItemStack(wolfSkin));
            registerPlantingBagRecipes(new ItemStack(Item.leather));
        }

        TileGrinder.addRecipe(new ItemStack(Item.fishRaw), getItemStack(fishFillet), 400);
        TileGrinder.addRecipe(new ItemStack(Item.bread), getItemStack(breadCrumbs), 1500);
        TileGrinder.addRecipe(getItemStack(roll), getItemStack(breadCrumbs), 400);
        TileGrinder.addRecipe(getItemStack(bun), getItemStack(breadCrumbs), 400);
    }

    private void registerPlantingBagRecipes(ItemStack s) {
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemPlantingBagSmall), " S ", "WDW", "WMW", 'S', Item.silk, 'W', s, 'D', JaffasTechnic.limsew, 'M', Item.ingotIron);
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemPlantingBagMedium), " S ", "WBW", "DWM", 'S', Item.silk, 'W', s, 'M', Item.ingotGold, 'B', JaffasTrees.itemPlantingBagSmall, 'D', JaffasTechnic.limsew);
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemPlantingBagBig), " S ", "WBW", "DMD", 'S', Item.silk, 'W', s, 'M', Item.diamond, 'B', JaffasTrees.itemPlantingBagMedium, 'D', JaffasTechnic.limsew);
        GameRegistry.addShapedRecipe(new ItemStack(JaffasTrees.itemCollectingBag), " S ", "WBW", "WWW", 'S', Item.silk, 'W', s, 'B', JaffasTrees.itemPlantingBagSmall);
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
