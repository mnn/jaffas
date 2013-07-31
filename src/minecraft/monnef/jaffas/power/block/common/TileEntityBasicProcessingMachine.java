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
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityBasicProcessingMachine extends TileEntityMachineWithInventory {
    private static final String PROCESS_TIME_TAG_NAME = "processTime";
    private static final String PROCESS_ITEM_TIME_TAG_NAME = "processItemTime";
    private static final String INPUT_ITEM_TAG = "inputItem";
    public static final String PROCESSING_INV_TAG = "ProcessingInv";
    public static final String SLOT_TAG = "Slot";

    private ContainerBasicProcessingMachine myContainerPrototype;

    public int processTime = 0;
    public int processItemTime = 1;
    private ItemStack[] processingInv;

    protected TileEntityBasicProcessingMachine() {
        super();
        processingInv = new ItemStack[getMyContainerPrototype().getInputSlotsCount()];
    }

    @Override
    public int getSizeInventory() {
        return getMyContainerPrototype().getSlotsCount();
    }

    public abstract IProcessingRecipeHandler getRecipeHandler();

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded = 1;
    }

    @Override
    protected void doMachineWork() {
        if (worldObj.isRemote) return;

        if (isWorking()) {
            processTime++;
            float power = getPowerProvider().useEnergy(powerNeeded, powerNeeded, true);
            if (power < powerNeeded) {
                JaffasFood.Log.printWarning("Inconsistency detected in power framework! " + getClass().getSimpleName());
            } else {
                if (processTime >= processItemTime) {
                    processTime = 0;
                    produceOutput(getRecipeHandler().findByInput(processingInv));
                }
            }
        } else {
            IProcessingRecipe recipe = getRecipeHandler().findByInput(createInputInv());
            if (recipe != null) {
                if (isOutputSlotFreeFor(recipe.getOutput())) {
                    processTime = 1;
                    processItemTime = recipe.getDuration();
                    processingInv = createProcessingInvAndConsume(recipe.getInput());
                }
            }
        }
    }

    private ItemStack[] createInputInv() {
        ItemStack[] ret = new ItemStack[getMyContainerPrototype().getInputSlotsCount()];
        for (int i = 0; i < ret.length; i++) {
            ItemStack inputStack = getStackInSlot(i);
            if (inputStack != null) {
                ret[i] = inputStack.copy();
            }
        }

        return ret;
    }

    private ItemStack[] createProcessingInvAndConsume(ItemStack[] recipeInput) {
        ItemStack[] ret = createInputInv();
        for (int i = 0; i < ret.length; i++) {
            ItemStack currStack = ret[i];
            if (currStack != null) {
                int recipeStackSize = recipeInput[i].stackSize;
                currStack.stackSize = recipeStackSize;
                decrStackSize(i, recipeStackSize);
            }
        }
        return ret;
    }

    private void produceOutput(IProcessingRecipe recipe) {
        ItemStack[] output = recipe.getOutput();
        if (!isOutputSlotFreeFor(output)) {
            throw new RuntimeException("Cannot produce output.");
        }

        for (int i = 0; i < output.length; i++) {
            produceOneOutput(output[i], i + getMyContainerPrototype().getStartIndexOfOutput());
        }
    }

    private void produceOneOutput(ItemStack output, int slot) {
        ItemStack outputSlotStack = getStackInSlot(slot);
        if (outputSlotStack == null) {
            setInventorySlotContents(slot, output.copy());
        } else {
            outputSlotStack.stackSize += output.stackSize;
        }
    }

    private boolean isOutputSlotFreeFor(ItemStack[] recipeOutput) {
        int start = getMyContainerPrototype().getStartIndexOfOutput();
        for (int i = start; i < getMyContainerPrototype().getSlotsCount(); i++) {
            int recipeOutputIndex = i - start;
            if (!ItemHelper.isOutputSlotFreeFor(recipeOutput[recipeOutputIndex], i, this)) return false;
        }

        return true;
    }

    private boolean isWorking() {
        return processTime > 0;
    }

    @Override
    public int getIntegersToSyncCount() {
        return 4;
    }

    @Override
    public int getCurrentValueOfIntegerToSync(int index) {
        int res = super.getCurrentValueOfIntegerToSync(index);
        if (res == -1) {
            switch (index) {
                case 2:
                    return processTime;

                case 3:
                    return processItemTime;

                default:
                    return -1;
            }
        } else {
            return res;
        }
    }

    @Override
    public void setCurrentValueOfIntegerToSync(int index, int value) {
        super.setCurrentValueOfIntegerToSync(index, value);
        switch (index) {
            case 2:
                processTime = value;
                break;

            case 3:
                processItemTime = value;
                break;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.processTime = tag.getInteger(PROCESS_TIME_TAG_NAME);
        this.processItemTime = tag.getInteger(PROCESS_ITEM_TIME_TAG_NAME);

        NBTTagList tagList = tag.getTagList(PROCESSING_INV_TAG);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound innerTag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = innerTag.getByte(SLOT_TAG);
            if (slot >= 0 && slot < processingInv.length) {
                processingInv[slot] = ItemStack.loadItemStackFromNBT(innerTag);
            }
        }
        getPowerProvider().readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(PROCESS_TIME_TAG_NAME, this.processTime);
        tag.setInteger(PROCESS_ITEM_TIME_TAG_NAME, this.processItemTime);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < processingInv.length; i++) {
            ItemStack stack = processingInv[i];
            if (stack != null) {
                NBTTagCompound innerTag = new NBTTagCompound();
                innerTag.setByte(SLOT_TAG, (byte) i);
                stack.writeToNBT(innerTag);
                itemList.appendTag(innerTag);
            }
        }
        tag.setTag(PROCESSING_INV_TAG, itemList);
        getPowerProvider().writeToNBT(tag);
    }

    public ContainerBasicProcessingMachine getMyContainerPrototype() {
        if (myContainerPrototype == null) {
            myContainerPrototype = ProcessingMachineRegistry.getContainerPrototype(this.getClass());
            if (myContainerPrototype == null) {
                throw new RuntimeException("cannot find container prototype for this class (" + getClass().getSimpleName() + ")");
            }
        }
        return myContainerPrototype;
    }

    public String getGuiBackgroundTexture() {
        return "/guipmachine.png";
    }
}
