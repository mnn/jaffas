package monnef.jaffas.xmas;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.IDProvider;
import monnef.core.Version;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.Material;
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
    public static BlockCandy BlockCandy;

    private int ItemGiantCandyID;
    public static ItemGiantCandy ItemGiantCandy;

    public static String textureFile = "/jaffas_04.png";
    public static int renderID;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            BlockCandyID = idProvider.getBlockIDFromConfig("candy");
            ItemGiantCandyID = idProvider.getItemIDFromConfig("giant candy");

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

        GameRegistry.registerTileEntity(TileEntityCandy.class, "jaffas.candy");

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        mod_jaffas.PrintInitialized(mod_jaffas.ModulesEnum.xmas);
    }

    private void createItems() {
        BlockCandy = new BlockCandy(BlockCandyID, 0, Material.wood);
        BlockCandy.setBlockName("jaffas.candy");
        GameRegistry.registerBlock(BlockCandy);
        LanguageRegistry.addName(BlockCandy, "Candy Cane");

        ItemGiantCandy = new ItemGiantCandy(ItemGiantCandyID, 0);
        ItemGiantCandy.setItemName("jaffas.giantCandy");
        LanguageRegistry.addName(ItemGiantCandy, "Giant Candy Cane");
    }

    private void installRecipes() {
    }
}
