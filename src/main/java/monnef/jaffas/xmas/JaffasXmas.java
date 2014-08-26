/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.JaffasModBase;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.JaffasRegistryHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import monnef.jaffas.xmas.block.BlockCandy;
import monnef.jaffas.xmas.block.BlockPresent;
import monnef.jaffas.xmas.block.ItemBlockPresent;
import monnef.jaffas.xmas.block.TileCandy;
import monnef.jaffas.xmas.block.TilePresent;
import monnef.jaffas.xmas.common.CommonProxy;
import monnef.jaffas.xmas.item.ItemGiantCandy;
import monnef.jaffas.xmas.item.ItemXmas;
import monnef.jaffas.xmas.item.ItemXmasFood;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;

import static monnef.jaffas.xmas.common.Reference.ModId;
import static monnef.jaffas.xmas.common.Reference.ModName;
import static monnef.jaffas.xmas.common.Reference.Version;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas")
public class JaffasXmas extends JaffasModBase {
    @Mod.Instance(ModId)
    public static JaffasXmas instance;

    @SidedProxy(clientSide = "monnef.jaffas.xmas.client.ClientProxy", serverSide = "monnef.jaffas.xmas.common.CommonProxy")
    public static CommonProxy proxy;

    private boolean debug;

    public static monnef.jaffas.xmas.block.BlockCandy blockCandy;
    public static monnef.jaffas.xmas.item.ItemGiantCandy itemGiantCandy;
    public static monnef.jaffas.xmas.block.BlockPresent blockPresent;

    public static String textureFile = "/jaffas_04.png";
    public static int renderID;

    private monnef.jaffas.xmas.item.Items items;

    public JaffasXmas() {
        this.items = new monnef.jaffas.xmas.item.Items();
        items.RegisterItemType(JaffaItemType.basic, ItemXmas.class);
        items.RegisterItemType(JaffaItemType.food, ItemXmasFood.class);
        items.InitializeItemInfos();
    }

    @Mod.EventHandler
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);

        try {
            config.load();
            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "Mod Jaffas (xmas) can't read config file.");
        } finally {
            config.save();
        }

        if (!ModuleManager.isModuleEnabled(ModulesEnum.xmas))
            return;

        creativeTab = new monnef.jaffas.food.common.JaffaCreativeTab("jaffas.xmas");

        JaffasRegistryHelper.registerTileEntity(TileCandy.class, "jaffas.candy");
        JaffasRegistryHelper.registerTileEntity(TilePresent.class, "jaffas.present");

        createItems();
        items.CreateItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.xmas", "en_US", "Jaffas and more! Christmas");
        creativeTab.setup(JaffasXmas.itemGiantCandy);
    }

    @Override
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        super.load(event);

        JaffasFood.printInitialized(ModulesEnum.xmas);
    }

    private void createItems() {
        blockCandy = new BlockCandy(16, Material.wood);
        RegistryUtils.registerBlock(blockCandy);

        itemGiantCandy = new ItemGiantCandy(16);
        RegistryUtils.registerItem(itemGiantCandy, "giantCandy");

        blockPresent = new BlockPresent(0, Material.cloth, ItemBlockPresent.count);
        RegistryUtils.registerMultiBlock(blockPresent, ItemBlockPresent.class, ItemBlockPresent.presentSubNamem);
    }

    private void installRecipes() {
        Recipes.addRecipe(new ShapedOreRecipe(new ItemStack(itemGiantCandy), " W ", "WRW", "WB ", 'W',
                Recipes.WOOD_PLANK, 'R', new ItemStack(Items.dye, 1, 1), 'B', new ItemStack(Items.dye, 1, 15)));

        GameRegistry.addRecipe(new ItemStack(blockPresent, 1, 0), "RS ", "PPP", "PPP", 'R', new ItemStack(Items.dye, 1, 1),
                'S', Items.string, 'P', Items.paper);

        GameRegistry.addRecipe(new ItemStack(blockPresent, 1, 6), "RS ", "PPP", 'R', new ItemStack(Items.dye, 1, 1),
                'S', Items.string, 'P', Items.paper);

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
        GameRegistry.addSmelting(getItem(JaffaItem.xcandyStarRaw), new ItemStack(getItem(JaffaItem.xcandyStar)), 0.2f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyStarChoco), 16), "C", "S", 'C', getItem(JaffaItem.chocolate), 'S', getItem(JaffaItem.xcandyStar));
        Item[] starJams = new Item[]{getItem(JaffaItem.jamR), getItem(JaffaItem.jamStrawberry), getItem(JaffaItem.jamRaspberry)};
        for (Item i : starJams) {
            GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyStarJam), 16), "S", "J", "S", 'S', getItem(JaffaItem.xcandyStar), 'J', i);

            // candy cane
            GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyCane)), " W ", "WRW", "W  ", 'W', Items.sugar, 'R', i);
        }

        // small roll
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandySmallRollRaw)), "X ", " X", "X ", 'X', getItem(JaffaItem.pastrySweet));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandySmallRollRaw), new ItemStack(getItem(JaffaItem.xcandySmallRoll)), 0.2f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandySmallRollChoco), 6), "C", "R", "C", 'C', getItem(JaffaItem.chocolate), 'R', getItem(JaffaItem.xcandySmallRoll));

        // circle
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoCircleRaw)), " X ", "XXX", " X ", 'X', getItem(JaffaItem.brownPastry));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandyChocoCircleRaw), new ItemStack(getItem(JaffaItem.xcandyChocoCircle)), 0.2f);
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoCircleCoated), 12), "C", "P", 'C', getItem(JaffaItem.chocolate), 'P', getItem(JaffaItem.xcandyChocoCircle));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoCircleSprinkled), 12), "S", "C", "P", 'C', getItem(JaffaItem.chocolate), 'P', getItem(JaffaItem.xcandyChocoCircle), 'S', getItem(JaffaItem.sprinkles));

        // choco - ball
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoBall)), " C ", "CBC", " C ", 'C', getItem(JaffaItem.chocolate), 'B', getItem(JaffaItem.butter));
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyChocoBallSprinkled), 6), "C", "B", 'C', getItem(JaffaItem.coconutPowder), 'B', getItem(JaffaItem.xcandyChocoBall));

        // gingerbread figure
        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.xcandyGingerFigureRaw)), "XXX", " X ", "X X", 'X', getItem(JaffaItem.gingerbread));
        GameRegistry.addSmelting(getItem(JaffaItem.xcandyGingerFigureRaw), new ItemStack(getItem(JaffaItem.xcandyGingerFigure)), 0.2f);
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.xcandyGingerCreamed), 26), getItem(JaffaItem.xcandyGingerFigure), getItem(JaffaItem.creamSweet));
    }

    private Item getItem(JaffaItem type) {
        return JaffasFood.getItem(type);
    }

    private void installPresentRecipe(int ribbonColor, int color, boolean big, int presentMeta) {
        if (big) {
            GameRegistry.addRecipe(new ItemStack(blockPresent, 1, presentMeta), "ASB", "PPP", "PPP", 'A', new ItemStack(Items.dye, 1, ribbonColor),
                    'S', Items.string, 'P', Items.paper, 'B', new ItemStack(Items.dye, 1, color));
        } else {
            GameRegistry.addRecipe(new ItemStack(blockPresent, 1, presentMeta), "ASB", "PPP", 'A', new ItemStack(Items.dye, 1, ribbonColor),
                    'S', Items.string, 'P', Items.paper, 'B', new ItemStack(Items.dye, 1, color));
        }
    }
}
