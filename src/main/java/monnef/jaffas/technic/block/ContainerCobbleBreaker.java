/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.block.ContainerMonnefCore;
import monnef.core.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCobbleBreaker extends ContainerMonnefCore {
    public final static int SLOT_INPUT = 0;
    public final static int SLOT_OUTPUT = 2;
    public final static int SLOT_FUEL = 1;

    private final TileCobbleBreaker tile;

    private int lastWorkMeter;
    private int lastBurnTime;
    private int lastBurnItemTime;

    public ContainerCobbleBreaker(InventoryPlayer inventoryPlayer, TileCobbleBreaker tile) {
        super(inventoryPlayer, tile);
        this.tile = tile;
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 57, 35));
        addSlotToContainer(new Slot(inv, SLOT_FUEL, 23, 35));
        addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 124, 35));
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.tile.getBurnTime());
        crafting.sendProgressBarUpdate(this, 1, this.tile.getWorkMeter());
        crafting.sendProgressBarUpdate(this, 2, this.tile.getBurnItemTime());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting crafter = (ICrafting) this.crafters.get(i);

            if (this.lastBurnTime != this.tile.getBurnTime()) {
                crafter.sendProgressBarUpdate(this, 0, this.tile.getBurnTime());
            }
            if (this.lastWorkMeter != this.tile.getWorkMeter()) {
                crafter.sendProgressBarUpdate(this, 1, this.tile.getWorkMeter());
            }
            if (this.lastBurnItemTime != this.tile.getBurnItemTime()) {
                crafter.sendProgressBarUpdate(this, 2, this.tile.getBurnItemTime());
            }
        }

        this.lastBurnTime = this.tile.getBurnTime();
        this.lastWorkMeter = this.tile.getWorkMeter();
        this.lastBurnItemTime = this.tile.getBurnItemTime();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int num, int value) {
        switch (num) {
            case 0:
                this.tile.setBurnTime(value);
                break;
            case 1:
                this.tile.setWorkCounter(value);
                break;
            case 2:
                this.tile.setBurnItemTime(value);
                break;
        }
    }
}
