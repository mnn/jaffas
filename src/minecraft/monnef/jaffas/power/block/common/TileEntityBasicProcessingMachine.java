/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.core.utils.ItemHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.common.IProcessingRecipe;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static monnef.jaffas.power.block.common.ContainerBasicProcessingMachine.SLOT_INPUT;
import static monnef.jaffas.power.block.common.ContainerBasicProcessingMachine.SLOT_OUTPUT;

public abstract class TileEntityBasicProcessingMachine extends TileEntityMachineWithInventory {
    private static final String PROCESS_TIME_TAG_NAME = "processTime";
    private static final String PROCESS_ITEM_TIME_TAG_NAME = "processItemTime";
    private static final String INPUT_ITEM_TAG = "inputItem";

    public int processTime = 0;
    public int processItemTime = 1;
    public ItemStack inputItem;

    @Override
    public int getSizeInventory() {
        return 2;
    }

    public abstract IProcessingRecipeHandler getRecipeHandler();

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded = 1;
    }

    @Override
    public void doWork() {
        if (worldObj.isRemote) return;

        if (isWorking()) {
            processTime++;
            float power = getPowerProvider().useEnergy(powerNeeded, powerNeeded, true);
            if (power != powerNeeded) {
                JaffasFood.Log.printWarning("Inconsistency detected in power framework! " + getClass().getSimpleName());
            }
            if (processTime >= processItemTime) {
                processTime = 0;
                produceOutput(getRecipeHandler().findByInput(inputItem));
            }
        } else {
            ItemStack inputStack = getStackInSlot(SLOT_INPUT);
            IProcessingRecipe recipe = getRecipeHandler().findByInput(inputStack);
            if (recipe != null) {
                if (isOutputFreeFor(recipe.getOutput())) {
                    consumeInput(recipe, inputStack);
                    processTime = 1;
                    processItemTime = recipe.getDuration();
                    inputItem = inputStack.copy();
                    inputItem.stackSize = recipe.getInput().stackSize;
                }
            }
        }
    }

    private void produceOutput(IProcessingRecipe recipe) {
        ItemStack output = recipe.getOutput();
        if (!isOutputFreeFor(output)) {
            throw new RuntimeException("Cannot produce output.");
        }

        ItemStack outputSlotStack = getStackInSlot(SLOT_OUTPUT);
        if (outputSlotStack == null) {
            setInventorySlotContents(SLOT_OUTPUT, output.copy());
        } else {
            outputSlotStack.stackSize += output.stackSize;
        }
    }

    private void consumeInput(IProcessingRecipe recipe, ItemStack inputStack) {
        inputStack.stackSize -= recipe.getInput().stackSize;
        if (inputStack.stackSize <= 0) setInventorySlotContents(SLOT_INPUT, null);
    }

    private boolean isOutputFreeFor(ItemStack output) {
        ItemStack outputSlotStack = getStackInSlot(SLOT_OUTPUT);
        if (outputSlotStack == null) return true;
        if (!ItemHelper.haveStacksSameIdAndDamage(outputSlotStack, output)) return false;
        if (outputSlotStack.stackSize + output.stackSize > outputSlotStack.getMaxStackSize()) return false;
        return true;
    }

    private boolean isWorking() {
        return processTime > 0;
    }

    @Override
    public int getIntegersToSyncCount() {
        return 2;
    }

    @Override
    public int getCurrentValueOfIntegerToSync(int index) {
        switch (index) {
            case 0:
                return processTime;

            case 1:
                return processItemTime;
        }

        return -1;
    }

    @Override
    public void setCurrentValueOfIntegerToSync(int index, int value) {
        switch (index) {
            case 0:
                processTime = value;
                break;

            case 1:
                processItemTime = value;
                break;

            default:
                return;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.processTime = tag.getInteger(PROCESS_TIME_TAG_NAME);
        this.processItemTime = tag.getInteger(PROCESS_ITEM_TIME_TAG_NAME);
        this.inputItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(INPUT_ITEM_TAG));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(PROCESS_TIME_TAG_NAME, this.processTime);
        tag.setInteger(PROCESS_ITEM_TIME_TAG_NAME, this.processItemTime);
        if (inputItem != null) {
            tag.setCompoundTag(INPUT_ITEM_TAG, inputItem.writeToNBT(new NBTTagCompound()));
        }
    }
}
