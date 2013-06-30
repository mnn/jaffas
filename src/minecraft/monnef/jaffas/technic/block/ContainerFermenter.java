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

public class ContainerFermenter extends ContainerJaffas {
    public final static int SLOT_INPUT = 0;
    public final static int SLOT_OUTPUT = 2;
    public final static int SLOT_KEG = 1;

    private final TileEntityFermenter tile;

    private int lastWorkMeter;
    private int lastLiquidType;
    private int lastLiquidAmount;

    public ContainerFermenter(InventoryPlayer inventoryPlayer, TileEntityFermenter tile) {
        super(inventoryPlayer, tile);
        this.tile = tile;
    }

    @Override
    protected int getSlotsCount() {
        return 3;
    }

    @Override
    protected int getOutputSlotsCount() {
        return 1;
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 78, 48));
        addSlotToContainer(new Slot(inv, SLOT_KEG, 78, 20));
        addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 137, 18));
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.tile.getLiquidAmount());
        crafting.sendProgressBarUpdate(this, 1, this.tile.getWorkMeter());
        crafting.sendProgressBarUpdate(this, 2, this.tile.getLiquid().ordinal());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting crafter = (ICrafting) this.crafters.get(i);

            if (this.lastLiquidType != this.tile.getLiquidAmount()) {
                crafter.sendProgressBarUpdate(this, 0, this.tile.getLiquidAmount());
            }
            if (this.lastWorkMeter != this.tile.getWorkMeter()) {
                crafter.sendProgressBarUpdate(this, 1, this.tile.getWorkMeter());
            }
            if (this.lastLiquidAmount != this.tile.getLiquid().ordinal()) {
                crafter.sendProgressBarUpdate(this, 2, this.tile.getLiquid().ordinal());
            }
        }

        this.lastLiquidType = this.tile.getLiquidAmount();
        this.lastWorkMeter = this.tile.getWorkMeter();
        this.lastLiquidAmount = this.tile.getLiquid().ordinal();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int num, int value) {
        switch (num) {
            case 0:
                this.tile.setLiquidAmount(value);
                break;
            case 1:
                this.tile.setWorkCounter(value);
                break;
            case 2:
                this.tile.setLiquid(value);
                break;
        }
    }
}
