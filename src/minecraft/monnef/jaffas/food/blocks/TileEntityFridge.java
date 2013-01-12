package monnef.jaffas.food.blocks;

import monnef.jaffas.food.crafting.RecipesFridge;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Random;

public class TileEntityFridge extends TileEntityJaffaMachine implements IInventory {
    public static Random rand = new Random();

    private int front;

    private int eventTime;
    public float temperature;

    private int tickCounter;

    public static int tickDivider = 20;

    public TileEntityFridge() {
        super(70);
        inv = new ItemStack[20 + 1];
        eventTime = 0;
        temperature = 24;
        fuelSlot = 20;
    }

    public void updateEntity() {
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
            } else if (stack.itemID == Block.ice.blockID || stack.itemID == Item.snowball.shiftedIndex) {
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
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {
        return "container.fridge";
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        setFront(tagCompound.getInteger("front"));
        eventTime = tagCompound.getInteger("eventTime");
        temperature = tagCompound.getFloat("temperature");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            ItemStack stack = inv[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
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

    /*
    @Override
    public int addItem(ItemStack stack, boolean doAdd, Orientations from) {
        if (from == Orientations.YNeg) {
            if (this.inv[fuelSlot] == null) {
                if (doAdd) setInventorySlotContents(fuelSlot, stack);
                return stack.stackSize;
            } else {
                if (this.inv[fuelSlot].itemID != stack.itemID) {
                    return 0;
                } else {
                    int newStackSize = stack.stackSize + inv[fuelSlot].stackSize;
                    if (doAdd) inv[fuelSlot].stackSize += stack.stackSize;

                    if (newStackSize > stack.getMaxStackSize()) {
                        if (doAdd) inv[fuelSlot].stackSize = stack.getMaxStackSize();
                        return stack.stackSize - (newStackSize % stack.getMaxStackSize());
                    } else {
                        return stack.stackSize;
                    }
                }
            }
        } else {
            return this.addItemToInventory(stack, doAdd);
        }
    }

    @Override
    public ItemStack[] extractItem(boolean doRemove, Orientations from, int maxItemCount) {
        int itemSlot = -1;

        for (int i = 0; i < fuelSlot; i++) {
            if (this.inv[i] != null) {
                itemSlot = i;
                i = fuelSlot;
            }
        }

        if (itemSlot == -1) {
            return null;
        }

        ItemStack stack = this.inv[itemSlot];
        ItemStack output = stack.copy();
        int newStackCount = stack.stackSize - maxItemCount;
        int outputStackCount = maxItemCount;
        if (newStackCount <= 0) {
            if (doRemove) setInventorySlotContents(itemSlot, null);
            outputStackCount = stack.stackSize;
        } else {
            if (doRemove) stack.stackSize = newStackCount;
        }

        output.stackSize = outputStackCount;
        return new ItemStack[]{output};
    }
    */

    protected void addEnergy(float i) {
        if (temperature > -10) {
            temperature -= i;
        }
    }
}
