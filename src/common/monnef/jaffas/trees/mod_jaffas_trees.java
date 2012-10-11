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

import java.util.logging.Level;

@Mod(modid = "moen-jaffas-trees", name = "Jaffas - trees", version = "0.3.5", dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_trees {
    private static MinecraftServer server;

    public static mod_jaffas_trees instance;

    public static int startID;
    private int actualID;

    private int blockFruitSaplingID;
    public static BlockFruitSapling blockFruitSapling;

    private int blockFruitLeavesID;
    public static BlockFruitLeaves blockFruitLeaves;

    private int itemFruitSeedsID;
    public static ItemFruitSeeds itemFruitSeeds;

    private int getID() {
        return this.actualID++;
    }

    private int getBlockID() {
        return getID() + 256;
    }

    public mod_jaffas_trees() {
        instance = this;
    }

    @SidedProxy(clientSide = "monnef.jaffas.trees.ClientProxyTutorial", serverSide = "monnef.jaffas.trees.CommonProxyTutorial")
    public static CommonProxyTutorial proxy;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        this.startID = mod_jaffas.topDefaultID;
        this.actualID = this.startID;

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();

            if (this.actualID <= 0) {
                throw new RuntimeException("unable to get ID from parent");
            }

            blockFruitSaplingID = config.getOrCreateIntProperty("fruit tree sapling", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
            blockFruitLeavesID = config.getOrCreateIntProperty("fruit sapling", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();
            itemFruitSeedsID = config.getOrCreateIntProperty("fruit seeds", Configuration.CATEGORY_ITEM, getID()).getInt();

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (trees) can't read config file.");
        } finally {
            config.save();
        }
    }


    @Mod.Init
    public void load(FMLInitializationEvent event) {
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
        itemFruitSeeds = new ItemFruitSeeds(itemFruitSeedsID, blockFruitSapling.blockID, 1);
        itemFruitSeeds.setItemName("fruitSeeds");
        LanguageRegistry.addName(itemFruitSeeds, "Fruit Seeds");

        GameRegistry.registerTileEntity(TileEntityFruitLeaves.class, "fruitLeaves");

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
    }

    private void installRecipes() {
    }
}
