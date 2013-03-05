package monnef.core;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ItemStackInInventory {
    private final IInventory inventory;
    private final int position;

    public ItemStackInInventory(IInventory inventory, int position) {
        this.inventory = inventory;
        this.position = position;
    }

    public boolean decreaseStackSize() {
        ItemStack currentItem = getItem();
        if (currentItem == null) return false;
        int size = currentItem.stackSize;

        inventory.decrStackSize(position, 1);

        return size > 0;
    }

    public IInventory getInventory() {
        return inventory;
    }

    public int getPosition() {
        return position;
    }

    public ItemStack getItem() {
        return inventory.getStackInSlot(position);
    }
}
