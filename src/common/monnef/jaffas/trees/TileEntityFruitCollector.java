package monnef.jaffas.trees;

import buildcraft.api.core.Orientations;
import buildcraft.api.inventory.ISpecialInventory;
import monnef.jaffas.food.TileEntityJaffaMachine;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityFruitCollector extends TileEntityJaffaMachine implements IInventory, ISpecialInventory {

    public static Random rand = new Random();

    private int eventTime;

    private int tickCounter;

    public static int tickDivider = 20;
    private AxisAlignedBB box;
    private static ArrayList<Integer> fruitList;


    static {
        fruitList = new ArrayList<Integer>();
        fruitList.add(mod_jaffas_trees.itemLemon.shiftedIndex);
        fruitList.add(mod_jaffas_trees.itemOrange.shiftedIndex);
        fruitList.add(mod_jaffas_trees.itemPlum.shiftedIndex);
        fruitList.add(mod_jaffas.getJaffaItem(mod_jaffas.JaffaItem.vanillaBeans).shiftedIndex);
        fruitList.add(Item.appleRed.shiftedIndex);
    }

    public TileEntityFruitCollector() {
        super(100);
        inv = new ItemStack[4 + 1];
        eventTime = 0;
        this.fuelSlot = 4;
    }

    public void updateEntity() {
        tickCounter++;

        if (tickCounter % tickDivider == 0) {
            // only every second do stuff

            if (isBurning()) {
                eventTime++;
                burnTime -= 7;
            }

            if (burnTime <= 0) {
                burnTime = 0;
                tryGetFuel();
            }

            if (eventTime > 5) {
                runSpecialEvent();
                eventTime = 0;
            }
        }
    }

    private void runSpecialEvent() {
        if (!worldObj.isRemote) {
            // TODO fruit picking
            box = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            box = box.expand(7, 2, 7);

            List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, box);
            Iterator<EntityItem> it = list.iterator();
            boolean notFound = true;
            EntityItem item = null;
            while (notFound && it.hasNext()) {
                item = it.next();
                if (fruitList.contains(item.item.itemID)) {
                    notFound = false;
                }
            }

            if (!notFound) {
                // fruit found, moving
                addItemToInventory(item.item.copy(), true);
                item.setDead();
            } else {
//                if (mod_jaffas_trees.debug) System.out.println("no fruit found");
            }
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
        return "container.fruitCollector";
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

        eventTime = tagCompound.getInteger("eventTime");
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
        tagCompound.setInteger("eventTime", eventTime);
    }


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
}
