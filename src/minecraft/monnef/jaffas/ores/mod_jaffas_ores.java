package monnef.jaffas.ores;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.Reference;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas_food;
import monnef.jaffas.mod_jaffas;
import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import java.util.logging.Level;

import static monnef.jaffas.food.crafting.Recipes.getItemStack;
import static monnef.jaffas.food.item.JaffaItem.*;

@Mod(modid = "moen-jaffas-ores", name = "Jaffas - ores", version = Reference.Version, dependencies = "required-after:moen-jaffas;after:moen-jaffas-trees")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_ores extends mod_jaffas {
    @Mod.Instance("moen-jaffas-ores")
    public static mod_jaffas_ores instance;

    @SidedProxy(clientSide = "monnef.jaffas.ores.ClientProxy", serverSide = "monnef.jaffas.ores.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3450, 26244);
    public boolean debug;

    private int JaffarrolID;
    public static ItemOres jaffarrol;

    private int JaffarrolRawID;
    public static ItemOres jaffarrolRaw;

    private int JaffarrolRefinedID;
    public static ItemOres jaffarrolRefined;

    private int LimsewID;
    public static ItemOres limsew;

    private int BlockJaffarrolID;
    public static BlockOres blockJaffarrol;

    private int BlockLimsewID;
    public static BlockOres blockLimsew;

    private int ItemCentralUnitID;
    public static ItemCentralUnit itemCentralUnit;

    private int FunnelID;
    public static ItemOres funnel;

    public static String textureFile = "/jaffas_03.png";

    public static JaffaCreativeTab CreativeTab;

    private int ItemCasingID;
    public static ItemOres itemCasing;

    private int ItemCasingRefinedID;
    public static ItemOres itemCasingRefined;

    private int BlockJaffarrolOreID;
    public static BlockOre blockJaffarrolOre;

    private int BlockLimsewOreID;
    public static BlockOre blockLimsewOre;

    private int ItemJAxeID;
    private int ItemJSpadeID;
    private int ItemJPickaxeID;
    private int ItemJHoeID;
    private int ItemJSwordID;

    public static ItemAxeOres axeJaffarrol;
    public static ItemPickaxeOres pickaxeJaffarrol;
    public static ItemSpadeOres spadeJaffarrol;
    public static ItemHoeOres hoeJaffarrol;
    public static ItemSwordOres swordJaffarrol;

    public static boolean generateOres;
    public static float switchgrassProbability;

    /*
    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    EMERALD(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);
    */
    public static EnumToolMaterial EnumToolMaterialJaffarrol = EnumHelper.addToolMaterial("Jaffarrol", 3, 1000, 9.0F, 3, 12);

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.linkWithConfig(config);

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

            BlockJaffarrolOreID = idProvider.getBlockIDFromConfig("jaffarrolOre");
            BlockLimsewOreID = idProvider.getBlockIDFromConfig("limsewOre");

            ItemJAxeID = idProvider.getItemIDFromConfig("jaffarrolAxe");
            ItemJPickaxeID = idProvider.getItemIDFromConfig("jaffarrolPickaxe");
            ItemJHoeID = idProvider.getItemIDFromConfig("jaffarrolHow");
            ItemJSpadeID = idProvider.getItemIDFromConfig("jaffarrolSpade");
            ItemJSwordID = idProvider.getItemIDFromConfig("jaffarrolSword");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            generateOres = config.get(Configuration.CATEGORY_GENERAL, "generateOres", true).getBoolean(true);
            switchgrassProbability = (float) config.get(Configuration.CATEGORY_GENERAL, "switchgrassProbability", 0.005, "Do not go too high, or face stack overflow caused by recursive chunk generation").getDouble(0.005);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (ores) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.ores))
            return;

        CreativeTab = new JaffaCreativeTab("jaffas.ores");

        createItems();
        installRecipes();
        addDrops();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.ores", "en_US", "Jaffas and more! Ores");

        itemCentralUnit.registerNames();

        OresWorldGen generator = new OresWorldGen();
        GameRegistry.registerWorldGenerator(generator);

        mod_jaffas_food.PrintInitialized(ModulesEnum.ores);
    }

    private void addDrops() {
        CustomDrop.addDrop(EntitySkeleton.class, limsew, 0.05f);
        CustomDrop.addDrop(EntityZombie.class, jaffarrolRaw, 0.15f);
        CustomDrop.addDrop(EntityEnderman.class, limsew, 0.25f);
    }

    private void createItems() {
        jaffarrol = new ItemOres(JaffarrolID, 0);
        jaffarrol.setItemName("jaffarrol");
        LanguageRegistry.addName(jaffarrol, "Jaffarrol Ingot");

        jaffarrolRaw = new ItemOres(JaffarrolRawID, 1);
        jaffarrolRaw.setItemName("jaffarrolRaw");
        LanguageRegistry.addName(jaffarrolRaw, "Raw Jaffarrol");

        jaffarrolRefined = new ItemOres(JaffarrolRefinedID, 2);
        jaffarrolRefined.setItemName("jaffarrolRefined");
        LanguageRegistry.addName(jaffarrolRefined, "Refined Jaffarrol");

        limsew = new ItemOres(LimsewID, 3);
        limsew.setItemName("limsewDust");
        LanguageRegistry.addName(limsew, "Limsew Dust");

        blockJaffarrol = new BlockOres(BlockJaffarrolID, 4, Material.iron);
        blockJaffarrol.setBlockName("blockOfJaffarrol").setHardness(6.0F).setResistance(12.0F);
        RegistryUtils.registerBlock(blockJaffarrol);
        LanguageRegistry.addName(blockJaffarrol, "Block of Jaffarrol");

        blockLimsew = new BlockOres(BlockLimsewID, 5, Material.iron);
        blockLimsew.setBlockName("blockOfLimsew").setHardness(4f).setResistance(7f);
        RegistryUtils.registerBlock(blockLimsew);
        LanguageRegistry.addName(blockLimsew, "Block of Limsew");

        itemCentralUnit = new ItemCentralUnit(ItemCentralUnitID, 6);

        funnel = new ItemOres(FunnelID, 16);
        funnel.setItemName("funnel");
        LanguageRegistry.addName(funnel, "Funnel");

        itemCasing = new ItemOres(ItemCasingID, 13);
        itemCasing.setItemName("casing");
        LanguageRegistry.addName(itemCasing, "Casing");

        itemCasingRefined = new ItemOres(ItemCasingRefinedID, 14);
        itemCasingRefined.setItemName("casingRefined");
        LanguageRegistry.addName(itemCasingRefined, "Refined Casing");

        // jarmor
        int renderIndexJaffarrol = mod_jaffas_food.proxy.addArmor("jarmor");
        mod_jaffas_food.instance.items.createJaffaArmor(jaffarrolHelmet, mod_jaffas_food.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.helm, "/jaffas_jarmor1.png", jaffarrol);
        mod_jaffas_food.instance.items.createJaffaArmor(jaffarrolChest, mod_jaffas_food.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.chest, "/jaffas_jarmor1.png", jaffarrol);
        mod_jaffas_food.instance.items.createJaffaArmor(jaffarrolLeggins, mod_jaffas_food.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.leggings, "/jaffas_jarmor2.png", jaffarrol);
        mod_jaffas_food.instance.items.createJaffaArmor(jaffarrolBoots, mod_jaffas_food.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.boots, "/jaffas_jarmor1.png", jaffarrol);

        blockJaffarrolOre = new BlockOre(BlockJaffarrolOreID, 17);
        MinecraftForge.setBlockHarvestLevel(blockJaffarrolOre, "pickaxe", 2);
        RegistryUtils.registerBlock(blockJaffarrolOre, "jaffarrolOre", "Jaffarrol Ore");

        blockLimsewOre = new BlockOre(BlockLimsewOreID, 18);
        MinecraftForge.setBlockHarvestLevel(blockLimsewOre, "pickaxe", 2);
        RegistryUtils.registerBlock(blockLimsewOre, "limsewOre", "Limsew Ore");

        createTools();
    }

    private void createTools() {
        EnumToolMaterialJaffarrol.customCraftingMaterial = jaffarrol;

        axeJaffarrol = new ItemAxeOres(ItemJAxeID, 21, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(axeJaffarrol, "axeJaffarrol", "Jaffarrol Axe");
        MinecraftForge.setToolClass(axeJaffarrol, "axe", 3);

        pickaxeJaffarrol = new ItemPickaxeOres(ItemJPickaxeID, 20, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(pickaxeJaffarrol, "pickaxeJaffarrol", "Jaffarrol Pickaxe");
        MinecraftForge.setToolClass(pickaxeJaffarrol, "pickaxe", 3);

        spadeJaffarrol = new ItemSpadeOres(ItemJSpadeID, 19, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(spadeJaffarrol, "spadeJaffarrol", "Jaffarrol Shovel");
        MinecraftForge.setToolClass(spadeJaffarrol, "shovel", 3);

        hoeJaffarrol = new ItemHoeOres(ItemJHoeID, 22, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(hoeJaffarrol, "hoeJaffarrol", "Jaffarrol Hoe");

        swordJaffarrol = new ItemSwordOres(ItemJSwordID, 23, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(swordJaffarrol, "swordJaffarrol", "Jaffarrol Sword");
    }

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrolRaw, 4),
                new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron),
                new ItemStack(Item.ingotIron), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotGold),
                new ItemStack(Item.redstone));

        GameRegistry.addSmelting(jaffarrolRaw.shiftedIndex, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(jaffarrol.shiftedIndex, new ItemStack(jaffarrolRefined), 1f);

        GameRegistry.addShapelessRecipe(new ItemStack(blockJaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol),
                new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol),
                new ItemStack(jaffarrol), new ItemStack(jaffarrol), new ItemStack(jaffarrol));

        GameRegistry.addShapelessRecipe(new ItemStack(blockLimsew), new ItemStack(limsew), new ItemStack(limsew),
                new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew),
                new ItemStack(limsew), new ItemStack(limsew), new ItemStack(limsew));

        GameRegistry.addShapelessRecipe(new ItemStack(limsew, 2), new ItemStack(Item.diamond), new ItemStack(Item.lightStoneDust),
                new ItemStack(Item.lightStoneDust), new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone),
                new ItemStack(Item.redstone), new ItemStack(Item.redstone), new ItemStack(Item.redstone));

        GameRegistry.addShapelessRecipe(new ItemStack(limsew, 9), new ItemStack(blockLimsew));
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrol, 9), new ItemStack(blockJaffarrol));

        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 0), "JRJ", "RLR", "JRJ", 'J', jaffarrol, 'R', Item.redstone, 'L', limsew);
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 1), "JRJ", "CLC", "JRJ", 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(itemCentralUnit, 1, 2), "LJL", "CLC", "LJL", 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew, 'C', new ItemStack(itemCentralUnit, 1, 1));

        GameRegistry.addRecipe(new ItemStack(funnel), "I I", "J J", " J ", 'I', Item.ingotIron, 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(itemCasing, 1, 0), "JJJ", "J J", "JJJ", 'J', jaffarrol);
        GameRegistry.addSmelting(itemCasing.shiftedIndex, new ItemStack(itemCasingRefined), 1f);

        GameRegistry.addRecipe(new ItemStack(mod_jaffas_food.blockFridge), "I&I", "JBJ", "ICI", 'I', Item.ingotIron,
                '&', itemCasing, 'J', jaffarrol, 'B', Block.fenceIron, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_jaffas_trees.blockFruitCollector), "JFJ", "J@J", "JCJ",
                'J', jaffarrol, 'F', funnel, '@', itemCasing, 'C', new ItemStack(itemCentralUnit, 1, 2));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.fryingPan)), "  J", "II ", "II ", 'I', Item.ingotIron, 'J', mod_jaffas_ores.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatCleaver)), "II", "II", " J", 'I', Item.ingotIron, 'J', mod_jaffas_ores.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grinderMeat)), " FS", "III", "III", 'I', Item.ingotIron, 'F', mod_jaffas_ores.funnel, 'S', Item.stick);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sink)), "J", "W", "I", 'J', mod_jaffas_ores.jaffarrol, 'W', Item.bucketEmpty, 'I', Block.blockSteel);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grater)), " J ", "III", "III", 'J', mod_jaffas_ores.jaffarrol, 'I', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bottleEmpty), 4), " J ", "G G", "GGG", 'J', mod_jaffas_ores.jaffarrol, 'G', Block.glass);

        GameRegistry.addRecipe(new ItemStack(mod_jaffas_food.blockBoard), "  W", "JJ ", "JJ ", 'W', Block.wood, 'J', mod_jaffas_ores.jaffarrol);

        // jarmor
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolHelmet)), "JJJ", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolChest)), "J J", "JJJ", "JJJ", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolBoots)), "J J", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolLeggins)), "JJJ", "J J", "J J", 'J', jaffarrol);

        // spawn stones
        if (mod_jaffas_food.spawnStonesEnabled) {
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneLittle)), " I ", "IJI", " I ", 'I', Item.ingotIron, 'J', jaffarrol);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneMedium)), "GRG", "LJL", "GRG", 'G', Item.ingotGold, 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneMedium)), "GLG", "LJR", "GRG", 'G', Item.ingotGold, 'J', jaffarrolRefined, 'R', Item.redstone, 'L', limsew);
            GameRegistry.addRecipe(new ItemStack(getItem(spawnStoneBig)), "LJB", "JDJ", "BJL", 'L', limsew, 'J', jaffarrolRefined, 'B', Item.blazeRod, 'D', Item.diamond);
        }

        GameRegistry.addRecipe(new ItemStack(getItem(juiceBottle), 2), "GJG", "G G", "GGG", 'G', Block.glass, 'J', jaffarrol);

        GameRegistry.addSmelting(BlockJaffarrolOreID, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(BlockLimsewOreID, new ItemStack(limsew), 1f);

        GameRegistry.addRecipe(new ItemStack(swordJaffarrol), "J", "J", "G", 'J', jaffarrol, 'G', Item.ingotGold);
        GameRegistry.addRecipe(new ItemStack(hoeJaffarrol), "JJ", " S", " S", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(spadeJaffarrol), "J", "S", "S", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(pickaxeJaffarrol), "JJJ", " S ", " S ", 'J', jaffarrol, 'S', Item.stick);
        GameRegistry.addRecipe(new ItemStack(axeJaffarrol), "JJ ", "JS ", " S ", 'J', jaffarrol, 'S', Item.stick);

        GameRegistry.addRecipe(getItemStack(cookingPot), "I I", "I I", "IJI", 'I', Item.ingotIron, 'J', jaffarrol);

        OreDictionary.registerOre("oreJaffarrol", blockJaffarrolOre);
        OreDictionary.registerOre("oreLimsew", blockLimsewOre);

        /*
        ItemStack hoe = new ItemStack(hoeJaffarrol);
        hoe.addEnchantment(Enchantment.fortune, 2);
        hoe.addEnchantment(Enchantment.unbreaking, 2);
        GameRegistry.addRecipe(hoe, "JJL", "LS ", " S ", 'J', jaffarrol, 'S', Item.stick, 'L', limsew);
        */
    }

    private Item getItem(JaffaItem item) {
        return mod_jaffas_food.getItem(item);
    }
}
