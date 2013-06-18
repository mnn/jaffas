/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.block.ContainerJaffas;
import monnef.jaffas.technic.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerCobbleBreaker extends ContainerJaffas {
    public final static int SLOT_INPUT = 0;
    public final static int SLOT_OUTPUT = 1;

    private final TileEntityCobbleBreaker tile;

    public ContainerCobbleBreaker(InventoryPlayer inventoryPlayer, TileEntityCobbleBreaker tile) {
        super(inventoryPlayer, tile);
        this.tile = tile;

        addSlotToContainer(new Slot(tile, SLOT_INPUT, 21, 35));
        addSlotToContainer(new SlotOutput(tile, SLOT_OUTPUT, 136, 35));
    }

    @Override
    protected int getSlotsCount() {
        return 2;
    }
}
