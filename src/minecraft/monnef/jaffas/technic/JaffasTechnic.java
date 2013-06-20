/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.DyeColor;
import monnef.core.utils.DyeHelper;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.BlockJDirectional;
import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.food.common.JaffasRegistryHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.JaffaCraftingHandler;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.jaffasMod;
import monnef.jaffas.technic.block.BlockCobbleBreaker;
import monnef.jaffas.technic.block.BlockCompostCore;
import monnef.jaffas.technic.block.BlockConstruction;
import monnef.jaffas.technic.block.BlockConstructionDummy;
import monnef.jaffas.technic.block.BlockFungiBox;
import monnef.jaffas.technic.block.BlockHighPlant;
import monnef.jaffas.technic.block.BlockKeg;
import monnef.jaffas.technic.block.BlockMultiLamp;
import monnef.jaffas.technic.block.BlockMultiLampDummy;
import monnef.jaffas.technic.block.BlockOre;
import monnef.jaffas.technic.block.BlockTechnic;
import monnef.jaffas.technic.block.TileEntityCobbleBreaker;
import monnef.jaffas.technic.block.TileEntityCompostCore;
import monnef.jaffas.technic.block.TileEntityConstructionDummy;
import monnef.jaffas.technic.block.TileEntityFungiBox;
import monnef.jaffas.technic.block.TileEntityHighPlant;
import monnef.jaffas.technic.block.TileEntityKeg;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.common.EnchantRecipe;
import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.FungusInfo;
import monnef.jaffas.technic.common.MushroomCropProvider;
import monnef.jaffas.technic.common.RepairRecipe;
import monnef.jaffas.technic.entity.EntityLocomotive;
import monnef.jaffas.technic.item.ItemAxeTechnic;
import monnef.jaffas.technic.item.ItemCentralUnit;
import monnef.jaffas.technic.item.ItemCompost;
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
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
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
import powercrystals.minefactoryreloaded.api.FarmingRegistry;
import thermalexpansion.api.crafting.CraftingHelpers;

import java.util.Map;
import java.util.logging.Level;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.crafting.Recipes.getItemStack;
import static monnef.jaffas.food.item.JaffaItem.bottleKetchup;
import static monnef.jaffas.food.item.JaffaItem.bottleMustard;
import static monnef.jaffas.food.item.JaffaItem.cakeTin;
import static monnef.jaffas.food.item.JaffaItem.cheeseSlice;
import static monnef.jaffas.food.item.JaffaItem.cookedMushroomsRaw;
import static monnef.jaffas.food.item.JaffaItem.cookingPot;
import static monnef.jaffas.food.item.JaffaItem.flyAgaricChopped;
import static monnef.jaffas.food.item.JaffaItem.friedMushrooms;
import static monnef.jaffas.food.item.JaffaItem.friedMushroomsInTinCooked;
import static monnef.jaffas.food.item.JaffaItem.friedMushroomsInTinRaw;
import static monnef.jaffas.food.item.JaffaItem.grinderMeat;
import static monnef.jaffas.food.item.JaffaItem.hamburgerBun;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolBoots;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolChest;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolHelmet;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolLeggins;
import static monnef.jaffas.food.item.JaffaItem.juiceBottle;
import static monnef.jaffas.food.item.JaffaItem.mincedMushrooms;
import static monnef.jaffas.food.item.JaffaItem.onionSliced;
import static monnef.jaffas.food.item.JaffaItem.plate;
import static monnef.jaffas.food.item.JaffaItem.sausage;
import static monnef.jaffas.food.item.JaffaItem.shroomburger;
import static monnef.jaffas.food.item.JaffaItem.shroomburgerInBun;
import static monnef.jaffas.food.item.JaffaItem.shroomburgerInBunWithCheese;
import static monnef.jaffas.food.item.JaffaItem.shroomburgerRaw;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneBig;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneLittle;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneMedium;
import static monnef.jaffas.food.item.JaffaItem.woodenBowl;
import static monnef.jaffas.technic.Reference.ModId;
import static monnef.jaffas.technic.Reference.ModName;
import static monnef.jaffas.technic.Reference.Version;
import static monnef.jaffas.technic.common.FungiCatalog.PARASOL_ID;
import static monnef.jaffas.technic.common.FungiCatalog.PORCINO_ID;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas;after:Jaffas-Trees")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class JaffasTechnic extends jaffasMod {
    @Mod.Instance(ModId)
    public static JaffasTechnic instance;

    @SidedProxy(clientSide = "monnef.jaffas.technic.client.ClientProxy", serverSide = "monnef.jaffas.technic.common.CommonProxy")
    public static CommonProxy proxy;

    private static final int ANY_DMG = OreDictionary.WILDCARD_VALUE;
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
    public static BlockMultiLamp lamp;
    public static BlockMultiLampDummy lampDummy;

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

    private int itemCobbleBreakerID;
    public static BlockCobbleBreaker cobbleBreaker;

    private int itemHopSeedsID;
    public static ItemTechnic hopSeeds;

    private int itemKegID;
    public static ItemKeg itemKeg;

    private int blockKegID;
    public static BlockKeg keg;

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

    @Mod.PreInit
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
                int lampDummyId = idProvider.getTempBlockId();
                lampDummy = new BlockMultiLampDummy(lampDummyId, 41);
                idProvider.safelyRemoveTempBlock(lampDummyId, lampDummy);
                itemRainbowDustID = idProvider.getItemIDFromConfig("rainbowDust");
                itemGemsID = idProvider.getItemIDFromConfig("gems");
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

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            generateOres = config.get(Configuration.CATEGORY_GENERAL, "generateOres", true).getBoolean(true);
            switchgrassProbability = (float) config.get(Configuration.CATEGORY_GENERAL, "switchgrassProbability", 0.005, "Do not go too high, or face stack overflow caused by recursive chunk generation").getDouble(0.005);
            disableOreRecipes = config.get(Configuration.CATEGORY_GENERAL, "disableOreRecipes", true).getBoolean(true);
            if (config.get(Configuration.CATEGORY_GENERAL, "preciseTikcingOfFungiBox", false).getBoolean(false)) {
                TileEntityFungiBox.tickQuantum = 1;
            }
            disableLampParticles = config.get(Configuration.CATEGORY_GENERAL, "disableLampParticles", false).getBoolean(false);
            TileEntityCobbleBreaker.setTimer(config.get(Configuration.CATEGORY_GENERAL, "cobbleBreakerTimer", 12).getInt());
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

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.isModuleEnabled(ModulesEnum.technic))
            return;

        creativeTab = new monnef.jaffas.food.common.JaffaCreativeTab("jaffas.technic");

        createItemsAndBlocks();
        createFungiStuff();
        installRecipes();
        addDrops();

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

    private void createFungiStuff() {
        fungiBox = new BlockFungiBox(blockFungiBoxID, 27);
        RegistryUtils.registerBlock(fungiBox, "fungiBox", "Fungi Box");
        JaffasRegistryHelper.registerTileEntity(TileEntityFungiBox.class, "jaffasFungiBox");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FarmingRegistry.registerHarvestable(fungiBox);
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
        CustomDrop.addDrop(EntityZombie.class, jaffarrolRaw, 0.15f);
        CustomDrop.addDrop(EntityEnderman.class, limsew, 0.25f);
    }

    private void createItemsAndBlocks() {
        jaffarrol = new ItemTechnic(JaffarrolID, 0);
        jaffarrol.setUnlocalizedName("jaffarrol");
        LanguageRegistry.addName(jaffarrol, "Jaffarrol Ingot");

        jaffarrolRaw = new ItemTechnic(JaffarrolRawID, 1);
        jaffarrolRaw.setUnlocalizedName("jaffarrolRaw");
        LanguageRegistry.addName(jaffarrolRaw, "Raw Jaffarrol");

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
        JaffasFood.instance.items.createJaffaArmor(jaffarrolHelmet, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.helm, "/jaffas_jarmor1.png", jaffarrol);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolChest, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.chest, "/jaffas_jarmor1.png", jaffarrol);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolLeggins, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.leggings, "/jaffas_jarmor2.png", jaffarrol);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolBoots, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.boots, "/jaffas_jarmor1.png", jaffarrol);

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
        RegistryUtils.registerMultiBlock(constructionBlock, ItemBlockJaffas.class, new String[]{"Construction Block - Alloy", "Construction Block - Alloy-Glass"}, new String[]{"alloy", "glass"});
        Item.itemsList[constructionBlock.blockID].setFull3D();

        JaffasRegistryHelper.registerTileEntity(TileEntityConstructionDummy.class, "jaffasConstructionDummy");
        JaffasRegistryHelper.registerTileEntity(TileEntityCompostCore.class, "jaffasCompostCore");

        dummyConstructionBlock = new BlockConstructionDummy(blockConstructionDummyID, 17);
        RegistryUtils.registerBlock(dummyConstructionBlock, "dummyConstruction", "Dummy Construction Block - are you a cheater?");

        compostCore = new BlockCompostCore(blockCompostCoreID, 35, Material.iron);
        RegistryUtils.registerBlock(compostCore, "compostCore", "Compost Core Block");

        compost = new ItemCompost(itemCompostID, 29);
        RegistryUtils.registerItem(compost, "compost", "Compost");
        if (JaffasFood.otherMods.isMineFactoryReloadedDetected()) {
            FarmingRegistry.registerFertilizer(compost);
        }

        if (!disableRedstoneGadgets) {
            lamp = new BlockMultiLamp(blockMultiLampID, 37, 38);
            RegistryUtils.registerBlock(lamp, "multiLamp", "Multi-Lamp");

            gems = new ItemTechnic(itemGemsID, 40);
            RegistryUtils.registerItem(gems, "colourfulGems", "Colourful Gems");
            gems.setMaxStackSize(16);

            rainbowDust = new ItemTechnic(itemRainbowDustID, 39);
            RegistryUtils.registerItem(rainbowDust, "rainbowDust", "Rainbow Dust");
            rainbowDust.setInfo("Maybe a key to the secret cow level?");
        }

        highPlant = new BlockHighPlant(blockHighPlantID, 44);
        RegistryUtils.registerBlock(highPlant, "highPlant", "High Plant");

        highPlantPost = new ItemHightPlantPost(itemHighPlantPostID, 44);
        RegistryUtils.registerItem(highPlantPost, "highPlantPost", "Plant Post");
        JaffasRegistryHelper.registerTileEntity(TileEntityHighPlant.class, "highPlantPost");

        hop = new ItemTechnic(itemHopID, 45);
        RegistryUtils.registerItem(hop, "hop", "Hop");

        processedHop = new ItemTechnic(itemProcessedHopID, 47);
        RegistryUtils.registerItem(processedHop, "hopProcessed", "Milled Hop");

        processedHopInBucket = new ItemTechnic(itemProcessedHopInBucketID, 48);
        RegistryUtils.registerItem(processedHopInBucket, "hopProcessedInBucket", "Milled Hop With Water");
        processedHopInBucket.setContainerItem(Item.bucketEmpty);

        brewedHopInBucket = new ItemTechnic(itemBrewedHopInBucketID, 49);
        RegistryUtils.registerItem(brewedHopInBucket, "brewedHopInBucket", "Brewed Hop");
        brewedHopInBucket.setContainerItem(Item.bucketEmpty);

        hopSeeds = new ItemTechnic(itemHopSeedsID, 46);
        RegistryUtils.registerItem(hopSeeds, "hopSeeds", "Hop Seeds");

        cobbleBreaker = new BlockCobbleBreaker(itemCobbleBreakerID, 50, 2, Material.rock, BlockJDirectional.TextureMappingType.ALL_SIDES);
        RegistryUtils.registerBlock(cobbleBreaker, "cobbleBreaker", "Cobble Breaker");
        JaffasRegistryHelper.registerTileEntity(TileEntityCobbleBreaker.class, "cobbleBreaker");

        itemKeg = new ItemKeg(itemKegID, 42);
        RegistryUtils.registerItem(itemKeg, "itemKeg", "Keg");
        itemKeg.registerTexts();

        keg = new BlockKeg(blockKegID, 42);
        RegistryUtils.registerBlock(keg, "keg", "Keg");
        JaffasRegistryHelper.registerTileEntity(TileEntityKeg.class, "keg");

        createTools();
    }

    private void createTools() {
        EnumToolMaterialJaffarrol.customCraftingMaterial = jaffarrol;

        axeJaffarrol = new ItemAxeTechnic(ItemJAxeID, 21, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(axeJaffarrol, "axeJaffarrol", "Jaffarrol Axe");
        MinecraftForge.setToolClass(axeJaffarrol, "axe", 3);

        pickaxeJaffarrol = new ItemPickaxeTechnic(ItemJPickaxeID, 20, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(pickaxeJaffarrol, "pickaxeJaffarrol", "Jaffarrol Pickaxe");
        MinecraftForge.setToolClass(pickaxeJaffarrol, "pickaxe", 3);

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
            GameRegistry.addShapelessRecipe(new ItemStack(limsew, 2), new ItemStack(Item.diamond), new ItemStack(Item.lightStoneDust),
                    new ItemStack(Item.lightStoneDust), new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone),
                    new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone));
        }

        GameRegistry.addShapelessRecipe(new ItemStack(limsew, 9), new ItemStack(blockLimsew));
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrol, 9), new ItemStack(blockJaffarrol));

        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 0), "JRJ", "RLR", "JRJ", 'J', jaffarrol, 'R', Item.redstone, 'L', limsew);
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 1), "JRJ", "CLC", "JRJ", 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 2), "LJL", "CLC", "LJL", 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 1));

        GameRegistry.addRecipe(new ItemStack(funnel), "I I", "J J", " J ", 'I', Item.ingotIron, 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(itemCasing, 1, 0), "JJJ", "J J", "JJJ", 'J', jaffarrol);
        GameRegistry.addSmelting(itemCasing.itemID, new ItemStack(itemCasingRefined), 1f);

        GameRegistry.addRecipe(new ItemStack(JaffasFood.blockFridge), "I&I", "JBJ", "ICI", 'I', Item.ingotIron,
                '&', itemCasing, 'J', jaffarrol, 'B', Block.fenceIron, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(JaffasTrees.blockFruitCollector), "JFJ", "J@J", "JCJ",
                'J', jaffarrol, 'F', funnel, '@', itemCasing, 'C', new ItemStack(itemCentralUnit, 1, 2));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.fryingPan)), "  J", "II ", "II ", 'I', Item.ingotIron, 'J', JaffasTechnic.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatCleaver)), "II", "II", " J", 'I', Item.ingotIron, 'J', JaffasTechnic.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grinderMeat)), " FS", "III", "III", 'I', Item.ingotIron, 'F', JaffasTechnic.funnel, 'S', Item.stick);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sink)), "J", "W", "I", 'J', JaffasTechnic.jaffarrol, 'W', Item.bucketEmpty, 'I', Block.blockIron);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grater)), " J ", "III", "III", 'J', JaffasTechnic.jaffarrol, 'I', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bottleEmpty), 4), " J ", "G G", "GGG", 'J', JaffasTechnic.jaffarrol, 'G', Block.glass);

        GameRegistry.addRecipe(new ItemStack(JaffasFood.blockBoard), "  W", "JJ ", "JJ ", 'W', Block.wood, 'J', JaffasTechnic.jaffarrol);

        // jarmor
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolHelmet)), "JJJ", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolChest)), "J J", "JJJ", "JJJ", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolBoots)), "J J", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolLeggins)), "JJJ", "J J", "J J", 'J', jaffarrol);

        // spawn stones
        if (JaffasFood.spawnStonesEnabled) {
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneLittle)), " I ", "IJI", " I ", 'I', Item.ingotIron, 'J', jaffarrol);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneMedium)), "GRG", "LJL", "GRG", 'G', Item.ingotGold, 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneMedium)), "GLG", "LJR", "GRG", 'G', Item.ingotGold, 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneBig)), "LJB", "JDJ", "BJL", 'L', limsew, 'J', jaffarrolRefined, 'B', Item.blazeRod, 'D', Item.diamond);
        }

        GameRegistry.addRecipe(new ItemStack(getItem(juiceBottle), 2), "GJG", "G G", "GGG", 'G', Block.glass, 'J', jaffarrol);

        GameRegistry.addSmelting(blockJaffarrolOre.blockID, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(blockLimsewOre.blockID, new ItemStack(limsew), 1f);

        GameRegistry.addRecipe(new ItemStack(swordJaffarrol), "J", "J", "G", 'J', jaffarrol, 'G', Item.ingotGold);
        GameRegistry.addRecipe(new ItemStack(hoeJaffarrol), "JJ", " S", " S", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(spadeJaffarrol), "J", "S", "S", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(pickaxeJaffarrol), "JJJ", " S ", " S ", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(axeJaffarrol), "JJ ", "JS ", " S ", 'J', jaffarrol, 'S', Item.stick);

        GameRegistry.addRecipe(getItemStack(cookingPot), "I I", "I I", "IJI", 'I', Item.ingotIron, 'J', jaffarrol);

        OreDictionary.registerOre("oreJaffarrol", blockJaffarrolOre);
        OreDictionary.registerOre("oreLimsew", blockLimsewOre);

        GameRegistry.addRecipe(new ItemStack(itemLocomotive), "I F", "BCB", "III", 'F', funnel, 'I', Item.ingotIron, 'B', Block.blockIron, 'C', itemCasingRefined);

        GameRegistry.addRecipe(new RepairRecipe(2, 1, new ItemStack(JaffasTechnic.swordJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(3, 1, new ItemStack(JaffasTechnic.axeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(2, 1, new ItemStack(JaffasTechnic.hoeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(3, 1, new ItemStack(JaffasTechnic.pickaxeJaffarrol), 333));
        GameRegistry.addRecipe(new RepairRecipe(1, 1, new ItemStack(JaffasTechnic.spadeJaffarrol), 333));

        GameRegistry.addRecipe(new EnchantRecipe(new ItemStack(swordJaffarrol, 1, ANY_DMG), new ItemStack(Item.swordGold), 1, 4));
        GameRegistry.addRecipe(new EnchantRecipe(new ItemStack(axeJaffarrol, 1, ANY_DMG), new ItemStack(Item.axeGold), 1, 4));
        GameRegistry.addRecipe(new EnchantRecipe(new ItemStack(hoeJaffarrol, 1, ANY_DMG), new ItemStack(Item.hoeGold), 1, 4));
        GameRegistry.addRecipe(new EnchantRecipe(new ItemStack(pickaxeJaffarrol, 1, ANY_DMG), new ItemStack(Item.pickaxeGold), 1, 4));
        GameRegistry.addRecipe(new EnchantRecipe(new ItemStack(spadeJaffarrol, 1, ANY_DMG), new ItemStack(Item.shovelGold), 1, 4));

        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(blockJaffarrolOre));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrol));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrolRaw));
        Recipes.addMalletShapedRecipe(new ItemStack(jaffarrolDust), new ItemStack(jaffarrolRefined));
        GameRegistry.addSmelting(jaffarrolDust.itemID, new ItemStack(jaffarrol), 0);

        GameRegistry.addShapelessRecipe(JaffasFood.instance.guideBook.copy(), Item.writableBook, jaffarrol);
        GameRegistry.addShapelessRecipe(JaffasFood.instance.guideBook.copy(), Item.writableBook, jaffarrolDust);
        GameRegistry.addShapelessRecipe(JaffasFood.instance.guideBook.copy(), Item.writableBook, limsew);

        GameRegistry.addRecipe(new ItemStack(constructionBlock, 8, 0), "JIJ", "IJI", "JIJ", 'J', jaffarrol, 'I', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(constructionBlock, 8, 1), "JIJ", "I I", "JIJ", 'J', jaffarrol, 'I', Block.glass);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mushroomKnife), " J", "W ", 'J', jaffarrol, 'W', "plankWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fungiBox), " W ", "PDP", "PPP", 'W', Item.bucketWater, 'D', Block.dirt, 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(compostCore), "JPJ", "PCP", "JPJ", 'J', jaffarrol, 'P', "plankWood", 'C', new ItemStack(itemCentralUnit, 1, 1)));

        // temporal recipes
        GameRegistry.addShapelessRecipe(new ItemStack(Block.mushroomBrown),
                new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID), new ItemStack(fungus, 1, PORCINO_ID));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.mushroomBrown),
                new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID), new ItemStack(fungus, 1, PARASOL_ID));

        Recipes.addRecipe(new ShapelessOreRecipe(getItem(cookedMushroomsRaw), getItem(woodenBowl), MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE));
        Recipes.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(cookedMushroomsRaw), 3), getItem(woodenBowl), getItem(woodenBowl), getItem(woodenBowl), MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE, MUSHROOMS_EATABLE));

        Recipes.addRecipe(new ShapedOreRecipe(getItem(friedMushroomsInTinRaw), "MSM", "PPP", " T ", 'M', MUSHROOMS_EATABLE, 'S', getItem(sausage), 'P', JaffasTrees.getFruit(JaffasTrees.bushType.Pea), 'T', getItem(cakeTin)));
        GameRegistry.addSmelting(getItem(friedMushroomsInTinRaw).itemID, getItemStack(friedMushroomsInTinCooked), 2f);
        JaffaCraftingHandler.AddPersistentItem(friedMushroomsInTinCooked, false, cakeTin);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(friedMushrooms), 3), getItem(plate), getItem(plate), getItem(plate), getItem(friedMushroomsInTinCooked));
        Recipes.addRecipe(new ShapelessOreRecipe(new ItemStack(getItem(mincedMushrooms), 2), MUSHROOMS_EATABLE, getItem(grinderMeat)));
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

            GameRegistry.addShapedRecipe(new ItemStack(lamp), "IGI", "RBR", "IRI", 'I', Item.ingotIron, 'R', Item.redstone, 'G', gems, 'B', new ItemStack(constructionBlock, 1, BlockConstruction.META_GLASSY));
        }

        GameRegistry.addShapelessRecipe(new ItemStack(processedHop), getItem(grinderMeat), hop);
        GameRegistry.addShapelessRecipe(new ItemStack(processedHopInBucket), Item.bucketWater, Item.bucketEmpty, processedHop, processedHop, processedHop, processedHop, processedHop, processedHop);
        GameRegistry.addSmelting(processedHopInBucket.itemID, new ItemStack(brewedHopInBucket), 1f);
        GameRegistry.addShapelessRecipe(new ItemStack(hopSeeds), hop);
        Recipes.addRecipe(new ShapedOreRecipe(itemKeg, "PSP", "PJP", "PSP", 'P', "plankWood", 'S', "slabWood", 'J', jaffarrol));

        GameRegistry.addShapedRecipe(new ItemStack(cobbleBreaker), "SSS", "JLJ", "TFT", 'S', Item.stick, 'J', jaffarrolRefined, 'L', Item.slimeBall, 'T', DyeHelper.getDye(DyeColor.YELLOW), 'F', Block.furnaceIdle);

        GameRegistry.addShapedRecipe(new ItemStack(Item.shears), " J", "J ", 'J', jaffarrol);
    }

    private Item getItem(JaffaItem item) {
        return JaffasFood.getItem(item);
    }
}
