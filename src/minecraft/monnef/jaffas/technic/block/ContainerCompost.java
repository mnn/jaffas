/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.block.ContainerJaffas;
import monnef.jaffas.technic.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCompost extends ContainerJaffas {
    private final TileEntityCompostCore core;
    public final static int SLOT_INPUT = 0;
    public final static int SLOT_OUTPUT = 1;
    private int lastTankMeter;
    private int lastWorkMeter;

    public ContainerCompost(InventoryPlayer inventoryPlayer, TileEntityCompostCore tile) {
        super(inventoryPlayer, tile);
        core = tile;
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 21, 35));
        addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 136, 35));
    }

    @Override
    protected int getSlotsCount() {
        return 2;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.core.getTankMeter());
        crafting.sendProgressBarUpdate(this, 1, this.core.getWorkMeter());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting crafter = (ICrafting) this.crafters.get(i);

            if (this.lastTankMeter != this.core.getTankMeter()) {
                crafter.sendProgressBarUpdate(this, 0, this.core.getTankMeter());
            }
            if (this.lastWorkMeter != this.core.getWorkMeter()) {
                crafter.sendProgressBarUpdate(this, 1, this.core.getWorkMeter());
            }
        }

        this.lastTankMeter = this.core.getTankMeter();
        this.lastWorkMeter = this.core.getWorkMeter();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int num, int value) {
        if (num == 0) {
            this.core.setTankMeter(value);
        }
        if (num == 1) {
            this.core.setWorkMeter(value);
        }
    }
}
