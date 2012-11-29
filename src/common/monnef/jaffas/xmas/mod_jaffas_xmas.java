package monnef.jaffas.xmas;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import monnef.core.IDProvider;
import monnef.core.Version;
import monnef.jaffas.food.mod_jaffas;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

@Mod(modid = "moen-jaffas-xmas", name = "Jaffas - xmas", version = Version.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_xmas {
    @Mod.Instance("moen-jaffas-xmas")
    public static mod_jaffas_xmas instance;

    @SidedProxy(clientSide = "monnef.jaffas.xmas.ClientProxy", serverSide = "monnef.jaffas.xmas.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3650, 26444);
    private boolean debug;

    private int BlockCandyID;
    public BlockXmas BlockCandy;

    public static String textureFile = "/jaffas_04.png";

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (xmas) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        if (!mod_jaffas.IsModuleEnable(mod_jaffas.ModulesEnum.xmas))
            return;

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        System.out.println("xmas module from 'Jaffas and more!' initialized");
    }

    private void createItems() {
    }

    private void installRecipes() {
    }
}
