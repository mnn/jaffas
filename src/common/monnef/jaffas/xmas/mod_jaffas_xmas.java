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
import monnef.jaffas.food.ModuleManager;
import monnef.jaffas.food.ModulesEnum;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
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

    private int BlockPresentID;
    public static BlockPresent BlockPresent;

    public static String textureFile = "/jaffas_04.png";
    public static int renderID;

    public static JaffaCreativeTab CreativeTab = new JaffaCreativeTab("jaffas.xmas");

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            BlockCandyID = idProvider.getBlockIDFromConfig("candy");
            ItemGiantCandyID = idProvider.getItemIDFromConfig("giant candy");

            BlockPresentID = idProvider.getBlockIDFromConfig("present");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (xmas) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        if (!ModuleManager.IsModuleEnabled(ModulesEnum.xmas))
            return;

        GameRegistry.registerTileEntity(TileEntityCandy.class, "jaffas.candy");
        GameRegistry.registerTileEntity(TileEntityPresent.class, "jaffas.present");

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.xmas", "en_US", "Jaffas and more! Christmas");

        mod_jaffas.PrintInitialized(ModulesEnum.xmas);
    }

    private void createItems() {
        BlockCandy = new BlockCandy(BlockCandyID, 16, Material.wood);
        BlockCandy.setBlockName("jaffas.candy");
        GameRegistry.registerBlock(BlockCandy);
        LanguageRegistry.addName(BlockCandy, "Candy Cane");

        ItemGiantCandy = new ItemGiantCandy(ItemGiantCandyID, 16);
        ItemGiantCandy.setItemName("jaffas.giantCandy");
        LanguageRegistry.addName(ItemGiantCandy, "Giant Candy Cane");

        BlockPresent = new BlockPresent(BlockPresentID, 0, Material.cloth, ItemBlockPresent.count);
        GameRegistry.registerBlock(BlockPresent, ItemBlockPresent.class);
    }

    private void installRecipes() {
        GameRegistry.addRecipe(new ItemStack(ItemGiantCandy), " W ", "WRW", "WB ", 'W',
                new ItemStack(Block.planks, 1, -1), 'R', new ItemStack(Item.dyePowder, 1, 1), 'B', new ItemStack(Item.dyePowder, 1, 15));

        GameRegistry.addRecipe(new ItemStack(BlockPresent, 1, 0), "RS ", "PPP", "PPP", 'R', new ItemStack(Item.dyePowder, 1, 1),
                'S', Item.silk, 'P', Item.paper);
    }
}
