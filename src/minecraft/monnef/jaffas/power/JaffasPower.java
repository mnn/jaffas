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
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityGrinder;
import monnef.jaffas.power.block.TileEntityKitchenUnit;
import monnef.jaffas.power.block.TileEntityLightningConductor;
import monnef.jaffas.power.block.TileEntityToaster;
import monnef.jaffas.power.block.common.ContainerBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.common.LightingHandler;
import monnef.jaffas.power.common.SimplePowerFramework;
import monnef.jaffas.power.item.ItemDebug;
import monnef.jaffas.power.item.ItemLinkTool;
import monnef.jaffas.power.item.ItemPipeWrench;
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
        registerTileEntity(TileEntityGenerator.class, "jp.generator");

        ItemDebug = new ItemDebug(ItemDebugID, 1);
        LanguageRegistry.addName(ItemDebug, "Power Debug Tool");

        antenna = new BlockAntenna(blockAntennaID, 5);
        RegistryUtils.registerBlock(antenna, "Small Antenna");
        registerTileEntity(TileEntityAntenna.class, "jp.antenna");

        wrench = new ItemPipeWrench(ItemWrenchID, 1);
        LanguageRegistry.addName(wrench, "Pipe Wrench");

        linkTool = new ItemLinkTool(ItemLinkToolID, 0);
        RegistryUtils.registerItem(linkTool, "itemLinkTool", "Link Gun");

        if (lightningConductorEnabled) {
            lightningConductor = new BlockLightningConductor(blockLightningConductorID, 5);
            RegistryUtils.registerBlock(lightningConductor, "Lightning Conductor");
            registerTileEntity(TileEntityLightningConductor.class, "jp.lightningConductor");
        }

        kitchenUnit = new BlockKitchenUnit(blockKitchenUnitID, 10, 3);
        RegistryUtils.registerMultiBlock(kitchenUnit, ItemBlockJaffas.class, kitchenUnit.generateTitles(), kitchenUnit.generateSubNames());
        registerTileEntity(TileEntityKitchenUnit.class, "kitchenUnit");

        TileEntityBasicProcessingMachine.registerContainerPrototype(TileEntityGrinder.class, ContainerBasicProcessingMachine.class);
        grinder = new BlockGrinder(blockGrinderID, 101, TileEntityGrinder.class, GuiHandler.GuiId.GRINDER, true, false);
        RegistryUtils.registerBlock(grinder, "grinder", "Grinder");
        registerTileEntity(TileEntityGrinder.class, "grinder");
        grinder.setRotationShiftInPlacing(1);

        TileEntityBasicProcessingMachine.registerContainerPrototype(TileEntityToaster.class, ContainerBasicProcessingMachine.class);
        toaster = new BlockToaster(blockToasterID, 101, TileEntityToaster.class, GuiHandler.GuiId.TOASTER, true, false);
        RegistryUtils.registerBlock(toaster, "toaster", "Toaster");
        registerTileEntity(TileEntityToaster.class, "toaster");
    }

    private void installRecipes() {
        if (ModuleManager.isModuleEnabled(ModulesEnum.technic)) {
            if (lightningConductorEnabled) {
                GameRegistry.addRecipe(new ItemStack(lightningConductor), "J", "J", "B", 'J', JaffasTechnic.jaffarrol, 'B', Block.blockIron);
            }
            Recipes.addRecipe(new ShapedOreRecipe(wrench, "JJ ", "BJR", "  J", 'J', JaffasTechnic.jaffarrol, 'B', DyeHelper.getDye(DyeColor.BLACK), 'R', DyeHelper.getDye(DyeColor.RED)));
            GameRegistry.addShapedRecipe(new ItemStack(generator, 2), " I ", "IFI", " C ", 'I', Item.ingotIron, 'F', Block.furnaceIdle, 'C', new ItemStack(JaffasTechnic.itemCentralUnit, 1, 0));

            GameRegistry.addRecipe(new ItemStack(grinder), " FS", "III", "III", 'I', Item.ingotIron, 'F', JaffasTechnic.funnel, 'S', Item.stick);

            GameRegistry.addShapedRecipe(new ItemStack(toaster), "CJ", "H ", 'C', JaffasTechnic.itemCasingRefined, 'J', JaffasTechnic.jaffarrol, 'H', new ItemStack(JaffasTechnic.itemCentralUnit, 1, 0));
        }

        TileEntityToaster.addRecipe(TileEntityToaster.ToastLevel.MEDIUM, JaffaItem.breadSlice, JaffaItem.breadSliceToasted, 100);
    }
}
