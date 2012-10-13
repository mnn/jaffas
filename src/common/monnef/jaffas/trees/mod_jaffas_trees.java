package monnef.jaffas.trees;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.common.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import static monnef.jaffas.food.mod_jaffas.getJaffaItem;

@Mod(modid = "moen-jaffas-trees", name = "Jaffas - trees", version = "0.4.0", dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_trees {
    private static MinecraftServer server;

    public static final String[] treeTypes = new String[]{"normal", "apple", "cocoa", "vanilla", "lemon", "orange", "plum"};
    public static final String[] seedsNames = new String[]{"[UNUSED]", "Apple Seeds", "Cocoa Seeds", "Vanilla Seeds", "Lemon Seeds", "Orange Seeds", "Plum Seeds"};

    public static fruitType getActualLeavesType(Block block, int blockMetadata) {
        BlockFruitLeaves b = (BlockFruitLeaves) block;
        int index = b.serialNumber * 4 + blockMetadata;
        fruitType fruitType = mod_jaffas_trees.fruitType.indexToFruitType(index);
        if (fruitType == null) {
            throw new RuntimeException("fruit not found!");
        }

        return fruitType;
    }

    public static enum fruitType {
        Normal(0), Apple(1), Cocoa(2), Vanilla(3), Lemon(4), Orange(5), Plum(6);
        private int value;
        private int blockNumber;
        private int metaNumber;

        fruitType(int value) {
            this.value = value;
            this.blockNumber = value % 4;
            this.metaNumber = value / 4;

            mapper.indexToFruitMap.put(value, this);
        }

        public int getValue() {
            return value;
        }

        public int getBlockNumber() {
            return blockNumber;
        }

        public static fruitType indexToFruitType(int index) {
            return mapper.indexToFruitMap.get(index);
        }

        private static class mapper {
            private static HashMap<Integer, fruitType> indexToFruitMap;

            static {
                indexToFruitMap = new HashMap<Integer, fruitType>();
            }
        }
    }

    public static mod_jaffas_trees instance;

    public static ArrayList<LeavesInfo> leavesList = new ArrayList<LeavesInfo>();

    public static final int leavesBlocksAllocated = 3;
    public static final int leavesTypesCount = 6;

    public static int startID;
    private int actualID;

    public static boolean debug;

    private int itemLemonID;
    private int itemOrangeID;
    private int itemPlumID;
    public static ItemJaffaFruit itemLemon;
    public static ItemJaffaFruit itemOrange;
    public static ItemJaffaFruit itemPlum;

    private int getID() {
        return this.actualID++;
    }

    private int getBlockID() {
        return getID()/* + 256*/;
    }

    public mod_jaffas_trees() {
        instance = this;
    }

    @SidedProxy(clientSide = "monnef.jaffas.trees.ClientProxyTutorial", serverSide = "monnef.jaffas.trees.CommonProxyTutorial")
    public static CommonProxyTutorial proxy;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        //this.startID = mod_jaffas.topDefaultID;
        this.startID = 3500;
        this.actualID = this.startID;

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();

            if (this.actualID <= 0) {
                throw new RuntimeException("unable to get ID from parent");
            }

//            blockFruitSaplingID = config.getOrCreateIntProperty("fruit tree sapling", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
            itemLemonID = config.getOrCreateIntProperty("lemon", Configuration.CATEGORY_ITEM, getID()).getInt();
            itemOrangeID = config.getOrCreateIntProperty("orange", Configuration.CATEGORY_ITEM, getID()).getInt();
            itemPlumID = config.getOrCreateIntProperty("plum", Configuration.CATEGORY_ITEM, getID()).getInt();

            //blockFruitLeavesID = config.getOrCreateIntProperty("fruit leaves", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
            for (int i = 0; i < leavesBlocksAllocated; i++) {
                int leavesID = config.getOrCreateIntProperty("fruit leaves " + i, Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
                int saplingID = config.getOrCreateIntProperty("fruit tree sapling " + i, Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
                int seedsID = config.getOrCreateIntProperty("fruit seeds " + i, Configuration.CATEGORY_ITEM, getID()).getInt();

                leavesList.add(new LeavesInfo(leavesID, saplingID, seedsID, i));
            }


            debug = config.getOrCreateBooleanProperty("debug", Configuration.CATEGORY_GENERAL, false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (trees) can't read config file.");
        } finally {
            config.save();
        }
    }


    @Mod.Init
    public void load(FMLInitializationEvent event) {
/*
        blockFruitSapling = new BlockFruitSapling(blockFruitSaplingID, 15);
        blockFruitSapling.setBlockName("fruitSapling").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(blockFruitSapling);
        LanguageRegistry.addName(blockFruitSapling, "Fruit Sapling");

        blockFruitLeaves = new BlockFruitLeaves(blockFruitLeavesID, 0);
        blockFruitLeaves.setLeavesRequiresSelfNotify().setBlockName("fruitLeaves").setCreativeTab(CreativeTabs.tabDeco).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep);
        GameRegistry.registerBlock(blockFruitLeaves);
        LanguageRegistry.addName(blockFruitLeaves, "Leaves");

        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[0] + ".name", "[UNUSED] Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[1] + ".name", "Apple Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[2] + ".name", "Cocoa Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[3] + ".name", "Vanilla Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[4] + ".name", "Lemon Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[5] + ".name", "Orange Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + TileEntityFruitLeaves.treeTypes[6] + ".name", "Plum Seeds");
        itemFruitSeeds = new ItemFruitSeeds(itemFruitSeedsID, blockFruitSapling.blockID, 1);
        itemFruitSeeds.setItemName("fruitSeeds");
        LanguageRegistry.addName(itemFruitSeeds, "Fruit Seeds");
        */

        // TODO general stuff bellow
        AddFruitTreesSequence(0, 0, 32, 4);
        AddFruitTreesSequence(1, 4, 32 + 4, 3);
        // end of TODO


        GameRegistry.registerTileEntity(TileEntityFruitLeaves.class, "fruitLeaves");

        itemLemon = new ItemJaffaFruit(itemLemonID);
        itemLemon.setItemName("lemon").setIconCoord(4, 4);
        LanguageRegistry.addName(itemLemon, "Lemon");

        itemOrange = new ItemJaffaFruit(itemOrangeID);
        itemOrange.setItemName("orange").setIconCoord(5, 4);
        LanguageRegistry.addName(itemOrange, "Orange");

        itemPlum = new ItemJaffaFruit(itemPlumID);
        itemPlum.setItemName("plum").setIconCoord(6, 4);
        LanguageRegistry.addName(itemPlum, "Plum");

        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        //GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());
    }

    private void AddFruitTreesSequence(int i, int leavesTexture, int seedTexture, int subCount) {
        LeavesInfo leaves = leavesList.get(i);
        leaves.leavesBlock = new BlockFruitLeaves(leaves.leavesID, leavesTexture);
        leaves.leavesBlock.serialNumber = i;
        leaves.leavesBlock.setLeavesRequiresSelfNotify().setBlockName("fruitLeaves" + i).setCreativeTab(CreativeTabs.tabDeco).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep);
        GameRegistry.registerBlock(leaves.leavesBlock);
        LanguageRegistry.addName(leaves.leavesBlock, "Leaves");

        leaves.saplingBlock = new BlockFruitSapling(leaves.saplingID, 15);
        leaves.saplingBlock.serialNumber = i;
        leaves.saplingBlock.setBlockName("fruitSapling" + i).setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(leaves.saplingBlock);
        LanguageRegistry.addName(leaves.saplingBlock, "Fruit Sapling");

        for (int j = 0; j < subCount; j++) {
            LanguageRegistry.instance().addStringLocalization("item.fruitSeeds" + i + "." + j + ".name", seedsNames[j + i * 4]);
        }
        leaves.seedsItem = new ItemFruitSeeds(leaves.seedsID, leaves.saplingID, seedTexture, subCount);
        leaves.seedsItem.setItemName("fruitSeeds" + i);
        leaves.seedsItem.serialNumber = i;
        LanguageRegistry.addName(leaves.seedsItem, "Fruit Seeds");
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
            manager.registerCommand(new CommandFruitDebug());
        }
    }

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(mod_jaffas.JaffaItem.lemons)),
                new ItemStack(mod_jaffas_trees.itemLemon),
                new ItemStack(mod_jaffas_trees.itemLemon),
                new ItemStack(mod_jaffas_trees.itemLemon),
                new ItemStack(mod_jaffas_trees.itemLemon));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(mod_jaffas.JaffaItem.oranges)),
                new ItemStack(mod_jaffas_trees.itemOrange),
                new ItemStack(mod_jaffas_trees.itemOrange),
                new ItemStack(mod_jaffas_trees.itemOrange),
                new ItemStack(mod_jaffas_trees.itemOrange));
        GameRegistry.addShapelessRecipe(new ItemStack(getJaffaItem(mod_jaffas.JaffaItem.plums)),
                new ItemStack(mod_jaffas_trees.itemPlum),
                new ItemStack(mod_jaffas_trees.itemPlum),
                new ItemStack(mod_jaffas_trees.itemPlum),
                new ItemStack(mod_jaffas_trees.itemPlum));
    }
}
