/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.core.block.TileMachineWithInventory;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.ItemHelper;
import monnef.jaffas.power.common.IProcessingRecipe;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;

import static monnef.jaffas.food.JaffasFood.Log;

@ContainerRegistry.ContainerTag(slotsCount = 2, containerClassName = "monnef.jaffas.power.block.common.ContainerBasicProcessingMachine", guiClassName = "monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine")
public abstract class TileEntityBasicProcessingMachine extends TileMachineWithInventory {
    private static final String PROCESS_TIME_TAG_NAME = "processTime";
    private static final String PROCESS_ITEM_TIME_TAG_NAME = "processItemTime";
    private static final String INPUT_ITEM_TAG = "inputItem";
    public static final String PROCESSING_INV_TAG = "ProcessingInv";
    public static final String SLOT_TAG = "Slot";

    public int processTime = 0;
    public int processItemTime = 1;
    private ItemStack[] processingInv;
    private MachineRecord myRecord = getMachineRecord(this.getClass());

    private static final HashMap<Class<? extends TileEntityBasicProcessingMachine>, MachineRecord> machineRecords = new HashMap<Class<? extends TileEntityBasicProcessingMachine>, MachineRecord>();

    public static class MachineRecord {
        private IProcessingRecipeHandler recipeHandler;
        private String guiBackgroundTexture;
        private String title;
        private InvType invType;

        public enum InvType {
            BASIC, DOUBLE
        }

        private MachineRecord(IProcessingRecipeHandler recipeHandler, String guiBackgroundTexture, String title, InvType invType) {
            this.recipeHandler = recipeHandler;
            this.guiBackgroundTexture = guiBackgroundTexture;
            this.title = title;
            this.invType = invType;
        }

        public IProcessingRecipeHandler getRecipeHandler() {
            return recipeHandler;
        }

        public String getGuiBackgroundTexture() {
            return guiBackgroundTexture;
        }

        public String getTitle() {
            return title;
        }

        public InvType getInvType() {
            return invType;
        }
    }

    @Override
    public String getMachineTitle() {
        return myRecord.getTitle();
    }

    public static MachineRecord getMachineRecord(Class<? extends TileEntityBasicProcessingMachine> clazz) {
        return machineRecords.get(clazz);
    }

    public static void registerMachine(Class<? extends TileEntityBasicProcessingMachine> clazz, IProcessingRecipeHandler handler, String backgroundTexture, String title) {
        registerMachine(clazz, handler, backgroundTexture, title, MachineRecord.InvType.BASIC);
    }

    public static void registerMachine(Class<? extends TileEntityBasicProcessingMachine> clazz, IProcessingRecipeHandler handler, String backgroundTexture, String title, MachineRecord.InvType invType) {
        if (machineRecords.containsKey(clazz)) {
            throw new RuntimeException("Re-registering recipe handler for class " + clazz.getName() + ".");
        }
        if (clazz == null || handler == null) {
            throw new NullPointerException("class or handler");
        }
        machineRecords.put(clazz, new MachineRecord(handler, backgroundTexture, title, invType));
    }

    protected TileEntityBasicProcessingMachine() {
        super();
        processingInv = new ItemStack[getSizeInventory()];
    }

    public static IProcessingRecipeHandler getRecipeHandler(Class<? extends TileEntityBasicProcessingMachine> machineClass) {
        if (!machineRecords.containsKey(machineClass)) return null;
        return machineRecords.get(machineClass).getRecipeHandler();
    }

    public static String getBackgroundTexture(Class<? extends TileEntityBasicProcessingMachine> machineClass) {
        if (!machineRecords.containsKey(machineClass)) return null;
        return machineRecords.get(machineClass).getGuiBackgroundTexture();
    }


    @Override
    public int getSizeInventory() {
        return getContainerDescriptor().getSlotsCount();
    }

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
            float power = consumeNeededPower();
            if (power < powerNeeded) {
                Log.printWarning("Inconsistency detected in power framework! " + getClass().getSimpleName());
            } else {
                if (processTime >= processItemTime) {
                    processTime = 0;
                    IProcessingRecipe recipe = getRecipeHandler(this.getClass()).findByInput(processingInv);
                    if (recipe == null) {
                        Log.printWarning("Null recipe in " + this.getClass().getSimpleName());
                    } else {
                        produceOutput(recipe);
                    }
                }
            }
        } else {
            IProcessingRecipe recipe = getRecipeHandler(this.getClass()).findByInput(createInputInv());
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
        ItemStack[] ret = new ItemStack[getContainerDescriptor().getInputSlotsCount()];
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
            produceOneOutput(output[i], i + getContainerDescriptor().getStartIndexOfOutput());
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
        int start = getContainerDescriptor().getStartIndexOfOutput();
        for (int i = start; i < getContainerDescriptor().getSlotsCount(); i++) {
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
        getPowerHandler().readFromNBT(tag);
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
        getPowerHandler().writeToNBT(tag);
    }

    public static String getDefaultGuiBackgroundTexture() {
        return "guipmachine.png";
    }
}
