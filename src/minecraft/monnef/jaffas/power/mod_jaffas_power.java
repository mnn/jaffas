package monnef.jaffas.power;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.IDProvider;
import monnef.core.Version;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.mod_jaffas;
import monnef.jaffas.power.block.BlockAntenna;
import monnef.jaffas.power.block.BlockGenerator;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.item.ItemDebug;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

import static cpw.mods.fml.common.Mod.Init;
import static cpw.mods.fml.common.Mod.PreInit;

@Mod(modid = "moen-jaffas-power", name = "Jaffas - power", version = Version.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_power {
    @Instance("moen-jaffas-power")
    public static mod_jaffas_power instance;

    @SidedProxy(clientSide = "monnef.jaffas.power.client.ClientProxy", serverSide = "monnef.jaffas.power.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3750, 26644);
    private boolean debug;

    public static String textureFile = "/jaffas_05.png";

    public static JaffaCreativeTab CreativeTab = new JaffaCreativeTab("jaffas.power");

    private int ItemDebugID;
    public static ItemDebug ItemDebug;

    public static int renderID;

    public static BlockGenerator generator;
    private int blockGeneratorID;

    public static BlockAntenna antenna;
    private int blockAntennaID;

    private int ItemWrenchID;
    public static ItemWrench wrench;

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            //JaffarrolID = idProvider.getItemIDFromConfig("jaffarrol");
            ItemDebugID = idProvider.getItemIDFromConfig("debug");
            ItemWrenchID = idProvider.getItemIDFromConfig("pipeWrench");

            blockGeneratorID = idProvider.getBlockIDFromConfig("generator");
            blockAntennaID = idProvider.getBlockIDFromConfig("antenna");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (power) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Init
    public void load(FMLInitializationEvent event) {
        if (!ModuleManager.IsModuleEnabled(ModulesEnum.power))
            return;

        GameRegistry.registerTileEntity(TileEntityGenerator.class, "jp.generator");
        GameRegistry.registerTileEntity(TileEntityAntenna.class, "jp.antenna");

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.power", "en_US", "Jaffas and more! Power");

        mod_jaffas.PrintInitialized(ModulesEnum.power);
    }

    private void createItems() {
        generator = new BlockGenerator(blockGeneratorID, 0);
        GameRegistry.registerBlock(generator, generator.getBlockName());
        LanguageRegistry.addName(generator, "Generator");

        ItemDebug = new ItemDebug(ItemDebugID, 1);
        LanguageRegistry.addName(ItemDebug, "Power Debug Tool");

        antenna = new BlockAntenna(blockAntennaID, 2);
        GameRegistry.registerBlock(antenna, antenna.getBlockName());
        LanguageRegistry.addName(antenna, "Small Antenna");

        wrench = new ItemWrench(ItemWrenchID, 1);
        LanguageRegistry.addName(wrench, "Pipe Wrench");
    }

    private void installRecipes() {
    }
}
