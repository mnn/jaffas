/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.MathHelper;
import monnef.core.block.TileWithInventory;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.item.ItemStack;

import static monnef.jaffas.food.JaffasFood.itemsAreSame;

@ContainerRegistry.ContainerTag(slotsCount = 9, containerClassName = "monnef.jaffas.food.block.ContainerRipeningBox", guiClassName = "monnef.jaffas.food.client.GuiRipeningBox")
public class TileRipeningBox extends TileWithInventory {
    public static final int SLOT_OUTPUT = 8;
    public static final int RIPENING_SLOTS = 8;
    public static final int MAX_RIPENING_STATUS = 100;
    private static final int TICK_EVERY = MonnefCorePlugin.debugEnv ? 2 : 20;
    private int[] ripeningStatus;
    private int counter;

    public TileRipeningBox() {
        ripeningStatus = new int[RIPENING_SLOTS];
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public String getInventoryName() {
        return "container.ripeningBox";
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return MathHelper.range(0, getSizeInventory());
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot != SLOT_OUTPUT;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == SLOT_OUTPUT;
    }

    public int getRipeningStatus(int id) {
        if (id < 0 || id > ripeningStatus.length) {
            if (MonnefCorePlugin.debugEnv) throw new IllegalArgumentException();
            else return 0;
        }
        //return id * MAX_RIPENING_STATUS / (RIPENING_SLOTS - 1);
        return ripeningStatus[id];
    }

    public void setRipeningStatusFromGUI(int id, int value) {
        if (!worldObj.isRemote) {
            throw new RuntimeException("Cannot set ripening status via gui method on a server!");
        }
        setRipeningStatus(id, value);
    }

    private void setRipeningStatus(int id, int value) {
        ripeningStatus[id] = value;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;

        counter++;
        if (counter % TICK_EVERY == 0) {
            for (int i = 0; i < RIPENING_SLOTS; i++) {
                int spd = getRipeningSpeed(getStackInSlot(i));
                if (spd == 0) {
                    setRipeningStatus(i, 0);
                    tryMoveOut(i);
                } else {
                    int newStatus = getRipeningStatus(i) + spd;
                    if (newStatus > MAX_RIPENING_STATUS) {
                        setInventorySlotContents(i, getRipeningProduct(getStackInSlot(i)));
                    } else {
                        setRipeningStatus(i, newStatus);
                    }
                }
            }
        }
    }

    private void tryMoveOut(int slot) {
        ItemStack output = getStackInSlot(SLOT_OUTPUT);
        ItemStack stack = getStackInSlot(slot);
        if (stack == null) return;
        if (output == null) {
            setInventorySlotContents(slot, null);
            setInventorySlotContents(SLOT_OUTPUT, stack);
        } else if (output.isItemEqual(stack) && (output.stackSize + stack.stackSize <= output.getMaxStackSize())) {
            setInventorySlotContents(slot, null);
            output.stackSize += stack.stackSize;
        }
    }

    public int getRipeningSpeed(ItemStack stack) {
        if (itemsAreSame(JaffaItem.cheeseRaw, stack)) return 1;
        return 0;
    }

    public ItemStack getRipeningProduct(ItemStack stack) {
        if (itemsAreSame(JaffaItem.cheeseRaw, stack)) return Recipes.getItemStack(JaffaItem.cheese);
        return null;
    }
}
