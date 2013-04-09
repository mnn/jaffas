package monnef.jaffas.power;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.jaffasMod;
import monnef.jaffas.power.block.BlockAntenna;
import monnef.jaffas.power.block.BlockGenerator;
import monnef.jaffas.power.block.BlockLightningConductor;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityLightningConductor;
import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.common.JaffaCreativeTab;
import monnef.jaffas.power.common.LightingHandler;
import monnef.jaffas.power.item.ItemDebug;
import monnef.jaffas.power.item.ItemLinkTool;
import monnef.jaffas.power.item.ItemPipeWrench;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.logging.Level;

import static cpw.mods.fml.common.Mod.Init;
import static cpw.mods.fml.common.Mod.PreInit;
import static monnef.jaffas.power.common.Reference.ModId;
import static monnef.jaffas.power.common.Reference.ModName;
import static monnef.jaffas.power.common.Reference.Version;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas;after:Jaffas-Technic")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class JaffasPower extends jaffasMod {
    @Instance("Jaffas-Power")
    public static JaffasPower instance;

    @SidedProxy(clientSide = "monnef.jaffas.power.client.ClientProxy", serverSide = "monnef.jaffas.power.common.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3750, 26644);
    private boolean debug;

    public static JaffaCreativeTab CreativeTab;

    private int ItemDebugID;
    public static ItemDebug ItemDebug;

    public static int renderID;

    public static BlockGenerator generator;
    private int blockGeneratorID;

    public static BlockAntenna antenna;
    private int blockAntennaID;

    private int ItemWrenchID;
    public static ItemPipeWrench wrench;

    private int ItemLinkToolID;
    public static ItemLinkTool linkTool;

    private int blockLightningConductorID;
    public static BlockLightningConductor lightningConductor;
    public static int lightningConductorRadius = 30;
    public static boolean lightningConductorEnabled;

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.linkWithConfig(config);

            //JaffarrolID = idProvider.getItemIDFromConfig("jaffarrol");
            ItemDebugID = idProvider.getItemIDFromConfig("debug");
            ItemWrenchID = idProvider.getItemIDFromConfig("pipeWrench");
            ItemLinkToolID = idProvider.getItemIDFromConfig("linkTool");

            blockGeneratorID = idProvider.getBlockIDFromConfig("generator");
            blockAntennaID = idProvider.getBlockIDFromConfig("antenna");

            lightningConductorEnabled = config.get(Configuration.CATEGORY_GENERAL, "lightningConductorEnabled", true).getBoolean(true);
            if (lightningConductorEnabled) {
                blockLightningConductorID = idProvider.getBlockIDFromConfig("lightningConductor");
            }

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (power) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.power))
            return;

        GameRegistry.registerTileEntity(TileEntityGenerator.class, "jp.generator");
        GameRegistry.registerTileEntity(TileEntityAntenna.class, "jp.antenna");
        GameRegistry.registerTileEntity(TileEntityLightningConductor.class, "jp.lightningConductor");

        CreativeTab = new JaffaCreativeTab("jaffas.power");

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.power", "en_US", "Jaffas and more! Power");

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());
        if (lightningConductorEnabled) {
            MinecraftForge.EVENT_BUS.register(new LightingHandler());
        }

        JaffasFood.PrintInitialized(ModulesEnum.power);
    }

    private void createItems() {
        generator = new BlockGenerator(blockGeneratorID, 5);
        RegistryUtils.registerBlock(generator, "Generator");

        ItemDebug = new ItemDebug(ItemDebugID, 1);
        LanguageRegistry.addName(ItemDebug, "Power Debug Tool");

        antenna = new BlockAntenna(blockAntennaID, 5);
        RegistryUtils.registerBlock(antenna, "Small Antenna");

        wrench = new ItemPipeWrench(ItemWrenchID, 1);
        LanguageRegistry.addName(wrench, "Pipe Wrench");

        linkTool = new ItemLinkTool(ItemLinkToolID, 0);
        RegistryUtils.registerItem(linkTool, "itemLinkTool", "Link Gun");

        if (lightningConductorEnabled) {
            lightningConductor = new BlockLightningConductor(blockLightningConductorID, 5);
            RegistryUtils.registerBlock(lightningConductor, "Lightning Conductor");
        }
    }

    private void installRecipes() {
        if (lightningConductorEnabled && ModuleManager.IsModuleEnabled(ModulesEnum.technic)) {
            GameRegistry.addRecipe(new ItemStack(lightningConductor), "J", "J", "B", 'J', JaffasTechnic.jaffarrol, 'B', Block.blockSteel);
        }
    }
}
