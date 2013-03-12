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
import monnef.jaffas.food.Reference;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.mod_jaffas_food;
import monnef.jaffas.mod_jaffas;
import monnef.jaffas.ores.mod_jaffas_ores;
import monnef.jaffas.power.block.BlockAntenna;
import monnef.jaffas.power.block.BlockGenerator;
import monnef.jaffas.power.block.BlockLightningConductor;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.power.item.ItemDebug;
import monnef.jaffas.power.item.ItemLinkTool;
import monnef.jaffas.power.item.ItemPipeWrench;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;
import java.util.logging.Level;

import static cpw.mods.fml.common.Mod.Init;
import static cpw.mods.fml.common.Mod.PreInit;

@Mod(modid = "moen-jaffas-power", name = "Jaffas - power", version = Reference.Version, dependencies = "required-after:moen-jaffas;after:moen-jaffas-ores")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_power extends mod_jaffas {
    @Instance("moen-jaffas-power")
    public static mod_jaffas_power instance;

    @SidedProxy(clientSide = "monnef.jaffas.power.client.ClientProxy", serverSide = "monnef.jaffas.power.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3750, 26644);
    private boolean debug;

    public static String textureFile = "/jaffas_05.png";

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
            blockLightningConductorID = idProvider.getBlockIDFromConfig("lightningConductor");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            getAnnotatedItemIDs();

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (power) can't read config file.");
        } finally {
            config.save();
        }
    }

    private void getAnnotatedItemIDs() {
        for (Field field : this.getClass().getFields()) {
            ItemID annotation = field.getAnnotation(ItemID.class);
            if (annotation != null) {
                idProvider.getItemIDFromConfig(annotation.nameInConfig().isEmpty() ? field.getName() : annotation.nameInConfig());
            }
        }
    }

    @Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.power))
            return;

        GameRegistry.registerTileEntity(TileEntityGenerator.class, "jp.generator");
        GameRegistry.registerTileEntity(TileEntityAntenna.class, "jp.antenna");

        CreativeTab = new JaffaCreativeTab("jaffas.power");

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.power", "en_US", "Jaffas and more! Power");

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());
        MinecraftForge.EVENT_BUS.register(new LightingHandler());

        mod_jaffas_food.PrintInitialized(ModulesEnum.power);
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

        lightningConductor = new BlockLightningConductor(blockLightningConductorID, 5);
        RegistryUtils.registerBlock(lightningConductor, "Lightning Conductor");
    }

    private void installRecipes() {
        if (ModuleManager.IsModuleEnabled(ModulesEnum.ores)) {
            GameRegistry.addRecipe(new ItemStack(lightningConductor), "J", "J", "B", 'J', mod_jaffas_ores.jaffarrol, 'B', Block.blockSteel);
        }
    }
}
