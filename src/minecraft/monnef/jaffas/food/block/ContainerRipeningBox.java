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
    protected TileRipeningBox box;
    private int[] lastRipStatus;

    public ContainerRipeningBox(InventoryPlayer inventoryPlayer, TileRipeningBox te) {
        super(inventoryPlayer, te);
        box = te;
        lastRipStatus = new int[RIPENING_SLOTS];
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        for (int i = 0; i < RIPENING_SLOTS; i++) {
            par1ICrafting.sendProgressBarUpdate(this, i, box.getRipeningStatus(i));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < RIPENING_SLOTS; i++) {
            for (int crafterIdx = 0; crafterIdx < this.crafters.size(); ++crafterIdx) {
                ICrafting currCrafter = (ICrafting) this.crafters.get(crafterIdx);

                if (lastRipStatus[i] != box.getRipeningStatus(i)) {
                    currCrafter.sendProgressBarUpdate(this, i, box.getRipeningStatus(i));
                }
            }

            lastRipStatus[i] = box.getRipeningStatus(i);
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        if (id >= 0 && id < RIPENING_SLOTS) {
            box.setRipeningStatusFromGUI(id, value);
        }
    }

    @Override
    public int getSlotsCount() {
        return box.getSizeInventory();
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