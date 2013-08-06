/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power;

import buildcraft.api.power.PowerFramework;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.DyeColor;
import monnef.core.utils.DyeHelper;
import monnef.core.utils.EntityHelper;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.food.common.JaffaCreativeTab;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.item.ItemCleaverHookContainer;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.jaffasMod;
import monnef.jaffas.power.block.BlockAntenna;
import monnef.jaffas.power.block.BlockGenerator;
import monnef.jaffas.power.block.BlockGrinder;
import monnef.jaffas.power.block.BlockKitchenUnit;
import monnef.jaffas.power.block.BlockLightningConductor;
import monnef.jaffas.power.block.BlockToaster;
import monnef.jaffas.power.block.BlockWebHarvester;
import monnef.jaffas.power.block.BlockWindGenerator;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileGrinder;
import monnef.jaffas.power.block.TileKitchenUnit;
import monnef.jaffas.power.block.TileLightningConductor;
import monnef.jaffas.power.block.TileToaster;
import monnef.jaffas.power.block.TileWebHarvester;
import monnef.jaffas.power.block.TileWindGenerator;
import monnef.jaffas.power.block.common.ContainerBasicProcessingMachine;
import monnef.jaffas.power.block.common.ProcessingMachineRegistry;
import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.common.LightingHandler;
import monnef.jaffas.power.common.SimplePowerFramework;
import monnef.jaffas.power.entity.EntityWindTurbine;
import monnef.jaffas.power.item.ItemDebug;
import monnef.jaffas.power.item.ItemLinkTool;
import monnef.jaffas.power.item.ItemPipeWrench;
import monnef.jaffas.power.item.ItemWindTurbine;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.logging.Level;

import static cpw.mods.fml.common.Mod.Init;
import static cpw.mods.fml.common.Mod.PostInit;
import static cpw.mods.fml.common.Mod.PreInit;
import static monnef.jaffas.food.common.JaffasRegistryHelper.registerTileEntity;
import static monnef.jaffas.power.common.Reference.ModId;
import static monnef.jaffas.power.common.Reference.ModName;
import static monnef.jaffas.power.common.Reference.Version;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:Jaffas;after:Jaffas-Technic")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class JaffasPower extends jaffasMod {
    @Instance("Jaffas-Power")
    public static JaffasPower instance;

    @SidedProxy(clientSide = "monnef.jaffas.power.client.ClientProxy", serverSide = "monnef.jaffas.power.common.CommonProxy")
    public static CommonProxy proxy;

    private boolean debug;

    private int ItemDebugID;
    public static ItemDebug ItemDebug;

    public static int renderID;

    public static BlockGenerator generator;
    private int blockGeneratorID;

    public static BlockAntenna antenna;
    private int blockAntennaID;

    private int ItemWrenchID;
    public static ItemPipeWrench wrench;

    private int ItemLinkToolID;
    public static ItemLinkTool linkTool;

    private int blockLightningConductorID;
    public static BlockLightningConductor lightningConductor;
    public static int lightningConductorRadius = 30;
    public static boolean lightningConductorEnabled;

    public static BlockKitchenUnit kitchenUnit;
    private int blockKitchenUnitID;

    public static BlockGrinder grinder;
    private int blockGrinderID;

    public static BlockToaster toaster;
    private int blockToasterID;

    public static BlockWebHarvester webHarvester;
    private int blockWebHarvesterID;

    public static boolean windGeneratorEnabled;
    public static BlockWindGenerator windGenerator;
    private int blockWindGeneratorID;

    public static ItemWindTurbine windTurbineWooden;
    private int itemWindTurbineWoodenID;

    public static ItemWindTurbine windTurbineMill;
    private int itemWindTurbineMillID;

    private int windTurbineEntityID;

    @PreInit
    @Override
    public void preLoad(FMLPreInitializationEvent event) {
        super.preLoad(event);

        try {
            config.load();
            idProvider.linkWithConfig(config);

            ItemDebugID = idProvider.getItemIDFromConfig("debug");
            ItemWrenchID = idProvider.getItemIDFromConfig("pipeWrench");
            ItemLinkToolID = idProvider.getItemIDFromConfig("linkTool");

            blockGeneratorID = idProvider.getBlockIDFromConfig("generator");
            blockAntennaID = idProvider.getBlockIDFromConfig("antenna");

            lightningConductorEnabled = config.get(Configuration.CATEGORY_GENERAL, "lightningConductorEnabled", true).getBoolean(true);
            if (lightningConductorEnabled) {
                blockLightningConductorID = idProvider.getBlockIDFromConfig("lightningConductor");
            }

            blockKitchenUnitID = idProvider.getBlockIDFromConfig("kitchenUnit");

            blockGrinderID = idProvider.getBlockIDFromConfig("grinder");
            blockToasterID = idProvider.getBlockIDFromConfig("toaster");
            blockWebHarvesterID = idProvider.getBlockIDFromConfig("webHarvester");

            windGeneratorEnabled = config.get(Configuration.CATEGORY_GENERAL, "windGeneratorEnabled", true).getBoolean(true);
            if (windGeneratorEnabled) {
                blockWindGeneratorID = idProvider.getBlockIDFromConfig("windGenerator");
                itemWindTurbineWoodenID = idProvider.getItemIDFromConfig("windTurbineWooden");
                itemWindTurbineMillID = idProvider.getItemIDFromConfig("windTurbineMill");
                windTurbineEntityID = idProvider.getEntityIDFromConfig("windTurbine");
            }

            debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false).getBoolean(false);
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas (power) can't read config file.");
        } finally {
            config.save();
        }
    }

    @Override
    protected int getStartOfItemsIdInterval() {
        return 26644;
    }

    @Override
    protected int getStartOfBlocksIdInterval() {
        return 3750;
    }

    @Init
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

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new ItemCleaverHookContainer());
        if (lightningConductorEnabled) {
            MinecraftForge.EVENT_BUS.register(new LightingHandler());
        }

        JaffasFood.PrintInitialized(ModulesEnum.power);
    }

    @PostInit
    public void postLoad(FMLPostInitializationEvent event) {
        if (PowerFramework.currentFramework == null) {
            JaffasFood.Log.printInfo("No BC power framework detected, using my simple one.");
            PowerFramework.currentFramework = new SimplePowerFramework();
        }
    }

    private void createItems() {
        generator = new BlockGenerator(blockGeneratorID, 5);
        RegistryUtils.registerBlock(generator, "Generator");
        registerTileEntity(TileGenerator.class, "jp.generator");

        ItemDebug = new ItemDebug(ItemDebugID, 1);
        LanguageRegistry.addName(ItemDebug, "Power Debug Tool");

        antenna = new BlockAntenna(blockAntennaID, 5);
        RegistryUtils.registerBlock(antenna, "Small Antenna");
        registerTileEntity(TileAntenna.class, "jp.antenna");

        wrench = new ItemPipeWrench(ItemWrenchID, 1);
        LanguageRegistry.addName(wrench, "Pipe Wrench");

        linkTool = new ItemLinkTool(ItemLinkToolID, 0);
        RegistryUtils.registerItem(linkTool, "itemLinkTool", "Link Gun");

        if (lightningConductorEnabled) {
            lightningConductor = new BlockLightningConductor(blockLightningConductorID, 5);
            RegistryUtils.registerBlock(lightningConductor, "Lightning Conductor");
            registerTileEntity(TileLightningConductor.class, "jp.lightningConductor");
        }

        kitchenUnit = new BlockKitchenUnit(blockKitchenUnitID, 10, 3);
        RegistryUtils.registerMultiBlock(kitchenUnit, ItemBlockJaffas.class, kitchenUnit.generateTitles(), kitchenUnit.generateSubNames());
        registerTileEntity(TileKitchenUnit.class, "kitchenUnit");

        ProcessingMachineRegistry.register(TileGrinder.class, ContainerBasicProcessingMachine.class, GuiContainerBasicProcessingMachine.class);
        grinder = new BlockGrinder(blockGrinderID, 101, TileGrinder.class, GuiHandler.GuiId.GRINDER, true, false);
        RegistryUtils.registerBlock(grinder, "grinder", "Grinder");
        registerTileEntity(TileGrinder.class, "grinder");
        grinder.setRotationShiftInPlacing(1);

        ProcessingMachineRegistry.register(TileToaster.class, ContainerBasicProcessingMachine.class, GuiContainerBasicProcessingMachine.class);
        toaster = new BlockToaster(blockToasterID, 50, TileToaster.class, GuiHandler.GuiId.TOASTER, true, false);
        RegistryUtils.registerBlock(toaster, "toaster", "Toaster");
        registerTileEntity(TileToaster.class, "toaster");

        webHarvester = new BlockWebHarvester(blockWebHarvesterID, 51, JaffasTechnic.breakableIronMaterial, false, false);
        RegistryUtils.registerBlock(webHarvester, "webHarvester", "Cobweb Harvester");
        registerTileEntity(TileWebHarvester.class, "webHarvester");

        if (windGeneratorEnabled) {
            windGenerator = new BlockWindGenerator(blockWindGeneratorID, 51, JaffasTechnic.breakableIronMaterial, false, false);
            RegistryUtils.registerBlock(windGenerator, "windGenerator", "Wind Generator");
            registerTileEntity(TileWindGenerator.class, "windGenerator");

            EntityHelper.registerEntity(EntityWindTurbine.class, "jaffasWindGenerator", 160, 1, true, windTurbineEntityID, this);

            windTurbineWooden = new ItemWindTurbine(itemWindTurbineWoodenID, 54, 100, 0);
            windTurbineWooden.configure(true, 1, false, 0.1f);
            RegistryUtils.registerItem(windTurbineWooden, "windTurbineWooden", "Wooden Wind Turbine");

            windTurbineMill = new ItemWindTurbine(itemWindTurbineMillID, 55, 1000, 0);
            windTurbineMill.configure(false, 7, true, 0.02f);
            RegistryUtils.registerItem(windTurbineMill, "windTurbineMill", "Windmill");
        }
    }

    private void installRecipes() {
        if (ModuleManager.isModuleEnabled(ModulesEnum.technic)) {
            if (lightningConductorEnabled) {
                GameRegistry.addRecipe(new ItemStack(lightningConductor), "J", "J", "B", 'J', JaffasTechnic.jaffarrol, 'B', Block.blockIron);
            }
            Recipes.addRecipe(new ShapedOreRecipe(wrench, "JJ ", "BJR", "  J", 'J', JaffasTechnic.jaffarrol, 'B', DyeHelper.getDye(DyeColor.BLACK), 'R', DyeHelper.getDye(DyeColor.RED)));
            GameRegistry.addShapedRecipe(new ItemStack(generator, 2), " I ", "IFI", " C ", 'I', Item.ingotIron, 'F', Block.furnaceIdle, 'C', getSimpleCentralUnitStack());

            GameRegistry.addRecipe(new ItemStack(grinder), " FS", "III", "III", 'I', Item.ingotIron, 'F', JaffasTechnic.funnel, 'S', Item.stick);

            GameRegistry.addShapedRecipe(new ItemStack(toaster), "CJ", "H ", 'C', JaffasTechnic.itemCasingRefined, 'J', JaffasTechnic.jaffarrol, 'H', getSimpleCentralUnitStack());

            addKitchenUnitRecipe(0, new ItemStack(Block.planks, 1, 2));
            addKitchenUnitRecipe(0, new ItemStack(Block.planks, 1, 0));
            addKitchenUnitRecipe(1, new ItemStack(Block.planks, 1, 3));
            addKitchenUnitRecipe(2, new ItemStack(Block.planks, 1, 1));

            GameRegistry.addRecipe(new ItemStack(webHarvester), "SFS", "S@S", "JCJ",
                    'J', JaffasTechnic.jaffarrol, 'F', JaffasTechnic.funnel, '@', JaffasTechnic.itemCasing, 'C', new ItemStack(JaffasTechnic.itemCentralUnit, 1, 2), 'S', Item.silk);
        }

        TileToaster.addRecipe(TileToaster.ToastLevel.MEDIUM, JaffaItem.breadSlice, JaffaItem.breadSliceToasted, 100);
    }

    private ItemStack getSimpleCentralUnitStack() {
        return new ItemStack(JaffasTechnic.itemCentralUnit, 1, 0);
    }

    private void addKitchenUnitRecipe(int unitId, ItemStack planks) {
        GameRegistry.addShapedRecipe(new ItemStack(kitchenUnit, 3, unitId), "QQQ", "PCP", "PSP",
                'S', Block.stone, 'Q', new ItemStack(Block.stoneSingleSlab, 1, 7), 'P', planks,
                'C', getSimpleCentralUnitStack()
        );
    }
}
