/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.block.ContainerJaffas;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCompost extends ContainerJaffas {
    private final TileEntityCompostCore core;
    public final static int SLOT_INPUT = 0;
    public final static int SLOT_OUTPUT = 1;

    public ContainerCompost(InventoryPlayer inventoryPlayer, TileEntityCompostCore tile) {
        super(inventoryPlayer, tile);
        core = tile;

        addSlotToContainer(new Slot((IInventory) core, SLOT_INPUT, 56, 35)); //  input
        addSlotToContainer(new Slot((IInventory) core, SLOT_OUTPUT, 116, 35)); // output
    }

    @Override
    protected int getSlotsCount() {
        return 2;
    }
}
