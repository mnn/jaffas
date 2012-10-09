package jaffas.trees;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import jaffas.food.mod_jaffas;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ServerCommandManager;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

@Mod(modid = "moen-jaffas-trees", name = "Jaffas - trees", version = "0.3.5", dependencies = "required-after:moen-jaffas")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_trees {
    private static MinecraftServer server;

    public static Object instance;

    public static int startID;
    private int actualID;

    private int blockFruitSaplingID;
    public static BlockFruitSapling blockFruitSapling;

    private int blockFruitLeavesID;
    public static BlockFruitLeaves blockFruitLeaves;

    private int getID() {
        return this.actualID++;
    }

    private int getBlockID() {
        return getID() + 256;
    }

    public mod_jaffas_trees() {
        instance = this;
    }

    @SidedProxy(clientSide = "jaffas.trees.ClientProxyTutorial", serverSide = "jaffas.trees.CommonProxyTutorial")
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
            blockFruitLeavesID = config.getOrCreateIntProperty("fruit leaves sapling", Configuration.CATEGORY_BLOCK, getBlockID()).getInt();

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (trees) can't read config file.");
        } finally {
            config.save();
        }
    }


    @Mod.Init
    public void load(FMLInitializationEvent event) {
        blockFruitSapling = new BlockFruitSapling(blockFruitSaplingID, 0);
        blockFruitSapling.setBlockName("fruitSapling").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(blockFruitSapling);

        blockFruitLeaves = new BlockFruitLeaves(blockFruitLeavesID, 1);
        blockFruitLeaves.setBlockName("fruitLeaves").setCreativeTab(CreativeTabs.tabDeco);
        GameRegistry.registerBlock(blockFruitLeaves);

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
