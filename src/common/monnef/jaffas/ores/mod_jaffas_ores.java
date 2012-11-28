package monnef.jaffas.ores;

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
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

@Mod(modid = "moen-jaffas-ores", name = "Jaffas - ores", version = Version.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_ores {
    @Mod.Instance("moen-jaffas-ores")
    public static mod_jaffas_ores instance;

    @SidedProxy(clientSide = "monnef.jaffas.ores.ClientProxy", serverSide = "monnef.jaffas.ores.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3600, 26244);
    private boolean debug;

    private int JaffarrolID;
    public ItemOres Jaffarrol;

    private int JaffarrolRawID;
    public ItemOres JaffarrolRaw;

    private int JaffarrolRefinedID;
    public ItemOres JaffarrolRefined;

    private int LimsewID;
    public ItemOres Limsew;

    private int BlockJaffarrolID;
    public BlockOres BlockJaffarrol;

    private int BlockLimsewID;
    public BlockOres BlockLimsew;

    public static String textureFile = "/jaffas_03.png";

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.setConfig(config);

            JaffarrolID = idProvider.getItemIDFromConfig("jaffarrol");
            JaffarrolRawID = idProvider.getItemIDFromConfig("jaffarrolRaw");
            JaffarrolRefinedID = idProvider.getItemIDFromConfig("jaffarrolRefined");
            LimsewID = idProvider.getItemIDFromConfig("limsew");

            BlockJaffarrolID = idProvider.getBlockIDFromConfig("jaffarrolBlock");
            BlockLimsewID = idProvider.getBlockIDFromConfig("limsewBlock");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (ores) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        System.out.println("ores module from 'Jaffas and more!' initialized");
    }

    private void createItems() {
        Jaffarrol = new ItemOres(JaffarrolID, 0);
        Jaffarrol.setItemName("jaffarrol");
        LanguageRegistry.addName(Jaffarrol, "Jaffarrol Ingot");

        JaffarrolRaw = new ItemOres(JaffarrolRawID, 1);
        JaffarrolRaw.setItemName("jaffarrolRaw");
        LanguageRegistry.addName(JaffarrolRaw, "Raw Jaffarrol");

        JaffarrolRefined = new ItemOres(JaffarrolRefinedID, 2);
        JaffarrolRefined.setItemName("jaffarrolRefined");
        LanguageRegistry.addName(JaffarrolRefined, "Refined Jaffarrol");

        Limsew = new ItemOres(LimsewID, 3);
        Limsew.setItemName("limsewDust");
        LanguageRegistry.addName(Limsew, "Limsew Dust");

        BlockJaffarrol = new BlockOres(BlockJaffarrolID, 4, Material.iron);
        BlockJaffarrol.setBlockName("blockOfJaffarrol").setHardness(6.0F).setResistance(12.0F);
        GameRegistry.registerBlock(BlockJaffarrol);
        LanguageRegistry.addName(BlockJaffarrol, "Block of Jaffarrol");

        BlockLimsew = new BlockOres(BlockLimsewID, 5, Material.iron);
        BlockLimsew.setBlockName("blockOfLimsew").setHardness(4f).setResistance(7f);
        GameRegistry.registerBlock(BlockLimsew);
        LanguageRegistry.addName(BlockLimsew, "Block of Limsew");
    }

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(JaffarrolRaw, 4),
                new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron),
                new ItemStack(Item.ingotIron), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotGold),
                new ItemStack(Item.redstone));

        GameRegistry.addSmelting(JaffarrolRaw.shiftedIndex, new ItemStack(Jaffarrol), 1f);
        GameRegistry.addSmelting(Jaffarrol.shiftedIndex, new ItemStack(JaffarrolRefined), 1f);

        GameRegistry.addShapelessRecipe(new ItemStack(BlockJaffarrol), new ItemStack(Jaffarrol), new ItemStack(Jaffarrol),
                new ItemStack(Jaffarrol), new ItemStack(Jaffarrol), new ItemStack(Jaffarrol), new ItemStack(Jaffarrol),
                new ItemStack(Jaffarrol), new ItemStack(Jaffarrol), new ItemStack(Jaffarrol));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockLimsew), new ItemStack(Limsew), new ItemStack(Limsew),
                new ItemStack(Limsew), new ItemStack(Limsew), new ItemStack(Limsew), new ItemStack(Limsew),
                new ItemStack(Limsew), new ItemStack(Limsew), new ItemStack(Limsew));

        GameRegistry.addShapelessRecipe(new ItemStack(Limsew, 2), new ItemStack(Item.diamond), new ItemStack(Item.lightStoneDust),
                new ItemStack(Item.lightStoneDust), new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone),
                new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone));
    }
}
