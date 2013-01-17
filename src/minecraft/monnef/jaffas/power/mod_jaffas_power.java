package monnef.jaffas.power;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.IDProvider;
import monnef.core.Version;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.mod_jaffas;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

import static cpw.mods.fml.common.Mod.Init;
import static cpw.mods.fml.common.Mod.PreInit;

@Mod(modid = "moen-jaffas-power", name = "Jaffas - power", version = Version.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_power {
    @Instance("moen-jaffas-power")
    public static mod_jaffas_power instance;

    @SidedProxy(clientSide = "monnef.jaffas.power.ClientProxy", serverSide = "monnef.jaffas.power.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3750, 26644);
    private boolean debug;

    public static String textureFile = "/jaffas_05.png";

    public static JaffaCreativeTab CreativeTab = new JaffaCreativeTab("jaffas.power");

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            //JaffarrolID = idProvider.getItemIDFromConfig("jaffarrol");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (power) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Init
    public void load(FMLInitializationEvent event) {
        if (!ModuleManager.IsModuleEnabled(ModulesEnum.ores))
            return;

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.power", "en_US", "Jaffas and more! Power");

        mod_jaffas.PrintInitialized(ModulesEnum.power);
    }

    private void createItems() {
    }

    private void installRecipes() {
    }
}
