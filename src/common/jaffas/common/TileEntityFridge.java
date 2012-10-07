package jaffas.common;

import net.minecraft.src.*;

import java.util.Random;

public class TileEntityFridge extends TileEntity implements IInventory {
    public static final int fuelSlot = 20;
    public static Random rand = new Random();

    private ItemStack[] inv;
    private int front;

    public int burnTime;
    public int burnItemTime;
    private int eventTime;
    public float temperature;

    private int tickCounter;

    public static int tickDivider = 20;

    public TileEntityFridge() {
        inv = new ItemStack[20 + 1];
        burnTime = 0;
        eventTime = 0;
        temperature = 24;
    }

    public int getBurnTimeRemainingScaled(int par1) {
        if (burnItemTime == 0) {
            burnItemTime = 200;
        }

        return (burnTime * par1) / burnItemTime;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public static boolean isItemFuel(ItemStack par0ItemStack) {
        return TileEntityFurnace.isItemFuel(par0ItemStack);
    }

    public void updateEntity() {
        tickCounter++;

        if (tickCounter % tickDivider == 0) {
            // only every second do stuff

            if (burnTime > 0) {
                burnTime--;
                addEnergy(0.1F);
            } else {
                melt();
            }

            if (burnTime <= 0) {
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
        int slotNum = Math.abs(rand.nextInt()) % fuelSlot;

        //TODO

        if (!worldObj.isRemote) {
            ItemStack stack = inv[slotNum];

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
                    for (int i = 0; i < fuelSlot - 1; i++) {
                        if (inv[i] == null) {
                            free = i;
                            break;
                        }
                    }

                    if (free != -1) {
                        // TODO adding to stack
                        inv[slotNum].stackSize--;
                        if (inv[slotNum].stackSize <= 0) setInventorySlotContents(slotNum, null);

                        inv[free] = output;
                        melt();
                    }
                }
            }
        }
    }

    private void tryGetFuel() {
        ItemStack item = getStackInSlot(fuelSlot);
        if (item == null) {
            return;
        }

        int fuelBurnTime = TileEntityFurnace.getItemBurnTime(item);
        if (fuelBurnTime > 0) {
            item.stackSize--;

            if (item.stackSize <= 0 && !worldObj.isRemote) setInventorySlotContents(fuelSlot, null);

            burnItemTime = fuelBurnTime;
            burnTime = fuelBurnTime;
        } else {
            return;
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

    private void addEnergy(float i) {
        if (temperature > -10) {
            temperature -= i;
        }
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
        burnTime = tagCompound.getInteger("burnTime");
        burnItemTime = tagCompound.getInteger("burnItemTime");
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
        tagCompound.setInteger("burnTime", burnTime);
        tagCompound.setInteger("burnItemTime", burnItemTime);
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