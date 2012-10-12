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
import java.util.logging.Level;

import static monnef.jaffas.food.mod_jaffas.getJaffaItem;

@Mod(modid = "moen-jaffas-trees", name = "Jaffas - trees", version = "0.4.0", dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_trees {
    private static MinecraftServer server;

    public static mod_jaffas_trees instance;

    public static ArrayList<LeavesInfo> leavesList = new ArrayList<LeavesInfo>();
    public static final int leavesBlocksCount = 3;

    public static int startID;
    private int actualID;

    private int blockFruitSaplingID;
    public static BlockFruitSapling blockFruitSapling;

    private int blockFruitLeavesID;
    public static BlockFruitLeaves blockFruitLeaves;

    private int itemFruitSeedsID;
    public static ItemFruitSeeds itemFruitSeeds;

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

            blockFruitSaplingID = config.getOrCreateIntProperty("fruit tree sapling", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
            itemFruitSeedsID = config.getOrCreateIntProperty("fruit seeds", Configuration.CATEGORY_ITEM, getID()).getInt();
            itemLemonID = config.getOrCreateIntProperty("lemon", Configuration.CATEGORY_ITEM, getID()).getInt();
            itemOrangeID = config.getOrCreateIntProperty("orange", Configuration.CATEGORY_ITEM, getID()).getInt();
            itemPlumID = config.getOrCreateIntProperty("plum", Configuration.CATEGORY_ITEM, getID()).getInt();

            for(int i=0;i<leavesBlocksCount;i++){
                //blockFruitLeavesID = config.getOrCreateIntProperty("fruit sapling", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
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
        int c = 0;
        while (!mod_jaffas.instance.itemsReady) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if (c++ > 50) {
                throw new RuntimeException("items not ready");
            }
        } */

        blockFruitSapling = new BlockFruitSapling(blockFruitSaplingID, 15);
        blockFruitSapling.setBlockName("fruitSapling").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(blockFruitSapling);
        LanguageRegistry.addName(blockFruitSapling, "Fruit Sapling");

        blockFruitLeaves = new BlockFruitLeaves(blockFruitLeavesID, 0);
        blockFruitLeaves.setLeavesRequiresSelfNotify().setBlockName("fruitLeaves").setCreativeTab(CreativeTabs.tabDeco).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep);
        GameRegistry.registerBlock(blockFruitLeaves);
        LanguageRegistry.addName(blockFruitLeaves, "Leaves");

        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[0] + ".name", "[UNUSED] Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[1] + ".name", "Apple Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[2] + ".name", "Cocoa Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[3] + ".name", "Vanilla Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[4] + ".name", "Lemon Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[5] + ".name", "Orange Seeds");
        LanguageRegistry.instance().addStringLocalization("item.fruitSeeds." + BlockFruitLeaves.treeTypes[6] + ".name", "Plum Seeds");
        itemFruitSeeds = new ItemFruitSeeds(itemFruitSeedsID, blockFruitSapling.blockID, 1);
        itemFruitSeeds.setItemName("fruitSeeds");
        LanguageRegistry.addName(itemFruitSeeds, "Fruit Seeds");

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
