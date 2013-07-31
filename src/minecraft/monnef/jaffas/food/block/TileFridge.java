/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.jaffas.food.crafting.RecipesFridge;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class TileFridge extends TileJaffaMachine implements IInventory {
    public static Random rand = new Random();

    private int front;

    private int eventTime;
    public float temperature;

    private int tickCounter;

    public static int tickDivider = 20;

    public TileFridge() {
        super(70);
        eventTime = 0;
        temperature = 24;
        fuelSlot = 20;
    }

    public void updateEntity() {
        super.updateEntity();
        tickCounter++;

        if (tickCounter % tickDivider == 0) {
            // only every second do stuff

            if (isBurning()) {
                //burnTime--;
                burnTime -= 7;
                addEnergy(0.1F);
            } else {
                melt(4);
            }

            if (burnTime <= 0) {
                burnTime = 0;
                tryGetFuel();
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
                slotNum = Math.abs(rand.nextInt()) % fuelSlot;

                stack = inv[slotNum];
                if (stack != null) {
                    breakCycle = RecipesFridge.getCopyOfResult(inv[slotNum].itemID) != null;
                }
            } while (tries++ < 5 && !breakCycle);


            if (stack == null) {
                if (rand.nextDouble() < 0.25) {
                    ItemStack newItem = rand.nextDouble() < 0.5D ? new ItemStack(Block.ice) : new ItemStack(Item.snowball);

                    inv[slotNum] = newItem;
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

                    for (int i = 0; i < fuelSlot - 1; i++) {
                        if (inv[i] == null) {
                            free = i;
                            i = fuelSlot;
                        } else if (inv[i].itemID == output.itemID && inv[i].stackSize < inv[i].getMaxStackSize()) {
                            addToStack = true;
                            free = i;
                            i = fuelSlot;
                        }
                    }

                    if (free != -1) {
                        inv[slotNum].stackSize--;
                        if (inv[slotNum].stackSize <= 0) setInventorySlotContents(slotNum, null);

                        if (addToStack) {
                            inv[free].stackSize++;
                        } else {
                            inv[free] = output;
                        }

                        melt();
                    }

                }
            }
        }
    }


    private void melt(int i) {
        if (i < 1) return;

        if (temperature < 24) {
            temperature += 0.05 * i;
        }
    }

    private void melt() {
        melt(1);
    }

    @Override
    public int getSizeInventory() {
        return 20 + 1;
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
        temperature = tagCompound.getFloat("temperature");
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

    protected void addEnergy(float i) {
        if (temperature > -10) {
            temperature -= i;
        }
    }
}
