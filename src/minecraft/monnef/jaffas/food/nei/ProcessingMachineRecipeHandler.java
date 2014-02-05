/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.client.FMLClientHandler;
import monnef.core.block.ContainerMonnefCore;
import monnef.core.client.PackageToModIdRegistry;
import monnef.core.client.ResourcePathHelper;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.power.block.common.ContainerBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.common.IProcessingRecipe;
import monnef.jaffas.power.common.RecipeItemType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.GUI;

public class ProcessingMachineRecipeHandler extends TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    private final static int SLOT_SHIFT_X = -5;
    private final static int SLOT_SHIFT_Y = -11;
    private Class<? extends TileEntityBasicProcessingMachine> machineClazz;
    private TileEntityBasicProcessingMachine.MachineRecord machine;
    private TileEntityBasicProcessingMachine fakeTile;

    public ProcessingMachineRecipeHandler(Class<? extends TileEntityBasicProcessingMachine> machineClazz) {
        this.machineClazz = machineClazz;
        this.machine = TileEntityBasicProcessingMachine.getMachineRecord(machineClazz);
        if (machine == null) {
            throw new RuntimeException("No record for machine " + machineClazz.getName() + ".");
        }
        this.fakeTile = createFakeTile();
    }

    private TileEntityBasicProcessingMachine createFakeTile() {
        switch (machine.getInvType()) {
            case BASIC:
                return new TileBPMDummy();

            case DOUBLE:
                return new TileDBPMDummy();

            default:
                throw new RuntimeException("Not supported machine inventory type.");
        }
    }

    @Override
    public String getGuiTexture() {
        return ResourcePathHelper.assemble(machine.getGuiBackgroundTexture(), PackageToModIdRegistry.searchModId(machineClazz), GUI);
    }

    @Override
    public String getRecipeName() {
        return machine.getTitle();
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        processRecipes(true, result, RecipeItemType.OUTPUT);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        processRecipes(false, ingredient, RecipeItemType.INPUT);
    }

    private void processRecipes(boolean repeatForEachResult, ItemStack filter, RecipeItemType type) {
        Collection<IProcessingRecipe> recipes = machine.getRecipeHandler().copyRecipes();
        for (IProcessingRecipe recipe : recipes) {
            if (!recipe.isAny(filter, type)) continue;
            for (int i = 0; i < recipe.getOutput().length; i++) {
                ContainerMonnefCore container = ContainerRegistry.createContainer(fakeTile, new InventoryPlayer(FMLClientHandler.instance().getClient().thePlayer));
                arecipes.add(new CachedMachineRecipe(recipe, i, (ContainerBasicProcessingMachine) container));
                if (!repeatForEachResult) break;
            }
        }
    }

    public class CachedMachineRecipe extends CachedRecipe {
        private final IProcessingRecipe recipe;
        private final int outputNumber;
        private final ContainerBasicProcessingMachine container;

        public CachedMachineRecipe(IProcessingRecipe recipe, int outputNumber, ContainerBasicProcessingMachine container) {
            this.recipe = recipe;
            this.outputNumber = outputNumber;
            this.container = container;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> res = new ArrayList<PositionedStack>();

            for (int i = 0; i < recipe.getInput().length; i++) {
                res.add(getPositionStack(i, RecipeItemType.INPUT));
            }

            for (int i = 0; i < recipe.getOutput().length; i++) {
                if (i != outputNumber)
                    res.add(getPositionStack(i, RecipeItemType.OUTPUT));
            }

            return res;
        }

        @Override
        public PositionedStack getResult() {
            return getPositionStack(outputNumber, RecipeItemType.OUTPUT);
        }

        protected PositionedStack getPositionStack(int numberInGroup, RecipeItemType type) {
            ItemStack outputStack = (type == RecipeItemType.INPUT ? recipe.getInput() : recipe.getOutput())[numberInGroup].copy();
            Slot slot = type == RecipeItemType.INPUT ? container.getInputSlot(numberInGroup) : container.getOutputSlot(numberInGroup);
            int offX = slot.xDisplayPosition;
            int offY = slot.yDisplayPosition;
            return new PositionedStack(outputStack, offX + SLOT_SHIFT_X, offY + SLOT_SHIFT_Y);
        }
    }

}