/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.DyeColor;
import monnef.core.utils.DyeHelper;
import monnef.core.utils.EntityHelper;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.JaffasModBase;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.food.common.JaffaCreativeTab;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.power.block.BlockAntenna;
import monnef.jaffas.power.block.BlockGenerator;
import monnef.jaffas.power.block.BlockGrinder;
import monnef.jaffas.power.block.BlockJuiceMaker;
import monnef.jaffas.power.block.BlockKitchenUnit;
import monnef.jaffas.power.block.BlockLightningConductor;
import monnef.jaffas.power.block.BlockToaster;
import monnef.jaffas.power.block.BlockWebHarvester;
import monnef.jaffas.power.block.BlockWindGenerator;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileGrinder;
import monnef.jaffas.power.block.TileJuiceMaker;
import monnef.jaffas.power.block.TileKitchenUnit;
import monnef.jaffas.power.block.TileLightningConductor;
import monnef.jaffas.power.block.TileToaster;
import monnef.jaffas.power.block.TileWebHarvester;
import monnef.jaffas.power.block.TileWindGenerator;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.common.LightingHandler;
import monnef.jaffas.power.entity.EntityWindTurbine;
import monnef.jaffas.power.item.ItemDebug;
import monnef.jaffas.power.item.ItemLinkTool;
import monnef.jaffas.power.item.ItemPipeWrench;
import monnef.jaffas.power.item.ItemTurbineBlade;
import monnef.jaffas.power.item.ItemWindTurbine;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.item.CentralUnitEnum;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;

import static monnef.core.utils.BreakableIronMaterial.breakableIronMaterial;
import static monnef.jaffas.food.common.JaffasRegistryHelper.registerTileEntity;
import static monnef.jaffas.power.common.Reference.ModId;
import static monnef.jaffas.power.common.Reference.ModName;
import static monnef.jaffas.power.common.Reference.Version;
import static monnef.jaffas.technic.JaffasTechnic.createCentralUnitStack;
import static monnef.jaffas.technic.JaffasTechnic.itemCasing;
import static monnef.jaffas.technic.JaffasTechnic.jaffarrol;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas;after:Jaffas-Technic")
public class JaffasPower extends JaffasModBase {
    @Instance("Jaffas-Power")
    public static JaffasPower instance;

    @SidedProxy(clientSide = "monnef.jaffas.power.client.ClientProxy", serverSide = "monnef.jaffas.power.common.CommonProxy")
    public static CommonProxy proxy;

    private boolean debug;

    public static ItemDebug itemDebug;
    public static BlockGenerator generator;
    public static BlockAntenna antenna;
    public static ItemPipeWrench wrench;
    public static ItemLinkTool linkTool;

    public static BlockLightningConductor lightningConductor;
    public static int lightningConductorRadius = 30;
    public static boolean lightningConductorEnabled;

    public static BlockKitchenUnit kitchenUnit;
    public static BlockGrinder grinder;
    public static BlockToaster toaster;
    public static BlockWebHarvester webHarvester;
    public static boolean windGeneratorEnabled;
    public static BlockWindGenerator windGenerator;
    public static ItemWindTurbine windTurbineWooden;
    public static ItemWindTurbine windTurbineMill;
    public static ItemTurbineBlade turbineBlade;
    public static BlockJuiceMaker juiceMaker;

    @Mod.EventHandler
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);

        try {
            config.load();

            lightningConductorEnabled = config.get(Configuration.CATEGORY_GENERAL, "lightningConductorEnabled", true).getBoolean(true);
            windGeneratorEnabled = config.get(Configuration.CATEGORY_GENERAL, "windGeneratorEnabled", true).getBoolean(true);

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "Mod Jaffas (power) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Override
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        super.load(event);

        if (!ModuleManager.isModuleEnabled(ModulesEnum.power))
            return;

        creativeTab = new JaffaCreativeTab("jaffas.power");

        createItems();
        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        LanguageRegistry.instance().addStringLocalization("itemGroup.jaffas.power", "en_US", "Jaffas and more! Power");
        creativeTab.setup(wrench);

        turbineBlade.registerNames();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        //MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());
        if (lightningConductorEnabled) {
            MinecraftForge.EVENT_BUS.register(new LightingHandler());
        }

        JaffasFood.printInitialized(ModulesEnum.power);
    }

    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            ContainerRegistry.assertAllItemsHasGuiClass();
        }
    }

    private void createItems() {
        generator = new BlockGenerator(5);
        RegistryUtils.registerBlock(generator);
        registerTileEntity(TileGenerator.class, "jp.generator");

        itemDebug = new ItemDebug(1);
        RegistryUtils.registerItem(itemDebug);

        antenna = new BlockAntenna(5);
        RegistryUtils.registerBlock(antenna);
        registerTileEntity(TileAntenna.class, "jp.antenna");

        wrench = new ItemPipeWrench(1);
        RegistryUtils.registerItem(wrench);

        linkTool = new ItemLinkTool(0);
        RegistryUtils.registerItem(linkTool, "itemLinkTool");

        if (lightningConductorEnabled) {
            lightningConductor = new BlockLightningConductor(5);
            RegistryUtils.registerBlock(lightningConductor);
            registerTileEntity(TileLightningConductor.class, "jp.lightningConductor");
        }

        kitchenUnit = new BlockKitchenUnit(10, 3);
        RegistryUtils.registerMultiBlock(kitchenUnit, ItemBlockJaffas.class, kitchenUnit.generateSubNames());
        registerTileEntity(TileKitchenUnit.class, "kitchenUnit");

        TileEntityBasicProcessingMachine.registerMachine(TileGrinder.class, TileGrinder.getRecipeHandler(), TileEntityBasicProcessingMachine.getDefaultGuiBackgroundTexture(), "Grinder");
        grinder = new BlockGrinder(101, TileGrinder.class, GuiHandler.GuiId.GRINDER, true, false);
        RegistryUtils.registerBlockWithName(grinder, "grinder");
        registerTileEntity(TileGrinder.class, "grinder");
        grinder.setRotationShiftInPlacing(1);

        TileEntityBasicProcessingMachine.registerMachine(TileToaster.class, TileToaster.getRecipeHandler(), TileEntityBasicProcessingMachine.getDefaultGuiBackgroundTexture(), "Toaster");
        toaster = new BlockToaster(50, TileToaster.class, GuiHandler.GuiId.TOASTER, true, false);
        RegistryUtils.registerBlockWithName(toaster, "toaster");
        registerTileEntity(TileToaster.class, "toaster");

        webHarvester = new BlockWebHarvester(51, breakableIronMaterial(), false, false);
        RegistryUtils.registerBlockWithName(webHarvester, "webHarvester");
        registerTileEntity(TileWebHarvester.class, "webHarvester");

        if (windGeneratorEnabled) {
            windGenerator = new BlockWindGenerator(54, breakableIronMaterial(), false, false);
            RegistryUtils.registerBlockWithName(windGenerator, "windGenerator");
            registerTileEntity(TileWindGenerator.class, "windGenerator");

            EntityHelper.registerModEntity(EntityWindTurbine.class, "jaffasWindGenerator", 160, 1, true, this);

            windTurbineWooden = new ItemWindTurbine(57, 100, 0);
            windTurbineWooden.configure(true, 1, false, 0.1f, 50);
            windTurbineWooden.setupStep(1.3f, 2, 3, 0.2f, 2, 6, 1, 3);
            RegistryUtils.registerItem(windTurbineWooden, "windTurbineWooden");

            windTurbineMill = new ItemWindTurbine(64, 1000, 0);
            windTurbineMill.configure(false, 7, true, 0.02f, 140);
            windTurbineMill.setupStep(0.8f, 2, 3, 0.2f, 2, 6, 1, 3);
            RegistryUtils.registerItem(windTurbineMill, "windTurbineMill");

            turbineBlade = new ItemTurbineBlade(58);
            RegistryUtils.registerItem(turbineBlade);
        }

        TileEntityBasicProcessingMachine.registerMachine(TileJuiceMaker.class, TileJuiceMaker.getRecipeHandler(), "guijuicemaker.png", "Juice Maker 1000", TileEntityBasicProcessingMachine.MachineRecord.InvType.DOUBLE);
        juiceMaker = new BlockJuiceMaker(51, TileJuiceMaker.class, GuiHandler.GuiId.JUICE_MAKER);
        RegistryUtils.registerBlockWithName(juiceMaker, "juiceMaker");
        registerTileEntity(TileJuiceMaker.class, "juiceMaker");
    }

    private void installRecipes() {
        if (ModuleManager.isModuleEnabled(ModulesEnum.technic)) {
            if (lightningConductorEnabled) {
                GameRegistry.addRecipe(new ItemStack(lightningConductor), "J", "J", "B", 'J', JaffasTechnic.jaffarrol, 'B', Blocks.iron_block);
            }
            Recipes.addRecipe(new ShapedOreRecipe(wrench, "JJ ", "BJR", "  J", 'J', JaffasTechnic.jaffarrol, 'B', DyeHelper.getDye(DyeColor.BLACK), 'R', DyeHelper.getDye(DyeColor.RED)));
            GameRegistry.addShapedRecipe(new ItemStack(generator, 2), " I ", "IFI", " C ", 'I', Items.iron_ingot, 'F', Blocks.furnace, 'C', createCentralUnitStack(CentralUnitEnum.SIMPLE));

            Recipes.addOreRecipe(grinder, " FS", "III", "III", 'I', Items.iron_ingot, 'F', JaffasTechnic.funnel, 'S', Recipes.WOOD_STICK);

            GameRegistry.addShapedRecipe(new ItemStack(toaster), "CJ", "H ", 'C', JaffasTechnic.itemCasingRefined, 'J', JaffasTechnic.jaffarrolNugget, 'H', createCentralUnitStack(CentralUnitEnum.SIMPLE));

            addKitchenUnitRecipe(0, new ItemStack(Blocks.planks, 1, 2));
            addKitchenUnitRecipe(0, new ItemStack(Blocks.planks, 1, 0));
            addKitchenUnitRecipe(1, new ItemStack(Blocks.planks, 1, 3));
            addKitchenUnitRecipe(2, new ItemStack(Blocks.planks, 1, 1));

            Recipes.addOreRecipe(webHarvester, "SFS", "S@S", "JCJ", 'J', JaffasTechnic.jaffarrol, 'F', JaffasTechnic.funnel, '@', JaffasTechnic.itemCasing, 'C', createCentralUnitStack(CentralUnitEnum.ADVANCED), 'S', Items.string);

            if (windGeneratorEnabled) {
                GameRegistry.addShapedRecipe(new ItemStack(windGenerator), " J ", "iCi", "IUI", 'J',
                        jaffarrol, 'I', Blocks.iron_block, 'i', Items.iron_ingot, 'U', createCentralUnitStack(CentralUnitEnum.SIMPLE), 'C', itemCasing);
                GameRegistry.addShapedRecipe(turbineBlade.constructBlade(ItemTurbineBlade.TurbineBladeEnum.millBlade), "iii", "iji", " j ", 'i', Items.iron_ingot, 'j', jaffarrol);
                GameRegistry.addRecipe(new ShapedOreRecipe(
                        turbineBlade.constructBlade(ItemTurbineBlade.TurbineBladeEnum.woodenBlade), "iii", "iji", " j ", 'i', Recipes.WOOD_PLANK, 'j', jaffarrol
                ));
                GameRegistry.addShapedRecipe(turbineBlade.constructBlade(ItemTurbineBlade.TurbineBladeEnum.ironBlade), "i", "i", "j", 'i', Items.iron_ingot, 'j', jaffarrol);
                GameRegistry.addShapedRecipe(turbineBlade.constructBlade(ItemTurbineBlade.TurbineBladeEnum.jaffarrolBlade), "jjj", "jij", " j ", 'i', Items.iron_ingot, 'j', jaffarrol);
                GameRegistry.addShapedRecipe(new ItemStack(windTurbineMill), " B ", "BiB", " B ", 'B', turbineBlade.constructBlade(ItemTurbineBlade.TurbineBladeEnum.millBlade), 'i', Items.iron_ingot);
                GameRegistry.addShapedRecipe(new ItemStack(windTurbineMill), "B B", " i ", "B B", 'B', turbineBlade.constructBlade(ItemTurbineBlade.TurbineBladeEnum.millBlade), 'i', Items.iron_ingot);
                for (int i = 0; i < ItemWindTurbine.BASIC_COLOURS_COUNT; i++) {
                    ItemStack turbine = windTurbineMill.constructColoredTurbine(i);
                    GameRegistry.addShapelessRecipe(turbine, windTurbineMill, DyeHelper.getDye(i));
                }
            }
        }

        TileToaster.addRecipe(TileToaster.ToastLevel.MEDIUM, JaffaItem.breadSlice, JaffaItem.breadSliceToasted, 100);

        GameRegistry.addShapedRecipe(new ItemStack(JaffasPower.juiceMaker), "GAG", "GPG", "NCN", 'A', Items.iron_axe, 'G', Blocks.glass, 'N', JaffasTechnic.jaffarrolNugget, 'C', JaffasTechnic.itemCasingRefined, 'P', createCentralUnitStack(CentralUnitEnum.SIMPLE));
    }

    private void addKitchenUnitRecipe(int unitId, ItemStack planks) {
        Recipes.addOreRecipe(new ItemStack(kitchenUnit, 3, unitId), "QQQ", "PCP", "PSP",
                'S', Recipes.STONE, 'Q', new ItemStack(Blocks.stone_slab, 1, 7), 'P', planks,
                'C', createCentralUnitStack(CentralUnitEnum.SIMPLE));
    }
}
