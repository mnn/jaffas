package monnef.jaffas.xmas;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.IDProvider;
import monnef.jaffas.food.Reference;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import monnef.jaffas.food.mod_jaffas_food;
import monnef.jaffas.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import java.util.logging.Level;

@Mod(modid = "Jaffas-Xmas", name = "Jaffas - Xmas", version = Reference.Version, dependencies = "required-after:Jaffas")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas_xmas extends mod_jaffas {
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

    public static JaffaCreativeTab CreativeTab;
    private Items items;

    public mod_jaffas_xmas() {
        this.items = new Items();
        items.RegisterItemType(JaffaItemType.basic, ItemXmas.class);
        items.RegisterItemType(JaffaItemType.food, ItemXmasFood.class);
        items.InitializeItemInfos();
    }

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();
            idProvider.linkWithConfig(config);

            BlockCandyID = idProvider.getBlockIDFromConfig("candy");
            ItemGiantCandyID = idProvider.getItemIDFromConfig("giant candy");

            BlockPresentID = idProvider.getBlockIDFromConfig("present");

            items.LoadItemsFromConfig(idProvider);

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (xmas) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.IsModuleEnabled(ModulesEnum.xmas))
            return;

        CreativeTab = new JaffaCreativeTab("jaffas.xmas");

        GameRegistry.registerTileEntity(TileEntityCandy.class, "jaffas.candy");
        GameRegistry.registerTileEntity(TileEntityPresent.class, "jaffas.present");

        createItems();
        items.CreateItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.xmas", "en_US", "Jaffas and more! Christmas");

        mod_jaffas_food.PrintInitialized(ModulesEnum.xmas);
    }

    private void createItems() {
        BlockCandy = new BlockCandy(BlockCandyID, 16, Material.wood);
        BlockCandy.setUnlocalizedName("jaffas.candy");
        GameRegistry.registerBlock(BlockCandy, "blockCandy");
        LanguageRegistry.addName(BlockCandy, "Candy Cane");

        ItemGiantCandy = new ItemGiantCandy(ItemGiantCandyID, 16);
        ItemGiantCandy.setUnlocalizedName("jaffas.giantCandy");
        LanguageRegistry.addName(ItemGiantCandy, "Giant Candy Cane");

        BlockPresent = new BlockPresent(BlockPresentID, 0, Material.cloth, ItemBlockPresent.count);
        GameRegistry.registerBlock(BlockPresent, ItemBlockPresent.class, "blockPresent");
    }

    private void installRecipes() {
        GameRegistry.addRecipe(new ItemStack(ItemGiantCandy), " W ", "WRW", "WB ", 'W',
                new ItemStack(Block.planks, 1, -1), 'R', new ItemStack(Item.dyePowder, 1, 1), 'B', new ItemStack(Item.dyePowder, 1, 15));

        GameRegistry.addRecipe(new ItemStack(BlockPresent, 1, 0), "RS ", "PPP", "PPP", 'R', new ItemStack(Item.dyePowder, 1, 1),
                'S', Item.silk, 'P', Item.paper);

        GameRegistry.addRecipe(new ItemStack(BlockPresent, 1, 6), "RS ", "PPP", 'R', new ItemStack(Item.dyePowder, 1, 1),
                'S', Item.silk, 'P', Item.paper);

        installPresentRecipe(2, 4, true, 1);
        installPresentRecipe(4, 11, true, 2);
        installPresentRecipe(11, 0, true, 3);

        installPresentRecipe(1, 13, true, 4);
        installPresentRecipe(1, 5, true, 4);

        installPresentRecipe(2, 0, true, 5);

        installPresentRecipe(2, 4, false, 7);
        installPresentRecipe(4, 11, false, 8);
        installPresentRecipe(11, 0, false, 9);

        installPresentRecipe(1, 13, false, 10);
        installPresentRecipe(1, 5, false, 10);

        installPresentRecipe(2, 0, false, 11);

        // star
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyStarRaw)), " X ", "XXX", " X ", 'X', getItem(JaffaItem.pastrySweet));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandyStarRaw).itemID, new ItemStack(getItem(JaffaItem.xcandyStar)), 0.2f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyStarChoco), 16), "C", "S", 'C', getItem(JaffaItem.chocolate), 'S', getItem(JaffaItem.xcandyStar));
        Item[] starJams = new Item[]{getItem(JaffaItem.jamR), getItem(JaffaItem.jamStrawberry), getItem(JaffaItem.jamRaspberry)};
        for (Item i : starJams) {
            GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyStarJam), 16), "S", "J", "S", 'S', getItem(JaffaItem.xcandyStar), 'J', i);

            // candy cane
            GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyCane)), " W ", "WRW", "W  ", 'W', Item.sugar, 'R', i);
        }

        // small roll
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandySmallRollRaw)), "X ", " X", "X ", 'X', getItem(JaffaItem.pastrySweet));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandySmallRollRaw).itemID, new ItemStack(getItem(JaffaItem.xcandySmallRoll)), 0.2f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandySmallRollChoco), 6), "C", "R", "C", 'C', getItem(JaffaItem.chocolate), 'R', getItem(JaffaItem.xcandySmallRoll));

        // circle
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoCircleRaw)), " X ", "XXX", " X ", 'X', getItem(JaffaItem.brownPastry));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandyChocoCircleRaw).itemID, new ItemStack(getItem(JaffaItem.xcandyChocoCircle)), 0.2f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoCircleCoated), 12), "C", "P", 'C', getItem(JaffaItem.chocolate), 'P', getItem(JaffaItem.xcandyChocoCircle));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoCircleSprinkled), 12), "S", "C", "P", 'C', getItem(JaffaItem.chocolate), 'P', getItem(JaffaItem.xcandyChocoCircle), 'S', getItem(JaffaItem.sprinkles));

        // choco - ball
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoBall)), " C ", "CBC", " C ", 'C', getItem(JaffaItem.chocolate), 'B', getItem(JaffaItem.butter));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoBallSprinkled), 6), "C", "B", 'C', getItem(JaffaItem.coconutPowder), 'B', getItem(JaffaItem.xcandyChocoBall));

        // gingerbread figure
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyGingerFigureRaw)), "XXX", " X ", "X X", 'X', getItem(JaffaItem.gingerbread));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandyGingerFigureRaw).itemID, new ItemStack(getItem(JaffaItem.xcandyGingerFigure)), 0.2f);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.xcandyGingerCreamed), 26), getItem(JaffaItem.xcandyGingerFigure), getItem(JaffaItem.cream));
    }

    private Item getItem(JaffaItem type) {
        return mod_jaffas_food.getItem(type);
    }

    private void installPresentRecipe(int ribbonColor, int color, boolean big, int presentMeta) {
        if (big) {
            GameRegistry.addRecipe(new ItemStack(BlockPresent, 1, presentMeta), "ASB", "PPP", "PPP", 'A', new ItemStack(Item.dyePowder, 1, ribbonColor),
                    'S', Item.silk, 'P', Item.paper, 'B', new ItemStack(Item.dyePowder, 1, color));
        } else {
            GameRegistry.addRecipe(new ItemStack(BlockPresent, 1, presentMeta), "ASB", "PPP", 'A', new ItemStack(Item.dyePowder, 1, ribbonColor),
                    'S', Item.silk, 'P', Item.paper, 'B', new ItemStack(Item.dyePowder, 1, color));
        }
    }
}
