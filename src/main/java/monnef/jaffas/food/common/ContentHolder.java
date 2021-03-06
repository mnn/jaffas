/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BiomeHelper;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.EntityHelper;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.BlockBoard;
import monnef.jaffas.food.block.BlockBricks;
import monnef.jaffas.food.block.BlockCoconutLamp;
import monnef.jaffas.food.block.BlockColumn;
import monnef.jaffas.food.block.BlockCross;
import monnef.jaffas.food.block.BlockFridge;
import monnef.jaffas.food.block.BlockJDirectional;
import monnef.jaffas.food.block.BlockJaffaBomb;
import monnef.jaffas.food.block.BlockJaffaFiniteFluid;
import monnef.jaffas.food.block.BlockJaffaStatue;
import monnef.jaffas.food.block.BlockLightlyTintedMulti;
import monnef.jaffas.food.block.BlockMeatDryer;
import monnef.jaffas.food.block.BlockPie;
import monnef.jaffas.food.block.BlockPizza;
import monnef.jaffas.food.block.BlockRipeningBox;
import monnef.jaffas.food.block.BlockSink;
import monnef.jaffas.food.block.BlockSpecialWeb;
import monnef.jaffas.food.block.BlockSwitchgrass;
import monnef.jaffas.food.block.BlockSwitchgrassSolid;
import monnef.jaffas.food.block.BlockTable;
import monnef.jaffas.food.block.BlockTintedMulti;
import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.food.block.ItemBlockPie;
import monnef.jaffas.food.block.ItemBlockSwitchgrass;
import monnef.jaffas.food.block.ItemBlockTable;
import monnef.jaffas.food.block.TileBoard;
import monnef.jaffas.food.block.TileCoconutLamp;
import monnef.jaffas.food.block.TileColumn;
import monnef.jaffas.food.block.TileCross;
import monnef.jaffas.food.block.TileFridge;
import monnef.jaffas.food.block.TileJaffaStatue;
import monnef.jaffas.food.block.TileMeatDryer;
import monnef.jaffas.food.block.TilePie;
import monnef.jaffas.food.block.TilePizza;
import monnef.jaffas.food.block.TileRipeningBox;
import monnef.jaffas.food.block.TileSink;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.entity.EntityDuck;
import monnef.jaffas.food.entity.EntityDuckEgg;
import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.entity.EntityLittleSpider;
import monnef.jaffas.food.entity.EntitySpiderEgg;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.item.ItemJaffaPainting;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.ItemJaffaSword;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

import static monnef.core.utils.BreakableIronMaterial.breakableIronMaterial;
import static monnef.core.utils.EntityHelper.getNextFreeGlobalEntityID;
import static monnef.jaffas.food.JaffasFood.instance;
import static monnef.jaffas.food.JaffasFood.otherMods;
import static monnef.jaffas.food.JaffasFood.proxy;
import static monnef.jaffas.food.common.ConfigurationManager.duckSpawnProbabilityHigh;
import static monnef.jaffas.food.common.ConfigurationManager.duckSpawnProbabilityLow;
import static monnef.jaffas.food.common.ConfigurationManager.duckSpawnProbabilityMed;
import static monnef.jaffas.food.common.ConfigurationManager.dungeonLootEnabled;
import static monnef.jaffas.food.common.JaffasRegistryHelper.registerTileEntity;
import static net.minecraft.item.Item.ToolMaterial;
import static net.minecraft.item.ItemArmor.ArmorMaterial;

public class ContentHolder {
    public static BlockJaffaBomb blockJaffaBomb;
    public static BlockFridge blockFridge;
    public static BlockCross blockCross;
    public static BlockSink blockSink;
    public static BlockBoard blockBoard;
    public static BlockPizza blockPizza;
    public static BlockColumn blockColumn;
    public static BlockJaffaStatue blockJaffaStatue;
    public static BlockPie blockPie;
    public static BlockTable blockTable;
    public static BlockSwitchgrass blockSwitchgrass;
    public static BlockJDirectional blockSwitchgrassSolid;
    public static BlockJDirectional blockDir1;
    public static BlockJDirectional blockDir2;
    public static BlockMeatDryer blockMeatDryer;
    public static BlockRipeningBox blockRipeningBox;
    public static BlockBricks blockBricks;
    public static BlockTintedMulti blockStainedStone;
    public static BlockTintedMulti blockStainedPolishedStone;
    public static BlockTintedMulti blockStainedPlanks;
    public static BlockCoconutLamp blockCoconutLamp;
    public static BlockSpecialWeb webEnder, webGlowstone, webRedstone, webLimsew;

    /*
    CLOTH(5, new int[]{1, 3, 2, 1}, 15),
    CHAIN(15, new int[]{2, 5, 4, 1}, 12),
    IRON(15, new int[]{2, 6, 5, 2}, 9),
    GOLD(7, new int[]{2, 5, 3, 1}, 25),
    DIAMOND(33, new int[]{3, 8, 6, 3}, 10);
    */
    public static ArmorMaterial EnumArmorMaterialJaffas = EnumHelper.addArmorMaterial("JaffaArmor", 10, new int[]{1, 4, 2, 3}, 23);
    public static ArmorMaterial EnumArmorMaterialDuck = EnumHelper.addArmorMaterial("Duck", 10, new int[]{1, 3, 2, 1}, 23);
    public static ArmorMaterial EnumArmorMaterialWolf = EnumHelper.addArmorMaterial("Wolf", 13, new int[]{3, 3, 2, 2}, 15);
    public static ArmorMaterial EnumArmorMaterialJaffarrol = EnumHelper.addArmorMaterial("Jaffarrol", 29, new int[]{4, 7, 5, 3}, 17);

    public static ItemJaffaPlate itemJaffaPlate;

    /*
    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    EMERALD(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);
    */
    public static ToolMaterial EnumToolMaterialJaffas = EnumHelper.addToolMaterial("Jaffa", 2, 400, 6.0F, 6, 15);
    public static ToolMaterial EnumToolMaterialCleaver = EnumHelper.addToolMaterial("JaffaCleaver", 2, 75, 2.0F, 0, 15);

    public static ItemJaffaSword itemJaffaSword;

    public static int renderID;
    public static int renderSwitchgrassID;
    public static int renderDirectionalBlockID;
    public static int renderBlockID;

    public static ItemJaffaPainting itemPainting;

    public static JaffaFluid corrosiveGoo, miningGoo, unstableGoo, waterOfLife;
    public static BlockJaffaFiniteFluid blockCorrosiveGoo, blockMiningGoo, blockUnstableGoo, blockWaterOfLife;

    private static JaffasFood jf;

    static {
        jf = instance;
    }

    public static void addToDungeons(JaffaItem item, int weight, int min, int max) {
        addToDungeons(Recipes.getItem(item), weight, min, max);
    }

    public static void addToDungeons(Item item, int weight, int min, int max) {
        addToDungeons(new ItemStack(item), weight, min, max);
    }

    public static void addToDungeons(Block block, int weight, int min, int max) {
        addToDungeons(new ItemStack(block), weight, min, max);
    }

    public static void addToDungeons(ItemStack stack, int weight, int min, int max) {
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(stack, min, max, weight));
    }

    public static void addToPyramids(JaffaItem item, int weight, int min, int max) {
        addToPyramids(Recipes.getItemStack(item), weight, min, max);
    }

    public static void addToPyramids(ItemStack stack, int weight, int min, int max) {
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(stack, min, max, weight));
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(stack, min, max, weight));
    }

    public static void addDungeonLoot() {
        if (!dungeonLootEnabled) return;

        // 100 ~ trash, 5 ~ treasure
        ContentHolder.addToDungeons(JaffaItem.malletIron, 25, 1, 1);
        ContentHolder.addToDungeons(JaffaItem.honey, 90, 5, 15);
        ContentHolder.addToDungeons(JaffaItem.kettle, 30, 1, 1);
        ContentHolder.addToDungeons(new ItemStack(ContentHolder.blockSwitchgrass, 1, BlockSwitchgrass.VALUE_TOP), 90, 5, 64);
        ContentHolder.addToDungeons(JaffaItem.magnifier, 30, 1, 1);
        ContentHolder.addToDungeons(JaffaItem.spawnStoneLittle, 10, 1, 1);
        ContentHolder.addToDungeons(JaffaItem.spawnStoneMedium, 5, 1, 1);
        ContentHolder.addToDungeons(JaffaItem.jaffa, 20, 5, 15);
        ContentHolder.addToDungeons(JaffaItem.scrap, 40, 1, 64);

        ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(Recipes.getItemStack(JaffaItem.mallet), 1, 1, 4));

        // 20 ~ trash, 3 ~ treasure
        ContentHolder.addToPyramids(JaffaItem.spawnStoneBig, 3, 1, 1);
        ContentHolder.addToPyramids(JaffaItem.malletHeadDiamond, 3, 1, 1);
        ContentHolder.addToPyramids(JaffaItem.spiderLegRaw, 7, 1, 4);
    }

    public static void registerCleaverRecords() {
        ItemCleaverHookContainer.registerMeatFromAnimal(EntityDuck.class, new ItemStack(getItem(JaffaItem.duckRaw)));
        ItemCleaverHookContainer.registerMeatFromAnimal(EntitySheep.class, new ItemStack(getItem(JaffaItem.muttonRaw)));
        ItemCleaverHookContainer.registerMeatFromAnimal(EntityWolf.class, new ItemStack(getItem(JaffaItem.wolfMeatRaw)));
    }

    protected static void createPainting() {
        ContentHolder.itemPainting = new ItemJaffaPainting();
        GameRegistry.registerItem(ContentHolder.itemPainting, ContentHolder.itemPainting.getUnlocalizedName());
        EntityHelper.registerModEntity(EntityJaffaPainting.class, "jaffaPainting", 160, Integer.MAX_VALUE, false, jf);
    }

    public static void registerDuck() {
        EntityHelper.registerGlobalEntity(EntityDuck.class, "jaffasDuck", 160, 1, true, jf, ColorHelper.getInt(0, 127, 75), ColorHelper.getInt(200, 200, 255));
        LanguageRegistry.instance().addStringLocalization("entity.jaffasDuck.name", "en_US", "Duck");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerGrindable", new EntityDuck.MFR());
        }
    }

    public static void registerDuckEgg() {
        EntityHelper.registerModEntity(EntityDuckEgg.class, "duckEgg", 160, 1, true, jf);
    }

    public static void registerLittleSpider() {
        EntityHelper.registerGlobalEntity(EntityLittleSpider.class, "jaffasSpider", 160, 1, true, jf, ColorHelper.getInt(122, 122, 122), ColorHelper.getInt(0, 0, 202));
        LanguageRegistry.instance().addStringLocalization("entity.jaffasSpider.name", "en_US", "Little Spider");
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerGrindable", new EntityLittleSpider.MFR());
        }
    }

    public static void registerSpiderEgg() {
        EntityHelper.registerModEntity(EntitySpiderEgg.class, "littleSpiderEgg", 160, 1, true, jf);
    }

    public static void registerDuckSpawns() {
        BiomeDictionary.registerAllBiomes();

        EntityRegistry.addSpawn(EntityDuck.class, duckSpawnProbabilityHigh, 4, 6, EnumCreatureType.creature, // high
                BiomeHelper.compileListOrAsArray(new BiomeDictionary.Type[]{BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.BEACH}));
        EntityRegistry.addSpawn(EntityDuck.class, duckSpawnProbabilityMed, 2, 5, EnumCreatureType.creature,  // med
                BiomeHelper.compileListOrAsArray(new BiomeDictionary.Type[]{BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST}));
        EntityRegistry.addSpawn(EntityDuck.class, duckSpawnProbabilityLow, 1, 4, EnumCreatureType.creature,  // low
                BiomeHelper.compileListOrAsArray(new BiomeDictionary.Type[]{BiomeDictionary.Type.JUNGLE}));
    }

    public static void createBlocks() {
        ContentHolder.blockFridge = new BlockFridge();
        GameRegistry.registerBlock(ContentHolder.blockFridge, "blockFridge");
        registerTileEntity(TileFridge.class, "Fridge");

        ContentHolder.blockJaffaBomb = new BlockJaffaBomb(35, Material.rock);
        GameRegistry.registerBlock(ContentHolder.blockJaffaBomb, "blockJaffaBomb");

        ContentHolder.blockCross = new BlockCross(5, Material.rock);
        GameRegistry.registerBlock(ContentHolder.blockCross, "blockCross");
        registerTileEntity(TileCross.class, "cross");

        ContentHolder.blockSink = new BlockSink(141);
        RegistryUtils.registerBlock(ContentHolder.blockSink);
        registerTileEntity(TileSink.class, "sink");

        ContentHolder.blockBoard = new BlockBoard(142, Material.wood);
        RegistryUtils.registerBlock(ContentHolder.blockBoard);
        registerTileEntity(TileBoard.class, "kitchenBoard");

        ContentHolder.blockPizza = new BlockPizza(149, Material.cake);
        RegistryUtils.registerBlock(ContentHolder.blockPizza);
        registerTileEntity(TilePizza.class, "pizza");

        ContentHolder.blockColumn = new BlockColumn(160, Material.rock);
        RegistryUtils.registerBlock(ContentHolder.blockColumn);
        registerTileEntity(TileColumn.class, "column");

        ContentHolder.blockJaffaStatue = new BlockJaffaStatue(6, Material.iron);
        RegistryUtils.registerBlock(ContentHolder.blockJaffaStatue);
        registerTileEntity(TileJaffaStatue.class, "jaffaStatue");

        ContentHolder.blockPie = new BlockPie(156);
        RegistryUtils.registerMultiBlock(ContentHolder.blockPie, ItemBlockPie.class, BlockPie.multiBlockNames);
        registerTileEntity(TilePie.class, "jaffaPie");

        ContentHolder.blockTable = new BlockTable(0, Material.wood);
        RegistryUtils.registerMultiBlock(ContentHolder.blockTable, ItemBlockTable.class, BlockTable.multiBlockNames);
        ContentHolder.blockTable.setSheetNumber(7);

        ContentHolder.blockSwitchgrass = new BlockSwitchgrass(238);
        RegistryUtils.registerMultiBlock(ContentHolder.blockSwitchgrass, ItemBlockSwitchgrass.class, ContentHolder.blockSwitchgrass.subBlockNames);
        MinecraftForge.EVENT_BUS.register(new SwitchgrassBonemealHandler());
        if (otherMods.isMineFactoryReloadedDetected()) {
            FactoryRegistry.sendMessage("registerFertilizable", ContentHolder.blockSwitchgrass);
            FactoryRegistry.sendMessage("registerHarvestable", ContentHolder.blockSwitchgrass);
            FactoryRegistry.sendMessage("registerPlantable", ContentHolder.blockSwitchgrass);
        }

        ContentHolder.blockSwitchgrassSolid = new BlockSwitchgrassSolid(240, 2);
        RegistryUtils.registerBlockWithName(ContentHolder.blockSwitchgrassSolid, "switchgrassSolid");

        ContentHolder.blockMeatDryer = new BlockMeatDryer(250);
        RegistryUtils.registerBlockWithName(ContentHolder.blockMeatDryer, "meatDryer");
        registerTileEntity(TileMeatDryer.class, "MeatDryer");

        if (MonnefCorePlugin.debugEnv) {
            ContentHolder.blockDir1 = new BlockJDirectional(35, 2, Material.rock, BlockJDirectional.TextureMappingType.LOG_LIKE);
            RegistryUtils.registerBlockWithName(ContentHolder.blockDir1, "dir1");

            ContentHolder.blockDir2 = new BlockJDirectional(35, 6, Material.rock, BlockJDirectional.TextureMappingType.ALL_SIDES);
            RegistryUtils.registerBlockWithName(ContentHolder.blockDir2, "dir2");
        }

        ContentHolder.blockRipeningBox = new BlockRipeningBox(257, breakableIronMaterial());
        RegistryUtils.registerBlockWithName(ContentHolder.blockRipeningBox, "ripeningBox");
        registerTileEntity(TileRipeningBox.class, "ripeningBox");

        blockBricks = new BlockBricks(291);
        RegistryUtils.registerMultiBlock(ContentHolder.blockBricks, ItemBlockJaffas.class, BlockBricks.subNames());

        blockStainedStone = new BlockTintedMulti(294, Material.rock, "Stained Stone");
        blockStainedStone.setHardness(1.7F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setBlockName("stainedStone");
        RegistryUtils.registerMultiBlock(ContentHolder.blockStainedStone, ItemBlockJaffas.class, blockStainedStone.subNames());

        blockStainedPolishedStone = new BlockLightlyTintedMulti(296, Material.rock, "Polished Stained Stone");
        blockStainedPolishedStone.setHardness(2F).setResistance(15.0F).setStepSound(Block.soundTypePiston).setBlockName("stainedPolishedStone");
        RegistryUtils.registerMultiBlock(ContentHolder.blockStainedPolishedStone, ItemBlockJaffas.class, blockStainedPolishedStone.subNames());

        blockStainedPlanks = new BlockLightlyTintedMulti(295, Material.wood, "Stained Planks");
        blockStainedPlanks.setHardness(2.0F).setResistance(4.7F).setStepSound(Block.soundTypeWood).setBlockName("stainedPlanks");
        RegistryUtils.registerMultiBlock(ContentHolder.blockStainedPlanks, ItemBlockJaffas.class, blockStainedPlanks.subNames());

        blockCoconutLamp = new BlockCoconutLamp(310);
        RegistryUtils.registerBlock(blockCoconutLamp);
        registerTileEntity(TileCoconutLamp.class, "coconutLamp");
    }

    public static void createJaffaArmorAndSword() {
        int armorRender = proxy.addArmor("Jaffa");
        ContentHolder.itemJaffaPlate = new ItemJaffaPlate(ContentHolder.EnumArmorMaterialJaffas, armorRender, ItemJaffaPlate.ArmorType.chest, "jaffabrn1.png", null, 90);
        LanguageRegistry.addName(ContentHolder.itemJaffaPlate, "Jaffa Hoodie");

        ContentHolder.itemJaffaSword = new ItemJaffaSword(88, ContentHolder.EnumToolMaterialJaffas);
        RegistryUtils.registerItem(ContentHolder.itemJaffaSword, "jaffaSword");
    }

    public static Item getItem(JaffaItem item) {
        return ItemManager.getItem(item);
    }

    public static void initEntityIDs() {
        createPainting();
        registerDuck();
        registerDuckEgg();
        registerLittleSpider();
        registerSpiderEgg();
    }

    public static void createFluids() {
        ContentHolderHelper.createFluids();
    }
}
