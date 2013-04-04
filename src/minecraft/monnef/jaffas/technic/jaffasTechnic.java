package monnef.jaffas.technic;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.CustomDrop;
import monnef.jaffas.food.item.ItemJaffaPlate;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.jaffasMod;
import monnef.jaffas.technic.block.BlockOre;
import monnef.jaffas.technic.block.BlockTechnic;
import monnef.jaffas.technic.client.JaffaCreativeTab;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityLocomotive;
import monnef.jaffas.technic.item.ItemAxeTechnic;
import monnef.jaffas.technic.item.ItemCentralUnit;
import monnef.jaffas.technic.item.ItemHoeTechnic;
import monnef.jaffas.technic.item.ItemLocomotive;
import monnef.jaffas.technic.item.ItemPickaxeTechnic;
import monnef.jaffas.technic.item.ItemSpadeTechnic;
import monnef.jaffas.technic.item.ItemSwordTechnic;
import monnef.jaffas.technic.item.ItemTechnic;
import monnef.jaffas.trees.JaffasTrees;
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
import static monnef.jaffas.food.item.JaffaItem.cookingPot;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolBoots;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolChest;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolHelmet;
import static monnef.jaffas.food.item.JaffaItem.jaffarrolLeggins;
import static monnef.jaffas.food.item.JaffaItem.juiceBottle;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneBig;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneLittle;
import static monnef.jaffas.food.item.JaffaItem.spawnStoneMedium;
import static monnef.jaffas.technic.Reference.ModId;
import static monnef.jaffas.technic.Reference.ModName;
import static monnef.jaffas.technic.Reference.Version;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas;after:Jaffas-Trees")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class JaffasTechnic extends jaffasMod {
    @Mod.Instance(ModId)
    public static JaffasTechnic instance;

    @SidedProxy(clientSide = "monnef.jaffas.technic.client.ClientProxy", serverSide = "monnef.jaffas.technic.common.CommonProxy")
    public static CommonProxy proxy;

    private static IDProvider idProvider = new IDProvider(3450, 26244);
    public boolean debug;

    private int JaffarrolID;
    public static ItemTechnic jaffarrol;

    private int JaffarrolRawID;
    public static ItemTechnic jaffarrolRaw;

    private int JaffarrolRefinedID;
    public static ItemTechnic jaffarrolRefined;

    private int LimsewID;
    public static ItemTechnic limsew;

    private int BlockJaffarrolID;
    public static BlockTechnic blockJaffarrol;

    private int BlockLimsewID;
    public static BlockTechnic blockLimsew;

    private int ItemCentralUnitID;
    public static ItemCentralUnit itemCentralUnit;

    private int FunnelID;
    public static ItemTechnic funnel;

    public static String textureFile = "/jaffas_03.png";

    public static JaffaCreativeTab CreativeTab;

    private int ItemCasingID;
    public static ItemTechnic itemCasing;

    private int ItemCasingRefinedID;
    public static ItemTechnic itemCasingRefined;

    private int BlockJaffarrolOreID;
    public static BlockOre blockJaffarrolOre;

    private int BlockLimsewOreID;
    public static BlockOre blockLimsewOre;

    private int ItemJAxeID;
    private int ItemJSpadeID;
    private int ItemJPickaxeID;
    private int ItemJHoeID;
    private int ItemJSwordID;

    public static ItemAxeTechnic axeJaffarrol;
    public static ItemPickaxeTechnic pickaxeJaffarrol;
    public static ItemSpadeTechnic spadeJaffarrol;
    public static ItemHoeTechnic hoeJaffarrol;
    public static ItemSwordTechnic swordJaffarrol;

    public static boolean generateOres;
    public static float switchgrassProbability;

    private int LocomotiveEntityID;

    private int ItemLocomotiveID;
    public static ItemLocomotive itemLocomotive;

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
            ItemLocomotiveID = idProvider.getBlockIDFromConfig("locomotive");

            LocomotiveEntityID = idProvider.getEntityIDFromConfig("locomotive");

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

            generateOres = config.get(Configuration.CATEGORY_GENERAL, "generateOres", true).getBoolean(true);
            switchgrassProbability = (float) config.get(Configuration.CATEGORY_GENERAL, "switchgrassProbability", 0.005, "Do not go too high, or face stack overflow caused by recursive chunk generation").getDouble(0.005);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (technic) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.technic))
            return;

        CreativeTab = new JaffaCreativeTab("jaffas.technic");

        createItems();
        installRecipes();
        addDrops();

        EntityRegistry.registerModEntity(EntityLocomotive.class, "locomotive", LocomotiveEntityID, this, 100, 5, false);

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.technic", "en_US", "Jaffas and more! Ores");

        itemCentralUnit.registerNames();

        TechnicWorldGen generator = new TechnicWorldGen();
        GameRegistry.registerWorldGenerator(generator);

        JaffasFood.PrintInitialized(ModulesEnum.technic);
    }

    private void addDrops() {
        CustomDrop.addDrop(EntitySkeleton.class, limsew, 0.05f);
        CustomDrop.addDrop(EntityZombie.class, jaffarrolRaw, 0.15f);
        CustomDrop.addDrop(EntityEnderman.class, limsew, 0.25f);
    }

    private void createItems() {
        jaffarrol = new ItemTechnic(JaffarrolID, 0);
        jaffarrol.setUnlocalizedName("jaffarrol");
        LanguageRegistry.addName(jaffarrol, "Jaffarrol Ingot");

        jaffarrolRaw = new ItemTechnic(JaffarrolRawID, 1);
        jaffarrolRaw.setUnlocalizedName("jaffarrolRaw");
        LanguageRegistry.addName(jaffarrolRaw, "Raw Jaffarrol");

        jaffarrolRefined = new ItemTechnic(JaffarrolRefinedID, 2);
        jaffarrolRefined.setUnlocalizedName("jaffarrolRefined");
        LanguageRegistry.addName(jaffarrolRefined, "Refined Jaffarrol");

        limsew = new ItemTechnic(LimsewID, 3);
        limsew.setUnlocalizedName("limsewDust");
        LanguageRegistry.addName(limsew, "Limsew Dust");

        blockJaffarrol = new BlockTechnic(BlockJaffarrolID, 4, Material.iron);
        blockJaffarrol.setUnlocalizedName("blockOfJaffarrol").setHardness(6.0F).setResistance(12.0F);
        RegistryUtils.registerBlock(blockJaffarrol);
        LanguageRegistry.addName(blockJaffarrol, "Block of Jaffarrol");

        blockLimsew = new BlockTechnic(BlockLimsewID, 5, Material.iron);
        blockLimsew.setUnlocalizedName("blockOfLimsew").setHardness(4f).setResistance(7f);
        RegistryUtils.registerBlock(blockLimsew);
        LanguageRegistry.addName(blockLimsew, "Block of Limsew");

        itemCentralUnit = new ItemCentralUnit(ItemCentralUnitID, 6);

        funnel = new ItemTechnic(FunnelID, 16);
        funnel.setUnlocalizedName("funnel");
        LanguageRegistry.addName(funnel, "Funnel");

        itemCasing = new ItemTechnic(ItemCasingID, 13);
        itemCasing.setUnlocalizedName("casing");
        LanguageRegistry.addName(itemCasing, "Casing");

        itemCasingRefined = new ItemTechnic(ItemCasingRefinedID, 14);
        itemCasingRefined.setUnlocalizedName("casingRefined");
        LanguageRegistry.addName(itemCasingRefined, "Refined Casing");

        // jarmor
        int renderIndexJaffarrol = JaffasFood.proxy.addArmor("jarmor");
        JaffasFood.instance.items.createJaffaArmor(jaffarrolHelmet, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.helm, "/jaffas_jarmor1.png", jaffarrol);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolChest, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.chest, "/jaffas_jarmor1.png", jaffarrol);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolLeggins, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.leggings, "/jaffas_jarmor2.png", jaffarrol);
        JaffasFood.instance.items.createJaffaArmor(jaffarrolBoots, JaffasFood.EnumArmorMaterialJaffarrol, renderIndexJaffarrol, ItemJaffaPlate.ArmorType.boots, "/jaffas_jarmor1.png", jaffarrol);

        blockJaffarrolOre = new BlockOre(BlockJaffarrolOreID, 17);
        MinecraftForge.setBlockHarvestLevel(blockJaffarrolOre, "pickaxe", 2);
        RegistryUtils.registerBlock(blockJaffarrolOre, "jaffarrolOre", "Jaffarrol Ore");

        blockLimsewOre = new BlockOre(BlockLimsewOreID, 18);
        MinecraftForge.setBlockHarvestLevel(blockLimsewOre, "pickaxe", 2);
        RegistryUtils.registerBlock(blockLimsewOre, "limsewOre", "Limsew Ore");

        itemLocomotive = new ItemLocomotive(ItemLocomotiveID, 24);
        LanguageRegistry.addName(itemLocomotive, "Mini-Locomotive");

        createTools();
    }

    private void createTools() {
        EnumToolMaterialJaffarrol.customCraftingMaterial = jaffarrol;

        axeJaffarrol = new ItemAxeTechnic(ItemJAxeID, 21, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(axeJaffarrol, "axeJaffarrol", "Jaffarrol Axe");
        MinecraftForge.setToolClass(axeJaffarrol, "axe", 3);

        pickaxeJaffarrol = new ItemPickaxeTechnic(ItemJPickaxeID, 20, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(pickaxeJaffarrol, "pickaxeJaffarrol", "Jaffarrol Pickaxe");
        MinecraftForge.setToolClass(pickaxeJaffarrol, "pickaxe", 3);

        spadeJaffarrol = new ItemSpadeTechnic(ItemJSpadeID, 19, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(spadeJaffarrol, "spadeJaffarrol", "Jaffarrol Shovel");
        MinecraftForge.setToolClass(spadeJaffarrol, "shovel", 3);

        hoeJaffarrol = new ItemHoeTechnic(ItemJHoeID, 22, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(hoeJaffarrol, "hoeJaffarrol", "Jaffarrol Hoe");

        swordJaffarrol = new ItemSwordTechnic(ItemJSwordID, 23, EnumToolMaterialJaffarrol);
        RegistryUtils.registerItem(swordJaffarrol, "swordJaffarrol", "Jaffarrol Sword");
    }

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(jaffarrolRaw, 4),
                new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron),
                new ItemStack(Item.ingotIron), new ItemStack(Item.ingotGold), new ItemStack(Item.ingotGold),
                new ItemStack(Item.redstone));

        GameRegistry.addSmelting(jaffarrolRaw.itemID, new ItemStack(jaffarrol), 1f);
        GameRegistry.addSmelting(jaffarrol.itemID, new ItemStack(jaffarrolRefined), 1f);

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
        GameRegistry.addSmelting(itemCasing.itemID, new ItemStack(itemCasingRefined), 1f);

        GameRegistry.addRecipe(new ItemStack(JaffasFood.blockFridge), "I&I", "JBJ", "ICI", 'I', Item.ingotIron,
                '&', itemCasing, 'J', jaffarrol, 'B', Block.fenceIron, 'C', new ItemStack(itemCentralUnit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(JaffasTrees.blockFruitCollector), "JFJ", "J@J", "JCJ",
                'J', jaffarrol, 'F', funnel, '@', itemCasing, 'C', new ItemStack(itemCentralUnit, 1, 2));

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.fryingPan)), "  J", "II ", "II ", 'I', Item.ingotIron, 'J', JaffasTechnic.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.meatCleaver)), "II", "II", " J", 'I', Item.ingotIron, 'J', JaffasTechnic.jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grinderMeat)), " FS", "III", "III", 'I', Item.ingotIron, 'F', JaffasTechnic.funnel, 'S', Item.stick);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.sink)), "J", "W", "I", 'J', JaffasTechnic.jaffarrol, 'W', Item.bucketEmpty, 'I', Block.blockSteel);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.grater)), " J ", "III", "III", 'J', JaffasTechnic.jaffarrol, 'I', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.bottleEmpty), 4), " J ", "G G", "GGG", 'J', JaffasTechnic.jaffarrol, 'G', Block.glass);

        GameRegistry.addRecipe(new ItemStack(JaffasFood.blockBoard), "  W", "JJ ", "JJ ", 'W', Block.wood, 'J', JaffasTechnic.jaffarrol);

        // jarmor
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolHelmet)), "JJJ", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolChest)), "J J", "JJJ", "JJJ", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolBoots)), "J J", "J J", 'J', jaffarrol);
        GameRegistry.addRecipe(new ItemStack(getItem(jaffarrolLeggins)), "JJJ", "J J", "J J", 'J', jaffarrol);

        // spawn stones
        if (JaffasFood.spawnStonesEnabled) {
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

        GameRegistry.addRecipe(new ItemStack(itemLocomotive), "I F", "BCB", "III", 'F', funnel, 'I', Item.ingotIron, 'B', Block.blockSteel, 'C', itemCasingRefined);
    }

    private Item getItem(JaffaItem item) {
        return JaffasFood.getItem(item);
    }
}
