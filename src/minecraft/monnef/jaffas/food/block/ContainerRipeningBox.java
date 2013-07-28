/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.technic.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import static monnef.jaffas.food.block.TileRipeningBox.RIPENING_SLOTS;

public class ContainerRipeningBox extends ContainerJaffas {
    protected TileRipeningBox board;
    private int[] lastRipStatus;

    public ContainerRipeningBox(InventoryPlayer inventoryPlayer, TileRipeningBox te) {
        super(inventoryPlayer, te);
        board = te;
        lastRipStatus = new int[RIPENING_SLOTS];
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        //par1ICrafting.sendProgressBarUpdate(this, 0, this.board.chopTime);
        for (int i = 0; i < RIPENING_SLOTS; i++) {
            par1ICrafting.sendProgressBarUpdate(this, i, board.getRipeningStatus(i));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        /*
        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.lastChopTime != this.board.chopTime) {
                var2.sendProgressBarUpdate(this, 0, this.board.chopTime);
            }
        }

        this.lastChopTime = this.board.chopTime;
        */

        for (int i = 0; i < RIPENING_SLOTS; i++) {

            for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
                ICrafting var2 = (ICrafting) this.crafters.get(var1);

                if (lastRipStatus[i] != board.getRipeningStatus(i)) {
                    var2.sendProgressBarUpdate(this, 0, board.getRipeningStatus(i));
                }
            }

            lastRipStatus[i] = board.getRipeningStatus(i);
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        /*
        if (id == 0) {
            this.board.chopTime = value;
        }
        */
        if (id >= 0 && id < RIPENING_SLOTS) {
            board.setRipeningStatus(id, value);
        }
    }

    @Override
    public int getSlotsCount() {
        return board.getSizeInventory();
    }

    @Override
    public void constructSlots(IInventory inv) {
        for (int i = 0; i < RIPENING_SLOTS; i++) {
            addSlotToContainer(new Slot(inv, i, 26 + (i % 4) * 20, 26 + (i / 4) * 20));
        }
        addSlotToContainer(new SlotOutput(inv, TileRipeningBox.SLOT_OUTPUT, 133, 20)); // output
    }

    @Override
    public int getOutputSlotsCount() {
        return 1;
    }
}