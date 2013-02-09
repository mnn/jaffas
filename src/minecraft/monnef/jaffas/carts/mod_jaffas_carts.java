package monnef.jaffas.carts;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.IDProvider;
import monnef.jaffas.food.Reference;
import monnef.jaffas.carts.client.JaffaCreativeTab;
import monnef.jaffas.carts.entity.EntityLocomotive;
import monnef.jaffas.carts.item.ItemLocomotive;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.mod_jaffas;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

import static cpw.mods.fml.common.Mod.Init;
import static cpw.mods.fml.common.Mod.PreInit;

@Mod(modid = "moen-jaffas-carts", name = "Jaffas - carts", version = Reference.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_carts {
    @Instance("moen-jaffas-carts")
    public static mod_jaffas_carts instance;

    @SidedProxy(clientSide = "monnef.jaffas.carts.client.ClientProxy", serverSide = "monnef.jaffas.carts.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3800, 26844);
    private boolean debug;

    public static String textureFile = "/jaffas_06.png";

    public static JaffaCreativeTab CreativeTab;

    private int LocomotiveEntityID;

    private int ItemLocomotiveID;
    public static ItemLocomotive itemLocomotive;

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.linkWithConfig(config);

            ItemLocomotiveID = idProvider.getBlockIDFromConfig("locomotive");

            LocomotiveEntityID = idProvider.getEntityIDFromConfig("locomotive");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (carts) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Init
    public void load(FMLInitializationEvent event) {
        if (!ModuleManager.IsModuleEnabled(ModulesEnum.carts))
            return;

        CreativeTab = new JaffaCreativeTab("jaffas.carts");

        createItems();
        installRecipes();

        EntityRegistry.registerModEntity(EntityLocomotive.class, "locomotive", LocomotiveEntityID, this, 100, 5, false);

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.carts", "en_US", "Jaffas and more! Carts");

        mod_jaffas.PrintInitialized(ModulesEnum.carts);
    }

    private void createItems() {
        itemLocomotive = new ItemLocomotive(ItemLocomotiveID, 0);
        LanguageRegistry.addName(itemLocomotive, "Mini-Locomotive");
    }

    private void installRecipes() {
    }
}
