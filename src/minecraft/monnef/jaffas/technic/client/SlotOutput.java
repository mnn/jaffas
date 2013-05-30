/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot {
    public SlotOutput(IInventory inventory, int slotNumber, int x, int y) {
        super(inventory, slotNumber, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return false;
    }
}
