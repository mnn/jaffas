/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import monnef.core.block.ContainerMachine;
import monnef.core.block.ContainerMonnefCore;
import monnef.core.block.TileContainerMonnefCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

import java.util.Iterator;

public class ContainerFruitCollector extends ContainerMachine {

    protected TileFruitCollector tileEntity;

    public ContainerFruitCollector(InventoryPlayer inventoryPlayer, TileFruitCollector te) {
        super(inventoryPlayer, te);
        tileEntity = te;
    }

    @Override
    public void constructSlotsFromTile(TileEntity tile) {
        IInventory inv = (IInventory) tile;
        int row, col;
        int colsPerRow = 2;
        for (int i = 0; i < getSlotsCount(); i++) {
            col = i % colsPerRow;
            row = i / colsPerRow;
            addSlotToContainer(new Slot(inv, i, 45 + col * 18, 22 + row * 18));
        }
    }
}