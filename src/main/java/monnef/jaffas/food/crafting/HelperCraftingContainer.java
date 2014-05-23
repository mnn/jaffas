/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class HelperCraftingContainer extends Container {
    public HelperCraftingContainer(IInventory craftMatrix) {
        super();
        for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
            addSlotToContainer(new Slot(craftMatrix, i, 0, 0));
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return false;
    }
}
