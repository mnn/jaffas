/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import monnef.core.block.ContainerMonnefCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.util.Iterator;

public class ContainerFruitCollector extends ContainerMonnefCore {

    protected TileFruitCollector tileEntity;
    protected int lastBurnTime;
    protected int lastItemBurnTime;

    public ContainerFruitCollector(InventoryPlayer inventoryPlayer, TileFruitCollector te) {
        super(inventoryPlayer, te);
        tileEntity = te;
    }

    @Override
    public void constructSlotsFromInventory(IInventory inv) {
        int row, col;
        int colsPerRow = 2;
        for (int i = 0; i < getSlotsCount(); i++) {
            col = i % colsPerRow;
            row = i / colsPerRow;
            addSlotToContainer(new Slot(inv, i, 45 + col * 18, 22 + row * 18));
        }
    }

    @Override
    public int getSlotsCount() {
        return 4;
    }

    @Override
    public int getOutputSlotsCount() {
        return 4;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Iterator var1 = this.crafters.iterator();

        while (var1.hasNext()) {
            ICrafting var2 = (ICrafting) var1.next();
        }
    }

    @Override
    public void updateProgressBar(int par1, int par2) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }
}