/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.block.TileMachineWithInventory;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.BiomeHelper;
import monnef.jaffas.food.crafting.RecipesFridge;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

@ContainerRegistry.ContainerTag(slotsCount = 20, outputSlotsCount = 0, containerClassName = "monnef.jaffas.food.block.ContainerFridge", guiClassName = "monnef.jaffas.food.client.GuiFridge")
public class TileFridge extends TileMachineWithInventory implements IInventory {
    public static Random rand = new Random();

    private int front;
    private int eventTime;
    public float temperature;

    private int tickCounter;
    public static int tickDivider = 20;
    private boolean setupTemperatureFromBiome = false;
    private int cachedEnvTemp = 0;
    private int cacheValidFor = 0;

    public TileFridge() {
        eventTime = 0;
        temperature = 20;
    }

    @Override
    protected void onFirstTick() {
        super.onFirstTick();
        if (setupTemperatureFromBiome)
            temperature = getEnvironmentTemperature();
    }

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        slowingCoefficient = 20;
        powerNeeded = 10;
    }

    @Override
    public String getMachineTitle() {
        return "Fridge";
    }

    private int getEnvironmentTemperature() {
        cacheValidFor--;
        if (cacheValidFor < 0) {
            cachedEnvTemp = computeEnvironmentTemperature();
            cacheValidFor = 60;
        }
        return cachedEnvTemp;
    }

    private int computeEnvironmentTemperature() {
        return Math.round(BiomeHelper.computeBiomeTemperatureInCelsius(worldObj.getBiomeGenForCoords(xCoord, zCoord)));
    }

    @Override
    protected void doMachineWork() {
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        tickCounter++;

        if (tickCounter % tickDivider == 0) {
            // only every second do stuff

            if (gotPowerToActivate()) {
                consumePower(7);
                coolDown(0.1F);
            } else {
                melt(4);
            }

            if (temperature < -5) {
                eventTime++;
                if (eventTime > 15) {
                    runSpecialEvent();
                    eventTime = 0;
                }
            }
        }
    }

    private void runSpecialEvent() {
        if (!worldObj.isRemote) {
            int tries = 0;
            int slotNum;
            boolean breakCycle = false;
            ItemStack stack;

            // try harder find a slot with proper recipe input
            do {
                slotNum = Math.abs(rand.nextInt()) % getSizeInventory();

                stack = getStackInSlot(slotNum);
                if (stack != null) {
                    breakCycle = RecipesFridge.getCopyOfResult(getStackInSlot(slotNum).itemID) != null;
                }
            } while (tries++ < 5 && !breakCycle);


            if (stack == null) {
                if (rand.nextDouble() < 0.25) {
                    ItemStack newItem = rand.nextDouble() < 0.5D ? new ItemStack(Block.ice) : new ItemStack(Item.snowball);

                    setInventorySlotContents(slotNum, newItem);
                    melt();
                }
            } else if (stack.itemID == Block.ice.blockID || stack.itemID == Item.snowball.itemID) {
                if (rand.nextDouble() < 0.25) {
                    if (stack.stackSize < stack.getMaxStackSize()) {
                        stack.stackSize++;
                        melt();
                    }
                }
            } else {
                ItemStack output = RecipesFridge.getCopyOfResult(stack.itemID);

                if (output != null) {
                    int free = -1;
                    boolean addToStack = false;

                    for (int i = 0; i < getSizeInventory() - 1; i++) {
                        if (getStackInSlot(i) == null) {
                            free = i;
                            i = getSizeInventory();
                        } else if (getStackInSlot(i).itemID == output.itemID && getStackInSlot(i).stackSize < getStackInSlot(i).getMaxStackSize()) {
                            addToStack = true;
                            free = i;
                            i = getSizeInventory();
                        }
                    }

                    if (free != -1) {
                        getStackInSlot(slotNum).stackSize--;
                        if (getStackInSlot(slotNum).stackSize <= 0) setInventorySlotContents(slotNum, null);

                        if (addToStack) {
                            getStackInSlot(free).stackSize++;
                        } else {
                            setInventorySlotContents(free, output);
                        }

                        melt();
                    }

                }
            }
        }
    }

    private void melt(int i) {
        if (i < 1) return;

        if (temperature < getEnvironmentTemperature()) {
            temperature += 0.05 * i;
        }
    }

    private void melt() {
        melt(1);
    }

    protected void coolDown(float i) {
        if (temperature > -10) {
            temperature -= i;
        }
    }

    @Override
    public String getInvName() {
        return "container.fridge";
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        setFront(tagCompound.getInteger("front"));
        eventTime = tagCompound.getInteger("eventTime");
        if (tagCompound.hasKey("temperature")) {
            temperature = tagCompound.getFloat("temperature");
        } else {
            setupTemperatureFromBiome = true;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("front", getFront());
        tagCompound.setInteger("eventTime", eventTime);
        tagCompound.setFloat("temperature", temperature);
    }

    public int getFront() {
        return front;
    }

    public void setFront(int front) {
        this.front = front;
    }

    public float getTemperature() {
        return temperature;
    }
}
