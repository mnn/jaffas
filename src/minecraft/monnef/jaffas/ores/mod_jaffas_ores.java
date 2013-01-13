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
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.mod_jaffas;
import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

@Mod(modid = "moen-jaffas-ores", name = "Jaffas - ores", version = Version.Version, dependencies = "required-after:moen-jaffas;required-after:moen-monnef-core;after:moen-jaffas-trees")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_ores {
    @Mod.Instance("moen-jaffas-ores")
    public static mod_jaffas_ores instance;

    @SidedProxy(clientSide = "monnef.jaffas.ores.ClientProxy", serverSide = "monnef.jaffas.ores.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3450, 26244);
    private boolean debug;

    private int JaffarrolID;
    public static ItemOres Jaffarrol;

    private int JaffarrolRawID;
    public static ItemOres JaffarrolRaw;

    private int JaffarrolRefinedID;
    public static ItemOres JaffarrolRefined;

    private int LimsewID;
    public static ItemOres Limsew;

    private int BlockJaffarrolID;
    public static BlockOres BlockJaffarrol;

    private int BlockLimsewID;
    public static BlockOres BlockLimsew;

    private int ItemCentralUnitID;
    public static ItemCentralUnit ItemCentralUnit;

    private int FunnelID;
    public static ItemOres Funnel;

    public static String textureFile = "/jaffas_03.png";

    public static JaffaCreativeTab CreativeTab = new JaffaCreativeTab("jaffas.ores");

    private int ItemCasingID;
    public static ItemOres ItemCasing;

    private int ItemCasingRefinedID;
    public static ItemOres ItemCasingRefined;

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

            ItemCentralUnitID = idProvider.getItemIDFromConfig("central unit");

            ItemCasingID = idProvider.getItemIDFromConfig("casing");
            ItemCasingRefinedID = idProvider.getItemIDFromConfig("casingRefined");

            FunnelID = idProvider.getItemIDFromConfig("funnel");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (ores) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        if (!ModuleManager.IsModuleEnabled(ModulesEnum.ores))
            return;

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.ores", "en_US", "Jaffas and more! Ores");

        ItemCentralUnit.registerNames();

        mod_jaffas.PrintInitialized(ModulesEnum.ores);
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

        ItemCentralUnit = new ItemCentralUnit(ItemCentralUnitID, 6);

        Funnel = new ItemOres(FunnelID, 16);
        Funnel.setItemName("funnel");
        LanguageRegistry.addName(Funnel, "Funnel");

        ItemCasing = new ItemOres(ItemCasingID, 13);
        ItemCasing.setItemName("casing");
        LanguageRegistry.addName(ItemCasing, "Casing");

        ItemCasingRefined = new ItemOres(ItemCasingRefinedID, 14);
        ItemCasingRefined.setItemName("casingRefined");
        LanguageRegistry.addName(ItemCasingRefined, "Refined Casing");
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

        GameRegistry.addShapelessRecipe(new ItemStack(Limsew, 9), new ItemStack(BlockLimsew));
        GameRegistry.addShapelessRecipe(new ItemStack(Jaffarrol, 9), new ItemStack(BlockJaffarrol));

        GameRegistry.addRecipe(new ItemStack(ItemCentralUnit, 1, 0), "JRJ", "RLR", "JRJ", 'J', Jaffarrol, 'R', Item.redstone, 'L', Limsew);
        GameRegistry.addRecipe(new ItemStack(ItemCentralUnit, 1, 1), "JRJ", "CLC", "JRJ", 'J', JaffarrolRefined, 'R', Item.redstone, 'L', Limsew, 'C', new ItemStack(ItemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(ItemCentralUnit, 1, 2), "LJL", "CLC", "LJL", 'J', JaffarrolRefined, 'R', Item.redstone, 'L', Limsew, 'C', new ItemStack(ItemCentralUnit, 1, 1));

        GameRegistry.addRecipe(new ItemStack(Funnel), "I I", "J J", " J ", 'I', Item.ingotIron, 'J', Jaffarrol);
        GameRegistry.addRecipe(new ItemStack(ItemCasing, 1, 0), "JJJ", "J J", "JJJ", 'J', Jaffarrol);
        GameRegistry.addSmelting(ItemCasing.shiftedIndex, new ItemStack(ItemCasingRefined), 1f);

        GameRegistry.addRecipe(new ItemStack(mod_jaffas.blockFridge), "I&I", "JBJ", "ICI", 'I', Item.ingotIron,
                '&', ItemCasing, 'J', Jaffarrol, 'B', Block.fenceIron, 'C', new ItemStack(ItemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_jaffas_trees.blockFruitCollector), "JFJ", "J@J", "JCJ",
                'J', Jaffarrol, 'F', Funnel, '@', ItemCasing, 'C', new ItemStack(ItemCentralUnit, 1, 2));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.fryingPan)), "  J", "II ", "II ", 'I', Item.ingotIron, 'J', mod_jaffas_ores.Jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatCleaver)), "II", "II", " J", 'I', Item.ingotIron, 'J', mod_jaffas_ores.Jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grinderMeat)), " FS", "III", "III", 'I', Item.ingotIron, 'F', mod_jaffas_ores.Funnel, 'S', Item.stick);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sink)), "J", "W", "I", 'J', mod_jaffas_ores.Jaffarrol, 'W', Item.bucketEmpty, 'I', Block.blockSteel);
    }

    private Item getItem(JaffaItem item) {
        return mod_jaffas.getItem(item);
    }
}
